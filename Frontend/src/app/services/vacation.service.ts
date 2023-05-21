import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {MessageService} from "./message.service";
import {environment} from "../../environments/environment";
import {GroupedVacation, Vacation} from "../models/vacation.model";
import {catchError} from "rxjs";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class VacationService {

  private url = `${environment.baseApiUrl}/api/v1/vacation_request`;
  private readonly getAllSuffix = "/all"

  constructor(private http: HttpClient, private messageService: MessageService) {
  }

  getVacationRequests() {
    return this.http.get<Vacation[]>(this.url).pipe(map(data => this.insertDatesForVacation(data)))
  }

  getAllVacationRequests() {
    return this.http.get<GroupedVacation[]>(this.url + this.getAllSuffix).pipe(map(data => this.insertDatesForGroupedVacation(data)))
  }

  makeVacationRequest(vacation: Vacation) {
    let params = new HttpParams()
      .set('startDate', this.formatDateToIsoDate(vacation.vacationStart))
      .set('endDate', this.formatDateToIsoDate(vacation.vacationEnd))
      .set('duration', vacation.duration)
      .set('comment', vacation.comment)

    return this.http.post(this.url, null, {params: params})
      .pipe(
        catchError(this.messageService.handleError('makeVacationRequest'))
      );
  }

  private insertDatesForVacation(data: Vacation[]): Vacation[] {
    return data.map(v => {
      console.log("Inserting dates: ", v, v.vacationStart, v.vacationEnd)
      v.vacationStart = new Date(v.vacationStart)
      v.vacationEnd = new Date(v.vacationEnd)

      return v
    })
  }

  private insertDatesForGroupedVacation(data: GroupedVacation[]): GroupedVacation[] {
    return data.map(v => {
      v.requests = v.requests.map(r => {
        console.log("Inserting dates: ", v, r.vacationStart, r.vacationEnd)
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

}
