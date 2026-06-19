import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, Member, MemberRequest } from '../../shared/models/api.model';

@Injectable({ providedIn: 'root' })
export class MemberService {
  private http = inject(HttpClient);
  private api = environment.apiUrl;

  /** Active members only (public homepage / roster). */
  getActive(): Observable<Member[]> {
    return this.http
      .get<ApiResponse<Member[]>>(`${this.api}/members`)
      .pipe(map((r) => r.data ?? []));
  }

  /** All members including inactive (admin). */
  getAll(): Observable<Member[]> {
    return this.http
      .get<ApiResponse<Member[]>>(`${this.api}/members/all`)
      .pipe(map((r) => r.data ?? []));
  }

  getById(id: number): Observable<Member> {
    return this.http
      .get<ApiResponse<Member>>(`${this.api}/members/${id}`)
      .pipe(map((r) => r.data));
  }

  create(req: MemberRequest): Observable<Member> {
    return this.http
      .post<ApiResponse<Member>>(`${this.api}/admin/members`, req)
      .pipe(map((r) => r.data));
  }

  update(id: number, req: MemberRequest): Observable<Member> {
    return this.http
      .put<ApiResponse<Member>>(`${this.api}/admin/members/${id}`, req)
      .pipe(map((r) => r.data));
  }

  delete(id: number): Observable<void> {
    return this.http
      .delete<ApiResponse<unknown>>(`${this.api}/admin/members/${id}`)
      .pipe(map(() => void 0));
  }
}
