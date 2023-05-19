import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {MessageService} from "./message.service";
import {environment} from "../../environments/environment";
import {Vacation} from "../models/vacation.model";
import {catchError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class VacationService {

  private url = `${environment.baseApiUrl}/api/v1/vacation_request`;
  private readonly manager_suffix = "/all"


  constructor(private http: HttpClient, private messageService: MessageService) { }

  getUserVacationRequest(){

  }

  makeVacationRequest(vacation: Vacation) {
    let params = new HttpParams()
      .set('startDate', this.formatDateToIsoDate(vacation.start))
      .set('endDate', this.formatDateToIsoDate(vacation.end))
      .set('duration', vacation.duration)
      .set('comment', vacation.comment)

    return this.http.post(this.url, null, {params: params})
      .pipe(
        catchError(this.messageService.handleError('makeVacationRequest'))
      );
  }

  private formatDateToIsoDate(date: Date): string {
    return date.toISOString().split("T")[0];
  }

}
