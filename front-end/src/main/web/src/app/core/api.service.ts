import { Injectable } from '@angular/core';
import {Application} from "../model/application.model";
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs/index";
import {ApiResponse} from "../model/api.response";

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

  // register(registerPayload): Observable<ApiResponse> {
  //   return this.http.post<ApiResponse>('http://localhost:8080/' + 'register', registerPayload);
  // }

  getApplications() : Observable<any> {
    return this.http.get<any>(USER_APPLICATION_API + '/all', {observe: 'response'});
  }

  // getPostById(id: number): Observable<ApiResponse> {
  //   return this.http.get<ApiResponse>(this.baseUrl + id);
  // }

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
