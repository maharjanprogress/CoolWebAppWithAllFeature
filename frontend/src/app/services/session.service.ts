import { Injectable } from '@angular/core';
import {RoleUtils, UserRole} from "../model/api-responses";

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private readonly SESSION_KEY = 'app_session_id';
  private readonly TOKEN_KEY = 'app_token_id';
  private readonly ROLE = 'app_role';
  private token: string;
  private role: UserRole | null;
  private sessionId: string;

  constructor() {
    this.sessionId = this.initializeSession();
    this.token = this.initializeToken();
    this.role = this.initializeRole();
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

  private initializeToken(): string {
    let existingToken = localStorage.getItem(this.TOKEN_KEY);

    if (existingToken) {
      return existingToken;
    }
    return "";
  }

  private initializeRole(): UserRole | null {
    let existingRole = localStorage.getItem(this.ROLE);

    if (existingRole) {
      return RoleUtils.fromString(existingRole)
    }
    return null;
  }

  private generateSessionId(): string {
    // Generate a random session ID using crypto API for better randomness
    if (typeof crypto !== 'undefined' && crypto.randomUUID) {
      return crypto.randomUUID();
    }

    // Fallback method for older browsers
    return 'session_' + Math.random().toString(36).substr(2, 16) + Date.now().toString(36);
  }

  public getToken(): string {
    return this.token;
  }

  public getRole(): UserRole | null {
    return this.role;
  }

  public getSessionId(): string {
    return this.sessionId;
  }

  public setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
    this.token = token;
  }

  public setRole(role: string | UserRole): void {
    let userRole: UserRole | null;

    if (typeof role === 'string') {
      userRole = RoleUtils.fromString(role);
      if (!userRole) {
        console.warn(`Invalid role received: ${role}`);
        return;
      }
    } else {
      userRole = role;
    }

    localStorage.setItem(this.ROLE, RoleUtils.toString(userRole));
    this.role = userRole;
  }

  public hasRole(role: UserRole): boolean {
    return this.role === role;
  }

  public refreshSession(): string {
    const newSessionId = this.generateSessionId();
    localStorage.setItem(this.SESSION_KEY, newSessionId);
    this.sessionId = newSessionId;
    return newSessionId;
  }

  public clearSession(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.ROLE);
    this.token = "";
    this.role = null;
  }

}
