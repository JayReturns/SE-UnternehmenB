import {AfterViewInit, Component, Input, SimpleChanges, ViewChild} from '@angular/core';
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
import {VEnvironmentRequestComponent} from "../v-environment-request-dialog/v-environment-request-dialog.component";
import {VEnvironmentRequestService} from "../../services/v-environment-request.service";
import {Observable, throwError } from 'rxjs';
import {environment} from "../../../environments/environment";

@Component({
  selector: 'vacation-request-table',
  templateUrl: './vacation-request-table.component.html',
  styleUrls: ['./vacation-request-table.component.scss']
})
export class VacationRequestTableComponent implements AfterViewInit {
  @Input() forManager!: boolean;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<Vacation>;
  dataSource = new MatTableDataSource();
  accessToken : any
  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns;
  snackbar: any;
  left_day: any;
  year: any = new Date().getFullYear()

  // TODO Move vEnvironmentRequestService to request table for virtual environments
  constructor(private vacationService: VacationService, private vEnvironmentRequestService: VEnvironmentRequestService, public dialog: MatDialog, private messageService: MessageService ) {
    this.displayedColumns = ['vacationStart', 'vacationEnd', 'duration', 'comment', 'status'];
  }



  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.dataSource.data = []
    this.dataSource.sortingDataAccessor = (item, property) => this.sortData(item as Vacation, property)
  }

  ngOnChanges(changes: SimpleChanges) {
    //if (!changes["forManager"].firstChange) {
    if (this.forManager) {
      this.displayedColumns = ['name', ...this.displayedColumns, 'action']
    }
    this.refresh()
    //}
  }

  refresh() {
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

  setDaysLeft(): number{
    this.left_day = this.vacationService.http.get(environment.baseApiUrl+'/api/v1/vacation/days?year='+this.year);
    console.log(this.left_day);
    return (this.left_day/30)*100;

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
    this.vacationService.rejectVacationRequest(id).subscribe(() => this.refresh());
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
      });
    })
  }

  //TODO Move openVEnvironmentDialog to request table for virtual environments
  openVEnvironmentDialog() {
    const dialogRef = this.dialog.open(VEnvironmentRequestComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (!result)
        return;

      this.vEnvironmentRequestService.makeVEnvironmentRequest(result).subscribe(() => {
      }, (err: { error: string; }) => {
        if (err) {
          if (err instanceof HttpErrorResponse) {
            this.messageService.notifyUser(err.error);
            console.log(err);
          }
        }
      });

      return;
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

}


