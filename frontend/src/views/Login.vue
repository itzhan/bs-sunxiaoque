<template>
  <div class="login-page">
    <div class="login-bg"></div>
    <div class="login-card">
      <div class="login-header">
        <span class="logo-icon"><Diamond :size="28" /></span>
        <h1 class="font-display">光影美术馆</h1>
        <p>{{ isRegister ? '创建您的账号' : '欢迎回来' }}</p>
      </div>

      <n-tabs v-model:value="activeTab" type="segment" animated>
        <n-tab-pane name="login" tab="登录">
          <n-form :model="loginForm" style="margin-top:24px;">
            <n-form-item label="用户名">
              <n-input v-model:value="loginForm.username" placeholder="请输入用户名" size="large" />
            </n-form-item>
            <n-form-item label="密码">
              <n-input v-model:value="loginForm.password" type="password" placeholder="请输入密码" size="large" show-password-on="mousedown" @keyup.enter="handleLogin" />
            </n-form-item>
            <n-button type="primary" size="large" block :loading="loading" @click="handleLogin" style="margin-top:8px;">
              登录
            </n-button>
          </n-form>
        </n-tab-pane>

        <n-tab-pane name="register" tab="注册">
          <n-form :model="registerForm" style="margin-top:24px;">
            <n-form-item label="用户名">
              <n-input v-model:value="registerForm.username" placeholder="请输入用户名" size="large" />
            </n-form-item>
            <n-form-item label="昵称">
              <n-input v-model:value="registerForm.nickname" placeholder="请输入昵称" size="large" />
            </n-form-item>
            <n-form-item label="密码">
              <n-input v-model:value="registerForm.password" type="password" placeholder="请输入密码" size="large" show-password-on="mousedown" />
            </n-form-item>
            <n-form-item label="手机号">
              <n-input v-model:value="registerForm.phone" placeholder="可选" size="large" />
            </n-form-item>
            <n-form-item label="邮箱">
              <n-input v-model:value="registerForm.email" placeholder="可选" size="large" />
            </n-form-item>
            <n-button type="primary" size="large" block :loading="loading" @click="handleRegister" style="margin-top:8px;">
              注册
            </n-button>
          </n-form>
        </n-tab-pane>
      </n-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { Diamond } from 'lucide-vue-next'
import { useRouter, useRoute } from 'vue-router'
import { useMessage } from 'naive-ui'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const message = useMessage()
const userStore = useUserStore()

const activeTab = ref('login')
const isRegister = computed(() => activeTab.value === 'register')
const loading = ref(false)

const loginForm = reactive({ username: '', password: '' })
const registerForm = reactive({ username: '', password: '', nickname: '', phone: '', email: '' })

async function handleLogin() {
  if (!loginForm.username || !loginForm.password) { message.warning('请输入用户名和密码'); return }
  loading.value = true
  try {
    await userStore.login(loginForm)
    message.success('登录成功')
    const redirect = (route.query.redirect as string) || '/'
    router.push(redirect)
  } catch (err: any) {
    message.error(err.message || '登录失败')
  } finally { loading.value = false }
}

async function handleRegister() {
  if (!registerForm.username || !registerForm.password || !registerForm.nickname) {
    message.warning('请填写用户名、昵称和密码'); return
  }
  loading.value = true
  try {
    await userStore.register(registerForm)
    message.success('注册成功')
    const redirect = (route.query.redirect as string) || '/'
    router.push(redirect)
  } catch (err: any) {
    message.error(err.message || '注册失败')
  } finally { loading.value = false }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
.login-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse at 30% 30%, rgba(199,169,81,0.08) 0%, transparent 50%),
    radial-gradient(ellipse at 70% 70%, rgba(27,40,56,0.06) 0%, transparent 50%),
    linear-gradient(135deg, #F5F4F0 0%, var(--color-bg-page) 100%);
}
.login-card {
  position: relative;
  z-index: 2;
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: 48px 40px;
  width: 100%;
  max-width: 440px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.08);
}
.login-header {
  text-align: center;
  margin-bottom: 32px;
}
.login-header .logo-icon {
  font-size: 2rem;
  color: var(--color-accent);
}
.login-header h1 {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 8px 0 4px;
  color: var(--color-text-primary);
}
.login-header p {
  color: var(--color-text-secondary);
  font-size: 0.9rem;
}

@media (max-width: 480px) {
  .login-card { margin: 16px; padding: 32px 24px; }
}
</style>
