import {User} from "./user.model";

/**
 * Names must match fields from json response
 * refer http://localhost:8080/swagger-ui/index.html#/ -> Schemas -> VirtualEnvironmentRequest
 */
export interface VEnvironmentRequest {
  virtualEnvironmentRequestId?: string;
  user?: User;
  environmentType: string;
  comment: string;
  status?: Status;
  rejectReason?: string;
}

/**
 * Names must match fields from json response
 * refer http://localhost:8080/swagger-ui/index.html#/ -> Schemas -> VirtualEnvironmentRequest
 */
export interface GroupedVEnvironmentRequest {
  user: User
  requests: VEnvironmentRequest[]
}

export enum Status {
  REQUESTED = "REQUESTED",
  APPROVED = "APPROVED",
  REJECTED = "REJECTED"
}
