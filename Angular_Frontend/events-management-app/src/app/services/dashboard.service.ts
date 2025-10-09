import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface DashboardCounts {
  events: number;
  clubs: number;
  participants: number;
  registrations: number;
  sponsors: number;
  venues: number;
  volunteers: number;
  budgets: number;
  departments: number;
  results: number;
  judges: number;
  feedback: number;
  admins: number;
  users: number;
  comments: number;
}

export interface MonthlyRegistration {
  month: string;
  count: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = `${environment.apiUrl}/api/admin/dashboard`;

  constructor(private http: HttpClient) {}

  getDashboardCounts(): Observable<DashboardCounts> {
    return this.http.get<DashboardCounts>(`${this.apiUrl}/counts`);
  }

  getMonthlyRegistrations(): Observable<MonthlyRegistration[]> {
    return this.http.get<MonthlyRegistration[]>(`${this.apiUrl}/registrations/monthly`);
  }
}
