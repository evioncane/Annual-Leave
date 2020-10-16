import { Component, OnInit } from '@angular/core';
import { Router} from "@angular/router";
import { User } from '../model/user.model';
import {ApiService} from "../core/api.service";

@Component({
  selector: 'app-admin-list-user',
  templateUrl: './admin-list-user.component.html',
  styleUrls: ['./admin-list-user.component.css']
})
export class AdminListUserComponent implements OnInit {

  users: User[];

  constructor( private router: Router, private apiService: ApiService) { }

  ngOnInit() {
    if(!window.localStorage.getItem('token')) {
      this.router.navigate(['login']);
      return;
    }
    this.apiService.getAllUsers()
      .subscribe( data => {
          this.users = data.body;
      },
      error => {
        window.alert('Something went wrong!')
      });
  }

  addUser(user: User) {
    this.router.navigate(['admin-add-user']);
  }

  deleteUser(user: User) {
    this.apiService.deleteUser(user.username)
      .subscribe((data) => {
        this.users = this.users.filter(u => u !== user);
        window.alert('User deleted');
      },
      (error)=> {window.alert(error.error.message);})
  }

}
