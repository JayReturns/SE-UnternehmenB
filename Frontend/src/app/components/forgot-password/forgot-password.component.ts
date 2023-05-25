import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../shared/services/auth.service";
import {EmailValidator, FormControl, FormGroup, Validators} from "@angular/forms";
import {ErrorStateMatcher} from '@angular/material/core';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit{

  form: FormGroup = new FormGroup({
    email: new FormControl(''),
  })

  constructor(
    public authService: AuthService
  ) { }
  ngOnInit() {
  }
}
