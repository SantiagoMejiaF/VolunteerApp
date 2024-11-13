import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Mission } from '../mission.model';
import { environment } from '../../../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class MissionsService {
    private apiUrl = `${environment.apiUrl}`;

    constructor(private http: HttpClient) { }

    getMissionsByOrganization(orgId: number): Observable<Mission[]> {
        return this.http.get<Mission[]>(`${this.apiUrl}/missions/organization/${orgId}`);
    }

    getMissionById(id: number): Observable<Mission> {
        return this.http.get<Mission>(`${this.apiUrl}/missions/${id}`);
    }

    getMissionTypes(): Observable<string[]> {
        return this.http.get<string[]>(`${this.apiUrl}/missions/mission-types`);
    }

    getVolunteerRequirements(): Observable<string[]> {
        return this.http.get<string[]>(`${this.apiUrl}/missions/volunteer-requirements`);
    }

    getRequiredSkills(): Observable<string[]> {
        return this.http.get<string[]>(`${this.apiUrl}/missions/required-skills`);
    }

    getMissionStatusOptions(): Observable<string[]> {
        return this.http.get<string[]>(`${this.apiUrl}/missions/mission-status-options`);
    }

    createMission(mission: Mission): Observable<Mission> {
        return this.http.post<Mission>(`${this.apiUrl}/missions`, mission);
    }

    getActivityById(activityId: number): Observable<any> {
        const url = `${this.apiUrl}/activities/${activityId}`;
        return this.http.get<any>(url);
    }

    getActivityCoordinator(coordinatorId: number): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/activity-coordinators/${coordinatorId}`);
    }

    getInterests(): Observable<string[]> {
        return this.http.get<string[]>(`${this.apiUrl}/missions/interests`);
    }

    getQRInicial(activityId: number): Observable<Blob> {
        return this.http.get<Blob>(`${this.apiUrl}/activities/checkin/${activityId}`, { responseType: 'blob' as 'json' });
    }

    getQRFinal(activityId: number): Observable<Blob> {
        return this.http.get<Blob>(`${this.apiUrl}/activities/checkout/${activityId}`, { responseType: 'blob' as 'json' });
    }

    joinActivity(volunteerId: number, activityId: number): Observable<string> {
        return this.http.post(`${this.apiUrl}/volunteers/${volunteerId}/activities/${activityId}/signup`, {}, { responseType: 'text' });
    }

    removeMission(missionId: number): Observable<any> {
        console.log('Eliminando misión con ID:', missionId); // Verifica si el id se pasa correctamente
        return this.http.delete<any>(`${this.apiUrl}/missions/${missionId}`);
    }

    updateMission(missionId: number, mission: any): Observable<Mission> {
        return this.http.put<Mission>(`${this.apiUrl}/missions/${missionId}`, mission);
    }

    updateActivity(activityId: number, activity: any): Observable<any> {
        return this.http.put<any>(`${this.apiUrl}/activities/${activityId}`, activity);
    }
}
