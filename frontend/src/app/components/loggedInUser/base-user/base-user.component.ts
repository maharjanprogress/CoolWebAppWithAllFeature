import {Component, OnInit, ViewChild} from '@angular/core';
import { RouterOutlet} from "@angular/router";
import {NgIf} from "@angular/common";
import {SnackbarComponent} from "../../shared/snackbar/snackbar.component";
import {DynamicMenuComponent} from "../dynamic-menu/dynamic-menu.component";
import {MatIconModule} from "@angular/material/icon";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {UserRole} from "../../../model/api-responses";
import {SessionService} from "../../../services/session.service";

@Component({
  selector: 'app-base-user',
  standalone: true,
  imports: [ RouterOutlet, NgIf, SnackbarComponent, DynamicMenuComponent, MatIconModule, DragDropModule],
  templateUrl: './base-user.component.html',
  styleUrl: './base-user.component.scss'
})
export class BaseUserComponent implements OnInit {
  token: string = '';
  role: UserRole | null = null;
  isMenuOpen = true;

  @ViewChild(DynamicMenuComponent) menuComponent!: DynamicMenuComponent;

  constructor(private sessionService: SessionService) {
  }

  ngOnInit(): void {
    this.token = this.sessionService.getToken();
    this.role = this.sessionService.getRole();
  }

  onMenuToggle(isOpen: boolean): void {
    this.isMenuOpen = isOpen;
  }

  openMenu(): void {
    if (this.menuComponent) {
      this.menuComponent.toggleMenu();
    }
  }
}
