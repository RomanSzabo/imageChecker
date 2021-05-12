import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../environments/environment";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {SimilarityReport} from "./interfaces.model";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable()
export class ImageService {

  constructor(private http: HttpClient, private snackBar: MatSnackBar) {}

  baseUrl = environment.baseUrl;
  result: SimilarityReport;

  private prepareOptions():any {
    let headers = new HttpHeaders();
    headers = headers
      //.set('Content-Type', 'application/json')
      .set('Authorization', 'Bearer ' + localStorage.getItem('msal.idtoken'))
    console.log("Token", localStorage.getItem('msal.idtoken'));
    console.log("Headers", headers);
    return headers;
  }

  test() {
    return this.http.get(this.baseUrl + 'api/test', {headers: this.prepareOptions()});
  }

  getKeypoitsImage(data): Observable<Blob> {
    return this.http.post(this.baseUrl + 'api/extract/features', data, {headers: this.prepareOptions(), responseType: "blob"});
  }

  getMetadata(data): Observable<any> {
    return this.http.post(this.baseUrl + "api/extract/metadata", data, {headers: this.prepareOptions()});
  }

  getImageSize(data): Observable<any> {
    return this.http.post(this.baseUrl + 'api/register/util/checkSize', data, {headers: this.prepareOptions()});
  }

  registerImage(data): Observable<any> {
    return this.http.post(this.baseUrl + 'api/register/image', data, {headers: this.prepareOptions()});
  }

  checkImage(data): Observable<any> {
    return this.http.post(this.baseUrl + 'api/check/image', data, {headers: this.prepareOptions()});
  }

  checkPublicImage(data): Observable<any> {
    return this.http.post(this.baseUrl + 'public/check/image', data);
  }

  updateUser(data): Observable<any> {
    return this.http.put(this.baseUrl + 'api/register/user', data, {headers: this.prepareOptions()});
  }

  sendNewMessage(data): Observable<any> {
    return this.http.post(this.baseUrl + 'api/message/new', data, {headers: this.prepareOptions()});
  }

  getMessages(): Observable<any> {
    return this.http.get(this.baseUrl + 'api/message/get', {headers: this.prepareOptions()});
  }

  snackBarMessage(message: string) {
    this.snackBar.open(message, 'Close', {duration: 5000, verticalPosition: 'top'});
  }


}
