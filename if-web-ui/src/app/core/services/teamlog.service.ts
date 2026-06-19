import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, TeamLog, TeamLogRequest } from '../../shared/models/api.model';

@Injectable({ providedIn: 'root' })
export class TeamlogService {
  private http = inject(HttpClient);
  private api = environment.apiUrl;

  getAll(): Observable<TeamLog[]> {
    return this.http
      .get<ApiResponse<TeamLog[]>>(`${this.api}/team-log`)
      .pipe(map((r) => r.data ?? []));
  }

  create(req: TeamLogRequest): Observable<TeamLog> {
    return this.http
      .post<ApiResponse<TeamLog>>(`${this.api}/admin/team-log`, req)
      .pipe(map((r) => r.data));
  }

  update(id: number, req: TeamLogRequest): Observable<TeamLog> {
    return this.http
      .put<ApiResponse<TeamLog>>(`${this.api}/admin/team-log/${id}`, req)
      .pipe(map((r) => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http
      .delete<ApiResponse<unknown>>(`${this.api}/admin/team-log/${id}`)
      .pipe(map(() => void 0));
  }
}
