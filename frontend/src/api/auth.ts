import request from '@/utils/request'

// 登录
export function login(data: { username: string; password: string }) {
  return request.post('/auth/login', data)
}

// 注册
export function register(data: { username: string; password: string; nickname: string; phone?: string; email?: string }) {
  return request.post('/auth/register', data)
}

// 获取当前用户信息
export function getUserInfo() {
  return request.get('/auth/info')
}
