import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginResponse } from '../../shared/models/api.model';

const TOKEN_KEY = 'if_token';
const USER_KEY = 'if_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private platformId = inject(PLATFORM_ID);
  private isBrowser = isPlatformBrowser(this.platformId);

  private apiUrl = `${environment.apiUrl}/auth`;
  private loggedIn$ = new BehaviorSubject<boolean>(this.hasToken());

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { username, password }).pipe(
      tap((res) => {
        this.store(TOKEN_KEY, res.token);
        this.store(USER_KEY, res.username);
        this.loggedIn$.next(true);
      })
    );
  }

  logout(): void {
    this.remove(TOKEN_KEY);
    this.remove(USER_KEY);
    this.loggedIn$.next(false);
    this.router.navigate(['/admin/login']);
  }

  getToken(): string | null {
    return this.read(TOKEN_KEY);
  }

  getUsername(): string | null {
    return this.read(USER_KEY);
  }

  hasToken(): boolean {
    return !!this.read(TOKEN_KEY);
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn$.asObservable();
  }

  /* ---- SSR-safe localStorage helpers ---- */
  private store(key: string, value: string): void {
    if (this.isBrowser) localStorage.setItem(key, value);
  }
  private read(key: string): string | null {
    return this.isBrowser ? localStorage.getItem(key) : null;
  }
  private remove(key: string): void {
    if (this.isBrowser) localStorage.removeItem(key);
  }
}
