import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {MessageService} from "./message.service";
import {environment} from "../../environments/environment";
import {GroupedVacation, Vacation} from "../models/vacation.model";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {VEnvironmentRequest} from "../models/v-environment-request.model";

@Injectable({
  providedIn: 'root'
})
export class VEnvironmentRequestService {

  private url = `${environment.baseApiUrl}/api/v1/v_environment_request`;

  constructor(private http: HttpClient, private messageService: MessageService) {
  }


  makeVEnvironmentRequest(vEnvironmentRequest: VEnvironmentRequest) {
    let params = new HttpParams()
      .set('environmentType', vEnvironmentRequest.environmentType)
      .set('comment', vEnvironmentRequest.comment);

    return this.http.post(this.url, null, {params: params, responseType: "text"});
  }

}
