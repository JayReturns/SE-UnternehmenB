import {Component} from '@angular/core';
import {AuthService} from "./shared/services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(public authService: AuthService, public router: Router) {
  }

  showLayout(): boolean {
    return ['/dashboard', '/venv'].includes(this.router.url)
  }

}
