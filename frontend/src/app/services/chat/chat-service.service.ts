import {Injectable} from '@angular/core';
import {SessionService} from "../session.service";
import {environment} from "../../../environments/environment";
import {MessegeRequest} from "../../model/api-request";
import {HttpClient} from "@angular/common/http";
import {PageableResponse, ResponseStatus, RestApiResponse} from "../../model/api-responses";
import {catchError, map, Observable, throwError} from "rxjs";
import {ApiService} from "../mainApi/api.service";

@Injectable({
  providedIn: 'root'
})
export class ChatServiceService{
  sessionId: string = '';
  userId = environment.userId;
  apiUrl = environment.apiUrl;

  constructor(private http: HttpClient,private sessionService: SessionService, private apiService: ApiService) {
    this.sessionId = this.sessionService.getSessionId();
  }

  getMessageResponse(message: string): Observable<string> {
    const messageRequest: MessegeRequest = {
      sessionId: this.sessionId,
      userId: this.userId,
      message: message
    };

    return this.http.post<RestApiResponse>(`${this.apiUrl}/chat`, messageRequest)
      .pipe(
        map((response: RestApiResponse) => {
          if (response.status === ResponseStatus.SUCCESS) {
            return response.message || 'Empty response';
          } else {
            throw new Error(response.message || 'Unknown error occurred');
          }
        }),
        catchError(error => {
          if (error.error && error.error.message) {
            return throwError(() => new Error(error.error.message));
          }

          if (error instanceof Error) {
            return throwError(() => error);
          }

          return throwError(() => new Error('Something went wrong. Please try again later.'));
        })
      );
  }

  getChatHistoryForSessionId(pageNo: number = 0): Observable<PageableResponse>{
    return this.apiService.getWithoutAuth<PageableResponse>(`/chat/history/${environment.userId}/${this.sessionId}/${pageNo}`);
  }
}
