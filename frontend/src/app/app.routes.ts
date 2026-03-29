import { Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {RedirectComponent} from "./components/redirect/redirect.component";
import {BaseUserComponent} from "./components/loggedInUser/base-user/base-user.component";
import {TrialBalanceComponent} from "./components/loggedInUser/content/excel/trial-balance/trial-balance.component";
import {ChatplaceComponent} from "./components/loggedInUser/content/chat/chatplace/chatplace.component";
import {MenuTemplateManagementComponent} from "./components/loggedInUser/content/admin/menu-template-management/menu-template-management.component";

export const routes: Routes = [
  { path: '', redirectTo: 'redirect', pathMatch: 'full' },
  {path: 'redirect', component: RedirectComponent},
  { path: 'login', component: LoginComponent },
  {
    path: 'user',
    component: BaseUserComponent,
    children:[
      {path: 'trialBalance', component: TrialBalanceComponent},
      {path: 'allChat', component: ChatplaceComponent},
      {path: 'menu-template', component: MenuTemplateManagementComponent}
    ]
  }
];
