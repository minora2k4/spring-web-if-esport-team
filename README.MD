# 🎮 Infinity Team — Website Esports

Website chính thức của đội tuyển Esports **Infinity Team (IF Team)**.

Dự án gồm **2 phần độc lập**:

| Thành phần | Công nghệ | Cổng mặc định | Thư mục |
|-----------|-----------|---------------|---------|
| **Backend** (REST API) | Spring Boot 3.5 · Java 25 · Spring Security + JWT · JPA/Hibernate · PostgreSQL | `8081` | `src/` |
| **Frontend** (giao diện) | Angular 19 (SSR) · TypeScript · SCSS | `4200` | `if-web-ui/` |

> Backend và frontend chạy thành **2 tiến trình riêng**. Khi phát triển, bạn cần khởi động cả hai.

---

## 📋 Mục lục

1. [Yêu cầu môi trường](#-yêu-cầu-môi-trường)
2. [Cài đặt nhanh](#-cài-đặt-nhanh-tldr)
3. [Bước 1 — Chuẩn bị cơ sở dữ liệu](#-bước-1--chuẩn-bị-cơ-sở-dữ-liệu-postgresql)
4. [Bước 2 — Chạy Backend](#-bước-2--chạy-backend-spring-boot)
5. [Bước 3 — Chạy Frontend](#-bước-3--chạy-frontend-angular)
6. [Đăng nhập quản trị](#-đăng-nhập-quản-trị)
7. [Tính năng](#-tính-năng)
8. [Danh sách API](#-danh-sách-api)
9. [Cấu trúc dự án](#-cấu-trúc-dự-án)
10. [Build production](#-build-production)
11. [Xử lý sự cố](#-xử-lý-sự-cố-thường-gặp)

---

## Yêu cầu môi trường

Hãy cài đặt sẵn các phần mềm sau:

- **Java JDK 25** (`java -version` phải hiển thị `25`)
- **Node.js 18+** và **npm** (`node -v`, `npm -v`)
- **PostgreSQL 14+** đang chạy ở `localhost:5432`
- **Maven**: dự án đã kèm sẵn Maven Wrapper (`mvnw`) nên **không cần** cài Maven riêng.

---

## Cài đặt nhanh (TL;DR)

```bash
# 1. Tạo database "ifweb" trong PostgreSQL (xem chi tiết bên dưới)

# 2. Chạy backend (tại thư mục gốc dự án)
./mvnw spring-boot:run          # Windows: mvnw.cmd spring-boot:run

# 3. Chạy frontend (mở terminal mới)
cd if-web-ui
npm install
npm start
```

Sau đó mở trình duyệt: **http://localhost:4200**

---

## Bước 1 — Chuẩn bị cơ sở dữ liệu (PostgreSQL)

Backend kết nối tới một database tên **`ifweb`**. Tạo database này trước khi chạy:

```sql
-- Đăng nhập psql với user postgres rồi chạy:
CREATE DATABASE ifweb;
```

Thông tin kết nối mặc định nằm trong [`src/main/resources/application.properties`](src/main/resources/application.properties):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ifweb
spring.datasource.username=postgres
spring.datasource.password=postgres
```

> 💡 Nếu user/mật khẩu PostgreSQL của bạn khác, hãy sửa lại 2 dòng `username` / `password` cho khớp.

Bạn **không cần tạo bảng thủ công**: cấu hình `spring.jpa.hibernate.ddl-auto=update` sẽ tự động
tạo/cập nhật toàn bộ bảng dựa trên các entity khi backend khởi động.

---

## Bước 2 — Chạy Backend (Spring Boot)

Tại **thư mục gốc** của dự án:

```bash
# macOS / Linux
./mvnw spring-boot:run

# Windows (PowerShell / CMD)
mvnw.cmd spring-boot:run
```

Khi khởi động thành công bạn sẽ thấy log dạng:

```
================================================
       IF TEAM WEB — APPLICATION STARTED
================================================
  Port    : 8081
  DB      : jdbc:postgresql://localhost:5432/ifweb
================================================
[DB] >>> Connected — PostgreSQL ...
[INIT] Default admin created — username: admin / password: admin123
```

API sẽ chạy tại **http://localhost:8081/api**.

Lần đầu chạy, hệ thống tự tạo sẵn **một tài khoản admin mặc định** (xem phần dưới).

Các lệnh Maven hữu ích khác:

```bash
./mvnw test                     # chạy toàn bộ test
./mvnw test -Dtest=TênLớpTest   # chạy 1 lớp test
./mvnw clean package            # đóng gói file .jar
```

---

## Bước 3 — Chạy Frontend (Angular)

Mở **một terminal mới**, vào thư mục `if-web-ui`:

```bash
cd if-web-ui
npm install      # chỉ cần chạy lần đầu để cài thư viện
npm start        # khởi động dev server (ng serve)
```

Truy cập **http://localhost:4200**.

Frontend gọi API qua biến cấu hình trong [`if-web-ui/src/environments/environment.ts`](if-web-ui/src/environments/environment.ts):

```ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8081/api'
};
```

> Nếu đổi cổng backend, hãy cập nhật `apiUrl` ở đây, đồng thời cập nhật
> `app.cors.allowed-origins` trong `application.properties` cho khớp cổng frontend.

Các script npm khác (chạy trong `if-web-ui/`):

```bash
npm run build                 # build production (kèm SSR)
npm test                      # chạy unit test (Karma/Jasmine)
npm run serve:ssr:if-web-ui   # chạy bản build SSR đã đóng gói
```

---

## Đăng nhập quản trị

Tài khoản admin mặc định được tạo tự động ở lần chạy backend đầu tiên:

| Tài khoản | Mật khẩu |
|-----------|----------|
| `admin`   | `admin123` |

Cách đăng nhập:

1. Vào **http://localhost:4200/admin/login** (hoặc bấm **"Đăng nhập"** trên thanh điều hướng).
2. Nhập `admin` / `admin123`.
3. Sau khi đăng nhập, bạn vào **Bảng điều khiển** (`/admin/dashboard`) để quản lý nội dung.

> ⚠️ **Bảo mật:** Hãy đổi mật khẩu mặc định và `app.jwt.secret` trong `application.properties`
> trước khi triển khai thật. Token JWT được lưu ở `localStorage` của trình duyệt và tự động
> đính kèm vào mọi request quản trị.

---

## Tính năng

### Trang công khai (ai cũng xem được)
- **Trang chủ** — hero giới thiệu đội, thống kê nhanh (số thành viên, game, giải đấu, trận thắng), đội hình & giải đấu nổi bật.
- **Thành viên** — danh sách tuyển thủ đang hoạt động kèm vai trò, thành tích và các tựa game.
- **Tựa game** — các bộ môn đội đang thi đấu.
- **Giải đấu** — dòng thời gian (timeline) các giải đã/đang tham dự kèm thành tích.
- **Kèo đấu** — bảng lịch sử kèo, kèm thống kê Thắng / Thua / Đang chờ.
- **Offteam** — album ảnh các buổi gặp mặt, có xem ảnh phóng to (lightbox).
- **Nhật ký đội** — timeline các cột mốc (gia nhập / rời đội / thành tích).

### Trang quản trị (cần đăng nhập)
- **Bảng điều khiển** tổng quan với số liệu từng mục.
- **CRUD đầy đủ** (Thêm / Sửa / Xóa) cho: Thành viên, Tựa game, Giải đấu, Kèo đấu, Offteam, Nhật ký đội.
- Form nhập liệu dạng modal, có kiểm tra dữ liệu và thông báo lỗi tiếng Việt.
- **Tải ảnh trực tiếp từ thiết bị** (máy tính/điện thoại) cho ảnh đại diện thành viên,
  logo game, ảnh bìa và album offteam — không cần dán link. Ảnh được lưu trong thư mục
  `uploads/` ở backend và phục vụ qua đường dẫn `/uploads/...`.

### Giao diện
- Thiết kế **hiện đại**, tông màu **đỏ–đen** trên nền trắng, bo góc mềm, đổ bóng nhẹ.
- **Đa nền tảng / Responsive** đầy đủ cho desktop, tablet và điện thoại (menu thu gọn,
  bảng cuộn ngang, popup vừa khít màn hình).

---

## Danh sách API

Tất cả endpoint có tiền tố `/api`. Endpoint công khai dùng `GET`; thao tác ghi nằm dưới
`/api/admin/**` và **yêu cầu JWT** (vai trò `ADMIN`).

### Xác thực
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| `POST` | `/api/auth/login` | Đăng nhập, trả về `{ token, username, role }` |

### Công khai (đọc)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| `GET` | `/api/members` | Thành viên đang hoạt động |
| `GET` | `/api/members/all` | Tất cả thành viên |
| `GET` | `/api/members/{id}` | Chi tiết thành viên |
| `GET` | `/api/games` | Danh sách tựa game |
| `GET` | `/api/tournaments` | Danh sách giải đấu |
| `GET` | `/api/bets` | Danh sách kèo đấu |
| `GET` | `/api/bets/pending` | Các kèo đang chờ |
| `GET` | `/api/offteam` | Danh sách sự kiện offteam |
| `GET` | `/api/team-log` | Nhật ký đội |

### Quản trị (cần JWT — `Authorization: Bearer <token>`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| `POST` / `PUT` / `DELETE` | `/api/admin/members[/{id}]` | Quản lý thành viên |
| `POST` / `PUT` / `DELETE` | `/api/admin/games[/{id}]` | Quản lý tựa game |
| `POST` / `PUT` / `DELETE` | `/api/admin/tournaments[/{id}]` | Quản lý giải đấu |
| `POST` / `PUT` / `DELETE` | `/api/admin/bets[/{id}]` | Quản lý kèo đấu |
| `POST` / `PUT` / `DELETE` | `/api/admin/offteam[/{id}]` | Quản lý offteam |
| `POST` / `PUT` / `DELETE` | `/api/admin/team-log[/{id}]` | Quản lý nhật ký |

Mọi phản hồi (trừ login) được bọc trong cấu trúc chung:

```json
{ "success": true, "message": "Thành công", "data": { } }
```

---

## 📁 Cấu trúc dự án

```
if-web/
├─ src/main/java/web/            # Backend (Spring Boot)
│  ├─ controller/                # REST controllers (Auth, Member, Game, ...)
│  ├─ service/                   # Business logic
│  ├─ repository/                # Spring Data JPA
│  ├─ model/                     # JPA entities (bảng dữ liệu)
│  ├─ dto/request | dto/response # DTO vào/ra
│  ├─ security/                  # JWT filter, JwtUtil, UserDetails
│  ├─ config/                    # SecurityConfig, CORS, khởi tạo admin, xử lý lỗi
│  └─ monitor/                   # Log khởi động / request / DB
├─ src/main/resources/
│  └─ application.properties     # Cấu hình DB, JWT, CORS, cổng
│
└─ if-web-ui/                    # Frontend (Angular)
   └─ src/app/
      ├─ core/                   # services (gọi API), guard, interceptor JWT
      ├─ features/               # các trang công khai + features/admin/* (quản trị)
      ├─ shared/                 # navbar, footer, models (TypeScript interfaces)
      ├─ app.routes.ts           # định tuyến (lazy-load + bảo vệ route admin)
      └─ ...
```

---

## Build production

**Backend** — tạo file `.jar`:
```bash
./mvnw clean package
java -jar target/if-web-0.0.1-SNAPSHOT.jar
```

**Frontend** — build bản tối ưu (kèm SSR):
```bash
cd if-web-ui
npm run build
npm run serve:ssr:if-web-ui     # chạy server SSR từ thư mục dist/
```

### Triển khai bằng Docker (khuyến nghị)

Muốn chạy cả hệ thống (DB + backend + frontend) chỉ bằng một lệnh, không cần cài Java/Node/PostgreSQL:

```bash
cp .env.example .env            # đổi mật khẩu & JWT secret
docker compose up -d --build    # mở http://localhost
```
---
