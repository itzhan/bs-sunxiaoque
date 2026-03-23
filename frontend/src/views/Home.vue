<template>
  <div class="home">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero-bg"></div>
      <div class="hero-overlay"></div>
      <div class="container hero-content">
        <div class="hero-badge"><Sparkles :size="14" /> 在线导览 · 预约参观</div>
        <h1 class="hero-title font-display">
          光影之间<br/>
          <span class="text-accent">探索艺术之美</span>
        </h1>
        <p class="hero-desc">
          沉浸于光影交织的艺术殿堂，感受经典与当代的对话。<br/>
          在线导览、智能预约，开启您的艺术之旅。
        </p>
        <div class="hero-actions">
          <router-link to="/exhibitions" class="btn-primary">
            浏览展览
          </router-link>
          <router-link to="/announcements" class="btn-outline">
            最新公告
          </router-link>
        </div>
        <!-- Stats -->
        <div class="hero-stats">
          <div class="stat-item" v-for="s in stats" :key="s.label">
            <span class="stat-value text-accent">{{ s.value }}</span>
            <span class="stat-label">{{ s.label }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- Featured Exhibitions -->
    <section class="section exhibitions-section">
      <div class="container">
        <h2 class="section-title font-display">精选展览</h2>
        <div class="gold-line"></div>
        <p class="section-subtitle">探索当前及即将开展的艺术盛宴</p>

        <div v-if="loading" class="loading-center">
          <n-spin size="large" />
        </div>

        <div v-else class="exhibitions-grid">
          <div
            v-for="item in exhibitions"
            :key="item.id"
            class="card exhibition-card"
            @click="$router.push(`/exhibitions/${item.id}`)"
          >
            <div class="card-image">
              <img :src="item.coverImage || defaultCover" :alt="item.title" />
              <div class="card-overlay">
                <span class="card-category" v-if="item.categoryName">{{ item.categoryName }}</span>
              </div>
              <div class="card-price" v-if="item.ticketPrice > 0">
                ¥{{ item.ticketPrice }}
              </div>
              <div class="card-price free" v-else>免费</div>
            </div>
            <div class="card-body">
              <h3 class="card-title">{{ item.title }}</h3>
              <p class="card-desc">{{ item.description?.slice(0, 60) }}{{ item.description?.length > 60 ? '...' : '' }}</p>
              <div class="card-meta">
                <span><Calendar :size="14" /> {{ item.startDate }} ~ {{ item.endDate }}</span>
                <span><Eye :size="14" /> {{ item.viewCount || 0 }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="section-action" v-if="exhibitions.length > 0">
          <router-link to="/exhibitions" class="btn-outline">查看全部展览 →</router-link>
        </div>
      </div>
    </section>

    <!-- Announcements -->
    <section class="section announcements-section">
      <div class="container">
        <h2 class="section-title font-display">最新公告</h2>
        <div class="gold-line"></div>

        <div class="announcements-list" v-if="announcements.length > 0">
          <div
            v-for="item in announcements"
            :key="item.id"
            class="announcement-item card"
            @click="$router.push(`/announcements/${item.id}`)"
          >
            <div class="ann-icon"><Megaphone :size="22" /></div>
            <div class="ann-content">
              <h4 class="ann-title">
                <n-tag v-if="item.topFlag === 1" type="warning" size="small" round>置顶</n-tag>
                {{ item.title }}
              </h4>
              <p class="ann-time">{{ item.createTime }}</p>
            </div>
            <span class="ann-arrow"><ArrowRight :size="18" /></span>
          </div>
        </div>
        <n-empty v-else description="暂无公告" />
      </div>
    </section>

    <!-- Features -->
    <section class="section features-section">
      <div class="container">
        <h2 class="section-title font-display">为什么选择光影美术馆</h2>
        <div class="gold-line"></div>
        <div class="features-grid">
          <div class="feature-item" v-for="f in features" :key="f.title">
            <div class="feature-icon"><component :is="f.icon" :size="36" :stroke-width="1.5" /></div>
            <h3 class="feature-title">{{ f.title }}</h3>
            <p class="feature-desc">{{ f.desc }}</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Calendar, Eye, Megaphone, ArrowRight, Sparkles, Palette, Glasses, CalendarCheck, MessageCircle } from 'lucide-vue-next'
import { getExhibitions, getAnnouncements, getCategories } from '@/api/public'

const defaultCover = 'https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=800&h=500&fit=crop'

const loading = ref(true)
const exhibitions = ref<any[]>([])
const announcements = ref<any[]>([])
const categories = ref<any[]>([])

const stats = ref([
  { value: '10+', label: '精彩展览' },
  { value: '200+', label: '珍贵展品' },
  { value: '360°', label: '全景导览' },
  { value: '1000+', label: '在线预约' }
])

const features = [
  { icon: Palette, title: '精选展览', desc: '汇聚国内外顶尖艺术作品，从古典到当代一应俱全' },
  { icon: Glasses, title: '虚拟导览', desc: '360°全景沉浸式体验，足不出户游览艺术殿堂' },
  { icon: CalendarCheck, title: '智能预约', desc: '在线选择日期与时段，轻松预约参观，告别排队' },
  { icon: MessageCircle, title: '互动交流', desc: '发表观展感想，与艺术爱好者分享交流' }
]

onMounted(async () => {
  try {
    const [exRes, annRes, catRes] = await Promise.all([
      getExhibitions({ page: 1, size: 6 }),
      getAnnouncements({ page: 1, size: 5 }),
      getCategories()
    ])
    
    const catMap: Record<number, string> = {}
    if (Array.isArray(catRes.data)) {
      catRes.data.forEach((c: any) => { catMap[c.id] = c.name })
    }
    
    const records = exRes.data?.records || []
    exhibitions.value = records.map((e: any) => ({
      ...e,
      categoryName: catMap[e.categoryId] || ''
    }))

    announcements.value = annRes.data?.records || []
  } catch (err) {
    console.error('Failed to load home data', err)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
/* ---- Hero ---- */
.hero {
  position: relative;
  min-height: 85vh;
  display: flex;
  align-items: center;
  overflow: hidden;
}
.hero-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse at 20% 50%, rgba(201,169,110,0.08) 0%, transparent 60%),
    radial-gradient(ellipse at 80% 20%, rgba(15,52,96,0.3) 0%, transparent 50%),
    linear-gradient(180deg, #0f0f1a 0%, #1a1a2e 100%);
}
.hero-overlay {
  position: absolute;
  inset: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" width="100" height="100"><rect fill="none" width="100" height="100"/><circle cx="50" cy="50" r="0.5" fill="rgba(201,169,110,0.15)"/></svg>');
  background-size: 80px 80px;
  opacity: 0.5;
}
.hero-content {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: 80px 0 60px;
}
.hero-badge {
  display: inline-block;
  padding: 6px 20px;
  border: 1px solid var(--color-accent);
  border-radius: 20px;
  color: var(--color-accent);
  font-size: 0.85rem;
  letter-spacing: 2px;
  margin-bottom: 32px;
  animation: fadeIn 0.8s var(--ease-out) forwards;
}
.hero-title {
  font-size: clamp(2.5rem, 5vw, 4rem);
  font-weight: 700;
  line-height: 1.2;
  margin-bottom: 24px;
  animation: fadeIn 1s var(--ease-out) 0.2s forwards;
  opacity: 0;
}
.hero-desc {
  color: var(--color-text-secondary);
  font-size: 1.1rem;
  line-height: 1.8;
  max-width: 560px;
  margin: 0 auto 40px;
  animation: fadeIn 1s var(--ease-out) 0.4s forwards;
  opacity: 0;
}
.hero-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-bottom: 60px;
  animation: fadeIn 1s var(--ease-out) 0.6s forwards;
  opacity: 0;
}
.hero-stats {
  display: flex;
  justify-content: center;
  gap: 48px;
  animation: fadeIn 1s var(--ease-out) 0.8s forwards;
  opacity: 0;
}
.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}
.stat-value {
  font-size: 1.8rem;
  font-weight: 700;
  font-family: var(--font-display);
}
.stat-label {
  font-size: 0.85rem;
  color: var(--color-text-muted);
}

/* ---- Section Common ---- */
.section {
  padding: 80px 0;
}
.loading-center {
  display: flex;
  justify-content: center;
  padding: 60px 0;
}
.section-action {
  text-align: center;
  margin-top: 48px;
}

/* ---- Exhibition Cards ---- */
.exhibitions-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}
.exhibition-card {
  cursor: pointer;
  overflow: hidden;
}
.card-image {
  position: relative;
  aspect-ratio: 16 / 10;
  overflow: hidden;
}
.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s var(--ease-out);
}
.exhibition-card:hover .card-image img {
  transform: scale(1.08);
}
.card-overlay {
  position: absolute;
  top: 12px;
  left: 12px;
}
.card-category {
  background: rgba(201,169,110,0.9);
  color: var(--color-primary);
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: 600;
}
.card-price {
  position: absolute;
  top: 12px;
  right: 12px;
  background: rgba(232,88,70,0.9);
  color: #fff;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 0.85rem;
  font-weight: 700;
}
.card-price.free {
  background: rgba(82,196,26,0.9);
}
.card-body {
  padding: 20px;
}
.card-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--color-text-primary);
}
.card-desc {
  font-size: 0.88rem;
  color: var(--color-text-secondary);
  margin-bottom: 12px;
  line-height: 1.5;
}
.card-meta {
  display: flex;
  justify-content: space-between;
  font-size: 0.82rem;
  color: var(--color-text-muted);
}

/* ---- Announcements ---- */
.announcements-section {
  background: var(--color-primary-light);
}
.announcements-list {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.announcement-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px 24px;
  cursor: pointer;
}
.ann-icon {
  font-size: 1.4rem;
  flex-shrink: 0;
}
.ann-content {
  flex: 1;
}
.ann-title {
  font-size: 1rem;
  font-weight: 500;
  color: var(--color-text-primary);
  display: flex;
  align-items: center;
  gap: 8px;
}
.ann-time {
  font-size: 0.82rem;
  color: var(--color-text-muted);
  margin-top: 4px;
}
.ann-arrow {
  color: var(--color-text-muted);
  font-size: 1.2rem;
  transition: transform var(--transition-fast);
}
.announcement-item:hover .ann-arrow {
  transform: translateX(4px);
  color: var(--color-accent);
}

/* ---- Features ---- */
.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}
.feature-item {
  text-align: center;
  padding: 32px 20px;
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border);
  transition: all var(--transition-normal);
}
.feature-item:hover {
  border-color: var(--color-accent);
  background: rgba(201,169,110,0.04);
}
.feature-icon {
  font-size: 2.5rem;
  margin-bottom: 16px;
  color: var(--color-accent);
}
.feature-title {
  font-size: 1.05rem;
  font-weight: 600;
  margin-bottom: 8px;
}
.feature-desc {
  font-size: 0.88rem;
  color: var(--color-text-secondary);
  line-height: 1.5;
}

/* ---- Responsive ---- */
@media (max-width: 1024px) {
  .exhibitions-grid { grid-template-columns: repeat(2, 1fr); }
  .features-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 640px) {
  .exhibitions-grid { grid-template-columns: 1fr; }
  .features-grid { grid-template-columns: 1fr; }
  .hero-stats { gap: 24px; flex-wrap: wrap; }
  .hero-actions { flex-direction: column; align-items: center; }
}
</style>
