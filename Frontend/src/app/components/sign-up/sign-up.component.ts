import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../shared/services/auth.service";
import {EmailValidator, FormControl, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {Role, User} from "../../models/user.model";
import {Router} from "@angular/router";
import {ErrorStateMatcher} from '@angular/material/core';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  form: FormGroup = new FormGroup({
    email: new FormControl(''),
    name: new FormControl(''),
    lastname: new FormControl(''),
    role: new FormControl('EMPLOYEE'),
    password: new FormControl('')
  })

  constructor(
    public authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {  }
  ngOnInit() { }

  signup() {
    if (this.form.valid) {

      let user: User = {
        email: this.form.get('email')!.value,
        lastName: this.form.get('lastname')!.value,
        name: this.form.get('name')!.value,
        role: this.form.get('role')!.value,
        vacationDays: 30
      }

      this.authService.SignUp(user.email, this.form.get('password')!.value).then(() => {
        this.userService.createUser(user).subscribe(result => {
          console.log("User Creation:");
          console.log(result);
          this.router.navigate(['verify-email-address']);
        });
      });

    }
  }
}
