import {Component, EventEmitter, HostBinding, Input, Output} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {MenuDTO} from "../../../model/api-responses";
import { NgForOf, NgIf} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {SessionService} from "../../../services/session.service";

@Component({
  selector: 'app-dynamic-menu',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    RouterLink,
    RouterLinkActive,
    MatIconModule
  ],
  templateUrl: './dynamic-menu.component.html',
  styleUrl: './dynamic-menu.component.scss'
})
export class DynamicMenuComponent {
  @HostBinding('class.menu-open') isMenuOpen = true;
  @Output() menuToggle = new EventEmitter<boolean>();
  @Input() menuItems: MenuDTO[] = [];
  @Input() activeSuperMenu: number | null = null;
  @Input() isLoading = false;

  constructor(
    private sessionService: SessionService,
    private router: Router
  ) {}

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
    this.menuToggle.emit(this.isMenuOpen);
  }

  toggleSubMenu(superMenuId: number): void {
    this.activeSuperMenu = this.activeSuperMenu === superMenuId ? null : superMenuId;
  }

  logout(): void {
    this.sessionService.clearSession();
    this.router.navigate(['/login']).catch(error => {
      console.error('Navigation failed', error);
    });
  }
}
