# ===================== Stage 1: build =====================
FROM node:22-alpine AS build
WORKDIR /app

# Cache lớp node_modules: copy package*.json trước rồi mới copy source.
COPY package*.json ./
RUN npm ci

COPY . .
# Build production (kèm SSR). angular.json đã đặt defaultConfiguration=production
# nên environment.ts sẽ được thay bằng environment.production.ts (apiUrl="/api").
RUN npm run build

# ===================== Stage 2: runtime =====================
FROM node:22-alpine AS runtime
WORKDIR /app

ENV NODE_ENV=production
# Cổng SSR server lắng nghe (server.ts đọc process.env.PORT).
ENV PORT=4000

# Chỉ cần kết quả build (đã gồm server SSR + static browser).
COPY --from=build /app/dist ./dist

EXPOSE 4000
CMD ["node", "dist/if-web-ui/server/server.mjs"]
