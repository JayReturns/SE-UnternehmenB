import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable, switchMap, take} from 'rxjs';
import {AuthService} from "../shared/services/auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    return this.authService.afAuth.idToken.pipe(
      take(1),
      switchMap((token: any) => {
        if (token) {
          request = request.clone({
            headers: request.headers.set('Authorization', `Bearer ${token}`)
          });
        }
        console.log("Request:");
        console.log(request);
        return next.handle(request);
      })
    )
  }

}
