import {Component} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {VacationDialogComponent} from "./components/vacation-dialog/vacation-dialog.component";
import {VacationService} from "./services/vacation.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {


  // TODO: IMPORTANT! Move this as soon as there is a better place. A vacation list / table or something like that
  // TODO: This only illustrates the way of implementation



  constructor(public dialog: MatDialog, private vacationService: VacationService) {
  }

  openDialog() {
    const dialogRef = this.dialog.open(VacationDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log("Jo! Result:")
        console.dir(result);
        this.vacationService.makeVacationRequest(result).subscribe();
      } else {
        console.log("No");
      }
    })
  }

}
