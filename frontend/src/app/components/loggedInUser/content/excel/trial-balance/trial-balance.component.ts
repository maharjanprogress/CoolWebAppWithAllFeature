import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FileUploadComponent} from "../../../shared/file-upload/file-upload.component";
import {Subscription} from "rxjs";
import {ExcelService} from "../../../../../services/Excel/excel.service";
import {WebsocketService} from "../../../../../services/websocket/websocket.service";
import {SnackbarService} from "../../../../../services/snackbar.service";
import {CommonModule} from "@angular/common";

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
  @ViewChild('fileUpload') fileUpload!: FileUploadComponent;

  isProcessing = false;
  currentProgress = 0;
  currentMessage = '';
  uploading = false;

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
        if (job && (job.status === 'PENDING' || job.status === 'PROCESSING')) {
          this.isProcessing = true;
          this.currentProgress = job.progress;
          this.currentMessage = job.currentStep;
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

          if (update.status === 'COMPLETED' || update.status === 'FAILED') {
            this.isProcessing = false;
            if (update.status === 'COMPLETED') {
              this.snackbar.show("Processing completed successfully.", 'success', 3);
            } else {
              this.snackbar.show("Processing failed: " + update.message, 'error', 5);
            }
          }
        }
      }
    });
  }

  uploadFile(): void {
    const file = this.fileUpload.getFile();
    if (!file) {
      this.snackbar.show("Please select a file to upload.", 'warning', 3);
      return;
    }

    this.uploading = true;
    this.excelService.uploadExcel(file).subscribe({
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
