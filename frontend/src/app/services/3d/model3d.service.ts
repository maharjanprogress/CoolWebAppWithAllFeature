import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface Model3dState {
  show: boolean;
  modelUrl: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class Model3dService {
  private model3dState = new BehaviorSubject<Model3dState>({ show: false, modelUrl: null });

  getState(): Observable<Model3dState> {
    return this.model3dState.asObservable();
  }

  open(modelUrl: string): void {
    this.model3dState.next({ show: true, modelUrl });
  }

  close(): void {
    this.model3dState.next({ show: false, modelUrl: null });
  }
}
