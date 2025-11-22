import {Injectable} from '@angular/core';
import {Client, IMessage, StompSubscription} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {BehaviorSubject, Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {SnackbarService} from "../snackbar.service";
import {SessionService} from "../session.service";
import {ExcelUploadResponse, JobStatus, ProgressUpdate} from "../../model/api-responses";

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private client: Client;
  private connectionState = new BehaviorSubject<'CONNECTED' | 'DISCONNECTED'>('DISCONNECTED');
  public connectionState$ = this.connectionState.asObservable();

  private progressSubject = new BehaviorSubject<ProgressUpdate | null>(null);
  public progress$: Observable<ProgressUpdate | null> = this.progressSubject.asObservable();

  private excelStatusSubscription: StompSubscription | null = null;

  constructor(
    private snackbar: SnackbarService,
    private sessionService: SessionService
  ) {
    this.client = new Client({
      webSocketFactory: () => {
        // The backend endpoint is /ws
        return new SockJS(`${environment.apiUrl}/ws`);
      },
      connectHeaders: {
        Authorization: `Bearer ${this.sessionService.getToken()}`
      },
      debug: (str) => {
        if (!environment.production) {
          console.log(new Date(), str);
        }
      },
      reconnectDelay: 5000,
      onConnect: () => {
        this.connectionState.next('CONNECTED');
        this.snackbar.show("WebSocket connected", 'info', 3);
        this.subscribeToExcelStatus();
      },
      onDisconnect: () => {
        this.connectionState.next('DISCONNECTED');
        this.snackbar.show("WebSocket disconnected", 'error', 3);
        this.excelStatusSubscription = null;
      },
      onStompError: (frame) => {
        this.snackbar.show("WebSocket error: " + frame.headers['message'], 'error', 5);
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      }
    });
  }

  connect(): void {
    if (!this.client.active) {
      this.client.activate();
    }
  }

  disconnect(): void {
    if (this.excelStatusSubscription) {
      this.excelStatusSubscription.unsubscribe();
      this.excelStatusSubscription = null;
    }
    this.client.deactivate();
  }

  subscribe(topic: string, callback: (message: IMessage) => void): StompSubscription {
    return this.client.subscribe(topic, callback);
  }

  sendMessage(destination: string, body: string): void {
    this.client.publish({ destination, body });
  }

  private subscribeToExcelStatus(): void {
    // Unsubscribe from previous subscription if exists
    if (this.excelStatusSubscription) {
      this.excelStatusSubscription.unsubscribe();
    }

    // Subscribe to user-specific Excel status updates
    this.excelStatusSubscription = this.client.subscribe(
      '/user/queue/excel-status',
      (message: IMessage) => {
        try {
          const baseResponse: ExcelUploadResponse = JSON.parse(message.body);
          if (baseResponse.status !== 'SUCCESS' || baseResponse.detail == null) {
            console.error('Received error status in progress update:', baseResponse.message);
            return;
          }
          const update: ProgressUpdate = baseResponse.detail;
          this.progressSubject.next(update);

          // Show snackbar notifications for important status changes
          if (update.status === JobStatus.COMPLETED) {
            this.snackbar.show('File processing completed!', 'success', 5);
          } else if (update.status === JobStatus.FAILED) {
            this.snackbar.show('File processing failed: ' + update.message, 'error', 5);
          }
        } catch (error) {
          console.error('Error parsing progress update:', error);
        }
      }
    );
  }

  // Clear progress when user manually resets or starts new upload
  clearProgress(): void {
    this.progressSubject.next(null);
  }

  // Check if WebSocket is connected
  isConnected(): boolean {
    return this.client.active && this.connectionState.value === 'CONNECTED';
  }

  // Reconnect with updated token (useful after token refresh)
  reconnectWithToken(token: string): void {
    this.disconnect();
    this.client.connectHeaders = {
      Authorization: `Bearer ${token}`
    };
    this.connect();
  }
}
