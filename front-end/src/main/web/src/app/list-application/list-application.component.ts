import { Component, OnInit , Inject} from '@angular/core';
import { Router} from "@angular/router";
import {Application} from "../model/application.model";
import {ApiService} from "../core/api.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import { UrlSerializer } from '@angular/router';


@Component({
  selector: 'app-list-application',
  templateUrl: './list-application.component.html',
  styleUrls: ['./list-application.component.css']
})
export class ListApplicationComponent implements OnInit {

  applications: Application[];
  searchForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private router: Router, private apiService: ApiService, private serializer: UrlSerializer) { }

  ngOnInit() {
    if(!window.localStorage.getItem('token')) {
      this.router.navigate(['login']);
      return;
    }
    this.apiService.getApplications()
      .subscribe( data => {
          this.applications = data.body;
      });
      this.searchForm = this.formBuilder.group({
        type: [],
        status: [],
        beforeDate: [],
        afterDate: [],
        
      });
  }

  deleteApplication(application: Application): void {
    this.apiService.deleteApplication(application.id)
      .subscribe((data) => {
        this.applications = this.applications.filter(a => a !== application);
        window.alert('Application deleted')
      },
      (error)=> {window.alert(error.message);})
  };

  editApplication(application: Application): void {
    // window.localStorage.removeItem("editApplicationId");
    // window.localStorage.setItem("editApplicationId", application.id.toString());
    this.router.navigate(['edit-application'], { queryParams: { id: application.id, type: application.type, days: application.days }});
  };

  addApplication(): void {
    this.router.navigate(['add-application']);
  };

  search(): void {
    var searchData = this.searchForm.value;
    const queryParamTree = this.router.createUrlTree([], { queryParams: { type: searchData.type, status: searchData.status,
       beforeDate: searchData.beforeDate, afterDate: searchData.afterDate} });
    this.apiService.filterApplications(this.serializer.serialize(queryParamTree))
    .subscribe((data) => {
      this.applications = data.body;
    },
    (error)=> {})
    this.searchForm = this.formBuilder.group({
      type: [],
      status: [],
      beforeDate: [],
      afterDate: [],
      
    });
  }
}

