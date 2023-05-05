import {Component, OnInit} from '@angular/core';
import {AuthService} from "../shared/services/auth.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {


  accessToken: any;
  constructor(public authService: AuthService, private http: HttpClient) {
    console.log(this.authService.idk());
    this.accessToken = this.authService.GetCredentialData().accessToken;
}

  ngOnInit(): void {}

  test(): void {
    var httpOptions = {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + this.accessToken
      })
    }


    console.log("Hey");
    this.http.get<String>("http://localhost:8080", httpOptions)
      .subscribe(x => {
      console.log(x);
    });
  }

}
