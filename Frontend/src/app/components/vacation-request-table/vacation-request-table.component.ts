import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { VacationRequestTableDataSource } from './vacation-request-table-datasource';
import {Vacation} from "../../models/vacation.model";

@Component({
  selector: 'app-vacation-request-table',
  templateUrl: './vacation-request-table.component.html',
  styleUrls: ['./vacation-request-table.component.scss']
})
export class VacationRequestTableComponent implements AfterViewInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<Vacation>;
  dataSource: VacationRequestTableDataSource;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['nr', 'start', 'end', 'duration', 'comment', 'status'];

  constructor() {
    this.dataSource = new VacationRequestTableDataSource();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }
}
