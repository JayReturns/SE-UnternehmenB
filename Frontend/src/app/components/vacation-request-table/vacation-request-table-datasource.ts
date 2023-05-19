import {DataSource} from '@angular/cdk/collections';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {map} from 'rxjs/operators';
import {merge, Observable, of as observableOf} from 'rxjs';
import {Status, Vacation} from "../../models/vacation.model";

// TODO: replace with db data (GET vacation request)
const EXAMPLE_DATA: (Vacation)[] = [
  {
    vacationRequestId: "1",
    start: new Date('2023-05-01'),
    end: new Date('2023-05-31'),
    duration: 20,
    comment: 'Ich bin ein Kommentar',
    status: Status.APPROVED
  },
  {
    vacationRequestId: "2",
    start: new Date('2023-05-01'),
    end: new Date('2023-05-31'),
    duration: 20,
    comment: 'Ich bin ein Kommentar',
    status: Status.REQUESTED
  },
  {
    vacationRequestId: "3",
    start: new Date('2023-05-01'),
    end: new Date('2023-05-31'),
    duration: 20,
    comment: 'Ich bin ein Kommentar',
    status: Status.REJECTED
  },

];

/**
 * Data source for the VacationRequestTable view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class VacationRequestTableDataSource extends DataSource<Vacation> {
  data: Vacation[] = EXAMPLE_DATA;
  paginator: MatPaginator | undefined;
  sort: MatSort | undefined;

  constructor() {
    super();
  }

  /**
   * Connect this data source to the table. The table will only update when
   * the returned stream emits new items.
   * @returns A stream of the items to be rendered.
   */
  connect(): Observable<Vacation[]> {
    if (this.paginator && this.sort) {
      // Combine everything that affects the rendered data into one update
      // stream for the data-table to consume.
      return merge(observableOf(this.data), this.paginator.page, this.sort.sortChange)
        .pipe(map(() => {
          return this.getPagedData(this.getSortedData([...this.data]));
        }));
    } else {
      throw Error('Please set the paginator and sort on the data source before connecting.');
    }
  }

  /**
   *  Called when the table is being destroyed. Use this function, to clean up
   * any open connections or free any held resources that were set up during connect.
   */
  disconnect(): void {
  }

  /**
   * Paginate the data (client-side). If you're using server-side pagination,
   * this would be replaced by requesting the appropriate data from the server.
   */
  private getPagedData(data: Vacation[]): Vacation[] {
    if (this.paginator) {
      const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
      return data.splice(startIndex, this.paginator.pageSize);
    } else {
      return data;
    }
  }

  /**
   * Sort the data (client-side). If you're using server-side sorting,
   * this would be replaced by requesting the appropriate data from the server.
   */
  private getSortedData(data: Vacation[]): Vacation[] {
    if (!this.sort || !this.sort.active || this.sort.direction === '') {
      return data;
    }

    return data.sort((a, b) => {
      const isAsc = this.sort?.direction === 'asc';
      const field_name = this.sort?.active;

      // @ts-ignore
      return field_name == undefined? 0 : compare(+a[field_name], +b[field_name], isAsc);
      //switch (this.sort?.active) {
      //  case 'nr': return compare(+a.vacationRequestId!, +b.vacationRequestId!, isAsc);
      //  case 'start': return compare(+a.start, +b.start, isAsc);
      //  case 'end': return compare(+a.end, +b.end, isAsc);
      //  case 'duration': return compare(+a.duration, +b.duration, isAsc);
      //  case 'comment': return compare(+a.comment, +b.comment, isAsc);
      //  case 'status': return compare(+a.status!, +b.status!, isAsc);
      //  default: return 0;
      //}
    });
  }
}

/** Simple sort comparator for example ID/Name columns (for client-side sorting). */
function compare(a: string | number, b: string | number, isAsc: boolean): number {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
