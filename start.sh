#!/bin/bash
# ==============================================
#  光影美术馆在线导览与预约系统 — 一键启动脚本
#  Usage: bash start.sh
# ==============================================

set -e

# ---- 配置 ----
DB_HOST="localhost"
DB_PORT="3306"
DB_NAME="gallery_tour"
DB_USER="root"
DB_PASS="ab123168"
BACKEND_PORT=8080
ADMIN_PORT=6680
FRONTEND_PORT=5173
PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
LOG_DIR="$PROJECT_DIR/.logs"

# ---- 颜色 ----
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'
BOLD='\033[1m'

banner() {
  echo ""
  echo -e "${YELLOW}╔════════════════════════════════════════════════╗${NC}"
  echo -e "${YELLOW}║${NC}    ${BOLD}光影美术馆在线导览与预约系统${NC}                ${YELLOW}║${NC}"
  echo -e "${YELLOW}╚════════════════════════════════════════════════╝${NC}"
  echo ""
}

check_cmd() {
  if ! command -v "$1" &>/dev/null; then
    echo -e "${RED}✗ 未找到 $1，请先安装${NC}"
    [ -n "$2" ] && echo -e "  安装方式: $2"
    return 1
  fi
  echo -e "${GREEN}✓${NC} $1 已就绪"
  return 0
}

banner

# ---- 环境检查 ----
echo -e "${CYAN}[1/6] 检查基础环境...${NC}"
MISSING=0
check_cmd java "brew install openjdk@17" || MISSING=1
check_cmd mvn "brew install maven" || MISSING=1
check_cmd node "brew install node" || MISSING=1
check_cmd mysql "brew install mysql" || MISSING=1

if ! command -v pnpm &>/dev/null; then
  echo -e "${YELLOW}⚠ pnpm 未安装，正在通过 npm 安装...${NC}"
  npm install -g pnpm || { echo -e "${RED}✗ pnpm 安装失败${NC}"; MISSING=1; }
fi
check_cmd pnpm || MISSING=1

[ $MISSING -eq 1 ] && { echo -e "${RED}请安装缺失工具后重试${NC}"; exit 1; }

# ---- 数据库检查 ----
echo ""
echo -e "${CYAN}[2/6] 检查 MySQL 数据库...${NC}"
if ! mysqladmin ping -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" --silent 2>/dev/null; then
  echo -e "${YELLOW}⚠ MySQL 未运行，尝试启动...${NC}"
  if [[ "$(uname)" == "Darwin" ]]; then
    brew services start mysql 2>/dev/null || true
  else
    sudo systemctl start mysql 2>/dev/null || sudo service mysql start 2>/dev/null || true
  fi
  sleep 3
  if ! mysqladmin ping -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" --silent 2>/dev/null; then
    echo -e "${RED}✗ MySQL 无法启动，请手动启动后重试${NC}"
    exit 1
  fi
fi
echo -e "${GREEN}✓${NC} MySQL 运行中"

# 检查数据库是否存在
DB_EXISTS=$(mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" --default-character-set=utf8mb4 -sse "SELECT COUNT(*) FROM information_schema.SCHEMATA WHERE SCHEMA_NAME='$DB_NAME'" 2>/dev/null || echo "0")
TABLE_COUNT=$(mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" --default-character-set=utf8mb4 -sse "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA='$DB_NAME'" 2>/dev/null || echo "0")

if [ "$DB_EXISTS" = "0" ] || [ "$TABLE_COUNT" = "0" ]; then
  echo -e "${YELLOW}⚠ 数据库不存在或为空，正在导入...${NC}"
  mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" --default-character-set=utf8mb4 < "$PROJECT_DIR/backend/sql/init.sql"
  echo -e "${GREEN}✓${NC} 表结构已导入"
  mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" --default-character-set=utf8mb4 < "$PROJECT_DIR/backend/sql/data.sql"
  echo -e "${GREEN}✓${NC} 测试数据已导入"
else
  echo -e "${GREEN}✓${NC} 数据库 $DB_NAME 已存在（${TABLE_COUNT} 张表）"
fi

# ---- 依赖检查 ----
echo ""
echo -e "${CYAN}[3/6] 检查项目依赖...${NC}"
if [ ! -d "$PROJECT_DIR/backend/target/classes" ]; then
  echo -e "${YELLOW}⚠ 后端未编译，正在编译...${NC}"
  cd "$PROJECT_DIR/backend" && mvn compile -q || { echo -e "${RED}✗ 后端编译失败${NC}"; exit 1; }
  echo -e "${GREEN}✓${NC} 后端编译完成"
else
  echo -e "${GREEN}✓${NC} 后端已编译"
fi

if [ ! -d "$PROJECT_DIR/admin/node_modules" ]; then
  echo -e "${YELLOW}⚠ 管理端依赖未安装，正在安装...${NC}"
  cd "$PROJECT_DIR/admin" && pnpm install || { echo -e "${RED}✗ 管理端依赖安装失败${NC}"; exit 1; }
fi
echo -e "${GREEN}✓${NC} 管理端依赖已就绪"

if [ ! -d "$PROJECT_DIR/frontend/node_modules" ]; then
  echo -e "${YELLOW}⚠ 用户端依赖未安装，正在安装...${NC}"
  cd "$PROJECT_DIR/frontend" && pnpm install || { echo -e "${RED}✗ 用户端依赖安装失败${NC}"; exit 1; }
fi
echo -e "${GREEN}✓${NC} 用户端依赖已就绪"

# ---- 端口检查 ----
echo ""
echo -e "${CYAN}[4/6] 检查端口...${NC}"
check_port() {
  local port=$1 name=$2
  if lsof -iTCP:$port -sTCP:LISTEN &>/dev/null; then
    echo -e "${YELLOW}⚠ 端口 $port ($name) 已被占用${NC}"
    read -p "  是否终止占用进程？[y/N] " ans
    if [[ "$ans" =~ ^[Yy] ]]; then
      lsof -iTCP:$port -sTCP:LISTEN -t | xargs kill -9 2>/dev/null
      echo -e "${GREEN}  已终止${NC}"
    else
      echo -e "${RED}  跳过，服务可能无法启动${NC}"
    fi
  else
    echo -e "${GREEN}✓${NC} 端口 $port ($name) 可用"
  fi
}
check_port $BACKEND_PORT "后端"
check_port $ADMIN_PORT "管理端"
check_port $FRONTEND_PORT "用户端"

# ---- 启动服务 ----
echo ""
echo -e "${CYAN}[5/6] 启动服务...${NC}"
mkdir -p "$LOG_DIR"

# 后端
cd "$PROJECT_DIR/backend"
nohup mvn spring-boot:run -q > "$LOG_DIR/backend.log" 2>&1 &
echo $! > "$LOG_DIR/backend.pid"
echo -e "${GREEN}✓${NC} 后端启动中 (PID: $(cat $LOG_DIR/backend.pid))"

# 管理端
cd "$PROJECT_DIR/admin"
nohup pnpm dev --port $ADMIN_PORT > "$LOG_DIR/admin.log" 2>&1 &
echo $! > "$LOG_DIR/admin.pid"
echo -e "${GREEN}✓${NC} 管理端启动中 (PID: $(cat $LOG_DIR/admin.pid))"

# 用户端
cd "$PROJECT_DIR/frontend"
nohup pnpm dev --port $FRONTEND_PORT > "$LOG_DIR/frontend.log" 2>&1 &
echo $! > "$LOG_DIR/frontend.pid"
echo -e "${GREEN}✓${NC} 用户端启动中 (PID: $(cat $LOG_DIR/frontend.pid))"

# ---- 等待就绪 ----
echo ""
echo -e "${CYAN}[6/6] 等待服务就绪...${NC}"
wait_port() {
  local port=$1 name=$2 max=60 count=0
  while ! lsof -iTCP:$port -sTCP:LISTEN &>/dev/null; do
    count=$((count+1))
    if [ $count -ge $max ]; then
      echo -e "${RED}✗ $name 启动超时${NC}"
      return 1
    fi
    sleep 1
  done
  echo -e "${GREEN}✓${NC} $name 已就绪"
}
wait_port $BACKEND_PORT "后端"
wait_port $ADMIN_PORT "管理端"
wait_port $FRONTEND_PORT "用户端"

# ---- 打印信息 ----
echo ""
echo -e "${YELLOW}╔════════════════════════════════════════════════╗${NC}"
echo -e "${YELLOW}║${NC}  ${BOLD}🎉 全部服务已启动！${NC}                          ${YELLOW}║${NC}"
echo -e "${YELLOW}╠════════════════════════════════════════════════╣${NC}"
echo -e "${YELLOW}║${NC}  ${CYAN}用户端${NC}    http://localhost:${FRONTEND_PORT}           ${YELLOW}║${NC}"
echo -e "${YELLOW}║${NC}  ${CYAN}管理端${NC}    http://localhost:${ADMIN_PORT}             ${YELLOW}║${NC}"
echo -e "${YELLOW}║${NC}  ${CYAN}后端API${NC}   http://localhost:${BACKEND_PORT}             ${YELLOW}║${NC}"
echo -e "${YELLOW}╠════════════════════════════════════════════════╣${NC}"
echo -e "${YELLOW}║${NC}  ${BOLD}测试账号${NC}                                      ${YELLOW}║${NC}"
echo -e "${YELLOW}║${NC}  管理员: admin / admin123                      ${YELLOW}║${NC}"
echo -e "${YELLOW}║${NC}  用  户: zhangwei / 123456                    ${YELLOW}║${NC}"
echo -e "${YELLOW}╚════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${BLUE}按 Ctrl+C 可停止所有服务，或运行 bash stop.sh${NC}"
echo ""

# ---- 实时日志 ----
trap 'echo -e "\n${YELLOW}正在停止服务...${NC}"; bash "$PROJECT_DIR/stop.sh" 2>/dev/null; exit 0' INT TERM

tail -f \
  "$LOG_DIR/backend.log" \
  "$LOG_DIR/admin.log" \
  "$LOG_DIR/frontend.log" 2>/dev/null | \
  sed -u \
    -e "s/^==> .*backend.log <==/[${RED}后端${NC}]/" \
    -e "s/^==> .*admin.log <==/[${GREEN}管理端${NC}]/" \
    -e "s/^==> .*frontend.log <==/[${BLUE}用户端${NC}]/"
