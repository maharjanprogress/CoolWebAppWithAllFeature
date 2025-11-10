import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../../services/theme/theme.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-dark-mode-toggle',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dark-mode-toggle.component.html',
  styleUrls: ['./dark-mode-toggle.component.scss']
})
export class DarkModeToggleComponent {
  isDarkMode$: Observable<boolean>;

  constructor(private themeService: ThemeService) {
    this.isDarkMode$ = this.themeService.isDarkMode$;
  }

  toggleTheme(): void {
    this.themeService.toggleTheme();
  }
}