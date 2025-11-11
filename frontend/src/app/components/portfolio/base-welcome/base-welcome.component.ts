import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from "@angular/common";
import { ChatplaceComponent } from "../chatplace/chatplace.component";
import {LoginService} from "../../../services/LoginAndRegistration/login.service";

@Component({
  selector: 'app-base-portfolio',
  standalone: true,
  imports: [
    CommonModule,
    ChatplaceComponent
  ],
  templateUrl: './base-welcome.component.html',
  styleUrl: './base-welcome.component.scss'
})
export class BaseWelcomeComponent implements OnInit, OnDestroy {
  constructor(private loginService: LoginService) { }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

  logout() {
    this.loginService.logout();
  }
}
