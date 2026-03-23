<template>
  <div class="announcement-list-page">
    <section class="page-header">
      <div class="container">
        <h1 class="page-title font-display">公告</h1>
        <div class="gold-line"></div>
        <p class="page-desc">了解美术馆最新动态</p>
      </div>
    </section>

    <section class="section">
      <div class="container">
        <div v-if="loading" class="loading-center"><n-spin size="large" /></div>

        <template v-else>
          <div v-if="list.length > 0" class="ann-list">
            <div v-for="item in list" :key="item.id" class="card ann-card" @click="$router.push(`/announcements/${item.id}`)">
              <div class="ann-cover" v-if="item.coverImage">
                <img :src="item.coverImage" :alt="item.title" />
              </div>
              <div class="ann-body">
                <div class="ann-header">
                  <n-tag v-if="item.topFlag === 1" type="warning" size="small" round>置顶</n-tag>
                  <span class="ann-date">{{ item.createTime?.slice(0, 10) }}</span>
                </div>
                <h3 class="ann-title">{{ item.title }}</h3>
                <p class="ann-excerpt">{{ item.content?.slice(0, 120) }}{{ (item.content?.length || 0) > 120 ? '...' : '' }}</p>
                <span class="read-more text-accent">阅读全文 →</span>
              </div>
            </div>
          </div>
          <n-empty v-else description="暂无公告" style="padding: 80px 0;" />
        </template>

        <div class="pagination-wrap" v-if="total > pageSize">
          <n-pagination v-model:page="page" :page-count="Math.ceil(total / pageSize)" @update:page="loadData" />
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getAnnouncements } from '@/api/public'

const loading = ref(true)
const list = ref<any[]>([])
const page = ref(1)
const pageSize = 10
const total = ref(0)

async function loadData() {
  loading.value = true
  try {
    const res = await getAnnouncements({ page: page.value, size: pageSize })
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch {} finally { loading.value = false }
}

onMounted(loadData)
</script>

<style scoped>
.page-header { text-align:center; padding:60px 0 40px; background:linear-gradient(180deg, rgba(201,169,110,0.06), transparent); }
.page-title { font-size:2.5rem; font-weight:700; }
.page-desc { color:var(--color-text-secondary); font-size:1.05rem; }
.section { padding: 0 0 80px; }
.loading-center { display:flex; justify-content:center; padding:60px 0; }

.ann-list { display:flex; flex-direction:column; gap:20px; max-width:800px; margin:0 auto; }
.ann-card { display:flex; overflow:hidden; cursor:pointer; }
.ann-cover { width:200px; flex-shrink:0; }
.ann-cover img { width:100%; height:100%; object-fit:cover; }
.ann-body { padding:24px; flex:1; }
.ann-header { display:flex; align-items:center; gap:8px; margin-bottom:8px; }
.ann-date { font-size:0.82rem; color:var(--color-text-muted); }
.ann-title { font-size:1.15rem; font-weight:600; margin-bottom:8px; }
.ann-excerpt { font-size:0.88rem; color:var(--color-text-secondary); line-height:1.6; margin-bottom:12px; }
.read-more { font-size:0.85rem; font-weight:500; }
.pagination-wrap { display:flex; justify-content:center; margin-top:48px; }

@media (max-width: 640px) {
  .ann-card { flex-direction:column; }
  .ann-cover { width:100%; height:180px; }
}
</style>
