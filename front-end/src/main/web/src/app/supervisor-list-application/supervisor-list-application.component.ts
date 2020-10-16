import { Component, OnInit, Inject } from '@angular/core';
import { Router } from "@angular/router";
import { Application } from "../model/application.model";
import { ApiService } from "../core/api.service";
import { FormBuilder, FormGroup } from "@angular/forms";
import { UrlSerializer } from '@angular/router';

@Component({
  selector: 'app-supervisor-list-application',
  templateUrl: './supervisor-list-application.component.html',
  styleUrls: ['./supervisor-list-application.component.css']
})
export class SupervisorListApplicationComponent implements OnInit {

  applications: Application[];
  searchForm: FormGroup;
  evaluateForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private router: Router, private apiService: ApiService,
    private serializer: UrlSerializer) { }

  ngOnInit() {
    if (!window.localStorage.getItem('token')) {
      this.router.navigate(['login']);
      return;
    }
    this.apiService.getAllApplications()
      .subscribe(data => {
        this.applications = data.body;
      });
    this.searchForm = this.formBuilder.group({
      type: [],
      status: [],
      beforeDate: [],
      afterDate: [],
      username: [],
    });
  }

  search(): void {
    var searchData = this.searchForm.value;
    const queryParamTree = this.router.createUrlTree([], {
      queryParams: {
        username: searchData.username, type: searchData.type,
        status: searchData.status, beforeDate: searchData.beforeDate, afterDate: searchData.afterDate
      }
    });
    this.apiService.filterAllApplications(this.serializer.serialize(queryParamTree))
      .subscribe((data) => {
        this.applications = data.body;
      },
        (error) => { })
    this.searchForm = this.formBuilder.group({
      type: [],
      status: [],
      beforeDate: [],
      afterDate: [],
      username: [],
    });
  }

  evaluate(application: Application) {
    this.router.navigate(['evaluate-application'], { queryParams: { id: application.id, type: application.type, days: application.days }});
    // if (st == 'REJECTED' && (application.message == '')) {
    //   window.alert('Message cannot be empty when rejected!');
    //   return
    // }
    // var tempStatus = application.status;
    // application.status = st;
    // this.apiService.evaluateApplication(application)
    //   .subscribe((data) => {
    //     window.alert(data.body.message);
    //   }, (error) => {
    //     window.alert(error.error.message);
    //     application.status = tempStatus;
    //   })
  }
}
