import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-base-portfolio',
  standalone: true,
  imports: [
    CommonModule,
  ],
  templateUrl: './base-welcome.component.html',
  styleUrl: './base-welcome.component.scss'
})
export class BaseWelcomeComponent implements OnInit, OnDestroy {

  constructor() {
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }
}
