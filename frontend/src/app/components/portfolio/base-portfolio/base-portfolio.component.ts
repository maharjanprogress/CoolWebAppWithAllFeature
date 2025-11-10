import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-base-portfolio',
  standalone: true,
  imports: [
    CommonModule,
  ],
  templateUrl: './base-portfolio.component.html',
  styleUrl: './base-portfolio.component.scss'
})
export class BasePortfolioComponent implements OnInit, OnDestroy {

  constructor() {
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }
}
