import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8085/api/v1/user-management/users/'; // Cambia esta URL por la de tu backend

  constructor(private http: HttpClient) { }

  updateRole(userId: number, roleId: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}${userId}/role`, { roleId });
  }
}
