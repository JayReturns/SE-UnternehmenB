import {User} from "./user.model";

/**
 * Names must match fields from json response
 * refer http://localhost:8080/swagger-ui/index.html#/ -> Schemas -> VacationRequest
 */
export interface Vacation {
  vacationRequestId?: string;
  user?: User;
  vacationStart: Date;
  vacationEnd: Date;
  duration: number;
  comment: string;
  status?: Status;
  rejectReason?: string;
}

/**
 * Names must match fields from json response
 * refer http://localhost:8080/swagger-ui/index.html#/ -> Schemas -> AllUsersVRResponseBody
 */
export interface GroupedVacation {
  user: User
  requests: Vacation[]
}

export enum Status {
  REQUESTED = "REQUESTED",
  APPROVED = "APPROVED",
  REJECTED = "REJECTED"
}
