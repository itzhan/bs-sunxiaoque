<template>
  <div class="announcement-detail-page">
    <div class="container">
      <div v-if="loading" class="loading-center" style="padding:120px 0;"><n-spin size="large" /></div>
      <template v-else-if="announcement">
        <div class="detail-wrap">
          <div class="back-link">
            <router-link to="/announcements">← 返回公告列表</router-link>
          </div>
          <h1 class="detail-title font-display">{{ announcement.title }}</h1>
          <div class="detail-meta">
            <span><Calendar :size="14" /> {{ announcement.createTime }}</span>
            <n-tag v-if="announcement.topFlag === 1" type="warning" size="small" round>置顶</n-tag>
          </div>
          <div v-if="announcement.coverImage" class="detail-cover">
            <img :src="announcement.coverImage" :alt="announcement.title" />
          </div>
          <div class="detail-content">{{ announcement.content }}</div>
        </div>
      </template>
      <n-empty v-else description="公告不存在" style="padding:120px 0;" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Calendar } from 'lucide-vue-next'
import request from '@/utils/request'

const route = useRoute()
const loading = ref(true)
const announcement = ref<any>(null)

onMounted(async () => {
  try {
    const res = await request.get(`/announcements/${route.params.id}`)
    announcement.value = res.data
  } catch {} finally { loading.value = false }
})
</script>

<style scoped>
.announcement-detail-page { padding: 40px 0 80px; }
.loading-center { display:flex; justify-content:center; }
.detail-wrap { max-width:800px; margin:0 auto; }
.back-link { margin-bottom:24px; }
.back-link a { color:var(--color-text-secondary); font-size:0.9rem; }
.back-link a:hover { color:var(--color-accent); }
.detail-title { font-size:2rem; font-weight:700; margin-bottom:12px; }
.detail-meta { display:flex; align-items:center; gap:12px; color:var(--color-text-muted); font-size:0.88rem; margin-bottom:32px; }
.detail-cover { margin-bottom:32px; border-radius:var(--radius-md); overflow:hidden; }
.detail-cover img { width:100%; }
.detail-content { color:var(--color-text-secondary); line-height:2; font-size:1rem; white-space:pre-line; }
</style>
