import {AfterViewInit, Component, Input, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import {GroupedVacation, Status, Vacation} from "../../models/vacation.model";
import {VacationService} from "../../services/vacation.service";
import {map} from "rxjs/operators";

// TODO: replace with db data (GET vacation request)
const EXAMPLE_DATA: (Vacation)[] = [
  {
    vacationRequestId: "1",
    vacationStart: new Date('2023-05-01'),
    vacationEnd: new Date('2023-05-31'),
    duration: 20,
    comment: 'Ich bin ein Kommentar',
    status: Status.APPROVED
  },
  {
    vacationRequestId: "2",
    vacationStart: new Date('2023-05-01'),
    vacationEnd: new Date('2023-05-31'),
    duration: 20,
    comment: 'Ich bin ein Kommentar',
    status: Status.REQUESTED
  },
  {
    vacationRequestId: "3",
    vacationStart: new Date('2023-05-01'),
    vacationEnd: new Date('2023-05-31'),
    duration: 20,
    comment: 'Ich bin ein Kommentar',
    status: Status.REJECTED
  },

];

@Component({
  selector: 'app-vacation-request-table',
  templateUrl: './vacation-request-table.component.html',
  styleUrls: ['./vacation-request-table.component.scss']
})
export class VacationRequestTableComponent implements AfterViewInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<Vacation>;
  dataSource = new MatTableDataSource();

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['nr', 'start', 'end', 'duration', 'comment', 'status'];

  constructor() {
    this.dataSource = new VacationRequestTableDataSource();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.dataSource.data = EXAMPLE_DATA
  }
}
