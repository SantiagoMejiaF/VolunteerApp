import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import { Organization } from '../organization.model';

const cabecera = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

@Injectable({
  providedIn: 'root'
})
export class OrganizationService {
  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) { }

  createOrganization(organizationData: Organization): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/organizations`, organizationData);
  }

  getOrganizationDetails(userId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/organizations/${userId}`);
  }

  getVolunteeringTypes(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/organizations/volunteering-types`);
  }

  getSectorTypes(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/organizations/sectors-types`);
  }

  getOrganizationTypes(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/organizations/organization-types`);
  }

  public getActiveOrganizations(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/users/count-organizations-status?authorizationStatus=AUTORIZADO`, cabecera);
  }
}
