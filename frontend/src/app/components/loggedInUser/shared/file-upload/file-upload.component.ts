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
  @Input() singleFile: boolean = true; // NEW: Default to single file mode

  // Changed to support multiple files
  selectedFiles: File[] = [];
  isDragging = false;

  constructor(private snackbar: SnackbarService) {}

  onFileSelected(event: any) {
    const files = Array.from(event.target.files) as File[];
    if (files.length > 0) {
      this.handleFiles(files);
    }
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    this.isDragging = false;
    const files = Array.from(event.dataTransfer?.files || []) as File[];
    if (files.length > 0) {
      this.handleFiles(files);
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

  handleFiles(files: File[]) {
    // Validate file types
    const validFiles: File[] = [];
    for (const file of files) {
      if (this.onlyInclude.length > 0 && !this.onlyInclude.includes(file.type as FileType)) {
        const allowedExtensions = this.onlyInclude.map(type => fileTypeExtensions[type]?.join(', ')).join(', ');
        this.snackbar.show(`Invalid file type for ${file.name}. Allowed: ${allowedExtensions}`, 'error', 4);
        continue;
      }
      validFiles.push(file);
    }

    if (validFiles.length === 0) return;

    // Single file mode: replace existing file
    if (this.singleFile) {
      this.selectedFiles = [validFiles[0]];
    } else {
      // Multiple file mode: add to existing files
      this.selectedFiles.push(...validFiles);
    }
  }

  removeFile(index: number) {
    this.selectedFiles.splice(index, 1);
  }

  removeAllFiles() {
    this.selectedFiles = [];
  }

  // Method parent component calls to get file(s)
  getFile(): File | null {
    return this.selectedFiles.length > 0 ? this.selectedFiles[0] : null;
  }

  getFiles(): File[] {
    return this.selectedFiles;
  }

  getFileIcon(file: File): string {
    return fileTypeIcons[file.type] || fileTypeIcons['default'];
  }

  // Dynamically generate the accept string for the file input
  get acceptTypes(): string {
    if (this.onlyInclude.length === 0) {
      return '*/*';
    }
    return this.onlyInclude
      .map(type => fileTypeExtensions[type])
      .flat()
      .filter(ext => ext)
      .join(',');
  }
}
