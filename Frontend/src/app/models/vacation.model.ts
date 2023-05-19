import {User} from "./user.model";

export interface Vacation {
  vacationRequestId?: string;
  user?: User;
  start: Date;
  end: Date;
  duration: number;
  comment: string;
  status?: Status;
  rejectReason?: string;
}

export enum Status {
  REQUESTED = "REQUESTED",
  APPROVED = "APPROVED",
  REJECTED = "REJECTED"
}
