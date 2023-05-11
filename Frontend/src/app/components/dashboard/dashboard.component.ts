import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../shared/services/auth.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {UserService} from "../../services/user.service";
import {User} from "../../models/user.model";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {


  accessToken: any;
  user: User | undefined;
  constructor(public authService: AuthService, private http: HttpClient, private userService: UserService) {
    this.accessToken = this.authService.GetCredentialData();
}

  ngOnInit(): void {
    this.userService.getUser().subscribe(x => this.user = x);
  }


}
