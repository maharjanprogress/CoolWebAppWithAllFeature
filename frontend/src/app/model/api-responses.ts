export interface RestApiResponse<T = any, U = any[]> {
  status?: ResponseStatus;
  message?: string;
  detail?: T;
  details?: U;
}

export enum ResponseStatus{
  SUCCESS = 'SUCCESS',
  ERROR = 'ERROR',
  CREATED = 'CREATED',
  NOT_FOUND = 'NOT_FOUND',
  BAD_REQUEST = 'BAD_REQUEST',
  UNAUTHORIZED = 'UNAUTHORIZED',
  FORBIDDEN = 'FORBIDDEN',
  INTERNAL_SERVER_ERROR = 'INTERNAL_SERVER_ERROR',
}

export interface LoginDetails{
  role: UserRole;
  userName: string;
  token: string;
  userId: number;
}

export enum UserRole {
  ADMIN = 'ADMIN',
  CLIENT = 'CLIENT'
}

export interface UserDTO {
  id: number;
  userName: string;
  password: string;
  firstName: string;
  lastName: string;
  middleName: string | null;
  phoneNumber: string;
  roleId: number;
  role: UserRole;
  email: string | null;
  isActive: boolean | null;
}

export interface SubMenuDTO {
  id: number;
  title: string;
  url: string;
}

export interface MenuDTO {
  id: number;
  title: string;
  subMenuDTOList: SubMenuDTO[];
}

export interface RoleDTO {
  id: number;
  roleAlias: UserRole
  roleName: string;
  remarks: string;
}

export interface ProcessingJob {
  id: number;
  fileName: string;
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED';
  progress: number;
  currentStep: string;
  errorMessage?: string;
}

export interface ProgressUpdate {
  jobId: number;
  progress: number;
  message: string;
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED';
}

export class RoleUtils {
  static fromString(role: string): UserRole | null {
    return Object.values(UserRole).includes(role as UserRole) ? role as UserRole : null;
  }

  static toString(role: UserRole): string {
    return role.valueOf();
  }

  static isValidRole(role: string): role is UserRole {
    return Object.values(UserRole).includes(role as UserRole);
  }
}

export interface LoginResponse extends RestApiResponse<LoginDetails, never> {}

export interface UserListResponse extends RestApiResponse<null, UserDTO[]> {}

export interface UserResponse extends RestApiResponse<UserDTO, never> {}

export interface MenuResponse extends RestApiResponse<null, MenuDTO[]> {}

export interface RoleListResponse extends RestApiResponse<null, RoleDTO[]> {}

export interface FileProcessResponse extends RestApiResponse<ProcessingJob, never> {}

