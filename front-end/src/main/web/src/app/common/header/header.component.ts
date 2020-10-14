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
    if (localStorage.getItem('roles') == null) {
      return false;
    }
    else {
      return true;
    }
  }
}
