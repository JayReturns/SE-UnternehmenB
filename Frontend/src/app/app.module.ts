import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {initializeApp, provideFirebaseApp} from '@angular/fire/app';
import {environment} from '../environments/environment';
import {provideAuth, getAuth} from '@angular/fire/auth';
import {SignInComponent} from './components/sign-in/sign-in.component';
import {SignUpComponent} from './components/sign-up/sign-up.component';
import {ForgotPasswordComponent} from './components/forgot-password/forgot-password.component';
import {VerifyEmailComponent} from './components/verify-email/verify-email.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {AngularFireDatabaseModule} from "@angular/fire/compat/database";
import {AngularFirestoreModule} from "@angular/fire/compat/firestore";
import {AngularFireStorageModule} from "@angular/fire/compat/storage";
import {AngularFireAuthModule} from "@angular/fire/compat/auth";
import {AuthService} from "./shared/services/auth.service";
import {AngularFireModule} from "@angular/fire/compat";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {MaterialModule} from "../material.module";
import {ReactiveFormsModule} from "@angular/forms";
import {AuthInterceptor} from "./interceptor/auth.interceptor";
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import {MatTooltipModule} from '@angular/material/tooltip';
import { MatSortModule } from '@angular/material/sort';
import { VacationRequestTableComponent } from './components/vacation-request-table/vacation-request-table.component';
import { VacationDialogComponent } from './components/vacation-dialog/vacation-dialog.component';
import {VEnvironmentRequestComponent} from "./components/v-environment-request-dialog/v-environment-request-dialog.component";
import {MAT_DATE_LOCALE, DateAdapter} from "@angular/material/core";
import { VacationConfirmationPopupComponent } from './components/vacation-confirmation-popup/vacation-confirmation-popup.component';
import {StartOfWeekAdapter} from "./adapter/start-of-week.adapter";
import {registerLocaleData} from "@angular/common";
import localeDe from '@angular/common/locales/de';
import { VEnvironmentConfirmationPopupComponent } from './components/v-environment-confirmation-popup/v-environment-confirmation-popup.component'

registerLocaleData(localeDe)

@NgModule({
  declarations: [
    AppComponent,
    SignInComponent,
    SignUpComponent,
    ForgotPasswordComponent,
    VerifyEmailComponent,
    DashboardComponent,
    VacationRequestTableComponent,
    DashboardComponent,
    VacationDialogComponent,
    VacationConfirmationPopupComponent,
    VEnvironmentRequestComponent,
    VEnvironmentConfirmationPopupComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        provideFirebaseApp(() => initializeApp(environment.firebase)),
        AngularFireModule.initializeApp(environment.firebase),
        provideAuth(() => getAuth()),
        AngularFireAuthModule,
        AngularFirestoreModule,
        AngularFireStorageModule,
        AngularFireDatabaseModule,
        HttpClientModule,
        MaterialModule,
        ReactiveFormsModule,
        MatTableModule,
        MatPaginatorModule,
        MatTooltipModule,
        MatSortModule
    ],
  providers: [AuthService,
    {provide: MAT_DATE_LOCALE, useValue: 'de-DE'},
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {provide: DateAdapter, useClass: StartOfWeekAdapter},
    {provide: LOCALE_ID, useValue: 'de-DE'}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
