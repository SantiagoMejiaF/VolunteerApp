import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TokenDto } from '../models/token-dto';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

const cabecera = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

@Injectable({
  providedIn: 'root'
})
export class OauthService {
  private oauthUrl = `${environment.apiUrl}`;

  constructor(private httpClient: HttpClient) { }

  public google(tokenDto: TokenDto): Observable<any> {
    return this.httpClient.post<any>(`${environment.apiUrl}/authentications/google`, tokenDto, cabecera);
  }

  public facebook(tokenDto: TokenDto): Observable<any> {
    return this.httpClient.post<any>(`${environment.apiUrl}/authentications/facebook`, tokenDto, cabecera);
  }

  public apple(tokenDto: TokenDto): Observable<any> {
    return this.httpClient.post<any>(`${environment.apiUrl}/authentications/apple`, tokenDto, cabecera);
  }
}
