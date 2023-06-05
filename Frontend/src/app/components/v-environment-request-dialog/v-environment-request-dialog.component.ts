import {Component, Inject} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {VEnvironmentRequest} from "../../models/v-environment-request.model";
import {MessageService} from "../../services/message.service";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {VEnvironmentConfirmationPopupComponent} from "../v-environment-confirmation-popup/v-environment-confirmation-popup.component";


@Component({
  selector: 'app-VEnvironmentRequest-dialog',
  templateUrl: './v-environment-request-dialog.component.html',
  styleUrls: ['./v-environment-request-dialog.component.scss']
})
export class VEnvironmentRequestComponent {

  maxCommentLength = 100;

  vEnvironmentRequestForm: FormGroup = new FormGroup<any>({});
  vEnvironmentRequest: VEnvironmentRequest | null;
  title: string;

  constructor(private formBuilder: FormBuilder,
              private messageService: MessageService,
              private dialogRef: MatDialogRef<VEnvironmentRequestComponent>,
              private dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.vEnvironmentRequest = data?.vEnvironmentRequest;
    this.title = `${data ? "bearbeiten" : "beantragen"}`;
    this.initializeForm();
  }

  initializeForm() {
    this.vEnvironmentRequestForm = new FormGroup({
      vEnvironmentRequestId: new FormControl<string | null>(this.vEnvironmentRequest?.virtualEnvironmentRequestId || null),
      environmentType: new FormControl<string | null>(this.vEnvironmentRequest?.environmentType || null, [Validators.required, Validators.min(1)]),
      comment: new FormControl<string | null>(this.vEnvironmentRequest?.comment || '', [Validators.maxLength(this.maxCommentLength)])
    });
  }

  onSubmit(): void {
    if (this.vEnvironmentRequestForm == null || !this.vEnvironmentRequestForm.valid || this.vEnvironmentRequestForm.errors) {
      this.messageService.notifyUserError();
      return;
    }

    const vEnvironmentRequest: VEnvironmentRequest = {
      environmentType: this.vEnvironmentRequestForm.controls['environmentType'].value,
      comment: this.vEnvironmentRequestForm.controls['comment'].value || ""
    }
    console.log(vEnvironmentRequest.environmentType);
    console.log(vEnvironmentRequest.comment);

    this.dialog.open(VEnvironmentConfirmationPopupComponent, {data: vEnvironmentRequest}).afterClosed().subscribe(confirmed => {
      if (confirmed)
        this.dialogRef.close(vEnvironmentRequest);
    });

  }

  close(): void {
    this.dialogRef.close();
  }


}
