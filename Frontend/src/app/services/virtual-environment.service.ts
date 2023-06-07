import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {MessageService} from "./message.service";
import {environment} from "../../environments/environment";
import {GroupedVERequest, VERequest, VirtualEnvironment} from "../models/virtual-environment.model";
import {Vacation} from "../models/vacation.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class VirtualEnvironmentService {

  private urlVE = `${environment.baseApiUrl}/api/v1/v_environment`;
  private urlRequests = `${this.urlVE}_request`;
  private readonly getAllSuffix = "/all"
  private readonly putStatusSuffix = "/status"

  constructor(private http: HttpClient, private messageService: MessageService) {
  }

  getVirtualEnvironments(){
    return this.http.get<VirtualEnvironment[]>(this.urlVE)
  }

  getVERequests() {
    return this.http.get<VERequest[]>(this.urlRequests)
  }

  getAllVERequests() {
    return this.http.get<GroupedVERequest[]>(this.urlRequests + this.getAllSuffix)
  }

  makeVERequest(veRequest: VERequest) {
    let params = new HttpParams()
      .set('environmentType', veRequest.environmentType)
      .set('comment', veRequest.comment)

    return this.http.post(this.urlRequests, null, {params: params, responseType: "text"});
  }

  updateVERequest(veRequest: VERequest): Observable<string> {
    let params = new HttpParams()
      .set('environmentType', veRequest.environmentType)
      .set('comment', veRequest.comment)
      .set('id', veRequest.virtualEnvironmentRequestId!)

    return this.http.put(this.urlRequests, null, {params: params, responseType: "text"})
  }

  acceptVERequest(veRequestId: string) {
    let params = new HttpParams()
      .set('id', veRequestId)
      .set('status', 'APPROVED')

    return this.http.put(this.urlRequests + this.putStatusSuffix, null, {params: params,responseType: "text"});
  }

  rejectVERequest(veRequestId: string, reason: string) {
    let params = new HttpParams()
      .set('id', veRequestId)
      .set('status', 'REJECTED')
      .set('rejectReason', reason)

    return this.http.put(this.urlRequests + this.putStatusSuffix, null, {params: params, responseType: "text"});
  }
}
