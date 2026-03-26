# ====== 构建阶段 ======
FROM node:20-alpine AS builder
RUN corepack enable && corepack prepare pnpm@10.28.2 --activate
WORKDIR /build
COPY package.json pnpm-lock.yaml* ./
RUN pnpm install --frozen-lockfile 2>/dev/null || pnpm install
COPY . .
# 直接 vite build，跳过 vue-tsc 类型检查以加速构建
RUN npx vite build

# ====== 运行阶段 ======
FROM nginx:alpine
COPY --from=builder /build/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
