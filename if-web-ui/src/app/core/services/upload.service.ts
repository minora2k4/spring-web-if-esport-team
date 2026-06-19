import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../shared/models/api.model';

@Injectable({ providedIn: 'root' })
export class UploadService {
  private http = inject(HttpClient);
  private api = environment.apiUrl;

  /** Tải 1 file ảnh từ thiết bị lên server, trả về URL công khai. */
  upload(file: File): Observable<string> {
    const form = new FormData();
    form.append('file', file);
    // Không tự set Content-Type để trình duyệt tự thêm boundary multipart.
    return this.http
      .post<ApiResponse<string>>(`${this.api}/admin/upload`, form)
      .pipe(map((r) => r.data));
  }
}
