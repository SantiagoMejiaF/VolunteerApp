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
  private apiUrl = `${environment.apiUrl}/missions/organization`;

  constructor(private http: HttpClient) { }

  // Obtener actividades de la organización utilizando el OrgId del localStorage
  getActivities(): Observable<Activity[]> {
    const orgId = localStorage.getItem('OrgId');
    return this.http.get<Activity[]>(`${this.apiUrl}/${orgId}/activities`, cabecera);
  }


}
