import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, Game, GameRequest } from '../../shared/models/api.model';

@Injectable({ providedIn: 'root' })
export class GameService {
  private http = inject(HttpClient);
  private api = environment.apiUrl;

  getAll(): Observable<Game[]> {
    return this.http
      .get<ApiResponse<Game[]>>(`${this.api}/games`)
      .pipe(map((r) => r.data ?? []));
  }

  getById(id: number): Observable<Game> {
    return this.http
      .get<ApiResponse<Game>>(`${this.api}/games/${id}`)
      .pipe(map((r) => r.data));
  }

  create(req: GameRequest): Observable<Game> {
    return this.http
      .post<ApiResponse<Game>>(`${this.api}/admin/games`, req)
      .pipe(map((r) => r.data));
  }

  update(id: number, req: GameRequest): Observable<Game> {
    return this.http
      .put<ApiResponse<Game>>(`${this.api}/admin/games/${id}`, req)
      .pipe(map((r) => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http
      .delete<ApiResponse<unknown>>(`${this.api}/admin/games/${id}`)
      .pipe(map(() => void 0));
  }
}
