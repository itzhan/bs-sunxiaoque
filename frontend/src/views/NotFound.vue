<template>
  <div class="not-found">
    <div class="container" style="text-align:center; padding:120px 0;">
      <div class="nf-icon"><Diamond :size="48" /></div>
      <h1 class="font-display" style="font-size:4rem; margin:16px 0;">404</h1>
      <p style="color:var(--color-text-secondary); margin-bottom:32px; font-size:1.1rem;">
        页面不存在或已被移除
      </p>
      <router-link to="/" class="btn-primary">返回首页</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Diamond } from 'lucide-vue-next'
</script>

<style scoped>
.not-found { min-height: 100vh; display: flex; align-items: center; justify-content: center; }
.nf-icon { font-size: 3rem; color: var(--color-accent); }
</style>
