import {Component, NgZone, OnDestroy, OnInit} from '@angular/core';
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
import {LoginDTO} from "../../model/api-request";
import {LoginService} from "../../services/LoginAndRegistration/login.service";
import {SnackbarService} from "../../services/snackbar.service";
import {
  FacebookLoginProvider,
  SocialAuthService,
} from "@abacritt/angularx-social-login";
declare const google: any;


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
export class LoginComponent implements OnInit, OnDestroy{
  isLoading = false;
  private loginSubscription?: Subscription;
  private socialAuthSubscription?: Subscription;

  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private loginService: LoginService,
    private router: Router,
    private snackbarService: SnackbarService,
    private ngZone: NgZone,
    private authService: SocialAuthService
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  ngOnInit() {
    // Wait for Google script to load
    this.initGoogleSignIn();
  }

  initGoogleSignIn() {
    if (typeof google === 'undefined') {
      console.log("temporarily waiting for google sdk");
      setTimeout(() => this.initGoogleSignIn(), 100);
      return;
      // this is to handle the asynchronous loading of Google's SDK
    }

    google.accounts.id.initialize({
      client_id: '169761438157-p3m6mv3bmfguqnb53bq6a5mmaqqredvh.apps.googleusercontent.com',
      callback: (response: any) => this.handleGoogleResponse(response)
    });

    google.accounts.id.renderButton(
      document.getElementById('googleButton'),
      {
        theme: 'outline',
        size: 'large',
        width: 250
      }
    );
  }

  handleGoogleResponse(response: any) {
    this.ngZone.run(() => {
      this.isLoading = true;
      this.loginService.loginWithGoogle(response.credential).subscribe({
        next: (res) => {
          this.isLoading = false;
          if (res.status === ResponseStatus.SUCCESS) {
            this.snackbarService.show('Login successful!', 'success', 2);
            this.router.navigate(['/app']).catch(error => {
              console.error('Navigation failed', error)
            });
          }
        },
        error: (err) => {
          this.isLoading = false;
          this.snackbarService.show(err.error?.message ||'Login failed', 'error');
          console.error('Google login error:', err);
        }
      });
    });
  }

  signInWithFacebook(): void {
    this.isLoading = true;
    this.authService.signIn(FacebookLoginProvider.PROVIDER_ID)
      .then((facebookUser) => {
        this.isLoading = false;
        console.log("Facebook user info:");
        console.log(facebookUser);
        this.loginService.loginWithFacebook(facebookUser.authToken).subscribe({
          next: (res) => {
            this.isLoading = false;
            if (res.status === ResponseStatus.SUCCESS) {
              this.snackbarService.show('Login successful!', 'success', 2);
              this.router.navigate(['/app']).catch(error => {
                console.error('Navigation failed', error)
              });
            }
          },
          error: (err) => {
            this.isLoading = false;
            this.snackbarService.show(err.error?.message ||'Login failed', 'error');
            console.error('Google login error:', err);
          }
        });
      })
      .catch((err) => {
        this.isLoading = false;
      console.error('Facebook login error:', err);
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
