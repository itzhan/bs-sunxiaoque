<script setup lang="ts">
import { MapPin, Phone, Mail, Diamond } from 'lucide-vue-next'
</script>

<template>
  <footer class="footer">
    <div class="container footer-content">
      <div class="footer-top">
        <div class="footer-brand">
          <div class="footer-logo">
            <span class="logo-icon"><Diamond :size="18" /></span>
            <span class="logo-text font-display">光影美术馆</span>
          </div>
          <p class="footer-desc">探索光与影的交响，感受艺术的脉动</p>
        </div>
        <div class="footer-links">
          <div class="link-group">
            <h4>导航</h4>
            <router-link to="/">首页</router-link>
            <router-link to="/exhibitions">展览</router-link>
            <router-link to="/announcements">公告</router-link>
          </div>
          <div class="link-group">
            <h4>关于</h4>
            <a href="#">参观指南</a>
            <a href="#">联系我们</a>
            <a href="#">隐私政策</a>
          </div>
          <div class="link-group">
            <h4>联系方式</h4>
            <p><MapPin :size="14" /> 北京市朝阳区光影大道1号</p>
            <p><Phone :size="14" /> 010-88886666</p>
            <p><Mail :size="14" /> info@gallery-tour.com</p>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>© 2026 光影美术馆在线导览与预约系统 · All Rights Reserved</p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background: var(--color-primary);
  margin-top: 80px;
}

.footer-content {
  padding-top: 48px;
  padding-bottom: 24px;
}

.footer-top {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 48px;
  padding-bottom: 36px;
  border-bottom: 1px solid rgba(255,255,255,0.12);
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}
.footer-logo .logo-icon {
  font-size: 1.2rem;
  color: var(--color-accent);
}
.footer-logo .logo-text {
  font-size: 1.15rem;
  font-weight: 700;
  color: #FFFFFF;
  letter-spacing: 2px;
}
.footer-desc {
  color: rgba(255,255,255,0.6);
  font-size: 0.9rem;
  line-height: 1.6;
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 32px;
}

.link-group h4 {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--color-accent);
  text-transform: uppercase;
  letter-spacing: 1px;
  margin-bottom: 16px;
}
.link-group a,
.link-group p {
  display: flex;
  align-items: center;
  gap: 6px;
  color: rgba(255,255,255,0.6);
  font-size: 0.88rem;
  margin-bottom: 10px;
  text-decoration: none;
  transition: color var(--transition-fast);
}
.link-group a:hover {
  color: var(--color-accent);
}

.footer-bottom {
  text-align: center;
  padding-top: 24px;
}
.footer-bottom p {
  color: rgba(255,255,255,0.4);
  font-size: 0.82rem;
}

@media (max-width: 768px) {
  .footer-top {
    grid-template-columns: 1fr;
    gap: 32px;
  }
  .footer-links {
    grid-template-columns: 1fr 1fr;
  }
}
</style>
