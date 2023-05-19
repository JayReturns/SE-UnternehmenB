import { DataSource } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { map } from 'rxjs/operators';
import { Observable, of as observableOf, merge } from 'rxjs';

export interface VacationRequestTableItem {
  vac_id: number;
  start_date: Date;
  end_date: Date;
  duration: number;
  comment: string;
  status: string;
}

export interface VacationRequestTableItem_GL {
  vac_id: number;
  first_name: string;
  last_name: string;
  start_date: Date;
  end_date: Date;
  duration: number;
  comment: string;
  status: string;
}


// TODO: replace with db data (GET vacation request)
const EXAMPLE_DATA: VacationRequestTableItem[] = [
  {vac_id: 1, start_date: new Date('2023-05-01'), end_date: new Date('2023-05-31'), duration: 20, comment: 'Ich bin ein Kommentar', status: 'In Bearbeitung'},
  {vac_id: 2, start_date: new Date('2023-05-01'), end_date: new Date('2023-05-31'), duration: 20, comment: 'Ich bin ein Kommentar', status: 'Abgelehnt'},
  {vac_id: 3, start_date: new Date('2023-05-01'), end_date: new Date('2023-05-31'), duration: 20, comment: 'Ich bin ein Kommentar', status: 'Genehmigt'},

];

/**
 * Data source for the VacationRequestTable view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class VacationRequestTableDataSource extends DataSource<VacationRequestTableItem> {
  data: VacationRequestTableItem[] = EXAMPLE_DATA;
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
  connect(): Observable<VacationRequestTableItem[]> {
    if (this.paginator && this.sort) {
      // Combine everything that affects the rendered data into one update
      // stream for the data-table to consume.
      return merge(observableOf(this.data), this.paginator.page, this.sort.sortChange)
        .pipe(map(() => {
          return this.getPagedData(this.getSortedData([...this.data ]));
        }));
    } else {
      throw Error('Please set the paginator and sort on the data source before connecting.');
    }
  }

  /**
   *  Called when the table is being destroyed. Use this function, to clean up
   * any open connections or free any held resources that were set up during connect.
   */
  disconnect(): void {}

  /**
   * Paginate the data (client-side). If you're using server-side pagination,
   * this would be replaced by requesting the appropriate data from the server.
   */
  private getPagedData(data: VacationRequestTableItem[]): VacationRequestTableItem[] {
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
  private getSortedData(data: VacationRequestTableItem[]): VacationRequestTableItem[] {
    if (!this.sort || !this.sort.active || this.sort.direction === '') {
      return data;
    }

    return data.sort((a, b) => {
      const isAsc = this.sort?.direction === 'asc';
      switch (this.sort?.active) {
        case 'id': return compare(+a.vac_id, +b.vac_id, isAsc);
        case 'start_date': return compare(+a.start_date, +b.start_date, isAsc);
        case 'end_date': return compare(+a.end_date, +b.end_date, isAsc);
        case 'duration': return compare(+a.duration, +b.duration, isAsc);
        case 'comment': return compare(+a.comment, +b.comment, isAsc);
        case 'status': return compare(+a.status, +b.status, isAsc);
        default: return 0;
      }
    });
  }
}

/** Simple sort comparator for example ID/Name columns (for client-side sorting). */
function compare(a: string | number, b: string | number, isAsc: boolean): number {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
