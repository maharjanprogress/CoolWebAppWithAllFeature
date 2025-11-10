import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SnackbarService} from "./services/snackbar.service";
import {ImageService} from "./services/image/image.service";
import {SnackbarComponent} from "./components/shared/snackbar/snackbar.component";
import {ImageExpandComponent} from "./components/shared/image-expand/image-expand.component";
import {AsyncPipe, NgIf} from "@angular/common";
import {Observable} from "rxjs";
import {Model3dService, Model3dState} from "./services/3d/model3d.service";
import {Model3dComponent} from "./components/shared/model3d/model3d.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SnackbarComponent, ImageExpandComponent, AsyncPipe, NgIf, Model3dComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  imageModalState$: Observable<{ show: boolean, imageUrl: string | null }>;
  model3dState$!: Observable<Model3dState>;
  constructor(private snackbarService: SnackbarService,
              private imageModalService: ImageService,
              private model3dService: Model3dService){
    this.imageModalState$ = this.imageModalService.getState();
    this.model3dState$ = this.model3dService.getState();
  }

  closeImageModal(): void {
    this.imageModalService.close();
  }

  closeModel3d(): void {
    this.model3dService.close();
  }
}
