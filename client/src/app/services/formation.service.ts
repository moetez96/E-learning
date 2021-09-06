import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};
const httpOptionsMultipart = {
  headers: new HttpHeaders({'Content-Type': 'multipart/form-data'})
};

@Injectable({
  providedIn: 'root'
})
export class FormationService {
  private AUTH_API = 'http://localhost:8088/api/formation/';

  constructor(private http: HttpClient) {
  }

  add(formation: any): Observable<any> {
    var form_data = new FormData();

    for (var key in formation) {
      form_data.append(key, formation[key]);
    }
    return this.http.post(this.AUTH_API + 'add', form_data);
  }

  getById(id: String): Observable<any> {
    return this.http.get(this.AUTH_API + 'getById/' + id, httpOptions);
  }

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.AUTH_API + 'all', httpOptions);
  }

  addMeet(id: String, meeting: { link: any, date: any }): Observable<any> {
    const params = new URLSearchParams();
    params.set('link', meeting.link);
    params.set('date', meeting.date);
    return this.http.post(this.AUTH_API + 'addMeet/' + id, {link: meeting.link, date: meeting.date}, httpOptions);
  }

  participate(id: String): Observable<any> {
    return this.http.post(this.AUTH_API + 'participate/' + id, httpOptions);
  }

  addComment(comment: any): Observable<any> {
    return this.http.post(this.AUTH_API + 'addComment/', comment, httpOptions);
  }

  deleteComment(idf: any, idc: any): Observable<any> {
    return this.http.post(this.AUTH_API + 'removeComment/' + idf + '/' + idc, httpOptions);
  }

  cancelParticipate(id: String): Observable<any> {
    return this.http.post(this.AUTH_API + 'cancelParticipation/' + id, httpOptions);
  }

  removeParticipate(id: String, uid: String): Observable<any> {
    return this.http.post(this.AUTH_API + 'removeParticipation/' + id + '/' + uid, httpOptions);
  }

  removeFile(id: String, idf: String): Observable<any> {
    return this.http.post(this.AUTH_API + 'removeFile/' + id + '/' + idf, httpOptions);
  }

  deleteFormation(id: String): Observable<any> {
    return this.http.delete(this.AUTH_API + 'deleteFormation/' + id, httpOptions);
  }

  addFile(file: File, id: String): Observable<any> {
    const data: FormData = new FormData();
    data.append('file', file);
    return this.http.post(this.AUTH_API + 'upload/' + id, data, {
      reportProgress: true,
      responseType: 'text'
    });
  }

  getBySepc(speciality: any): Observable<any[]> {
    return this.http.get<any[]>(this.AUTH_API + 'allByType/' + speciality, httpOptions);
  }

  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>(this.AUTH_API + 'allUsers', httpOptions);

  }

  deleteuser(id: String): Observable<any> {
    return this.http.delete(this.AUTH_API + 'deleteUser/' + id, httpOptions);

  }

  deleteFormationByAdmin(id: String): Observable<any> {
    return this.http.delete(this.AUTH_API + 'deleteFormationByAdmin/' + id, httpOptions);

  }

  update(formation: any, id: any) {
    var form_data = new FormData();

    for (var key in formation) {
      form_data.append(key, formation[key]);
    }
    return this.http.post(this.AUTH_API + 'update/' + id, form_data);
  }
}
