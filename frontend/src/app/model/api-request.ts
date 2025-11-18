import {UserDTO} from "./api-responses";

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

export type CreateUserDTO = Omit<UserDTO, 'id'>;
