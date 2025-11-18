import {Component, Input} from '@angular/core';
import {FileType, fileTypeExtensions, fileTypeIcons} from "../../../../model/file-types";
import {CommonModule} from "@angular/common";
import {SnackbarService} from "../../../../services/snackbar.service";

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.scss'
})
export class FileUploadComponent {
  @Input() onlyInclude: FileType[] = [];

  selectedFile: File | null = null;
  fileIcon = '';
  isDragging = false;

  constructor(private snackbar: SnackbarService) {}

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.handleFile(file);
    }
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    this.isDragging = false;
    const file = event.dataTransfer?.files[0];
    if (file) {
      this.handleFile(file);
    }
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    this.isDragging = true;
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    this.isDragging = false;
  }

  handleFile(file: File) {
    if (this.onlyInclude.length > 0 && !this.onlyInclude.includes(file.type as FileType)) {
      const allowedExtensions = this.onlyInclude.map(type => fileTypeExtensions[type]?.join(', ')).join(', ');
      this.snackbar.show(`Invalid file type. Please upload one of: ${allowedExtensions}`, 'error', 4);
      return;
    }
    this.selectedFile = file;
    this.fileIcon = fileTypeIcons[file.type] || fileTypeIcons['default'];
  }

  removeFile() {
    this.selectedFile = null;
    this.fileIcon = '';
  }

  // Method parent will call
  getFile(): File | null {
    return this.selectedFile;
  }

  // Dynamically generate the accept string for the file input
  get acceptTypes(): string {
    if (this.onlyInclude.length === 0) {
      return '*/*'; // Allow all files if no filter is provided
    }
    return this.onlyInclude
      .map(type => fileTypeExtensions[type])
      .flat()
      .filter(ext => ext) // Filter out any undefined extensions
      .join(',');
  }
}
