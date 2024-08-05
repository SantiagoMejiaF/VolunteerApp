import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Organization } from '../models/organization.model';

@Injectable({
  providedIn: 'root'
})
export class OrganizationService {
  private apiUrl = 'http://localhost:8085/api/v1/user-management/organizations/'; // Cambia esta URL por la de tu backend

  constructor(private http: HttpClient) { }

  createOrganization(organizationData: Organization): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}`, organizationData);
  }
}
