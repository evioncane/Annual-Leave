import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ApiService} from "../core/api.service";
import { enableRipple } from '@syncfusion/ej2-base';
enableRipple(false);
import { DateTimePicker } from '@syncfusion/ej2-calendars';

@Component({
  selector: 'app-add-application',
  templateUrl: './add-application.component.html',
  styleUrls: ['./add-application.component.css']
})
export class AddApplicationComponent implements OnInit {

  constructor(private formBuilder: FormBuilder,private router: Router, private apiService: ApiService) { }

  addForm: FormGroup;

  datetimepicker: DateTimePicker;

  ngOnInit() {
    if(!window.localStorage.getItem('token')) {
      this.router.navigate(['login']);
      return;
    }
    // this.datetimepicker  = new DateTimePicker({
    //   format: 'dd-MMM-yy hh:mm a',
    //   value: new Date(),
    //   placeholder: 'Select a date and time',
    //   width: "233px"
    // });

    // console.log("Date is:" + this.datetimepicker.value.toISOString())
    // this.datetimepicker.appendTo('#datetimepicker');
    // this.addForm = this.formBuilder.group({
    //   type: ['', Validators.required],
    //   days: ['', Validators.required],
    //   date: [this.datetimepicker.value.toISOString(), Validators.required]
    // });

  }

  onSubmit() {
    var app = this.addForm.value;
    this.apiService.createApplication(app.type, app.days)
      .subscribe( data => {
        this.router.navigate(['list-application']);
      });
  }


}
