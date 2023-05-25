import {Component} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {VacationDialogComponent} from "./components/vacation-dialog/vacation-dialog.component";
import {VacationService} from "./services/vacation.service";
import {MessageService} from "./services/message.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {


  // TODO: IMPORTANT! Move this as soon as there is a better place. A vacation list / table or something like that
  // TODO: This only illustrates the way of implementation



  constructor(public dialog: MatDialog, private vacationService: VacationService, private messageService: MessageService) {
  }

  openDialog() {
    const dialogRef = this.dialog.open(VacationDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (!result)
        return;

      this.vacationService.makeVacationRequest(result).subscribe(() => { }, err => {
        if (err) {
          if (err instanceof HttpErrorResponse) {
            this.messageService.notifyUser(err.error);
            console.log(err);
          }
        }
      });
    })
  }

}
