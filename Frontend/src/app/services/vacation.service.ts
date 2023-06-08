import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {GroupedVacation, Vacation} from "../models/vacation.model";
import {Vacation_left_max_daysModel} from "../models/vacation_left_max_days.model"
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class VacationService {

  private url = `${environment.baseApiUrl}/api/v1/vacation_request`;
  private readonly getAllSuffix = "/all"

  constructor(public http: HttpClient) {
  }

  getVacationRequests() {
    return this.http.get<Vacation[]>(this.url).pipe(map(data => this.insertDatesForVacation(data)))
  }

  getAllVacationRequests() {
    return this.http.get<GroupedVacation[]>(this.url + this.getAllSuffix).pipe(map(data => this.insertDatesForGroupedVacation(data)))
  }

  getDaysLeft() {
    return this.http.get<Vacation_left_max_daysModel>(environment.baseApiUrl+'/api/v1/vacation/days?year='+this.getCurrentYear())
  }

  getCurrentYear(): number {
    let date = new Date().getFullYear();
    return date;
  }
  makeVacationRequest(vacation: Vacation) {
    let params = new HttpParams()
      .set('startDate', this.formatDateToIsoDate(vacation.vacationStart))
      .set('endDate', this.formatDateToIsoDate(vacation.vacationEnd))
      .set('duration', vacation.duration)
      .set('comment', vacation.comment)

    return this.http.post(this.url, null, {params: params, responseType: "text"});
  }

  acceptVacationRequest(vacationRequestId: string) {
    let params = new HttpParams()
      .set('vacationId', vacationRequestId)
      .set('status', 'APPROVED')

    return this.http.put(this.url, null, {params: params,responseType: "text"});
  }

  rejectVacationRequest(vacationRequestId: string, rejectReason: string) {
    let params = new HttpParams()
      .set('vacationId', vacationRequestId)
      .set('status', 'REJECTED')
      .set('rejection_cause', rejectReason)

    return this.http.put(this.url, null, {params: params,responseType: "text"});
  }

  private insertDatesForVacation(data: Vacation[]): Vacation[] {
    return data.map(v => {
      v.vacationStart = new Date(v.vacationStart)
      v.vacationEnd = new Date(v.vacationEnd)

      return v
    })
  }

  private insertDatesForGroupedVacation(data: GroupedVacation[]): GroupedVacation[] {
    return data.map(v => {
      v.requests = v.requests.map(r => {
        r.vacationStart = new Date(r.vacationStart)
        r.vacationEnd = new Date(r.vacationEnd)

        return r
      })

      return v
    })
  }

  private formatDateToIsoDate(date: Date): string {
    return date.toISOString().split("T")[0];
  }

  updateVacationRequest(vacation: Vacation): Observable<string> {
    let params = new HttpParams()
      .set('begin', this.formatDateToIsoDate(vacation.vacationStart))
      .set('end', this.formatDateToIsoDate(vacation.vacationEnd))
      .set('days', vacation.duration)
      .set('note', vacation.comment)
      .set('vacationId', vacation.vacationRequestId!)

    return this.http.put(this.url, null, {params: params, responseType: "text"})
  }

  deleteVacationRequest(vacationRequestId: string) {
    let params = new HttpParams()
      .set('vacationRequestId', vacationRequestId);

    return this.http.delete(this.url, {params: params, responseType: "text"});
  }

}
