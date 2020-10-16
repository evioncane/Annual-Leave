import { Component, OnInit } from '@angular/core';
import {ApiService} from "../core/api.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Application} from "../model/application.model";

@Component({
  selector: 'app-evaluate-application',
  templateUrl: './evaluate-application.component.html',
  styleUrls: ['./evaluate-application.component.css']
})
export class EvaluateApplicationComponent implements OnInit {

  id: number;
  evaluateForm: FormGroup;

  constructor(private formBuilder: FormBuilder,  private route: ActivatedRoute, private router: Router, private apiService: ApiService) { }

  ngOnInit() {
    this.route
      .queryParams
      .subscribe(params => {
        this.id = parseInt(params['id']);
        this.evaluateForm.value.type = params['type'];
        this.evaluateForm.value.days = params['days'];
      });
      this.evaluateForm = this.formBuilder.group({
        message: ['', Validators.required],
      });
  }

  evaluate(st: string) {
    if (st == 'REJECTED' && (this.evaluateForm.value.message == '')) {
      window.alert('Message cannot be empty when rejected!');
      return
    }
    this.apiService.evaluateApplication(this.id, st, this.evaluateForm.value.message)
      .subscribe((data) => {
        window.alert(data.body.message);
        this.router.navigate(['supervisor-list-application']);
      }, (error) => {
        window.alert(error.error.message);
        this.router.navigate(['supervisor-list-application']);
      })
  }

}
