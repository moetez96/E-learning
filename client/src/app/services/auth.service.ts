import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

const AUTH_API = 'http://localhost:8088/api/auth/';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
  }

  login(credentials: { username: any; password: any; }): Observable<any> {
    return this.http.post(AUTH_API + 'signin', {
      username: credentials.username,
      password: credentials.password
    }, httpOptions);
  }

  register(user: { username: any; email: any; roles: any[]; password: any; firstName: any; lastName: any; speciality: any; birthDate: string; }): Observable<any> {
    return this.http.post(AUTH_API + 'signup', user, httpOptions);
  }

  registerAdmin(user: { firstName: any; lastName: any; password: any; roles: string[]; birthDate: string; email: any; username: any }): Observable<any> {
    return this.http.post(AUTH_API + 'signup', user, httpOptions);
  }
}
