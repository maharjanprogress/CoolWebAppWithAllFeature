import { Injectable } from '@angular/core';
import {ApiService} from "../mainApi/api.service";
import {FileProcessResponse} from "../../model/api-responses";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ExcelService {

  constructor(private apiService : ApiService) { }

  uploadExcel(file: File): Observable<FileProcessResponse> {
    const formData = new FormData();
    formData.append('file', file);
    return this.apiService.postMultipart<FileProcessResponse>('/api/excel/upload', formData);
  }

  getStatus(): Observable<FileProcessResponse> {
    return this.apiService.get<FileProcessResponse>('/api/excel/status');
  }
}
