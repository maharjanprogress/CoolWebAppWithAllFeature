import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Input,
  OnDestroy,
  Output,
  ViewChild
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import {SnackbarService} from "../../../services/snackbar.service";

@Component({
  selector: 'app-model3d',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatProgressBarModule],
  templateUrl: './model3d.component.html',
  styleUrl: './model3d.component.scss'
})
export class Model3dComponent implements AfterViewInit, OnDestroy {
  @Input() modelUrl: string | null = null;
  @Output() close = new EventEmitter<void>();

  @ViewChild('rendererCanvas', { static: true })
  private rendererCanvas!: ElementRef<HTMLCanvasElement>;

  isLoading = true;
  loadingPercentage = 0;

  private scene!: THREE.Scene;
  private camera!: THREE.PerspectiveCamera;
  private renderer!: THREE.WebGLRenderer;
  private controls!: OrbitControls;
  private frameId: number | null = null;

  constructor(
    private snackbarService: SnackbarService
  ) {}

  ngAfterViewInit(): void {
    this.initScene();
    if (this.modelUrl) {
      this.loadModel(this.modelUrl);
    }
    this.animate();
  }

  ngOnDestroy(): void {
    if (this.frameId !== null) {
      cancelAnimationFrame(this.frameId);
    }
    this.renderer.dispose();
    this.controls.dispose();
  }

  onClose(): void {
    this.close.emit();
  }

  private initScene(): void {
    this.scene = new THREE.Scene();
    this.scene.background = new THREE.Color(0x1a232e); // Dark blue-gray background

    const canvas = this.rendererCanvas.nativeElement;
    this.camera = new THREE.PerspectiveCamera(75, canvas.clientWidth / canvas.clientHeight, 0.1, 1000);
    this.camera.position.z = 13;
    this.camera.position.x = 20;
    this.camera.position.y = 10;

    this.renderer = new THREE.WebGLRenderer({ canvas, antialias: true });
    this.renderer.setSize(canvas.clientWidth, canvas.clientHeight);
    this.renderer.setPixelRatio(window.devicePixelRatio);

    const ambientLight = new THREE.AmbientLight(0xffffff, 1.5);
    this.scene.add(ambientLight);

    const sideDirectionalLight = new THREE.DirectionalLight(0xffffff, 5);
    sideDirectionalLight.position.set(170,20,110);
    sideDirectionalLight.castShadow = false;
    this.scene.add(sideDirectionalLight);


    const topDirectionalLight = new THREE.DirectionalLight(0xffffff, 5);
    topDirectionalLight.position.set(-160, 20, -120);
    this.scene.add(topDirectionalLight);

    // const axesHelper = new THREE.AxesHelper(5);
    // this.scene.add(axesHelper);
    //
    // const  keyLightHelper = new THREE.DirectionalLightHelper(sideDirectionalLight, 5);
    // this.scene.add(keyLightHelper);
    //
    // const  keyLightHelper1 = new THREE.DirectionalLightHelper(topDirectionalLight, 5);
    // this.scene.add(keyLightHelper1);

    this.controls = new OrbitControls(this.camera, this.renderer.domElement);
    this.controls.enableDamping = true;
  }

  private loadModel(url: string): void {
    const loader = new GLTFLoader();
    loader.load(url,
      (gltf) => {
        this.scene.add(gltf.scene);
        this.isLoading = false;
      },
      (progress) => {
        this.loadingPercentage = (progress.loaded / progress.total) * 100;
      },
      (error) => {
        console.error('An error happened while loading the model', error);
        this.snackbarService.show('Failed to load 3D model.', 'error');
        this.isLoading = false;
      }
    );
  }

  private animate(): void {
    this.frameId = requestAnimationFrame(() => this.animate());
    this.controls.update();
    this.renderer.render(this.scene, this.camera);
  }

  @HostListener('window:resize', ['$event'])
  onWindowResize() {
    const canvas = this.rendererCanvas.nativeElement;
    this.camera.aspect = canvas.clientWidth / canvas.clientHeight;
    this.camera.updateProjectionMatrix();
    this.renderer.setSize(canvas.clientWidth, canvas.clientHeight);
  }
}
