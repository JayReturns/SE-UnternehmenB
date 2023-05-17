import {User} from "./user.model";

export interface Vacation {
  vacationRequestId?: string;
  user?: User;
  vacationStart: Date;
  vacationEnd: Date;
  comment: string;
  status?: Status;
  rejectReason?: string;
  vacationDays: number;
}

export enum Status {
  REQUESTED = "REQUESTED",
  APPROVED = "APPROVED",
  REJECTED = "REJECTED"
}
