@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
title 光影美术馆在线导览与预约系统

echo.
echo ╔════════════════════════════════════════════════╗
echo ║    光影美术馆在线导览与预约系统                ║
echo ╚════════════════════════════════════════════════╝
echo.

:: ---- 检查 Docker ----
where docker >nul 2>&1 || (
  echo [✗] 未找到 Docker，请先安装 Docker Desktop
  goto :error
)
echo [✓] Docker 已就绪

where docker-compose >nul 2>&1
if errorlevel 1 (
  docker compose version >nul 2>&1
  if errorlevel 1 (
    echo [✗] 未找到 docker-compose，请先安装
    goto :error
  )
  set COMPOSE_CMD=docker compose
) else (
  set COMPOSE_CMD=docker-compose
)
echo [✓] Docker Compose 已就绪

:: ---- 启动 Docker 容器 ----
echo.
echo [启动] 正在启动 Docker 容器...
%COMPOSE_CMD% up -d
if errorlevel 1 (
  echo [✗] Docker 容器启动失败
  goto :error
)
echo [✓] Docker 容器已启动

:: ---- 打印访问信息 ----
echo.
echo ╔════════════════════════════════════════════════════╗
echo ║  🎉 系统已启动！                                  ║
echo ╠════════════════════════════════════════════════════╣
echo ║                                                    ║
echo ║  📍 访问地址                                       ║
echo ║  用户端    http://localhost:5173                    ║
echo ║  管理端    http://localhost:6680                    ║
echo ║  后端API   http://localhost:8080                    ║
echo ║                                                    ║
echo ╠════════════════════════════════════════════════════╣
echo ║                                                    ║
echo ║  🔑 测试账号                                       ║
echo ║  ┌──────────┬──────────┬──────────┬────────────┐   ║
echo ║  │ 角色     │ 用户名   │ 密码     │ 姓名       │   ║
echo ║  ├──────────┼──────────┼──────────┼────────────┤   ║
echo ║  │ 管理员   │ admin    │ admin123 │ 系统管理员 │   ║
echo ║  ├──────────┼──────────┼──────────┼────────────┤   ║
echo ║  │ 普通用户 │ zhangwei │ 123456   │ 张伟       │   ║
echo ║  ├──────────┼──────────┼──────────┼────────────┤   ║
echo ║  │ 普通用户 │ lina     │ 123456   │ 李娜       │   ║
echo ║  ├──────────┼──────────┼──────────┼────────────┤   ║
echo ║  │ 普通用户 │ wangfang │ 123456   │ 王芳       │   ║
echo ║  ├──────────┼──────────┼──────────┼────────────┤   ║
echo ║  │ 普通用户 │ liuyang  │ 123456   │ 刘洋       │   ║
echo ║  └──────────┴──────────┴──────────┴────────────┘   ║
echo ║                                                    ║
echo ╚════════════════════════════════════════════════════╝
echo.
echo 提示: 运行 stop.bat 可停止全部容器
pause
exit /b 0

:error
echo.
echo [✗] 启动失败
pause
exit /b 1
