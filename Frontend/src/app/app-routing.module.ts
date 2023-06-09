import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {SignInComponent} from "./components/sign-in/sign-in.component";
import {ForgotPasswordComponent} from "./components/forgot-password/forgot-password.component";
import {VerifyEmailComponent} from "./components/verify-email/verify-email.component";
import {SignUpComponent} from "./components/sign-up/sign-up.component";
import {AuthGuard} from "./shared/guard/auth.guard";
import {VacationRequestTableComponent} from "./components/vacation-request-table/vacation-request-table.component";
import {
  VirtualEnvironmentContainerComponent
} from "./components/virtual-environment-container/virtual-environment-container.component";

const routes: Routes = [
  { path: '', redirectTo: '/sign-in', pathMatch: 'full' },
  { path: 'sign-in', component: SignInComponent },
  { path: 'register-user', component: SignUpComponent },
  { path: 'dashboard', component: VacationRequestTableComponent, canActivate: [AuthGuard]},
  { path: 'venv', component: VirtualEnvironmentContainerComponent, canActivate: [AuthGuard]},
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'verify-email-address', component: VerifyEmailComponent }
];
@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
