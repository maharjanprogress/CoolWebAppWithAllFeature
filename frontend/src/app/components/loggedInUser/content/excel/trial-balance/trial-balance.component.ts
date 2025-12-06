import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FileUploadComponent} from "../../../shared/file-upload/file-upload.component";
import {Subscription} from "rxjs";
import {ExcelService} from "../../../../../services/Excel/excel.service";
import {WebsocketService} from "../../../../../services/websocket/websocket.service";
import {SnackbarService} from "../../../../../services/snackbar.service";
import {CommonModule} from "@angular/common";
import {FileType} from "../../../../../model/file-types";
import {JobStatus} from "../../../../../model/api-responses";
import {environment} from "../../../../../../environments/environment";

@Component({
  selector: 'app-trial-balance',
  standalone: true,
  imports: [
    FileUploadComponent,
    CommonModule
  ],
  templateUrl: './trial-balance.component.html',
  styleUrl: './trial-balance.component.scss'
})
export class TrialBalanceComponent implements OnInit, OnDestroy {
  @ViewChild('trialBalanceUpload') trialBalanceUpload!: FileUploadComponent;
  @ViewChild('profitAndLossUpload') profitAndLossUpload!: FileUploadComponent;
  @ViewChild('balanceSheetUpload') balanceSheetUpload!: FileUploadComponent;
  @ViewChild('loanAgeingSheetUpload') loanAgeingSheetUpload!: FileUploadComponent;
  @ViewChild('loanSummarySheetUpload') loanSummarySheetUpload!: FileUploadComponent;
  @ViewChild('savingSummarySheetUpload') savingSummarySheetUpload!: FileUploadComponent;
  @ViewChild('fileUpload') fileUpload!: FileUploadComponent;

  allowedFileTypes = [FileType.XLSX, FileType.XLS, FileType.CSV];

  isProcessing = false;
  currentProgress = 0;
  currentMessage = '';
  uploading = false;

  excelDownloadUrl: string | null = null;
  powerpointDownloadUrl: string | null = null;

  private progressSubscription?: Subscription;

  constructor(
    private excelService: ExcelService,
    private wsService: WebsocketService,
    private snackbar: SnackbarService
  ) {}

  ngOnInit(): void {
    // Connect to WebSocket
    this.wsService.connect();

    // Check if there's an active job
    this.excelService.getStatus().subscribe({
      next: (jobDetail) => {
        const job = jobDetail.detail;
        if (job && (job.status === JobStatus.PENDING || job.status === JobStatus.PROCESSING)) {
          this.isProcessing = true;
          this.currentProgress = job.progress;
          this.currentMessage = job.currentStep;
        }
        if (job) {
          this.excelDownloadUrl = job.processedExcelFilePath ? environment.apiUrl + job.processedExcelFilePath : null;
          this.powerpointDownloadUrl = job.processedPowerpointFilePath ? environment.apiUrl + job.processedPowerpointFilePath : null;
        }
      },
      error: (err) => {
        this.snackbar.show("Failed to fetch job status: " + (err.error?.message || err.message), 'error', 3);
      } // No active job
    });

    // Subscribe to progress updates
    this.progressSubscription = this.wsService.progress$.subscribe({
      next: (update) => {
        if (update) {
          this.isProcessing = true;
          this.currentProgress = update.progress;
          this.currentMessage = update.message;
          //todo: Always Check for completion percentage of files
          if (update.excelComplete && update.progress === 65) {
            this.excelDownloadUrl = environment.apiUrl + update.message;
          }
          if (update.powerpointComplete && update.progress === 85) {
            this.powerpointDownloadUrl = environment.apiUrl + update.message;
          }
          if (update.status === JobStatus.COMPLETED || update.status === JobStatus.FAILED) {
            this.isProcessing = false;
            if (update.status === JobStatus.FAILED) {
              this.snackbar.show("Processing failed: " + update.message, 'error', 5);
            }
          }
        }
      }
    });
  }

  uploadFile(): void {
    const trialBalance = this.trialBalanceUpload.getFile();
    const profitAndLoss = this.profitAndLossUpload.getFile();
    const balanceSheet = this.balanceSheetUpload.getFile();
    const loanAgeingSheet = this.loanAgeingSheetUpload.getFile();
    const loanSummarySheetUpload = this.loanSummarySheetUpload.getFile();
    const savingSummarySheetUpload = this.savingSummarySheetUpload.getFile();

    if (
      !trialBalance || !profitAndLoss || !balanceSheet || !loanAgeingSheet
      || !loanSummarySheetUpload || !savingSummarySheetUpload
    ) {
      this.snackbar.show("Please select all three files to upload.", 'warning', 3);
      return;
    }

    this.uploading = true;
    // Reset previous results
    this.excelDownloadUrl = null;
    this.powerpointDownloadUrl = null;

    this.excelService.uploadExcel(
      trialBalance, profitAndLoss,
      balanceSheet, loanAgeingSheet,
      loanSummarySheetUpload, savingSummarySheetUpload
    ).subscribe({
      next: (jobDetail) => {
        const job = jobDetail.detail;
        this.uploading = false;
        this.isProcessing = true;
        if (job){
          this.currentProgress = job.progress;
          this.currentMessage = job.currentStep;
        }else {
          this.currentMessage = 'Failed to start processing job';
          this.currentProgress = 50;
        }

      },
      error: (error) => {
        this.uploading = false;
        this.snackbar.show('File upload failed: ' + (error.error?.message || error.message), 'error', 3);
      }
    });
  }

  ngOnDestroy(): void {
    this.progressSubscription?.unsubscribe();
    this.wsService.disconnect();
  }
}
