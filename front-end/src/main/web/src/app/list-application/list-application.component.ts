import { Component, OnInit , Inject} from '@angular/core';
import {Router} from "@angular/router";
import {Application} from "../model/application.model";
import {ApiService} from "../core/api.service";

@Component({
  selector: 'app-list-application',
  templateUrl: './list-application.component.html',
  styleUrls: ['./list-application.component.css']
})
export class ListApplicationComponent implements OnInit {

  applications: Application[];

  constructor(private router: Router, private apiService: ApiService) { }

  ngOnInit() {
    if(!window.localStorage.getItem('token')) {
      this.router.navigate(['login']);
      return;
    }
    this.apiService.getApplications()
      .subscribe( data => {
          this.applications = data.body;
      });
  }

  deleteApplication(application: Application): void {
    console.log('application')
    this.apiService.deleteApplication(application.id)
      .subscribe( data => {
        if (data.status == 200) {
          this.applications = this.applications.filter(a => a !== application);
        }
      })
  };

  editApplication(application: Application): void {
    window.localStorage.removeItem("editApplicationId");
    window.localStorage.setItem("editApplicationId", application.id.toString());
    this.router.navigate(['edit-application']);
  };

  addApplication(): void {
    this.router.navigate(['add-application']);
  };
}

