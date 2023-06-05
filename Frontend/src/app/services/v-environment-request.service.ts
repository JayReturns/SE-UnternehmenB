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
  private readonly getAllSuffix = "/all"

  constructor(private http: HttpClient, private messageService: MessageService) {
  }


  makeVEnvironmentRequest(vEnvironmentRequest: VEnvironmentRequest) {
    let params = new HttpParams()
      .set('type', vEnvironmentRequest.environmentType)
      .set('comment', vEnvironmentRequest.comment)

    return this.http.post(this.url, null, {params: params, responseType: "text"});
  }

  acceptVEnvironmentRequest(vacationRequestId: string) {
    let params = new HttpParams()
      .set('vacationId', vacationRequestId)
      .set('status', 'APPROVED')

    return this.http.put(this.url, null, {params: params, responseType: "text"});
  }

}
