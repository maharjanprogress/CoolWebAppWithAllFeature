import { Injectable } from '@angular/core';
import {Observable, tap} from "rxjs";
import {SessionService} from "../session.service";
import {ApiService} from "../mainApi/api.service";
import {GoogleLoginRequest, LoginDTO} from "../../model/api-request";
import {LoginResponse} from "../../model/api-responses";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  constructor(
    private apiService: ApiService,
    private sessionService: SessionService,
    private router: Router
  ) { }

  login(credentials: LoginDTO): Observable<LoginResponse> {
    return this.apiService.postWithoutAuth<LoginResponse>('/login', credentials).pipe(
      tap(response => {
        // On successful login, store the token
        if (response.status === 'SUCCESS' && response.detail?.token) {
          this.sessionService.setToken(response.detail.token);
          this.sessionService.setRole(response.detail.role);
        }
      })
    );
  }

  loginWithGoogle(googleToken: string): Observable<LoginResponse> {
    const request: GoogleLoginRequest = { token: googleToken };
    return this.apiService.postWithoutAuth<LoginResponse>('/login/oauth2/google', request).pipe(
      tap(response => {
        if (response.status === 'SUCCESS' && response.detail?.token) {
          this.sessionService.setToken(response.detail.token);
          this.sessionService.setRole(response.detail.role);
        }
      })
    );
  }

  logout(): void {
    this.sessionService.clearSession();
    this.router.navigate(['/login']).catch(error => {
      console.error('Navigation failed', error);
    });
  }

}
