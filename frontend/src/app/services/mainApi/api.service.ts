import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {environment} from "../../../environments/environment";
import {SnackbarService} from "../snackbar.service";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient,
    private snackbarService: SnackbarService
  ) { }

  getWithoutAuth<T>(path: string, params: HttpParams = new HttpParams()): Observable<T> {
    return this.http.get<T>(`${this.apiUrl}${path}`, { headers: this.getHeadersWithoutAuth(), params })
      .pipe(catchError(this.handleError.bind(this)));
  }

  postWithoutAuth<T>(path:string, body: object = {}): Observable<T> {
    return this.http.post<T>(`${this.apiUrl}${path}`, body, { headers: this.getHeadersWithoutAuth() })
      .pipe(catchError(this.handleError.bind(this)));
  }

  // // Generic GET method
  // get<T>(path: string, params: HttpParams = new HttpParams()): Observable<T> {
  //   return this.http.get<T>(`${this.apiUrl}${path}`, { headers: this.getHeaders(), params })
  //
  //     .pipe(catchError(this.handleError.bind(this)));
  //
  // }
  // // Generic POST method
  // post<T>(path:string, body: object = {}): Observable<T> {
  //   return this.http.post<T>(`${this.apiUrl}${path}`, body, { headers: this.getHeaders() })
  //     .pipe(catchError(this.handleError.bind(this)));
  //
  // }
  //
  // // Generic POST method for multipart/form-data (file uploads)
  // postMultipart<T>(path: string, formData: FormData): Observable<T> {
  //   return this.http.post<T>(`${this.apiUrl}${path}`, formData, { headers: this.getHeaders(true) })
  //     .pipe(catchError(this.handleError.bind(this)));
  // }
  //
  // // You can add put, delete, etc. in the same way
  // put<T>(path: string, body: object = {}): Observable<T> {
  //   return this.http.put<T>(`${this.apiUrl}${path}`, body, { headers: this.getHeaders() })
  //     .pipe(catchError(this.handleError.bind(this)));
  // }
  //
  // // Generic DELETE method
  // delete<T>(path: string): Observable<T> {
  //   return this.http.delete<T>(`${this.apiUrl}${path}`, { headers: this.getHeaders() })
  //     .pipe(catchError(this.handleError.bind(this)));
  // }
  //
  // private getHeaders(isMultipart: boolean = false): HttpHeaders {
  //   let headers = new HttpHeaders();
  //
  //   if (!isMultipart) {
  //     // For JSON requests, set the Content-Type.
  //     // For multipart, let the browser set it with the correct boundary.
  //     headers = headers.set('Content-Type', 'application/json');
  //   }
  //
  //   const token = this.sessionService.getToken();
  //   if (token) {
  //     headers = headers.set('Authorization', `Bearer ${token}`);
  //   }
  //   return headers;
  // }

  private getHeadersWithoutAuth(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json'
    });
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 401 && error.error?.detail?.tokenError) {
      this.snackbarService.show('Please Contact Administrator.', 'error');
    }
    return throwError(() => error);
  }
}
