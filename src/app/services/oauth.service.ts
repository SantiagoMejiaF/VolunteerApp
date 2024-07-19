import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TokenDto } from '../models/token-dto';
import { Observable } from 'rxjs';

const cabecera = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }

@Injectable({
  providedIn: 'root'
})
export class OauthService {

  oauth = 'http://localhost:8085/api/v1/user-management/users/'

  constructor(private httpClient: HttpClient) { }

  public google(tokenDto: TokenDto): Observable<TokenDto> {
    return this.httpClient.post<TokenDto>(this.oauth + 'google', tokenDto, cabecera)
  }

  public facebook(tokenDto: TokenDto): Observable<TokenDto> {
    return this.httpClient.post<TokenDto>(this.oauth + 'facebook', tokenDto, cabecera);
  }

  public apple(tokenDto: TokenDto): Observable<TokenDto> {
    return this.httpClient.post<TokenDto>(this.oauth + 'apple', tokenDto, cabecera);
  }

}
