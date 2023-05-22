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
  @Input() forManager = false;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<Vacation>;
  dataSource = new MatTableDataSource();

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['nr', 'vacationStart', 'vacationEnd', 'duration', 'comment', 'status'];

  constructor(private vacationService: VacationService) {
    if (this.forManager) {
      this.displayedColumns = [this.displayedColumns[0], 'name', ...this.displayedColumns.slice(1)]
    }
    this.refresh()
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.dataSource.data = EXAMPLE_DATA
    this.dataSource.sortingDataAccessor = (item, property) => this.sortData(item as Vacation, property)
  }

  refresh() {
    console.log("Get vacation data")
    this.getData().subscribe(vacations => {
      console.log("New data is there", vacations)
      this.dataSource.data = vacations
      console.log("Data rendered")
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
}
