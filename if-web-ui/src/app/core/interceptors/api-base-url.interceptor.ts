import { HttpInterceptorFn } from '@angular/common/http';
import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformServer } from '@angular/common';

/**
 * Khi render phía server (SSR), HttpClient KHÔNG dùng được URL tương đối ("/api/...")
 * vì không có origin. Interceptor này chỉ chạy trên server: nó thêm host nội bộ của
 * backend (đọc từ biến môi trường API_INTERNAL_URL, mặc định http://localhost:8081)
 * vào đầu mọi request bắt đầu bằng "/". Trên trình duyệt thì giữ nguyên URL tương đối.
 */
export const apiBaseUrlInterceptor: HttpInterceptorFn = (req, next) => {
  const isServer = isPlatformServer(inject(PLATFORM_ID));

  if (isServer && req.url.startsWith('/')) {
    const env = (globalThis as { process?: { env?: Record<string, string> } }).process?.env;
    const base = env?.['API_INTERNAL_URL'] ?? 'http://localhost:8081';
    req = req.clone({ url: base.replace(/\/$/, '') + req.url });
  }

  return next(req);
};
