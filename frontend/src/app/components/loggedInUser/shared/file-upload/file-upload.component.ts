import { Component } from '@angular/core';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [],
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.scss'
})
export class FileUploadComponent {
  selectedFile: File | null = null;

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    this.selectedFile = event.dataTransfer?.files[0] || null;
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
  }

  // Method parent will call
  getFile(): File | null {
    return this.selectedFile;
  }
}
