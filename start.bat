@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
title 光影美术馆在线导览与预约系统

:: ---- 配置 ----
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=gallery_tour
set DB_USER=root
set DB_PASS=ab123168
set BACKEND_PORT=8080
set ADMIN_PORT=6680
set FRONTEND_PORT=5173
set PROJECT_DIR=%~dp0

echo.
echo ╔════════════════════════════════════════════════╗
echo ║    光影美术馆在线导览与预约系统                ║
echo ╚════════════════════════════════════════════════╝
echo.

:: ---- 环境检查 ----
echo [1/6] 检查基础环境...
where java >nul 2>&1 || (echo [✗] 未找到 java && goto :error)
echo [✓] java 已就绪
where mvn >nul 2>&1 || (echo [✗] 未找到 mvn && goto :error)
echo [✓] mvn 已就绪
where node >nul 2>&1 || (echo [✗] 未找到 node && goto :error)
echo [✓] node 已就绪
where pnpm >nul 2>&1 || (
  echo [!] pnpm 未安装，正在安装...
  npm install -g pnpm
)
where pnpm >nul 2>&1 || (echo [✗] pnpm 安装失败 && goto :error)
echo [✓] pnpm 已就绪

:: ---- 数据库检查 ----
echo.
echo [2/6] 检查 MySQL 数据库...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% --default-character-set=utf8mb4 -e "SELECT 1" >nul 2>&1
if errorlevel 1 (
  echo [!] MySQL 未运行，尝试启动...
  net start MySQL >nul 2>&1
  timeout /t 3 /nobreak >nul
  mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% --default-character-set=utf8mb4 -e "SELECT 1" >nul 2>&1
  if errorlevel 1 (
    echo [✗] MySQL 无法启动
    goto :error
  )
)
echo [✓] MySQL 运行中

:: 检查数据库
for /f %%i in ('mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% --default-character-set=utf8mb4 -sse "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA='%DB_NAME%'" 2^>nul') do set TABLE_COUNT=%%i
if "%TABLE_COUNT%"=="" set TABLE_COUNT=0
if %TABLE_COUNT% EQU 0 (
  echo [!] 数据库为空, 正在导入...
  mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% --default-character-set=utf8mb4 < "%PROJECT_DIR%backend\sql\init.sql"
  mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% --default-character-set=utf8mb4 < "%PROJECT_DIR%backend\sql\data.sql"
  echo [✓] 数据已导入
) else (
  echo [✓] 数据库已存在
)

:: ---- 依赖检查 ----
echo.
echo [3/6] 检查项目依赖...
if not exist "%PROJECT_DIR%backend\target\classes" (
  echo [!] 后端未编译，正在编译...
  cd /d "%PROJECT_DIR%backend" && mvn compile -q
  if errorlevel 1 (echo [✗] 编译失败 && goto :error)
)
echo [✓] 后端已编译

if not exist "%PROJECT_DIR%admin\node_modules" (
  echo [!] 管理端依赖未安装...
  cd /d "%PROJECT_DIR%admin" && pnpm install
)
echo [✓] 管理端依赖就绪

if not exist "%PROJECT_DIR%frontend\node_modules" (
  echo [!] 用户端依赖未安装...
  cd /d "%PROJECT_DIR%frontend" && pnpm install
)
echo [✓] 用户端依赖就绪

:: ---- 端口检查 ----
echo.
echo [4/6] 检查端口...
for %%p in (%BACKEND_PORT% %ADMIN_PORT% %FRONTEND_PORT%) do (
  netstat -aon | findstr ":%%p " | findstr "LISTENING" >nul 2>&1
  if not errorlevel 1 (
    echo [!] 端口 %%p 被占用
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":%%p " ^| findstr "LISTENING"') do (
      taskkill /PID %%a /F >nul 2>&1
    )
    echo [✓] 已释放端口 %%p
  ) else (
    echo [✓] 端口 %%p 可用
  )
)

:: ---- 启动服务 ----
echo.
echo [5/6] 启动服务...

:: 后端 (红色窗口)
start "后端-Spring Boot" /min cmd /c "color 4F && cd /d "%PROJECT_DIR%backend" && mvn spring-boot:run"

:: 管理端 (绿色窗口)
start "管理端-Admin" /min cmd /c "color 2F && cd /d "%PROJECT_DIR%admin" && pnpm dev --port %ADMIN_PORT%"

:: 用户端 (蓝色窗口)
start "用户端-Frontend" /min cmd /c "color 1F && cd /d "%PROJECT_DIR%frontend" && pnpm dev --port %FRONTEND_PORT%"

echo [✓] 服务窗口已启动

:: ---- 等待就绪 ----
echo.
echo [6/6] 等待服务就绪...
:wait_backend
timeout /t 2 /nobreak >nul
netstat -aon | findstr ":%BACKEND_PORT% " | findstr "LISTENING" >nul 2>&1
if errorlevel 1 goto :wait_backend
echo [✓] 后端已就绪

:wait_admin
netstat -aon | findstr ":%ADMIN_PORT% " | findstr "LISTENING" >nul 2>&1
if errorlevel 1 (timeout /t 1 /nobreak >nul && goto :wait_admin)
echo [✓] 管理端已就绪

:wait_frontend
netstat -aon | findstr ":%FRONTEND_PORT% " | findstr "LISTENING" >nul 2>&1
if errorlevel 1 (timeout /t 1 /nobreak >nul && goto :wait_frontend)
echo [✓] 用户端已就绪

:: ---- 打印信息 ----
echo.
echo ╔════════════════════════════════════════════════╗
echo ║  🎉 全部服务已启动！                           ║
echo ╠════════════════════════════════════════════════╣
echo ║  用户端    http://localhost:%FRONTEND_PORT%           ║
echo ║  管理端    http://localhost:%ADMIN_PORT%             ║
echo ║  后端API   http://localhost:%BACKEND_PORT%             ║
echo ╠════════════════════════════════════════════════╣
echo ║  测试账号                                      ║
echo ║  管理员: admin / admin123                      ║
echo ║  用  户: zhangwei / 123456                    ║
echo ╚════════════════════════════════════════════════╝
echo.
echo 关闭本窗口不会停止服务，运行 stop.bat 可停止全部
pause
exit /b 0

:error
echo.
echo [✗] 启动失败
pause
exit /b 1
