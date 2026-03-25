<template>
  <header class="navbar" :class="{ scrolled: isScrolled }">
    <div class="container navbar-inner">
      <!-- Logo -->
      <router-link to="/" class="logo">
        <span class="logo-icon"><Diamond :size="20" /></span>
        <span class="logo-text">光影美术馆</span>
      </router-link>

      <!-- Navigation Links -->
      <nav class="nav-links">
        <router-link to="/" class="nav-link" exact-active-class="active">首页</router-link>
        <router-link to="/exhibitions" class="nav-link" active-class="active">展览</router-link>
        <router-link to="/announcements" class="nav-link" active-class="active">公告</router-link>
      </nav>

      <!-- User Area -->
      <div class="user-area">
        <template v-if="userStore.isLoggedIn">
          <n-dropdown :options="userMenuOptions" @select="handleUserMenu" trigger="click">
            <div class="user-btn">
              <n-avatar :size="32" round :style="{ backgroundColor: '#1B2838', color: '#FFFFFF' }">
                {{ userStore.nickname?.charAt(0) || 'U' }}
              </n-avatar>
              <span class="user-name">{{ userStore.nickname }}</span>
            </div>
          </n-dropdown>
        </template>
        <template v-else>
          <router-link to="/login" class="btn-outline login-btn">登录</router-link>
        </template>
      </div>

      <!-- Mobile Menu -->
      <button class="mobile-toggle" @click="mobileOpen = !mobileOpen">
        <span></span><span></span><span></span>
      </button>
    </div>

    <!-- Mobile Nav -->
    <transition name="slide">
      <div v-if="mobileOpen" class="mobile-nav">
        <router-link to="/" @click="mobileOpen = false">首页</router-link>
        <router-link to="/exhibitions" @click="mobileOpen = false">展览</router-link>
        <router-link to="/announcements" @click="mobileOpen = false">公告</router-link>
        <template v-if="userStore.isLoggedIn">
          <router-link to="/profile" @click="mobileOpen = false">个人中心</router-link>
          <a @click="handleLogout">退出登录</a>
        </template>
        <template v-else>
          <router-link to="/login" @click="mobileOpen = false">登录</router-link>
        </template>
      </div>
    </transition>
  </header>
</template>

<script setup lang="ts">
import { ref, h, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { Diamond } from 'lucide-vue-next'
import { useUserStore } from '@/stores/user'
import { NIcon } from 'naive-ui'
import { PersonOutline, LogOutOutline } from '@vicons/ionicons5'

const router = useRouter()
const userStore = useUserStore()
const mobileOpen = ref(false)
const isScrolled = ref(false)

function onScroll() {
  isScrolled.value = window.scrollY > 20
}
onMounted(() => window.addEventListener('scroll', onScroll))
onUnmounted(() => window.removeEventListener('scroll', onScroll))

const userMenuOptions = [
  { label: '个人中心', key: 'profile', icon: () => h(NIcon, null, { default: () => h(PersonOutline) }) },
  { type: 'divider', key: 'd1' },
  { label: '退出登录', key: 'logout', icon: () => h(NIcon, null, { default: () => h(LogOutOutline) }) }
]

function handleUserMenu(key: string) {
  if (key === 'profile') {
    router.push('/profile')
  } else if (key === 'logout') {
    handleLogout()
  }
}

function handleLogout() {
  userStore.logout()
  mobileOpen.value = false
  router.push('/')
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  height: var(--header-height);
  background: rgba(250,250,247,0.85);
  backdrop-filter: blur(16px);
  border-bottom: 1px solid transparent;
  transition: all var(--transition-normal);
}
.navbar.scrolled {
  border-bottom-color: var(--color-border);
  box-shadow: 0 1px 12px rgba(0,0,0,0.04);
}

.navbar-inner {
  display: flex;
  align-items: center;
  height: 100%;
  gap: 40px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  flex-shrink: 0;
}
.logo-icon {
  font-size: 1.4rem;
  color: var(--color-accent);
}
.logo-text {
  font-family: var(--font-display);
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--color-text-primary);
  letter-spacing: 2px;
}

.nav-links {
  display: flex;
  gap: 8px;
  flex: 1;
}
.nav-link {
  padding: 8px 16px;
  color: var(--color-text-secondary);
  font-size: 0.95rem;
  font-weight: 500;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
  text-decoration: none;
}
.nav-link:hover,
.nav-link.active {
  color: var(--color-primary);
  background: rgba(27,40,56,0.06);
}

.user-area {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: var(--radius-sm);
  transition: background var(--transition-fast);
}
.user-btn:hover {
  background: rgba(27,40,56,0.06);
}
.user-name {
  font-size: 0.9rem;
  color: var(--color-text-primary);
}

.login-btn {
  padding: 8px 20px !important;
  font-size: 0.85rem !important;
}

.mobile-toggle {
  display: none;
  flex-direction: column;
  gap: 5px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
}
.mobile-toggle span {
  width: 22px;
  height: 2px;
  background: var(--color-text-primary);
  transition: all 0.3s;
}

.mobile-nav {
  display: none;
  flex-direction: column;
  padding: 16px 24px;
  background: var(--color-bg-card);
  border-bottom: 1px solid var(--color-border);
}
.mobile-nav a {
  padding: 12px 0;
  color: var(--color-text-secondary);
  border-bottom: 1px solid var(--color-border);
  text-decoration: none;
}
.mobile-nav a:last-child { border-bottom: none; }

.slide-enter-active, .slide-leave-active {
  transition: all 0.3s var(--ease-out);
}
.slide-enter-from, .slide-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

@media (max-width: 768px) {
  .nav-links, .user-area { display: none; }
  .mobile-toggle { display: flex; }
  .mobile-nav { display: flex; }
}
</style>
