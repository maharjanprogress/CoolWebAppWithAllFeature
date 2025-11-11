import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {SessionService} from "../../services/session.service";

@Component({
  selector: 'app-redirect',
  standalone: true,
  template: `
  `,
  imports: []
})
export class RedirectComponent implements OnInit{
  constructor(
    private router: Router,
    private sessionService: SessionService
  ) {}

  ngOnInit(): void {
    if (this.sessionService.getToken()) {
      this.router.navigate(['/app'], { replaceUrl: true }).catch(error => {
        console.error('Navigation failed', error);
      });
    } else {
      this.router.navigate(['/login'], { replaceUrl: true }).catch(error => {
        console.error('Navigation failed', error);
      });
    }
  }
}
