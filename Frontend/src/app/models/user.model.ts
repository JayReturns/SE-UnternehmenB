export interface User {
  userId?: string;
  email: string;
  name: string;
  lastName: string;
  vacationDays: number;
  role: Role;
}

export enum Role {
  MANAGER,
  EMPLOYEE
}
