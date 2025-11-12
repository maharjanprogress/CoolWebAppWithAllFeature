export interface MessegeRequest {
  sessionId: string;
  userId: number;
  message: string;
}


export interface LoginDTO {
  userName: string;
  password: string;
}

export interface OauthLoginRequest {
  token: string;
}
