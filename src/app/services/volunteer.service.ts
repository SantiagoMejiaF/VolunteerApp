import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Volunteer } from '../models/volunteer.model';

const cabecera = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

@Injectable({
  providedIn: 'root'
})
export class VolunteerService {
  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) { }

  createVolunteer(volunteerData: Volunteer): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/volunteers`, volunteerData);
  }

  getVolunteerDetails(userId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/volunteers/${userId}`);
  }

  getInterests(): Observable<string[]> {
    return this.http.get<string[]>(`${environment.apiUrl}/volunteers/interests`);
  }

  getSkills(): Observable<string[]> {
    return this.http.get<string[]>(`${environment.apiUrl}/volunteers/skills`);
  }

  getAvailabilities(): Observable<string[]> {
    return this.http.get<string[]>(`${environment.apiUrl}/volunteers/availabilities`);
  }

  getRelationships(): Observable<string[]> {
    return this.http.get<string[]>(`${environment.apiUrl}/volunteers/relationships`);
  }

  public getActiveVolunteers(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/volunteers/active-count`, cabecera);
  }
}
