import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

interface ImageModalState {
  show: boolean;
  imageUrl: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  private imageModalState = new BehaviorSubject<ImageModalState>({ show: false, imageUrl: null });

  getState(): Observable<ImageModalState> {
    return this.imageModalState.asObservable();
  }

  open(imageUrl: string): void {
    this.imageModalState.next({ show: true, imageUrl });
  }

  close(): void {
    this.imageModalState.next({ show: false, imageUrl: null });
  }
}
