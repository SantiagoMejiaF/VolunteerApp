import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

const cabecera = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = `${environment.apiUrl}`;

  constructor(private httpClient: HttpClient) { }

  public getTotalUsers(): Observable<number> {
    return this.httpClient.get<number>(`${this.apiUrl}/users/count`, cabecera);
  }

  public getActiveVolunteers(): Observable<number> {
    return this.httpClient.get<number>(`${this.apiUrl}/volunteers/active-count`, cabecera);
  }

  public getActiveOrganizations(): Observable<number> {
    return this.httpClient.get<number>(`${this.apiUrl}/users/count-organizations-status?authorizationStatus=AUTORIZADO`, cabecera);
  }

  public getAuthorizedUsers(): Observable<any[]> {
    return this.httpClient.get<any[]>(`${this.apiUrl}/users/status/AUTORIZADO`, cabecera);
  }

  public getPendingUsers(): Observable<any[]> {
    return this.httpClient.get<any[]>(`${this.apiUrl}/users/status/PENDIENTE`, cabecera);
  }

  public sendApprovalEmail(userId: number, approved: boolean): Observable<any> {
    return this.httpClient.post<any>(
      `${this.apiUrl}/users/${userId}/send-approval-or-rejection-email?userId=${userId}&approved=${approved}`,
      {},
      cabecera
    );
  }
}
