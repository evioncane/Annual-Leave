import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ApiService} from "../core/api.service";
import {HttpClient, HttpHeaders, } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  invalidLogin: boolean = false;
  constructor(private formBuilder: FormBuilder, private router: Router, private apiService: ApiService) { }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }
    const loginPayload = {
      username: this.loginForm.controls.username.value,
      password: this.loginForm.controls.password.value
    }
    var response = this.apiService.login(loginPayload)

    response.subscribe(data => {

      if(data.status === 200) {
        window.localStorage.setItem('token', data.body.token);
        var role_list = data.body.roles;
        localStorage.setItem('roles', role_list);
        if (role_list.indexOf('ROLE_ADMIN') >= 0){
          this.router.navigate(['admin-list-user']);
        }
        else if (role_list.indexOf('ROLE_SUPERVISOR') >= 0) {
          this.router.navigate(['supervisor-list-application']);
        }
        else {
          this.router.navigate(['list-application']);
        }
      }else {
        this.invalidLogin = true;
        debugger;
        alert(data.body);
      }
    });
  }

  ngOnInit() {
    window.localStorage.removeItem('token');
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.compose([Validators.required])],
      password: ['', Validators.required]
    });
  }

}