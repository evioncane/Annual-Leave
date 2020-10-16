import { Component, OnInit } from '@angular/core';
import {ApiService} from "../core/api.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.css']
})
export class UpdatePasswordComponent implements OnInit {

  updateForm: FormGroup;

  constructor(private formBuilder: FormBuilder,private router: Router, private apiService: ApiService) { }

  ngOnInit() {
    this.updateForm = this.formBuilder.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      newPasswordConfirmation: ['', Validators.required],
    });
  }

  onSubmit() {
    var app = this.updateForm.value;
    var updateForm = {oldPassword: app.oldPassword, newPassword: app.newPassword, newPasswordConfirmation: app.newPasswordConfirmation};
    this.apiService.updatePassword(updateForm)
      .subscribe( (data) => {
        this.router.navigate(['list-application']);
      },
      (error) => {
        window.alert(error.error.message)
        this.router.navigate(['list-application']);
      });
  }

}
