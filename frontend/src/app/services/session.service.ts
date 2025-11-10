import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private readonly SESSION_KEY = 'app_session_id';
  private sessionId: string;

  constructor() {
    this.sessionId = this.initializeSession();
  }

  private initializeSession(): string {
    // Check if session ID already exists in localStorage
    let existingSessionId = localStorage.getItem(this.SESSION_KEY);

    if (existingSessionId) {
      return existingSessionId;
    }

    // Generate new session ID if none exists
    const newSessionId = this.generateSessionId();
    localStorage.setItem(this.SESSION_KEY, newSessionId);
    return newSessionId;
  }

  private generateSessionId(): string {
    // Generate a random session ID using crypto API for better randomness
    if (typeof crypto !== 'undefined' && crypto.randomUUID) {
      return crypto.randomUUID();
    }

    // Fallback method for older browsers
    return 'session_' + Math.random().toString(36).substr(2, 16) + Date.now().toString(36);
  }

  public getSessionId(): string {
    return this.sessionId;
  }
}
