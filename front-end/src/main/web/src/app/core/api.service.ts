import { Injectable } from '@angular/core';
import {Application} from "../model/application.model";
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs/index";
import {EvaluationRequest} from "../model/application.model";
import { User } from '../model/user.model';


const AUTH_API = 'http://localhost:8080/auth';
const APPLICATION_API = 'http://localhost:8080/application';
const USER_APPLICATION_API = APPLICATION_API + '/personal'

@Injectable()
export class ApiService {

  constructor(private http: HttpClient) { }
  baseUrl: string = 'http://localhost:8080/ddd/api/posts/';

  login(loginPayload) : Observable<any> {
    return this.http.post<any>(AUTH_API + '/signin', loginPayload, {observe: 'response'});
  }

  logut() : Observable<any> {
    return this.http.post<any>(AUTH_API + '/logout', {observe: 'response'});
  }

  getApplications() : Observable<any> {
    return this.http.get<any>(USER_APPLICATION_API + '/list-application', {observe: 'response'});
  }

  filterApplications(path: string) : Observable<any> {
    return this.http.get<any>(USER_APPLICATION_API + path, {observe: 'response'});
  }

  getAllApplications() : Observable<any> {
    return this.http.get<any>(APPLICATION_API + '/supervisor-list-application', {observe: 'response'});
  }

  filterAllApplications(path: string) : Observable<any> {
    return this.http.get<any>(APPLICATION_API + path, {observe: 'response'});
  }

  getAllUsers() : Observable<any> {
    return this.http.get<any>(AUTH_API + '/all', {observe: 'response'});
  }

  register(registerPayload: User) : Observable<any> {
    return this.http.post<any>(AUTH_API + '/create/user', registerPayload, {observe: 'response'});
  }

  deleteUser(username: string) : Observable<any> {
    return this.http.delete<any>(AUTH_API + '/delete/' + username, {observe: 'response'})
  }

  updatePassword(updateForm) : Observable<any> {
    return this.http.put<any>(AUTH_API + '/update/password/', updateForm, {observe: 'response'})
  }

  evaluateApplication(id: number, status: string, message: string) : Observable<any> {
    var evaluationReq = new EvaluationRequest(id, status, message);
    return this.http.post<any>(APPLICATION_API + '/evaluate', evaluationReq, {observe: 'response'});
  }

  createApplication(type: string, days: number): Observable<any> {
    return this.http.post<any>(USER_APPLICATION_API + '?days=' + days + '&type=' + type, {observe: 'response'});
  }

  updateApplication(id: number, type: string, days: number): Observable<any> {
    var queryParameters = '';
    if (type != null) {
      queryParameters += '&type=' + type;
    }
    if (days != null) {
      queryParameters += '&days=' + days;
    }
    return this.http.put<any>(USER_APPLICATION_API + '?id=' + id + queryParameters, {observe: 'response'});
  }

  deleteApplication(id: number): Observable<any> {
    return this.http.delete<any>(USER_APPLICATION_API + '/' + id, {observe: 'response'});
  }
}
