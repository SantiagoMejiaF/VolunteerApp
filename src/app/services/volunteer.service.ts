import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Volunteer } from '../models/volunteer.model';

@Injectable({
  providedIn: 'root'
})
export class VolunteerService {
  private apiUrl = `${environment.apiUrl}/volunteers`;

  constructor(private http: HttpClient) { }

  createVolunteer(volunteerData: Volunteer): Observable<any> {
    return this.http.post<any>(this.apiUrl, volunteerData);
  }
}
