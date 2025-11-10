import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CommonModule} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";

@Component({
  selector: 'app-image-expand',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './image-expand.component.html',
  styleUrl: './image-expand.component.scss'
})
export class ImageExpandComponent {
  @Input() imageUrl: string | null = null;
  @Output() close = new EventEmitter<void>();

  onClose(): void {
    this.close.emit();
  }
}
