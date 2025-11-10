import { Routes } from '@angular/router';
import {BasePortfolioComponent} from "./components/portfolio/base-portfolio/base-portfolio.component";

export const routes: Routes = [
  { path: '', redirectTo: 'portfolio', pathMatch: 'full' },
  { path: 'portfolio', component: BasePortfolioComponent }
];
