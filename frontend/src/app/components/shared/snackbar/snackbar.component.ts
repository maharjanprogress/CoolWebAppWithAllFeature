import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { SnackbarService, SnackbarState } from '../../../services/snackbar.service';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-snackbar',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './snackbar.component.html',
  styleUrl: './snackbar.component.scss'
})
export class SnackbarComponent implements OnInit, OnDestroy {
  state: SnackbarState = { show: false, message: '', type: 'info', duration: 3000 };
  private subscription?: Subscription;
  private timer?: any;

  constructor(private snackbarService: SnackbarService) {}

  ngOnInit(): void {
    this.subscription = this.snackbarService.getState().subscribe(state => {
      this.state = state;
      if (this.state.show) {
        clearTimeout(this.timer);
        this.timer = setTimeout(() => this.close(), this.state.duration);
      }
    });
  }

  getIcon(): string {
    const iconMap = { success: 'check_circle', error: 'error', warning: 'warning', info: 'info' };
    return iconMap[this.state.type] || 'info';
  }

  close(): void {
    this.snackbarService.hide();
    clearTimeout(this.timer);
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
    clearTimeout(this.timer);
  }
}
