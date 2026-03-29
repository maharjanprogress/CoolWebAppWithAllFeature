import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from "@angular/router";
import {NgIf} from "@angular/common";
import {SnackbarComponent} from "../../shared/snackbar/snackbar.component";
import {DynamicMenuComponent} from "../dynamic-menu/dynamic-menu.component";
import {MatIconModule} from "@angular/material/icon";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {MenuDTO, ResponseStatus, UserRole} from "../../../model/api-responses";
import {SessionService} from "../../../services/session.service";
import {MenuService} from "../../../services/menu/menu.service";
import {SnackbarService} from "../../../services/snackbar.service";
import {HttpErrorResponse} from "@angular/common/http";
import {filter, Subscription} from "rxjs";

@Component({
  selector: 'app-base-user',
  standalone: true,
  imports: [ RouterOutlet, NgIf, SnackbarComponent, DynamicMenuComponent, MatIconModule, DragDropModule],
  templateUrl: './base-user.component.html',
  styleUrl: './base-user.component.scss'
})
export class BaseUserComponent implements OnInit, OnDestroy {
  token: string = '';
  role: UserRole | null = null;
  isMenuOpen = true;
  isMenuLoading = true;
  menuLoadError: string | null = null;
  menuItems: MenuDTO[] = [];
  activeSuperMenu: number | null = null;

  @ViewChild(DynamicMenuComponent) menuComponent!: DynamicMenuComponent;
  private readonly subscriptions = new Subscription();

  constructor(
    private sessionService: SessionService,
    private menuService: MenuService,
    private snackbarService: SnackbarService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.token = this.sessionService.getToken();
    this.role = this.sessionService.getRole();
    this.loadMenu();
    this.subscriptions.add(
      this.router.events
        .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
        .subscribe(() => this.updateActiveMenuFromRoute())
    );
  }

  onMenuToggle(isOpen: boolean): void {
    this.isMenuOpen = isOpen;
  }

  openMenu(): void {
    if (this.menuComponent) {
      this.menuComponent.toggleMenu();
    }
  }

  retryMenuLoad(): void {
    this.loadMenu();
  }

  private loadMenu(): void {
    this.isMenuLoading = true;
    this.menuLoadError = null;

    this.subscriptions.add(
      this.menuService.getMenuForRole().subscribe({
        next: (response) => {
          if (response.status === ResponseStatus.SUCCESS) {
            this.menuItems = response.details || [];
            this.handleInitialRoute();
          } else {
            this.menuItems = [];
            const errorMessage = response.message || 'Failed to load menu.';
            this.menuLoadError = errorMessage;
            this.snackbarService.show(errorMessage, 'error');
          }
          this.isMenuLoading = false;
        },
        error: (err: HttpErrorResponse) => {
          this.menuItems = [];
          const errorMessage = err.error?.message || 'A server error occurred while loading the menu.';
          this.menuLoadError = errorMessage;
          this.snackbarService.show(errorMessage, 'error');
          this.isMenuLoading = false;
          console.error(err);
        }
      })
    );
  }

  private handleInitialRoute(): void {
    const currentChildPath = this.getCurrentChildPath();
    const matchingSuperMenu = currentChildPath ? this.findSuperMenuBySubMenuUrl(currentChildPath) : null;

    if (matchingSuperMenu) {
      this.activeSuperMenu = matchingSuperMenu.id;
      return;
    }

    const initialSubMenu = this.findPrimarySubMenu();
    if (!initialSubMenu) {
      return;
    }

    this.activeSuperMenu = initialSubMenu.parentMenuId;
    this.router.navigate(['/user', initialSubMenu.url], {replaceUrl: true}).catch(error => {
      console.error('Navigation failed', error);
    });
  }

  private updateActiveMenuFromRoute(): void {
    const currentChildPath = this.getCurrentChildPath();
    const matchingSuperMenu = currentChildPath ? this.findSuperMenuBySubMenuUrl(currentChildPath) : null;
    if (matchingSuperMenu) {
      this.activeSuperMenu = matchingSuperMenu.id;
    }
  }

  private findSuperMenuBySubMenuUrl(subMenuUrl: string): MenuDTO | null {
    return this.menuItems.find(menu =>
      menu.subMenuDTOList.some(subMenu => subMenu.url === subMenuUrl)
    ) || null;
  }

  private getCurrentChildPath(): string | null {
    const currentUrl = this.router.url.split('?')[0].split('#')[0];
    if (!currentUrl.startsWith('/user')) {
      return null;
    }

    const childPath = currentUrl.slice('/user'.length).replace(/^\/+/, '');
    return childPath.length > 0 ? childPath : null;
  }

  private findPrimarySubMenu() {
    return this.menuItems
      .flatMap(menu => menu.subMenuDTOList)
      .find(subMenu => subMenu.primaryMenu) ?? this.menuItems.flatMap(menu => menu.subMenuDTOList)[0];
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
