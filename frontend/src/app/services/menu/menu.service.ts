import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {ApiService} from "../mainApi/api.service";
import {MenuResponse} from "../../model/api-responses";

@Injectable({
  providedIn: 'root'
})
export class MenuService {

  constructor(private apiService: ApiService) { }

  getMenuForRole(): Observable<MenuResponse> {
    return this.apiService.get<MenuResponse>('/api/menu-templates/role/formatted');
  }
}
