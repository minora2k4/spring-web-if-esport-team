// Bản build production (Docker): gọi API cùng origin qua reverse proxy nginx.
// Trình duyệt gọi "/api/..." -> nginx chuyển tiếp sang backend (không dính CORS).
// Khi render phía server (SSR), apiBaseUrlInterceptor sẽ tự thêm host nội bộ.
export const environment = {
  production: true,
  apiUrl: '/api',
};
