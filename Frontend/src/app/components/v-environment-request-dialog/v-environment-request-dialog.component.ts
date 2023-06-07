import {Component, Inject} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MessageService} from "../../services/message.service";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {
  VEnvironmentConfirmationPopupComponent
} from "../v-environment-confirmation-popup/v-environment-confirmation-popup.component";
import {Status, VERequest} from "../../models/virtual-environment.model";
import {
  ConfirmationDialogComponent,
  ConfirmDialogModel
} from "../shared/confirmation-dialog/confirmation-dialog.component";
import {VirtualEnvironmentService} from "../../services/virtual-environment.service";


@Component({
  selector: 'app-VEnvironmentRequest-dialog',
  templateUrl: './v-environment-request-dialog.component.html',
  styleUrls: ['./v-environment-request-dialog.component.scss']
})
export class VEnvironmentRequestComponent {

  maxCommentLength = 100;

  vEnvironmentRequestForm: FormGroup = new FormGroup<any>({});
  vEnvironmentRequest: VERequest | null;
  title: string;
  editMode!: boolean;

  constructor(private formBuilder: FormBuilder,
              private veService: VirtualEnvironmentService,
              private messageService: MessageService,
              private dialogRef: MatDialogRef<VEnvironmentRequestComponent>,
              private dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.vEnvironmentRequest = data?.vEnvironmentRequest;
    this.title = `${data ? "bearbeiten" : "beantragen"}`;
    this.editMode = !!data && !!data.vEnvironmentRequest;
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

    const vEnvironmentRequest: VERequest = {
      environmentType: this.vEnvironmentRequestForm.controls['environmentType'].value,
      comment: this.vEnvironmentRequestForm.controls['comment'].value || "",
      status: Status.REQUESTED
    }

    this.dialog.open(VEnvironmentConfirmationPopupComponent, {data: vEnvironmentRequest}).afterClosed().subscribe(confirmed => {
      if (confirmed)
        this.dialogRef.close(vEnvironmentRequest);
    });

  }

  delete() {
    const message = "Soll diese Bedarfsmeldung wirklich gelöscht werden?"

    const dialogDate = new ConfirmDialogModel("Bedarfsmeldung löschen", message);

    this.dialog.open(ConfirmationDialogComponent, {
      data: dialogDate
    }).afterClosed().subscribe(result => {
        if (result) {
          this.veService.deleteVERequest(this.vEnvironmentRequest?.virtualEnvironmentRequestId!).subscribe(() => {
            this.dialogRef.close({refresh: true});
          })
        }
      }
    )
  }

  close(): void {
    this.dialogRef.close();
  }


  protected readonly Status = Status;
}
