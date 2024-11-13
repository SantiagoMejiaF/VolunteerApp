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

  getMatchingOrganizations(volunteerId: number, numberOfMatches: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/volunteers/${volunteerId}/match-organizations?numberOfMatches=${numberOfMatches}`);
  }

  joinOrganization(volunteerId: number, organizationId: number): Observable<any> {
    const body = {
      volunteerId: volunteerId,
      organizationId: organizationId
    };

    return this.http.post<any>(`${this.apiUrl}/volunteers-organizations/pending`, body, cabecera);
  }

  getActivitiesByYear(volunteerId: number, year: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/activities/volunteer/${volunteerId}/year/${year}`);
  }

  getRecentOrganizations(volunteerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/volunteers-organizations/recent/${volunteerId}`);
  }

  getAllOrganizations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/organizations-dashboard/get-cards-all-organizations`);
  }

  getVolunteerHistory(volunteerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/reviews/history/volunteer/${volunteerId}`);
  }

  approveVolunteer(volunteerId: number, organizationId: number, approved: boolean): Observable<any> {
    const url = `${this.apiUrl}/organizations/approve-volunteer?volunteerId=${volunteerId}&organizationId=${organizationId}&approved=${approved}`;
    return this.http.post<any>(url, {}, cabecera); // Se hace la solicitud POST al servidor
  }

  removeActivity(volunteerId: number, activityId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/volunteers/${volunteerId}/activities/${activityId}/remove`, { responseType: 'text' as 'json' });
  }

}
