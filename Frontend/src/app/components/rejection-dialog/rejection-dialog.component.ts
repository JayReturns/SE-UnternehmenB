
import {Component, Inject} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Status, Vacation} from "../../models/vacation.model";
import {MessageService} from "../../services/message.service";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";



@Component({
  selector: 'app-rejection-dialog',
  templateUrl: './rejection-dialog.component.html',
  styleUrls: ['./rejection-dialog.component.scss']
})
export class RejectionDialogComponent {

  maxCommentLength = 100;

  rejectionForm: FormGroup = new FormGroup<any>({});
  vacation: Vacation | null;

  constructor(private formBuilder: FormBuilder,
              private messageService: MessageService,
              private dialogRef: MatDialogRef<RejectionDialogComponent>,
              //private dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.vacation = data?.vacation;
    this.initializeForm();
  }

  initializeForm() {
    this.rejectionForm = new FormGroup({
      rejectReason: new FormControl<string | null>(this.vacation?.rejectReason || null, [Validators.required]),
    });
  }

  onSubmit(): void {

    if (!this.rejectionForm.valid || this.rejectionForm.errors) {
      this.messageService.notifyUserError();
      return;
    }

    let rejectReason = this.rejectionForm.get('rejectReason')!.value;

    this.dialogRef.close(rejectReason);

  }

  close(): void {
    this.dialogRef.close();
  }

}
