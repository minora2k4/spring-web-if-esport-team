import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, Tournament, TournamentRequest } from '../../shared/models/api.model';

@Injectable({ providedIn: 'root' })
export class TournamentService {
  private http = inject(HttpClient);
  private api = environment.apiUrl;

  getAll(): Observable<Tournament[]> {
    return this.http
      .get<ApiResponse<Tournament[]>>(`${this.api}/tournaments`)
      .pipe(map((r) => r.data ?? []));
  }

  getById(id: number): Observable<Tournament> {
    return this.http
      .get<ApiResponse<Tournament>>(`${this.api}/tournaments/${id}`)
      .pipe(map((r) => r.data));
  }

  create(req: TournamentRequest): Observable<Tournament> {
    return this.http
      .post<ApiResponse<Tournament>>(`${this.api}/admin/tournaments`, req)
      .pipe(map((r) => r.data));
  }

  update(id: number, req: TournamentRequest): Observable<Tournament> {
    return this.http
      .put<ApiResponse<Tournament>>(`${this.api}/admin/tournaments/${id}`, req)
      .pipe(map((r) => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http
      .delete<ApiResponse<unknown>>(`${this.api}/admin/tournaments/${id}`)
      .pipe(map(() => void 0));
  }
}
