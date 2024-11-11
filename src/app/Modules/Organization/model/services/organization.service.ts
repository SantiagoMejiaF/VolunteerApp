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
    return this.http.get<any>(`${this.apiUrl}/organizations/user/${userId}`);
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

  getActiveOrganizations(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/organizations-dashboard/active-count`);
  }

  updateOrganization(id: number, organizationData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/organizations/${id}`, organizationData, cabecera);
  }

  getOrganizationByUserId(userId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/organizations/user/${userId}`);
  }

  getActivityCoordinators(orgId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/activity-coordinators/organization/${orgId}`, cabecera);
  }

  getUserDetails(userId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/users/${userId}`);
  }

  createActivityCoordinator(coordinatorData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/activity-coordinators`, coordinatorData, cabecera);
  }

  getOrganizationDetailsById(organizationId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/organizations/${organizationId}`);
  }

  getOrganizationHistory(organizationId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/organizations-dashboard/history-by-organization/${organizationId}`);
  }

  getOrganizationActivities(organizationId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/missions/organization/${organizationId}/activities`);
  }

  getCompletedMissionsCount(orgId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/organizations-dashboard/completed-missions-count-by-organization/${orgId}`);
  }

  getTotalBeneficiariesImpacted(orgId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/organizations-dashboard/total-beneficiaries-impacted-by-organization/${orgId}`);
  }

  getVolunteerAvailabilityCount(orgId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/organizations-dashboard/volunteer-availability-count-by-organization/${orgId}`);
  }

  getSkillCounts(orgId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/organizations-dashboard/skill-counts-by-organization/${orgId}`);
  }

  getActivitiesCountByYear(orgId: number, year: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/organizations-dashboard/activities-count-by-organization-and-year?organizationId=${orgId}&year=${year}`);
  }

  getActivitiesByOrganization(orgId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/missions/organization/${orgId}/activities`);
  }

  getActivitiesByMissionId(missionId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/activities/mission/${missionId}`);
  }

  getPendingVolunteers(orgId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/volunteers-organizations/pending/${orgId}`);
  }

  getVolunteerDetails(volunteerId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/volunteers/${volunteerId}`);
  }

  getAcceptedVolunteers(orgId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/volunteers-organizations/accepted/${orgId}`);
  }

  getAcceptedVolunteersCount(orgId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/volunteers-organizations/count/accepted/${orgId}`);
  }

  getRecentAcceptedVolunteers(orgId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/volunteers-organizations/recent/list/accepted/${orgId}`);
  }

  removeActivityCoordinator(coordinatorId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/activity-coordinators/${coordinatorId}`, cabecera);
  }

}
