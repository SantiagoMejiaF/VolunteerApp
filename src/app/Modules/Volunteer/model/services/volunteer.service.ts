import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import { Volunteer } from '../volunteer.model';

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
    return this.http.get<any>(`${this.apiUrl}/volunteers/user/${userId}`);
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

  getActiveVolunteers(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/volunteers-dashboard/active-count`, cabecera);
  }

  updateVolunteer(id: number, volunteerData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/volunteers/${id}`, volunteerData, cabecera);
  }

  getCompletedActivities(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/activities/volunteer/${userId}/completed`);
  }

  // Método para obtener la puntuación promedio
  getAverageRating(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/activities/volunteer/${userId}/rating`);
  }

  // Método para obtener los beneficiarios impactados
  getBeneficiaries(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/activities/volunteer/${userId}/beneficiaries`);
  }

  getActivitiesByVolunteerId(volunteerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/activities/volunteer/${volunteerId}`);
  }

  getVolunteerFoundations(volunteerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/volunteers-dashboard/foundations/${volunteerId}`);
  }
}
