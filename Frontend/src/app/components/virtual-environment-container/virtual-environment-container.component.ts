import { Component } from '@angular/core';
import {UserService} from "../../services/user.service";
import {Role, User} from "../../models/user.model";

@Component({
  selector: 'app-virtual-environment-container',
  templateUrl: './virtual-environment-container.component.html',
  styleUrls: ['./virtual-environment-container.component.scss']
})
export class VirtualEnvironmentContainerComponent {
  user!: User
  constructor(private userService: UserService) {
    this.userService.getUser().subscribe(user => {
      this.user = user
    })
  }

  protected readonly Role = Role;
}
