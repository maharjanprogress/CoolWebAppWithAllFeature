import {Component, EventEmitter, HostBinding, OnDestroy, OnInit, Output} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {MenuDTO, ResponseStatus} from "../../../model/api-responses";
import {Subscription} from "rxjs";
import { NgForOf, NgIf} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {HttpErrorResponse} from "@angular/common/http";
import {MenuService} from "../../../services/menu/menu.service";
import {SessionService} from "../../../services/session.service";
import {SnackbarService} from "../../../services/snackbar.service";

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
export class DynamicMenuComponent implements OnInit, OnDestroy {
  @HostBinding('class.menu-open') isMenuOpen = true;
  @Output() menuToggle = new EventEmitter<boolean>();

  menuItems: MenuDTO[] = [];
  activeSuperMenu: number | null = null;
  isLoading = false;
  private menuSubscription?: Subscription;

  constructor(
    private menuService: MenuService,
    private sessionService: SessionService,
    private router: Router,
    private snackbarService: SnackbarService
  ) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.menuSubscription = this.menuService.getMenuForRole().subscribe({
      next: (response) => {
        if (response.status === ResponseStatus.SUCCESS) {
          this.menuItems = response.details || [];
        } else {
          this.snackbarService.show(response.message || 'Failed to load menu.', 'error');
        }
        this.isLoading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.snackbarService.show(err.error?.message || 'A server error occurred while loading the menu.', 'error');
        this.isLoading = false;
        console.error(err);
      }
    });
  }

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

  ngOnDestroy(): void {
    this.menuSubscription?.unsubscribe();
  }
}
