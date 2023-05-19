import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { VacationRequestTableDataSource, VRTableItem_Employee, VRTableItem_Manager } from './vacation-request-table-datasource';

@Component({
  selector: 'app-vacation-request-table',
  templateUrl: './vacation-request-table.component.html',
  styleUrls: ['./vacation-request-table.component.css']
})
export class VacationRequestTableComponent implements AfterViewInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<VRTableItem_Employee>;
  dataSource: VacationRequestTableDataSource;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'start_date', 'end_date', 'duration', 'comment', 'status'];

  constructor() {
    this.dataSource = new VacationRequestTableDataSource();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }
}
