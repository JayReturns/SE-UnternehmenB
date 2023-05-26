import {AfterViewInit, Component, Input, SimpleChanges, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import {GroupedVacation, Vacation} from "../../models/vacation.model";
import {VacationService} from "../../services/vacation.service";
import {map} from "rxjs/operators";
import {MatDialog} from "@angular/material/dialog";
import {MessageService} from "../../services/message.service";
import {VacationDialogComponent} from "../vacation-dialog/vacation-dialog.component";
import {HttpErrorResponse} from "@angular/common/http";

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

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns;
  snackbar: any;

  constructor(private vacationService: VacationService, public dialog: MatDialog, private messageService: MessageService) {
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
    this.vacationService.acceptVacationRequest(id).subscribe(()=>this.refresh());
  }

  reject(id: string) {
    this.vacationService.rejectVacationRequest(id).subscribe(()=>this.refresh());
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


