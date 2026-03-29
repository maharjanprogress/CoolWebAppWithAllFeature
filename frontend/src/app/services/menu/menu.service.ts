import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {ApiService} from "../mainApi/api.service";
import {MenuResponse, MenuTemplateDTO, MenuTemplateResponse} from "../../model/api-responses";

@Injectable({
  providedIn: 'root'
})
export class MenuService {

  constructor(private apiService: ApiService) { }

  getMenuForRole(): Observable<MenuResponse> {
    return this.apiService.get<MenuResponse>('/api/menu-templates/role/formatted');
  }

  getTemplateByRoleId(roleId: number): Observable<MenuTemplateResponse> {
    return this.apiService.get<MenuTemplateResponse>(`/api/menu-templates/role/${roleId}`);
  }

  saveMenuTemplate(menuTemplate: MenuTemplateDTO): Observable<MenuTemplateResponse> {
    return this.apiService.post<MenuTemplateResponse>('/api/menu-templates', menuTemplate);
  }
}
