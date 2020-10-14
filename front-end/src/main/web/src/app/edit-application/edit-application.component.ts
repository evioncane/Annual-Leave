import { Component, OnInit , Inject} from '@angular/core';
import {Router} from "@angular/router";
import {Application} from "../model/application.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {first} from "rxjs/operators";
import {ApiService} from "../core/api.service";
import { enableRipple } from '@syncfusion/ej2-base';
enableRipple(false);
import { DateTimePicker } from '@syncfusion/ej2-calendars';

@Component({
  selector: 'app-application-post',
  templateUrl: './edit-application.component.html',
  styleUrls: ['./edit-application.component.css']
})
export class EditApplicationComponent implements OnInit {

  application: Application;
  editForm: FormGroup;
  datetimepicker: DateTimePicker;
  constructor(private formBuilder: FormBuilder,private router: Router, private apiService: ApiService) { }

  ngOnInit() {
    if(!window.localStorage.getItem('token')) {
      this.router.navigate(['login']);
      return;
    }
    // let dateStr;
    let applicationId = window.localStorage.getItem("editApplicationId");
    if(!applicationId) {
      alert("Invalid action.")
      this.router.navigate(['list-application']);
      return;
    }
    this.editForm = this.formBuilder.group({
      id: [''],
      type: ['', Validators.required],
      days: ['', Validators.required]
    });
    // this.apiService.getApplicationById(+postId)
    //   .subscribe( data => {
    //     this.editForm.setValue(data.result);
    //     dateStr = data.result.date;
    //     this.setDate(dateStr);
    // });

    

  }

  // private setDate(dateStr: any) {
  //   this.datetimepicker = new DateTimePicker({
  //     format: 'dd-MMM-yy hh:mm a',
  //     value: new Date(dateStr),
  //     placeholder: 'Select a date and time',
  //     width: "233px"
  //   });
  //   setTimeout(() => {
  //     console.log(this.editForm.controls['date'].value);
  //   }, 1000);
  //   this.datetimepicker.appendTo('#datetimepicker');
  // }

  onSubmit() {
    var app = this.editForm.value
    this.apiService.updateApplication(app.id, app.type, app.days)
      .pipe(first())
      .subscribe(
        data => {
          if(data.status === 200) {
            alert('Application updated successfully.');
            this.router.navigate(['list-application']);
          }else {
            alert(data.message);
          }
        },
        error => {
          alert(error);
        });
  }

}