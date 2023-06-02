import {User} from "./user.model";

export interface VirtualEnvironment {
  virtualEnvironmentId: string
  environmentType: string
  ipAddress: string
  userName: string
  password: string
}

export interface VERequest {
  virtualEnvironmentRequestId: string
  user?: User
  environmentType: string
  comment: string
  status: Status
  rejectReason?: string
}

export interface GroupedVERequest {
  user: User
  requests: VERequest[]
}


export enum Status {
  REQUESTED = "REQUESTED",
  APPROVED = "APPROVED",
  REJECTED = "REJECTED"
}
