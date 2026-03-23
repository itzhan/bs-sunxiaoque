@echo off
chcp 65001 >nul
title 停止光影美术馆

echo 正在停止所有服务...

:: 终止服务窗口
taskkill /FI "WINDOWTITLE eq 后端-Spring Boot*" /F >nul 2>&1
taskkill /FI "WINDOWTITLE eq 管理端-Admin*" /F >nul 2>&1
taskkill /FI "WINDOWTITLE eq 用户端-Frontend*" /F >nul 2>&1

:: 通过端口释放
for %%p in (8080 6680 5173) do (
  for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":%%p " ^| findstr "LISTENING" 2^>nul') do (
    taskkill /PID %%a /F >nul 2>&1
  )
)

echo [✓] 全部服务已停止
pause
