import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Volunteer } from '../models/volunteer.model';

@Injectable({
  providedIn: 'root'
})
export class VolunteerService {
  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) { }

  createVolunteer(volunteerData: Volunteer): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/volunteers`, volunteerData);
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
}
