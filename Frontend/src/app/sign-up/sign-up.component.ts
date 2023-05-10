import {Component, OnInit} from '@angular/core';
import {AuthService} from "../shared/services/auth.service";
import {EmailValidator, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  form: FormGroup = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  })

  constructor(
    public authService: AuthService
  ) { }
  ngOnInit() { }

  signup() {

  }

}
