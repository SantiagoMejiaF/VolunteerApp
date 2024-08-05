import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Organization } from '../models/organization.model';

@Injectable({
  providedIn: 'root'
})
export class OrganizationService {
  private apiUrl = `${environment.apiUrl}/organizations`;

  constructor(private http: HttpClient) { }

  createOrganization(organizationData: Organization): Observable<any> {
    return this.http.post<any>(this.apiUrl, organizationData);
  }
}
