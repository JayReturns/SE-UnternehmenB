import {Component} from '@angular/core';
import {AuthService} from "../../shared/services/auth.service";
import {FormControl, FormGroup} from "@angular/forms";
import {MessageService} from "../../services/message.service";

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {

  hide = true;
  form: FormGroup  = new FormGroup({
    email: new FormControl(''),
    password: new FormControl('')
  })
  constructor(
    public authService: AuthService,
    private messageService: MessageService
  ) { }

  signIn() {
    if (!this.form.valid) {
      this.messageService.notifyUser("Malformatted Credentials!")
    }
    console.log(this.form);
    this.authService.SignIn(this.form.get('email')!.value, this.form.get('password')!.value);
  }

}
