export interface User {
  userId?: string;
  email: string;
  name: string;
  lastName: string;
  vacationDays: VacationDays;
  role: Role;
}

interface VacationDays{
  maxDays: number;
  leftDays: number;
  leftDaysOnlyApproved: number;
}

export enum Role {
  MANAGER = "MANAGER",
  EMPLOYEE = "EMPLOYEE"
}
