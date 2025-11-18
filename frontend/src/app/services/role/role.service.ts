import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RoleListResponse } from '../../model/api-responses';
import {ApiService} from "../mainApi/api.service";

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  constructor(private apiService: ApiService) { }

  getAllRoles(): Observable<RoleListResponse> {
    return this.apiService.get<RoleListResponse>('/api/roles/all');
  }
}
