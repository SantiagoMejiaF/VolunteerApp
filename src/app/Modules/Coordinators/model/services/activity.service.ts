import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Activity } from '../activity.model';
import { environment } from '../../../../../environments/environment';

const cabecera = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

@Injectable({
  providedIn: 'root'
})
export class ActivityService {
  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) { }

  // Obtener actividades de la organización utilizando el OrgId del localStorage
  getActivities(): Observable<Activity[]> {
    const orgId = localStorage.getItem('OrgId');
    return this.http.get<Activity[]>(`${this.apiUrl}/missions/organization/3/activities`, cabecera);
  }

  createActivity(activity: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/activities`, activity, cabecera);
  }

  getActivitiesByMissionId(missionId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/activities/mission/${missionId}`, cabecera);
  }

  getCoordinatorsByOrganizationId(orgId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/activity-coordinators/organization/${orgId}`, cabecera);
  }

  getActivityById(activityId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/activities/${activityId}`);
  }

  getActivitiesByCoordinator(coordinatorId: number): Observable<Activity[]> {
    return this.http.get<any[]>(`${this.apiUrl}/activities/coordinator/${coordinatorId}`, cabecera);
  }

  getQRInicial(activityId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/activities/checkout/${activityId}`, { responseType: 'blob' });
  }

  getCoordinatorReviewHistory(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/organizations-dashboard/coordinator-review-history/${userId}`);
  }

  getVolunteersByActivity(activityId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/volunteers-dashboard/volunteers-by-activity/${activityId}`, cabecera);
  }

  updateCoordinatorDetails(coordinatorId: number, data: any): Observable<any> {
    return this.http.patch(`${this.apiUrl}/activity-coordinators/${coordinatorId}`, data, cabecera);
  }

}
