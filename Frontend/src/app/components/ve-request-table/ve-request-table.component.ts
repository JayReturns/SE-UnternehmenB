import {Component, Input, SimpleChanges, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import {Vacation} from "../../models/vacation.model";
import {map} from "rxjs/operators";
import {MatDialog} from "@angular/material/dialog";
import {MessageService} from "../../services/message.service";
import {VacationDialogComponent} from "../vacation-dialog/vacation-dialog.component";
import {HttpErrorResponse} from "@angular/common/http";
import {GroupedVERequest, Status, VERequest} from "../../models/virtual-environment.model";
import {VirtualEnvironmentService} from "../../services/virtual-environment.service";
import {VEnvironmentRequestComponent} from "../v-environment-request-dialog/v-environment-request-dialog.component";
import {RejectionDialogComponent} from "../rejection-dialog/rejection-dialog.component";

@Component({
  selector: 've-request-table',
  templateUrl: './ve-request-table.component.html',
  styleUrls: ['./ve-request-table.component.scss']
})
export class VeRequestTableComponent {
  @Input() forManager!: boolean;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<VERequest>;
  dataSource = new MatTableDataSource();

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns;
  snackbar: any;

  constructor(private veService: VirtualEnvironmentService, public dialog: MatDialog, private messageService: MessageService) {
    this.displayedColumns = ['environmentType', 'comment', 'status'];
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.dataSource.data = []
    this.dataSource.sortingDataAccessor = (item, property) => this.sortData(item as Vacation, property)
  }

  ngOnChanges(changes: SimpleChanges) {

    if (this.forManager) {
      console.log("SHOW ME MANAGER")
      this.displayedColumns = ['name', ...this.displayedColumns, 'action']
    }
    this.refresh()
  }

  refresh() {
    this.getData().subscribe(veRequests => {
      console.log("Got data:", veRequests)
      this.dataSource.data = veRequests
    })
  }

  getData() {
    if (this.forManager) {
      return this.veService.getAllVERequests().pipe(map(v => this.castToVERequest(v)))
    } else {
      return this.veService.getVERequests();
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


  castToVERequest(data: GroupedVERequest[]): VERequest[] {
    return data.map(d =>
      d.requests.map(r => {
        r.user = d.user
        return r
      })
    ).flat()
  }

  accept(id: string) {
    this.veService.acceptVERequest(id).subscribe(() => this.refresh());
  }


  reject(id: string) {
    const dialogRef = this.dialog.open(RejectionDialogComponent);

    dialogRef.afterClosed().subscribe(rejectReason => {
      if (!rejectReason)
        return;

      this.veService.rejectVERequest(id,rejectReason).subscribe(() => this.refresh());
    })

  }

  openVEnvironmentDialog() {
    const dialogRef = this.dialog.open(VEnvironmentRequestComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (!result)
        return;

      this.veService.makeVERequest(result).subscribe(() => {
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

  editVacationRequest(row: VERequest) {
    if (this.forManager || row.status != Status.REQUESTED)
      return;

    this.dialog.open(VEnvironmentRequestComponent, {
      data: {
        vEnvironmentRequest: row
      }
    }).afterClosed().subscribe(result => {
      if (result)
        if ('refresh' in result) {
          this.refresh()
        } else
          this.veService.updateVERequest(result).subscribe(res => {
            console.log(res);
            this.refresh();
          });
    })
  }

  protected readonly Status = Status;
}
