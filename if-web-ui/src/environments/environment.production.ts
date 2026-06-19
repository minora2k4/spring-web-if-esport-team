// Bản build production trên Render: frontend và backend là 2 domain riêng (không có
// nginx gộp origin), nên trình duyệt phải gọi thẳng domain backend.
// Khi render phía server (SSR), apiBaseUrlInterceptor đọc env API_INTERNAL_URL nên
// KHÔNG dùng giá trị này — chỉ trình duyệt (client) mới dùng apiUrl bên dưới.
// Lưu ý: giá trị này được inline lúc build, đổi tên service backend thì phải sửa ở đây.
export const environment = {
  production: true,
  apiUrl: 'https://if-team-api.onrender.com/api',
};
