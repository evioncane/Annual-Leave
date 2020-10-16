import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {ApiService} from "../core/api.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import { User } from '../model/user.model';

@Component({
  selector: 'app-admin-add-user',
  templateUrl: './admin-add-user.component.html',
  styleUrls: ['./admin-add-user.component.css']
})
export class AdminAddUserComponent implements OnInit {

  addForm: FormGroup;

  constructor(private formBuilder: FormBuilder,private router: Router, private apiService: ApiService) { }

  ngOnInit() {
    if(!window.localStorage.getItem('token')) {
      this.router.navigate(['login']);
      return;
    }
    this.addForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      registeredDate: [{}, Validators.required],
      roles: [[], Validators.required],
    });
  }

  onSubmit() {
    var app = this.addForm.value;
    var user = new User();
    user.username = app.username;
    user.email = app.email;
    user.firstName = app.firstName;
    user.lastName = app.lastName;
    user.registeredDate = app.registeredDate;
    user.roles = app.roles;
    this.apiService.register(user)
      .subscribe( (data) => {
        this.router.navigate(['admin-list-user']);
      },
      (error) => {
        window.alert(error.error.message)
        this.router.navigate(['admin-list-user']);
      });
  }

}
