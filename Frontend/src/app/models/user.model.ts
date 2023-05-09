export interface User {
  userId?: string;
  email: string;
  name: string;
  lastName: string;
  vacationDays: number;
  role: Role;
}

// TODO: Change ADMIN, USER to something else
export enum Role {
  ADMIN,
  USER
}
