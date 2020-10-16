import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {LoginComponent} from './login/login.component';
import {AddApplicationComponent} from './add-application/add-application.component';
import {EditApplicationComponent} from './edit-application/edit-application.component';
import {ListApplicationComponent} from './list-application/list-application.component';
import {ApiService} from "./core/api.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import {routing} from "./app.routing";
import {TokenInterceptor} from "./core/interceptor";
import {MatToolbarModule} from "@angular/material";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './common/header/header.component';
import { SupervisorListApplicationComponent } from './supervisor-list-application/supervisor-list-application.component';
import { AdminListUserComponent } from './admin-list-user/admin-list-user.component';
import {MatIconModule} from '@angular/material/icon';
import { EvaluateApplicationComponent } from './evaluate-application/evaluate-application.component';
import { AdminAddUserComponent } from './admin-add-user/admin-add-user.component';
import { UpdatePasswordComponent } from './update-password/update-password.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AddApplicationComponent,
    EditApplicationComponent,
    ListApplicationComponent,
    HeaderComponent,
    SupervisorListApplicationComponent,
    AdminListUserComponent,
    EvaluateApplicationComponent,
    AdminAddUserComponent,
    UpdatePasswordComponent,
  ],
  imports: [
    BrowserModule,
    routing,
    ReactiveFormsModule,
    HttpClientModule,
    MatToolbarModule,
    BrowserAnimationsModule,
    MatIconModule,
  ],
  providers: [ApiService, {provide: HTTP_INTERCEPTORS,
    useClass: TokenInterceptor,
    multi : true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
