import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable, catchError} from "rxjs";
import {User} from "../models/user.model";
import {environment} from "../../environments/environment";
import {MessageService} from "./message.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private url = `${environment.baseApiUrl}/api/v1/user`;
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

  constructor(private http: HttpClient, private messageService: MessageService) { }

  getUser(): Observable<User> {
    return this.http.get<User>(this.url)
      .pipe(
        catchError(this.messageService.handleError<User>('getUser'))
      )
  }

  createUser(user: User): Observable<any> {
    let params = new HttpParams()
      .set('name', user.name)
      .set('lastname', user.lastName)
      .set('role', user.role);

    return this.http.post(this.url,null, { params: params });
  }

}
