import {Component, Input, SimpleChanges, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MatTable, MatTableDataSource} from "@angular/material/table";
import {GroupedVERequest, VERequest, VirtualEnvironment} from "../../models/virtual-environment.model";
import {VirtualEnvironmentService} from "../../services/virtual-environment.service";
import {MatDialog} from "@angular/material/dialog";
import {MessageService} from "../../services/message.service";
import {Vacation} from "../../models/vacation.model";
import {map} from "rxjs/operators";
import {VacationDialogComponent} from "../vacation-dialog/vacation-dialog.component";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'virtual-environments-table',
  templateUrl: './virtual-environments-table.component.html',
  styleUrls: ['./virtual-environments-table.component.scss']
})
export class VirtualEnvironmentsTableComponent {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<VirtualEnvironment>;
  dataSource = new MatTableDataSource();

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns;
  snackbar: any;

  constructor(private veService: VirtualEnvironmentService, public dialog: MatDialog, private messageService: MessageService) {
    this.displayedColumns = ['environmentType', 'ipAddress', 'userName', 'password'];
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.dataSource.data = []
    this.dataSource.sortingDataAccessor = (item, property) => this.sortData(item as Vacation, property)
  }

  refresh() {
    this.getData().subscribe(virtualEnvironments => {
      this.dataSource.data = virtualEnvironments
    })
  }

  getData() {
    return this.veService.getVirtualEnvironments();
  }

  sortData(item: Vacation, property: string): string | number {
    // @ts-ignore
    return item[property];
  }
}
