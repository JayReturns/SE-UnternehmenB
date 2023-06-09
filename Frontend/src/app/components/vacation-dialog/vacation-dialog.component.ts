import {Component, Inject} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Status, Vacation} from "../../models/vacation.model";
import {MessageService} from "../../services/message.service";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {VacationConfirmationPopupComponent} from "../vacation-confirmation-popup/vacation-confirmation-popup.component";
import {ConfirmationDialogComponent,ConfirmDialogModel} from "../shared/confirmation-dialog/confirmation-dialog.component";
import {VacationService} from "../../services/vacation.service";

@Component({
  selector: 'app-vacation-dialog',
  templateUrl: './vacation-dialog.component.html',
  styleUrls: ['./vacation-dialog.component.scss']
})
export class VacationDialogComponent {

  maxCommentLength = 100;

  vacationForm: FormGroup = new FormGroup<any>({});
  vacation: Vacation | null;
  title: string;
  editMode: boolean;

  constructor(private formBuilder: FormBuilder,
              private messageService: MessageService,
              private dialogRef: MatDialogRef<VacationDialogComponent>,
              private dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private vacationService: VacationService) {
    this.vacation = data?.vacation;
    this.editMode = !!data && !!data.vacation;
    this.title = `${this.editMode ? "bearbeiten" : "beantragen"}`;
    this.initializeForm();
  }

  initializeForm() {
    this.vacationForm = new FormGroup({
      vacationRequestId: new FormControl<string | null>(this.vacation?.vacationRequestId || null),
      start: new FormControl<Date | null>(this.vacation?.vacationStart || null),
      end: new FormControl<Date | null>(this.vacation?.vacationEnd || null),
      comment: new FormControl<string | null>(this.vacation?.comment || '', [Validators.maxLength(this.maxCommentLength)]),
      duration: new FormControl<number | null>(this.vacation?.duration || null, [Validators.required, Validators.min(1)])
    });
  }

  onSubmit(): void {
    if (this.vacationForm == null || !this.vacationForm.valid || this.vacationForm.errors) {
      this.messageService.notifyUserError();
      return;
    }
    const vacation: Vacation = {
      vacationStart: this.vacationForm.controls['start'].value!,
      vacationEnd: this.vacationForm.controls['end'].value!,
      duration: this.vacationForm.controls['duration'].value!,
      comment: this.vacationForm.controls['comment'].value || "",
      vacationRequestId: this.vacationForm.controls['vacationRequestId'].value || undefined
    }

    this.dialog.open(VacationConfirmationPopupComponent, {data: vacation}).afterClosed().subscribe(confirmed => {
      if (confirmed)
        this.dialogRef.close(vacation);
    });
  }

  close(): void {
    this.dialogRef.close();
  }

  handleDateChanges() {
    const startDate = this.vacationForm.controls['start'].value;
    const endDate = this.vacationForm.controls['end'].value;
    if (startDate == null || endDate == null)
      return;

    this.vacationForm.patchValue({
      duration: this.getBusinessDatesCount(startDate, endDate)
    });
  }

  getBusinessDatesCount(startDate: Date, endDate: Date): number {
    let count = 0;
    const curDate = new Date(startDate.getTime());
    while (curDate <= endDate) {
      const dayOfWeek = curDate.getDay();
      if (dayOfWeek !== 0 && dayOfWeek !== 6) count++;
      curDate.setDate(curDate.getDate() + 1);
    }
    return count;
  }

  delete() {
    const message = "Soll dieser Urlaubsantrag wirklich gelöscht werden?"

    const dialogDate = new ConfirmDialogModel("Urlaubsantrag löschen", message);

    this.dialog.open(ConfirmationDialogComponent, {
      data: dialogDate
    }).afterClosed().subscribe(result => {
        if (result) {
          this.vacationService.deleteVacationRequest(this.vacation?.vacationRequestId!).subscribe(() => {
            this.dialogRef.close({refresh: true});
          })
        }
      }
    )
  }

  protected readonly Status = Status;
}
