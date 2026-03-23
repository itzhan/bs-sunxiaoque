<template>
  <div class="exhibition-list-page">
    <!-- Page Header -->
    <section class="page-header">
      <div class="container">
        <h1 class="page-title font-display">展览</h1>
        <div class="gold-line"></div>
        <p class="page-desc">探索精彩纷呈的艺术展览</p>
      </div>
    </section>

    <section class="section">
      <div class="container">
        <!-- Filters -->
        <div class="filters">
          <div class="category-tabs">
            <button
              class="tab-btn"
              :class="{ active: selectedCategory === null }"
              @click="selectedCategory = null; loadData()"
            >全部</button>
            <button
              v-for="cat in categories"
              :key="cat.id"
              class="tab-btn"
              :class="{ active: selectedCategory === cat.id }"
              @click="selectedCategory = cat.id; loadData()"
            >{{ cat.name }}</button>
          </div>
          <div class="search-box">
            <n-input
              v-model:value="searchKeyword"
              placeholder="搜索展览..."
              clearable
              @keyup.enter="loadData()"
              @clear="loadData()"
            >
              <template #prefix><Search :size="16" /></template>
            </n-input>
          </div>
        </div>

        <!-- Grid -->
        <div v-if="loading" class="loading-center">
          <n-spin size="large" />
        </div>

        <template v-else>
          <div v-if="exhibitions.length > 0" class="exhibitions-grid">
            <div
              v-for="item in exhibitions"
              :key="item.id"
              class="card exhibition-card"
              @click="$router.push(`/exhibitions/${item.id}`)"
            >
              <div class="card-image">
                <img :src="item.coverImage || defaultCover" :alt="item.title" />
                <div class="card-status">
                  <n-tag :type="getStatusType(item)" size="small" round>{{ getStatusText(item) }}</n-tag>
                </div>
                <div class="card-price-tag" v-if="item.ticketPrice > 0">¥{{ item.ticketPrice }}</div>
                <div class="card-price-tag free" v-else>免费</div>
              </div>
              <div class="card-body">
                <h3 class="card-title">{{ item.title }}</h3>
                <p class="card-desc">{{ item.description?.slice(0, 80) }}{{ (item.description?.length || 0) > 80 ? '...' : '' }}</p>
                <div class="card-info">
                  <span><MapPin :size="14" /> {{ item.location || '展厅' }}</span>
                  <span><Calendar :size="14" /> {{ item.startDate }} ~ {{ item.endDate }}</span>
                </div>
                <div class="card-footer">
                  <span class="view-count"><Eye :size="14" /> {{ item.viewCount || 0 }} 浏览</span>
                  <span class="detail-link text-accent">查看详情 <ArrowRight :size="14" /></span>
                </div>
              </div>
            </div>
          </div>
          <n-empty v-else description="暂无展览" style="padding: 80px 0;" />
        </template>

        <!-- Pagination -->
        <div class="pagination-wrap" v-if="total > pageSize">
          <n-pagination
            v-model:page="currentPage"
            :page-count="Math.ceil(total / pageSize)"
            @update:page="loadData"
          />
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Search, MapPin, Calendar, Eye, ArrowRight } from 'lucide-vue-next'
import { getExhibitions, getCategories } from '@/api/public'

const defaultCover = 'https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=800&h=500&fit=crop'

const loading = ref(true)
const exhibitions = ref<any[]>([])
const categories = ref<any[]>([])
const selectedCategory = ref<number | null>(null)
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = 9
const total = ref(0)

function getStatusText(item: any) {
  const now = new Date().toISOString().slice(0, 10)
  if (item.startDate > now) return '即将开展'
  if (item.endDate < now) return '已结束'
  return '展出中'
}

function getStatusType(item: any): 'info' | 'success' | 'default' {
  const now = new Date().toISOString().slice(0, 10)
  if (item.startDate > now) return 'info'
  if (item.endDate < now) return 'default'
  return 'success'
}

async function loadData() {
  loading.value = true
  try {
    const params: any = { page: currentPage.value, size: pageSize }
    if (selectedCategory.value) params.categoryId = selectedCategory.value
    if (searchKeyword.value) params.title = searchKeyword.value
    const res = await getExhibitions(params)
    exhibitions.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  try {
    const catRes = await getCategories()
    categories.value = Array.isArray(catRes.data) ? catRes.data : []
  } catch {}
  loadData()
})
</script>

<style scoped>
.page-header {
  text-align: center;
  padding: 60px 0 40px;
  background: linear-gradient(180deg, rgba(201,169,110,0.06) 0%, transparent 100%);
}
.page-title {
  font-size: 2.5rem;
  font-weight: 700;
}
.page-desc {
  color: var(--color-text-secondary);
  font-size: 1.05rem;
}

.section { padding: 0 0 80px; }

.filters {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  gap: 24px;
  flex-wrap: wrap;
}
.category-tabs {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
.tab-btn {
  padding: 8px 20px;
  border: 1px solid var(--color-border);
  border-radius: 20px;
  background: transparent;
  color: var(--color-text-secondary);
  cursor: pointer;
  font-size: 0.88rem;
  transition: all var(--transition-fast);
}
.tab-btn:hover { border-color: var(--color-accent); color: var(--color-accent); }
.tab-btn.active {
  background: var(--color-accent);
  color: var(--color-primary);
  border-color: var(--color-accent);
  font-weight: 600;
}
.search-box { width: 280px; }

.loading-center { display: flex; justify-content: center; padding: 60px 0; }

.exhibitions-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}
.exhibition-card { cursor: pointer; overflow: hidden; }
.card-image {
  position: relative;
  aspect-ratio: 16 / 10;
  overflow: hidden;
}
.card-image img {
  width: 100%; height: 100%;
  object-fit: cover;
  transition: transform 0.6s var(--ease-out);
}
.exhibition-card:hover .card-image img { transform: scale(1.06); }
.card-status { position: absolute; top: 12px; left: 12px; }
.card-price-tag {
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
.card-price-tag.free { background: rgba(82,196,26,0.9); }
.card-body { padding: 20px; }
.card-title { font-size: 1.1rem; font-weight: 600; margin-bottom: 8px; }
.card-desc { font-size: 0.88rem; color: var(--color-text-secondary); margin-bottom: 12px; line-height: 1.5; }
.card-info { display: flex; flex-direction: column; gap: 4px; font-size: 0.82rem; color: var(--color-text-muted); margin-bottom: 12px; }
.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid var(--color-border);
}
.view-count { font-size: 0.82rem; color: var(--color-text-muted); }
.detail-link { font-size: 0.85rem; font-weight: 500; }

.pagination-wrap { display: flex; justify-content: center; margin-top: 48px; }

@media (max-width: 1024px) { .exhibitions-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) {
  .exhibitions-grid { grid-template-columns: 1fr; }
  .filters { flex-direction: column; align-items: stretch; }
  .search-box { width: 100%; }
}
</style>
