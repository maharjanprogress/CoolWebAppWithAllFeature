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

export interface JobExperiencePhotoDTO {
  id: number;
  imageUrl: string;
  caption: string;
  displayOrder: number;
}

export interface JobExperienceDTO {
  id: number;
  title: string;
  company: string;
  location: string | null;
  employmentType: string;
  description: string;
  achievements: string[];
  technologiesUsed: string[];
  startDate: string;
  endDate: string;
  isCurrent: boolean;
  displayOrder: number;
  insertUser: string;
  editUser: string;
  photos: JobExperiencePhotoDTO[];
  extra?: Record<string, string> | null;
}

export interface jobExperienceResponse extends RestApiResponse<never, JobExperienceDTO[]> {}
