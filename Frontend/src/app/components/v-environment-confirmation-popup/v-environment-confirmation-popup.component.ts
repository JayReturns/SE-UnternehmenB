import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {VEnvironmentRequest} from "../../models/v-environment-request.model";


@Component({
  selector: 'app-v-environment-confirmation-popup',
  templateUrl: './v-environment-confirmation-popup.component.html',
  styleUrls: ['./v-environment-confirmation-popup.component.scss']
})
export class VEnvironmentConfirmationPopupComponent {

  vEnvironmentRequest: VEnvironmentRequest;
  comment = "";

  constructor(private dialogRef: MatDialogRef<VEnvironmentConfirmationPopupComponent>,
              @Inject(MAT_DIALOG_DATA) data: any) {
    this.vEnvironmentRequest = data;
  }

  save() {
    this.dialogRef.close(true);
  }

  close() {
    this.dialogRef.close(false);
  }
}
