import { Routes } from '@angular/router';
import {BaseWelcomeComponent} from "./components/portfolio/base-welcome/base-welcome.component";

export const routes: Routes = [
  { path: '', redirectTo: 'portfolio', pathMatch: 'full' },
  { path: 'portfolio', component: BaseWelcomeComponent }
];
