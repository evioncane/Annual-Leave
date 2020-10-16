import { Component, OnInit } from '@angular/core';
import {ApiService} from "../../core/api.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  role: string;

  constructor(private router: Router, private apiService: ApiService) {
  }

  ngOnInit() {
  }

  logout() {
    window.localStorage.removeItem('token');
    localStorage.removeItem('roles');
    this.apiService.logut();
    this.router.navigate(['login']);
  }

  isLoggedIn() {
    var currentUrl = this.router.url;
    if (currentUrl == '/' || currentUrl == '/login'){
      return false;
    }
    if (localStorage.getItem('roles') == null) {
      return false;
    }
    else {
      return true;
    }
  }

  isAdmin() {
    if (localStorage.getItem('roles').indexOf('ROLE_ADMIN') >= 0){
      return true;
    }
  }
  goToAdmin() {
    this.router.navigate(['admin-list-user']);
  }

  isSupervisor() {
    if (localStorage.getItem('roles').indexOf('ROLE_SUPERVISOR') >= 0) {
      return true;
    }
  }

  goToSupervisor() {
    this.router.navigate(['supervisor-list-application']);
  }

  isUser(){
    if (localStorage.getItem('roles').indexOf('ROLE_USER') >= 0) {
      return true;
    }
  }

  goToUser() {
    this.router.navigate(['list-application']);
  }

  updatePassword() {
    this.router.navigate(['update-password']);
  }
}
