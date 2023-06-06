import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Vacation} from "../../models/vacation.model";

@Component({
  selector: 'app-vacation-confirmation-popup',
  templateUrl: './vacation-confirmation-popup.component.html',
  styleUrls: ['./vacation-confirmation-popup.component.scss']
})
export class VacationConfirmationPopupComponent {

  vacation: Vacation;

  constructor(private dialogRef: MatDialogRef<VacationConfirmationPopupComponent>,
              @Inject(MAT_DIALOG_DATA) data: any) {
    this.vacation = data;
  }

  save() {
    this.dialogRef.close(true);
  }

  close() {
    this.dialogRef.close(false);
  }
}
