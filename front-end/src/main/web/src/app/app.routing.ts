import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {AddApplicationComponent} from "./add-application/add-application.component";
import {ListApplicationComponent} from "./list-application/list-application.component";
import {EditApplicationComponent} from "./edit-application/edit-application.component";
import { SupervisorListApplicationComponent } from './supervisor-list-application/supervisor-list-application.component';
import { AdminListUserComponent } from './admin-list-user/admin-list-user.component';
import { AdminAddUserComponent } from './admin-add-user/admin-add-user.component';
import { EvaluateApplicationComponent } from './evaluate-application/evaluate-application.component';
import { UpdatePasswordComponent } from './update-password/update-password.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'add-application', component: AddApplicationComponent },
  { path: 'list-application', component: ListApplicationComponent },
  { path: 'edit-application', component: EditApplicationComponent },
  { path: 'supervisor-list-application', component: SupervisorListApplicationComponent },
  { path: 'admin-list-user', component: AdminListUserComponent },
  { path: 'admin-add-user', component: AdminAddUserComponent },
  { path: 'evaluate-application', component: EvaluateApplicationComponent },
  { path: 'update-password', component: UpdatePasswordComponent },
  { path : '', component : LoginComponent}
];

export const routing = RouterModule.forRoot(routes);
