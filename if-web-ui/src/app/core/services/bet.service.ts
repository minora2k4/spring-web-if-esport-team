import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, Bet, BetRequest } from '../../shared/models/api.model';

@Injectable({ providedIn: 'root' })
export class BetService {
  private http = inject(HttpClient);
  private api = environment.apiUrl;

  getAll(): Observable<Bet[]> {
    return this.http
      .get<ApiResponse<Bet[]>>(`${this.api}/bets`)
      .pipe(map((r) => r.data ?? []));
  }

  getPending(): Observable<Bet[]> {
    return this.http
      .get<ApiResponse<Bet[]>>(`${this.api}/bets/pending`)
      .pipe(map((r) => r.data ?? []));
  }

  getById(id: number): Observable<Bet> {
    return this.http
      .get<ApiResponse<Bet>>(`${this.api}/bets/${id}`)
      .pipe(map((r) => r.data));
  }

  create(req: BetRequest): Observable<Bet> {
    return this.http
      .post<ApiResponse<Bet>>(`${this.api}/admin/bets`, req)
      .pipe(map((r) => r.data));
  }

  update(id: number, req: BetRequest): Observable<Bet> {
    return this.http
      .put<ApiResponse<Bet>>(`${this.api}/admin/bets/${id}`, req)
      .pipe(map((r) => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http
      .delete<ApiResponse<unknown>>(`${this.api}/admin/bets/${id}`)
      .pipe(map(() => void 0));
  }
}
