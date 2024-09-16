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
}
