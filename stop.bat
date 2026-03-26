@echo off
chcp 65001 >nul
title 停止光影美术馆

echo 正在停止 Docker 容器...

where docker-compose >nul 2>&1
if errorlevel 1 (
  docker compose down
) else (
  docker-compose down
)

echo [✓] 全部容器已停止
pause
