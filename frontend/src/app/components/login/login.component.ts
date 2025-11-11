import {Component, OnDestroy} from '@angular/core';
import {CommonModule} from "@angular/common";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {Subscription} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {ResponseStatus} from "../../model/api-responses";
import {GoogleLoginRequest, LoginDTO} from "../../model/api-request";
import {LoginService} from "../../services/LoginAndRegistration/login.service";
import {SnackbarService} from "../../services/snackbar.service";
import {GoogleLoginProvider, SocialAuthService} from "@abacritt/angularx-social-login";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnDestroy{
  isLoading = false;
  private loginSubscription?: Subscription;
  private socialAuthSubscription?: Subscription;

  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private loginService: LoginService,
    private socialAuthService: SocialAuthService,
    private router: Router,
    private snackbarService: SnackbarService
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  signInWithGoogle(): void {
    this.isLoading = true;
    this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID).then(socialUser => {
      this.loginSubscription = this.loginService.loginWithGoogle(socialUser.idToken).subscribe({
        next: (response) => {
          this.isLoading = false;
          if (response.status === ResponseStatus.SUCCESS) {
            this.snackbarService.show('Login successful!', 'success', 2);
            this.router.navigate(['/app']).catch(error => console.error('Navigation failed', error));
          } else {
            this.snackbarService.show(response.message || 'An unexpected error occurred.', 'error');
          }
        },
        error: (err: HttpErrorResponse) => {
          this.isLoading = false;
          this.snackbarService.show(err.error?.message || 'Google login failed.', 'error', 5);
        }
      });
    }).catch(error => {
      this.isLoading = false;
      // This usually happens if the user closes the popup.
      if (error !== 'Login providers not ready yet') {
        this.snackbarService.show('Google Sign-In was cancelled or failed.', 'info');
      }
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) { return; }

    this.isLoading = true;
    const credentials: LoginDTO = { userName: this.loginForm.value.username, password: this.loginForm.value.password };

    this.loginSubscription = this.loginService.login(credentials).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.status === ResponseStatus.SUCCESS) {
          this.snackbarService.show('Login successful!', 'success',3);
          this.router.navigate(['/app']).catch(error => {
            console.error('Navigation failed', error);
          });
        } else {
          this.snackbarService.show(response.message || 'An unexpected error occurred.', 'error');
        }
      },
      error: (err: HttpErrorResponse) => {
        this.isLoading = false;
        this.snackbarService.show(err.error?.message || 'Login failed. Please check credentials.', 'error',5);
        console.error(err);
      }
    });
  }

  ngOnDestroy(): void {
    this.loginSubscription?.unsubscribe();
    this.socialAuthSubscription?.unsubscribe();
  }
}
