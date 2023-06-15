import {AfterViewInit, Component, Injectable, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import {GroupedVacation, Status, Vacation} from "../../models/vacation.model";
import {VacationService} from "../../services/vacation.service";
import {map} from "rxjs/operators";
import {MatDialog} from "@angular/material/dialog";
import {MessageService} from "../../services/message.service";
import {VacationDialogComponent} from "../vacation-dialog/vacation-dialog.component";
import {HttpErrorResponse} from "@angular/common/http";
import {RejectionDialogComponent} from "../rejection-dialog/rejection-dialog.component";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'vacation-request-table',
  templateUrl: './vacation-request-table.component.html',
  styleUrls: ['./vacation-request-table.component.scss'],
})
@Injectable({
  providedIn: 'root'
})
export class VacationRequestTableComponent implements AfterViewInit {
  forManager: boolean | undefined;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<Vacation>;
  dataSource = new MatTableDataSource();
  accessToken : any
  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns;
  snackbar: any;
  daysRequested!: number;
  daysLeft!: number;
  progress!: number;

  constructor(private vacationService: VacationService,
              public dialog: MatDialog, private messageService: MessageService,
              private userService: UserService) {
    this.displayedColumns = ['vacationStart', 'vacationEnd', 'duration', 'comment', 'status'];
    this.userService.getUser().subscribe(user => {
      this.forManager = user?.role == 'MANAGER';
      if (this.forManager) {
        this.displayedColumns = ['name', ...this.displayedColumns, 'action']
      }
      this.refresh()
    })
  }



  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.dataSource.data = []
    this.dataSource.sortingDataAccessor = (item, property) => this.sortData(item as Vacation, property)
  }

  refresh() {
    this.vacationService.getDaysLeft().subscribe(d => {
      this.daysRequested = Math.abs(d.leftDays - d.leftDaysOnlyApproved)
      this.daysLeft = d.leftDaysOnlyApproved
      this.progress = d.leftDaysOnlyApproved/30 * 100;
    })
    this.getData().subscribe(vacations => {
      this.dataSource.data = vacations
    })
  }

  getData() {
    if (this.forManager) {
      return this.vacationService.getAllVacationRequests().pipe(map(v => this.castToVacation(v)))
    } else {
      return this.vacationService.getVacationRequests();
    }
  }

  sortData(item: Vacation, property: string): string | number {
    switch (property) {
      case 'name': {
        return (item.user?.lastName || "") + ", " + (item.user?.name || "");
      }
      default: {
        // @ts-ignore
        return item[property];
      }
    }
  }


  castToVacation(data: GroupedVacation[]): Vacation[] {
    return data.map(d =>
      d.requests.map(r => {
        r.user = d.user
        return r
      })
    ).flat()
  }

  accept(id: string) {
    this.vacationService.acceptVacationRequest(id).subscribe(() => this.refresh());
  }

  reject(id: string) {
    const dialogRef = this.dialog.open(RejectionDialogComponent);

    dialogRef.afterClosed().subscribe(rejectReason => {
      if (!rejectReason)
        return;

      this.vacationService.rejectVacationRequest(id,rejectReason).subscribe(() => this.refresh());
    })

  }

  openDialog() {
    const dialogRef = this.dialog.open(VacationDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (!result)
        return;

      this.vacationService.makeVacationRequest(result).subscribe(() => {
      }, err => {
        if (err) {
          if (err instanceof HttpErrorResponse) {
            this.messageService.notifyUser(err.error);
            console.log(err);
          }
        }
      }, () => {
        this.refresh()
      });
    })
  }

  editVacationRequest(row: Vacation) {
    if (this.forManager || row.status != Status.REQUESTED)
      return;

    this.dialog.open(VacationDialogComponent, {
      data: {
        vacation: row
      }
    }).afterClosed().subscribe(result => {
      if (result)
        if ('refresh' in result) {
          this.refresh()
        } else
          this.vacationService.updateVacationRequest(result).subscribe(res => {
            console.log(res);
            this.refresh();
          });
    })
  }

  protected readonly Status = Status;
}


