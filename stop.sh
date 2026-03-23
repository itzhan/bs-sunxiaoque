#!/bin/bash
# ==============================================
#  光影美术馆在线导览与预约系统 — 一键停止脚本
# ==============================================

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
LOG_DIR="$PROJECT_DIR/.logs"
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}正在停止所有服务...${NC}"

# 通过 PID 文件停止
for svc in backend admin frontend; do
  PID_FILE="$LOG_DIR/${svc}.pid"
  if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if kill -0 "$PID" 2>/dev/null; then
      kill "$PID" 2>/dev/null
      # 等待进程结束
      for i in $(seq 1 10); do
        kill -0 "$PID" 2>/dev/null || break
        sleep 0.5
      done
      # 强制终止
      kill -9 "$PID" 2>/dev/null
      echo -e "${GREEN}✓${NC} $svc 已停止 (PID: $PID)"
    fi
    rm -f "$PID_FILE"
  fi
done

# 通过端口号双重清理
for port in 8080 6680 5173; do
  PIDS=$(lsof -iTCP:$port -sTCP:LISTEN -t 2>/dev/null)
  if [ -n "$PIDS" ]; then
    echo "$PIDS" | xargs kill -9 2>/dev/null
    echo -e "${GREEN}✓${NC} 端口 $port 已释放"
  fi
done

# 清理 tail 监控进程
pkill -f "tail -f.*\.logs/" 2>/dev/null

echo -e "${GREEN}全部服务已停止${NC}"
