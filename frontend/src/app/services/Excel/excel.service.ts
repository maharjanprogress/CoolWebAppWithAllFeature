import { Injectable } from '@angular/core';
import {ApiService} from "../mainApi/api.service";
import {FileProcessResponse} from "../../model/api-responses";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ExcelService {

  constructor(private apiService : ApiService) { }

  uploadExcel(
    trialBalance: File,
    profitAndLoss: File,
    balanceSheet: File,
    loanAgeingSheet: File,
    loanSummary: File,
    savingSummary: File,
    previousBalanceSheet: File,
    loanMember: File,
    savingMember: File,
    previousLoanAgeing: File
  ): Observable<FileProcessResponse> {
    const formData = new FormData();
    formData.append('trialBalance', trialBalance);
    formData.append('profitAndLoss', profitAndLoss);
    formData.append('balanceSheet', balanceSheet);
    formData.append('loanAgeingSheet', loanAgeingSheet);
    formData.append('loanSummary', loanSummary);
    formData.append('savingSummary', savingSummary);
    formData.append('previousBalanceSheet', previousBalanceSheet);
    formData.append('loanMember', loanMember);
    formData.append('savingMember', savingMember);
    formData.append('previousLoanAgeing', previousLoanAgeing);
    return this.apiService.postMultipart<FileProcessResponse>('/api/excel/upload', formData);
  }

  getStatus(): Observable<FileProcessResponse> {
    return this.apiService.get<FileProcessResponse>('/api/excel/status');
  }
}
