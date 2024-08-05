import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VolunteerService {
  private apiUrl = 'http://localhost:8085/api/v1/user-management/volunteers'; // Cambia esta URL por la de tu backend

  constructor(private http: HttpClient) { }

  createVolunteer(volunteerData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}`, volunteerData);
  }
}
