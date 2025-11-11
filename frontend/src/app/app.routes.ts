import { Routes } from '@angular/router';
import {BaseWelcomeComponent} from "./components/portfolio/base-welcome/base-welcome.component";
import {LoginComponent} from "./components/login/login.component";
import {RedirectComponent} from "./components/redirect/redirect.component";

export const routes: Routes = [
  { path: '', redirectTo: 'redirect', pathMatch: 'full' },
  {path: 'redirect', component: RedirectComponent},
  { path: 'app', component: BaseWelcomeComponent },
  { path: 'login', component: LoginComponent }
];
