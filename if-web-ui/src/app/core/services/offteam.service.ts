import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, OffteamEvent, OffteamEventRequest } from '../../shared/models/api.model';

@Injectable({ providedIn: 'root' })
export class OffteamService {
  private http = inject(HttpClient);
  private api = environment.apiUrl;

  getAll(): Observable<OffteamEvent[]> {
    return this.http
      .get<ApiResponse<OffteamEvent[]>>(`${this.api}/offteam`)
      .pipe(map((r) => r.data ?? []));
  }

  getById(id: number): Observable<OffteamEvent> {
    return this.http
      .get<ApiResponse<OffteamEvent>>(`${this.api}/offteam/${id}`)
      .pipe(map((r) => r.data));
  }

  create(req: OffteamEventRequest): Observable<OffteamEvent> {
    return this.http
      .post<ApiResponse<OffteamEvent>>(`${this.api}/admin/offteam`, req)
      .pipe(map((r) => r.data));
  }

  update(id: number, req: OffteamEventRequest): Observable<OffteamEvent> {
    return this.http
      .put<ApiResponse<OffteamEvent>>(`${this.api}/admin/offteam/${id}`, req)
      .pipe(map((r) => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http
      .delete<ApiResponse<unknown>>(`${this.api}/admin/offteam/${id}`)
      .pipe(map(() => void 0));
  }
}
