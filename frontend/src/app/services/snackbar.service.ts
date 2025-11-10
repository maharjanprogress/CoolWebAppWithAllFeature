import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export type SnackbarType = 'success' | 'error' | 'warning' | 'info';

export interface SnackbarState {
  show: boolean;
  message: string;
  type: SnackbarType;
  duration: number; // Duration in milliseconds
}

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {
  private snackbarState = new BehaviorSubject<SnackbarState>({
    show: false,
    message: '',
    type: 'info',
    duration: 3000
  });

  getState(): Observable<SnackbarState> {
    return this.snackbarState.asObservable();
  }

  show(message: string, type: SnackbarType = 'info', durationSeconds: number = 5): void {
    this.snackbarState.next({ show: true, message, type, duration: durationSeconds * 1000 });
  }

  hide(): void {
    this.snackbarState.next({ ...this.snackbarState.value, show: false });
  }
}