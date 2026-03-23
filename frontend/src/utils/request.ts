import axios from 'axios'
import { useUserStore } from '@/stores/user'
import { useMessage } from 'naive-ui'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000
})

// 请求拦截器：自动附加 Token
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：解包 { code, data, message }
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res // 返回完整的 { code, data, message }
    }
    // 业务错误
    const msg = res.message || '请求失败'
    // 使用 window 自定义事件通知
    window.dispatchEvent(new CustomEvent('app-error', { detail: msg }))
    return Promise.reject(new Error(msg))
  },
  error => {
    if (error.response) {
      const status = error.response.status
      if (status === 401) {
        // Token 过期或无效，清除登录状态
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        window.location.href = '/login'
        return Promise.reject(new Error('登录已过期，请重新登录'))
      }
      if (status === 403) {
        window.dispatchEvent(new CustomEvent('app-error', { detail: '没有权限执行此操作' }))
        return Promise.reject(new Error('没有权限'))
      }
    }
    const msg = error.response?.data?.message || error.message || '网络错误'
    window.dispatchEvent(new CustomEvent('app-error', { detail: msg }))
    return Promise.reject(error)
  }
)

export default request
