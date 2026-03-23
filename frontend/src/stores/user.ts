import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, register as registerApi, getUserInfo } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref<any>(JSON.parse(localStorage.getItem('userInfo') || 'null'))

  const isLoggedIn = computed(() => !!token.value)
  const nickname = computed(() => userInfo.value?.nickname || '')
  const username = computed(() => userInfo.value?.username || '')
  const userId = computed(() => userInfo.value?.id || userInfo.value?.userId || null)

  function setToken(t: string) {
    token.value = t
    localStorage.setItem('token', t)
  }

  function setUserInfo(info: any) {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  async function login(data: { username: string; password: string }) {
    const res = await loginApi(data)
    setToken(res.data.token)
    setUserInfo(res.data)
    return res.data
  }

  async function register(data: any) {
    const res = await registerApi(data)
    setToken(res.data.token)
    setUserInfo(res.data)
    return res.data
  }

  async function refreshInfo() {
    try {
      const res = await getUserInfo()
      setUserInfo(res.data)
    } catch {
      // ignore
    }
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return { token, userInfo, isLoggedIn, nickname, username, userId, login, register, refreshInfo, logout }
})
