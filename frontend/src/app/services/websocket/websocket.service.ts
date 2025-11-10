import { Injectable } from '@angular/core';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject } from 'rxjs';
import { environment } from '../../../environments/environment';
import {SnackbarService} from "../snackbar.service";

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private client: Client;
  private connectionState = new BehaviorSubject<'CONNECTED' | 'DISCONNECTED'>('DISCONNECTED');
  public connectionState$ = this.connectionState.asObservable();

  constructor(private snackbar: SnackbarService) {
    this.client = new Client({
      webSocketFactory: () => {
        // The backend endpoint is /ws
        return new SockJS(`${environment.apiUrl}/ws`);
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
      },
      onDisconnect: () => {
        this.connectionState.next('DISCONNECTED');
        this.snackbar.show("WebSocket disconnected", 'error', 3);
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
    this.client.deactivate();
  }

  subscribe(topic: string, callback: (message: IMessage) => void): StompSubscription {
    return this.client.subscribe(topic, callback);
  }

  sendMessage(destination: string, body: string): void {
    this.client.publish({ destination, body });
  }
}
