<template>
  <div class="detail-page">
    <div v-if="loading" class="loading-center" style="padding: 120px 0;">
      <n-spin size="large" />
    </div>

    <template v-else-if="exhibition">
      <!-- Hero Banner -->
      <section class="detail-hero">
        <div class="hero-bg" :style="{ backgroundImage: `url(${exhibition.coverImage || defaultCover})` }"></div>
        <div class="hero-mask"></div>
        <div class="container hero-content">
          <n-tag :type="getStatusType()" size="medium" round>{{ getStatusText() }}</n-tag>
          <h1 class="detail-title font-display">{{ exhibition.title }}</h1>
          <div class="detail-meta">
            <span><MapPin :size="15" /> {{ exhibition.location || exhibition.hallName || '展厅' }}</span>
            <span><Calendar :size="15" /> {{ exhibition.startDate }} ~ {{ exhibition.endDate }}</span>
            <span v-if="exhibition.ticketPrice > 0" class="price">¥{{ exhibition.ticketPrice }}</span>
            <span v-else class="price free">免费</span>
          </div>
          <div class="detail-actions">
            <button class="btn-primary hero-btn" @click="scrollToBooking">立即预约</button>
            <button class="btn-outline hero-outline fav-btn" @click="handleFavorite">
              <Heart :size="16" :fill="isFavorited ? 'currentColor' : 'none'" /> {{ isFavorited ? '已收藏' : '收藏' }}
            </button>
          </div>
        </div>
      </section>

      <div class="container detail-body">
        <!-- Description -->
        <section class="desc-section">
          <h2 class="section-title font-display" style="text-align:left; font-size:1.5rem;">展览介绍</h2>
          <div class="gold-line" style="margin-left:0;"></div>
          <p class="desc-text">{{ exhibition.description }}</p>
          <!-- Gallery -->
          <div class="image-gallery" v-if="exhibition.images && exhibition.images.length > 0">
            <div v-for="(img, idx) in exhibition.images" :key="idx" class="gallery-item">
              <img :src="img" :alt="`展览图片 ${idx + 1}`" />
            </div>
          </div>
        </section>

        <!-- Exhibit Items -->
        <section class="items-section" v-if="exhibition.exhibitItems && exhibition.exhibitItems.length > 0">
          <h2 class="section-title font-display" style="text-align:left; font-size:1.5rem;">展品</h2>
          <div class="gold-line" style="margin-left:0;"></div>
          <div class="items-grid">
            <div v-for="item in exhibition.exhibitItems" :key="item.id" class="card item-card">
              <div class="item-image" v-if="item.coverImage">
                <img :src="item.coverImage" :alt="item.name" />
              </div>
              <div class="item-body">
                <h3 class="item-name">{{ item.name }}</h3>
                <p class="item-artist" v-if="item.artist">{{ item.artist }} <span v-if="item.era">· {{ item.era }}</span></p>
                <p class="item-desc" v-if="item.description">{{ item.description.slice(0, 100) }}{{ item.description.length > 100 ? '...' : '' }}</p>
              </div>
            </div>
          </div>
        </section>

        <!-- Virtual Tours -->
        <section class="tours-section" v-if="exhibition.virtualTours && exhibition.virtualTours.length > 0">
          <h2 class="section-title font-display" style="text-align:left; font-size:1.5rem;"><Glasses :size="22" style="vertical-align:middle;margin-right:6px;" /> 虚拟导览</h2>
          <div class="gold-line" style="margin-left:0;"></div>
          <div class="tours-list">
            <div v-for="tour in exhibition.virtualTours" :key="tour.id" class="card tour-card-full">
              <div class="tour-header">
                <div class="tour-info">
                  <h3>{{ tour.title }}</h3>
                  <n-tag :type="tour.tourType === 0 ? 'info' : 'success'" size="small" round>{{ tour.tourType === 0 ? '全景' : '3D模型' }}</n-tag>
                </div>
                <p v-if="tour.description" class="tour-desc">{{ tour.description }}</p>
              </div>
              <!-- 内嵌全景 iframe -->
              <div v-if="isEmbeddableUrl(tour.panoramaUrl)" class="tour-iframe-wrap">
                <iframe
                  :src="tour.panoramaUrl"
                  width="100%"
                  height="450"
                  style="border:0; border-radius: 8px;"
                  allowfullscreen
                  loading="lazy"
                  referrerpolicy="no-referrer-when-downgrade"
                ></iframe>
              </div>
              <!-- 无法嵌入时显示缩略图+链接 -->
              <div v-else-if="tour.panoramaUrl && tour.panoramaUrl !== '#'" class="tour-external">
                <div class="tour-thumb" v-if="tour.thumbnail">
                  <img :src="tour.thumbnail" :alt="tour.title" />
                </div>
                <a :href="tour.panoramaUrl" target="_blank" class="btn-outline" style="margin-top:12px; display:inline-flex; align-items:center; gap:4px; padding:8px 16px; font-size:0.85rem;">
                  开始导览 <ArrowRight :size="14" />
                </a>
              </div>
            </div>
          </div>
        </section>

        <!-- Booking -->
        <section class="booking-section" id="booking-section">
          <h2 class="section-title font-display" style="text-align:left; font-size:1.5rem;"><CalendarCheck :size="22" style="vertical-align:middle;margin-right:6px;" /> 预约参观</h2>
          <div class="gold-line" style="margin-left:0;"></div>

          <div v-if="!userStore.isLoggedIn" class="login-hint card" style="padding:32px; text-align:center;">
            <p style="margin-bottom:16px; color:var(--color-text-secondary);">请先登录后再进行预约</p>
            <router-link :to="`/login?redirect=/exhibitions/${exhibition.id}`" class="btn-primary">去登录</router-link>
          </div>

          <!-- 已预约状态 -->
          <div v-else-if="myReservation" class="card booked-card" style="padding:32px;">
            <div class="booked-header">
              <CalendarCheck :size="28" style="color: #52c41a;" />
              <div>
                <h3 style="margin:0 0 4px 0; font-size:1.1rem; color:var(--color-text-primary);">您已成功预约此展览</h3>
                <p style="margin:0; font-size:0.88rem; color:var(--color-text-secondary);">预约日期：{{ myReservation.reservationDate }} · {{ myReservation.numVisitors }}人</p>
              </div>
              <n-tag :type="reservationStatusType" size="medium" round style="margin-left:auto;">{{ reservationStatusText }}</n-tag>
            </div>
            <div v-if="myReservation.status === 0 || myReservation.status === 1" style="margin-top:16px; text-align:right;">
              <n-button size="small" type="warning" ghost @click="handleCancelReservation">取消预约</n-button>
            </div>
          </div>

          <div v-else-if="exhibition.timeSlots && exhibition.timeSlots.length > 0" class="booking-form card" style="padding:32px;">
            <n-form :model="bookingForm" label-placement="left" label-width="100">
              <n-form-item label="预约日期">
                <n-date-picker
                  v-model:value="bookingForm.date"
                  type="date"
                  style="width:100%"
                  :is-date-disabled="isDateDisabled"
                  @update:value="onDateChange"
                />
              </n-form-item>
              <n-form-item label="选择时段">
                <n-radio-group v-model:value="bookingForm.timeSlotId">
                  <n-space>
                    <n-radio
                      v-for="slot in exhibition.timeSlots"
                      :key="slot.id"
                      :value="slot.id"
                      :disabled="bookingForm.date != null && slotAvailability[slot.id] !== undefined && slotAvailability[slot.id] <= 0"
                    >
                      {{ slot.startTime?.slice(0,5) }} - {{ slot.endTime?.slice(0,5) }}
                      <template v-if="bookingForm.date != null && slotAvailability[slot.id] !== undefined">
                        <n-tag v-if="slotAvailability[slot.id] > 0" size="tiny" type="success" style="margin-left:6px;">
                          余{{ slotAvailability[slot.id] }}票
                        </n-tag>
                        <n-tag v-else size="tiny" type="error" style="margin-left:6px;">已约满</n-tag>
                      </template>
                    </n-radio>
                  </n-space>
                </n-radio-group>
              </n-form-item>
              <n-form-item label="参观人数">
                <n-input-number v-model:value="bookingForm.numVisitors" :min="1" :max="10" />
              </n-form-item>
              <n-form-item label="联系人">
                <n-input v-model:value="bookingForm.contactName" placeholder="请输入联系人姓名" />
              </n-form-item>
              <n-form-item label="联系电话">
                <n-input v-model:value="bookingForm.contactPhone" placeholder="请输入联系电话" />
              </n-form-item>
              <n-form-item>
                <n-button type="primary" size="large" @click="showBookingConfirm" block>
                  提交预约
                </n-button>
              </n-form-item>
            </n-form>
          </div>
          <div v-else class="card" style="padding:32px; text-align:center;">
            <p style="color:var(--color-text-secondary);">暂未开放预约时段</p>
          </div>
        </section>

        <!-- 预约确认弹窗 -->
        <n-modal v-model:show="confirmVisible" preset="dialog" title="确认预约信息" positive-text="确认预约" negative-text="返回修改" :loading="bookingLoading" @positive-click="doSubmitBooking" @negative-click="confirmVisible = false" style="max-width: 480px;">
          <div style="padding: 8px 0;">
            <div style="display: flex; flex-direction: column; gap: 14px;">
              <div style="display: flex; justify-content: space-between; border-bottom: 1px dashed #e8e8e8; padding-bottom: 10px;">
                <span style="color: #999; font-size: 0.88rem;">展览名称</span>
                <span style="font-weight: 600; color: var(--color-text-primary);">{{ exhibition.title }}</span>
              </div>
              <div style="display: flex; justify-content: space-between; border-bottom: 1px dashed #e8e8e8; padding-bottom: 10px;">
                <span style="color: #999; font-size: 0.88rem;">预约日期</span>
                <span>{{ confirmInfo.dateStr }}</span>
              </div>
              <div style="display: flex; justify-content: space-between; border-bottom: 1px dashed #e8e8e8; padding-bottom: 10px;">
                <span style="color: #999; font-size: 0.88rem;">参观时段</span>
                <span>{{ confirmInfo.slotName }}</span>
              </div>
              <div style="display: flex; justify-content: space-between; border-bottom: 1px dashed #e8e8e8; padding-bottom: 10px;">
                <span style="color: #999; font-size: 0.88rem;">参观人数</span>
                <span>{{ bookingForm.numVisitors }} 人</span>
              </div>
              <div style="display: flex; justify-content: space-between; border-bottom: 1px dashed #e8e8e8; padding-bottom: 10px;">
                <span style="color: #999; font-size: 0.88rem;">联系人</span>
                <span>{{ bookingForm.contactName }}</span>
              </div>
              <div style="display: flex; justify-content: space-between; border-bottom: 1px dashed #e8e8e8; padding-bottom: 10px;">
                <span style="color: #999; font-size: 0.88rem;">联系电话</span>
                <span>{{ bookingForm.contactPhone }}</span>
              </div>
              <div style="background: #FFFBE6; border-radius: 8px; padding: 16px; margin-top: 4px;">
                <div style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                  <span style="color: #999; font-size: 0.88rem;">票价（每人）</span>
                  <span style="color: var(--color-accent); font-weight: 600;">{{ exhibition.ticketPrice > 0 ? '¥' + exhibition.ticketPrice : '免费' }}</span>
                </div>
                <div style="display: flex; justify-content: space-between; font-size: 1.1rem;">
                  <span style="font-weight: 600;">预估总价</span>
                  <span style="color: #E85846; font-weight: 700; font-size: 1.25rem;">{{ exhibition.ticketPrice > 0 ? '¥' + (exhibition.ticketPrice * bookingForm.numVisitors).toFixed(2) : '免费' }}</span>
                </div>
              </div>
            </div>
          </div>
        </n-modal>

        <!-- Comments -->
        <section class="comments-section">
          <h2 class="section-title font-display" style="text-align:left; font-size:1.5rem;"><MessageSquare :size="22" style="vertical-align:middle;margin-right:6px;" /> 观展评价</h2>
          <div class="gold-line" style="margin-left:0;"></div>

          <!-- Post Comment -->
          <div v-if="userStore.isLoggedIn" class="comment-form card" style="padding:24px; margin-bottom:24px;">
            <div style="display:flex; align-items:center; gap:12px; margin-bottom:16px;">
              <span>评分：</span>
              <n-rate v-model:value="commentForm.rating" :count="5" />
            </div>
            <n-input
              v-model:value="commentForm.content"
              type="textarea"
              placeholder="分享您的观展感受..."
              :rows="3"
            />
            <div style="text-align:right; margin-top:12px;">
              <n-button type="primary" :loading="commentLoading" @click="submitComment">发表评论</n-button>
            </div>
          </div>

          <!-- Comment List -->
          <div v-if="comments.length > 0" class="comment-list">
            <div v-for="c in comments" :key="c.id" class="comment-item">
              <div class="comment-header">
                <n-avatar :size="36" round :style="{ backgroundColor: '#1B2838', color: '#FFFFFF' }">
                  {{ (c.username || '?').charAt(0) }}
                </n-avatar>
                <div class="comment-info">
                  <span class="comment-user">{{ c.username }}</span>
                  <span class="comment-time">{{ c.createTime }}</span>
                </div>
                <n-rate :value="c.rating" :count="5" readonly size="small" />
              </div>
              <p class="comment-content">{{ c.content }}</p>
            </div>
          </div>
          <n-empty v-else description="暂无评价" style="padding: 40px 0;" />

          <div class="pagination-wrap" v-if="commentTotal > commentPageSize">
            <n-pagination v-model:page="commentPage" :page-count="Math.ceil(commentTotal / commentPageSize)" @update:page="loadComments" />
          </div>
        </section>
      </div>
    </template>

    <div v-else class="container" style="text-align:center; padding:120px 0;">
      <n-empty description="展览不存在" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, reactive } from 'vue'
import { useRoute } from 'vue-router'
import { useMessage } from 'naive-ui'
import { MapPin, Calendar, Heart, Glasses, ArrowRight, CalendarCheck, MessageSquare } from 'lucide-vue-next'
import { useUserStore } from '@/stores/user'
import { getExhibitionDetail, getExhibitionComments } from '@/api/public'
import { createReservation, cancelReservation, getMyReservations, toggleFavorite, checkFavorite, postComment, getTimeSlotAvailability } from '@/api/user'

const route = useRoute()
const message = useMessage()
const userStore = useUserStore()

const defaultCover = 'https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=1200&h=600&fit=crop'
const loading = ref(true)
const exhibition = ref<any>(null)
const isFavorited = ref(false)

// Booking
const bookingForm = reactive({
  date: null as number | null,
  timeSlotId: null as number | null,
  numVisitors: 1,
  contactName: '',
  contactPhone: ''
})
const bookingLoading = ref(false)
const slotAvailability = ref<Record<number, any>>({})

// 判断URL是否可嵌入iframe
function isEmbeddableUrl(url: string) {
  if (!url || url === '#') return false
  return url.includes('google.com/maps/embed') ||
         url.includes('artsandculture.google.com') ||
         url.includes('panoraven.com') ||
         url.includes('panoee.com') ||
         url.includes('my.matterport.com') ||
         url.includes('kuula.co')
}

// Comments
const comments = ref<any[]>([])
const commentPage = ref(1)
const commentPageSize = 10
const commentTotal = ref(0)
const commentForm = reactive({ content: '', rating: 5 })
const commentLoading = ref(false)

// Reservation status
const myReservation = ref<any>(null)
const reservationStatusText = computed(() => {
  if (!myReservation.value) return ''
  const s = myReservation.value.status
  if (s === 0) return '待确认'
  if (s === 1) return '已确认'
  if (s === 3) return '已完成'
  return ''
})
const reservationStatusType = computed<'info' | 'success' | 'default'>(() => {
  if (!myReservation.value) return 'default'
  const s = myReservation.value.status
  if (s === 0) return 'info'
  if (s === 1) return 'success'
  return 'default'
})

function getStatusText() {
  if (!exhibition.value) return ''
  const now = new Date().toISOString().slice(0, 10)
  if (exhibition.value.startDate > now) return '即将开展'
  if (exhibition.value.endDate < now) return '已结束'
  return '展出中'
}
function getStatusType(): 'info' | 'success' | 'default' {
  if (!exhibition.value) return 'default'
  const now = new Date().toISOString().slice(0, 10)
  if (exhibition.value.startDate > now) return 'info'
  if (exhibition.value.endDate < now) return 'default'
  return 'success'
}

function isDateDisabled(ts: number) {
  if (!exhibition.value) return true
  const d = new Date(ts).toISOString().slice(0, 10)
  const today = new Date().toISOString().slice(0, 10)
  return d < today || d < exhibition.value.startDate || d > exhibition.value.endDate
}

async function onDateChange(val: number | null) {
  if (!val || !exhibition.value?.timeSlots) return
  const dateStr = new Date(val).toISOString().slice(0, 10)
  // 并行查询所有时段的余票
  const results: Record<number, number> = {}
  await Promise.all(
    exhibition.value.timeSlots.map(async (slot: any) => {
      try {
        const res = await getTimeSlotAvailability(slot.id, dateStr)
        results[slot.id] = typeof res.data === 'number' ? res.data : (res.data as any)?.availableCount ?? 0
      } catch {
        results[slot.id] = 0
      }
    })
  )
  slotAvailability.value = results
  // 如果当前选中的时段已约满，自动取消选择
  if (bookingForm.timeSlotId && results[bookingForm.timeSlotId] !== undefined && results[bookingForm.timeSlotId] <= 0) {
    bookingForm.timeSlotId = null
  }
}

function scrollToBooking() {
  document.getElementById('booking-section')?.scrollIntoView({ behavior: 'smooth' })
}

async function handleFavorite() {
  if (!userStore.isLoggedIn) {
    message.warning('请先登录')
    return
  }
  try {
    const res = await toggleFavorite(exhibition.value.id)
    isFavorited.value = res.data?.favorited ?? !isFavorited.value
    message.success(isFavorited.value ? '已收藏' : '已取消收藏')
  } catch {}
}

// 确认弹窗
const confirmVisible = ref(false)
const confirmInfo = reactive({ dateStr: '', slotName: '' })

function showBookingConfirm() {
  if (!bookingForm.date) { message.warning('请选择预约日期'); return }
  if (!bookingForm.timeSlotId) { message.warning('请选择时段'); return }
  if (!bookingForm.contactName) { message.warning('请输入联系人姓名'); return }
  if (!bookingForm.contactPhone) { message.warning('请输入联系电话'); return }

  // 准备确认信息
  confirmInfo.dateStr = new Date(bookingForm.date).toISOString().slice(0, 10)
  const slot = exhibition.value.timeSlots?.find((s: any) => s.id === bookingForm.timeSlotId)
  confirmInfo.slotName = slot ? `${slot.startTime?.slice(0,5)} - ${slot.endTime?.slice(0,5)}` : ''
  confirmVisible.value = true
}

async function doSubmitBooking() {
  bookingLoading.value = true
  try {
    await createReservation({
      exhibitionId: exhibition.value.id,
      timeSlotId: bookingForm.timeSlotId,
      reservationDate: confirmInfo.dateStr,
      numVisitors: bookingForm.numVisitors,
      contactName: bookingForm.contactName,
      contactPhone: bookingForm.contactPhone
    })
    message.success('预约提交成功！请等待确认')
    confirmVisible.value = false
    // 刷新预约状态
    await checkMyReservation()
    bookingForm.date = null
    bookingForm.timeSlotId = null
    bookingForm.numVisitors = 1
    bookingForm.contactName = ''
    bookingForm.contactPhone = ''
  } catch (err: any) {
    message.error(err.message || '预约失败')
  } finally {
    bookingLoading.value = false
  }
}

async function checkMyReservation() {
  if (!userStore.isLoggedIn || !exhibition.value) return
  try {
    const res = await getMyReservations({ size: 100 })
    const records = res.data?.records || []
    // 找到该展览的有效预约（待确认/已确认/已完成，排除已取消/已过期）
    const active = records.find((r: any) =>
      r.exhibitionId === exhibition.value.id && [0, 1, 3].includes(r.status)
    )
    myReservation.value = active || null
  } catch {}
}

async function handleCancelReservation() {
  if (!myReservation.value) return
  try {
    await cancelReservation(myReservation.value.id)
    message.success('预约已取消')
    myReservation.value = null
  } catch (err: any) {
    message.error(err.message || '取消失败')
  }
}

async function loadComments() {
  try {
    const res = await getExhibitionComments(route.params.id as string, { page: commentPage.value, size: commentPageSize })
    comments.value = res.data?.records || []
    commentTotal.value = res.data?.total || 0
  } catch {}
}

async function submitComment() {
  if (!commentForm.content.trim()) { message.warning('请输入评论内容'); return }
  commentLoading.value = true
  try {
    await postComment({
      exhibitionId: exhibition.value.id,
      content: commentForm.content,
      rating: commentForm.rating
    })
    message.success('评论提交成功，待审核后显示')
    commentForm.content = ''
    commentForm.rating = 5
  } catch (err: any) {
    message.error(err.message || '评论失败')
  } finally {
    commentLoading.value = false
  }
}

onMounted(async () => {
  try {
    const res = await getExhibitionDetail(route.params.id as string)
    const data = res.data
    // Backend returns { exhibition, items, tours, slots } as separate keys
    const ex = data.exhibition || data
    // Parse images JSON string if needed
    if (typeof ex.images === 'string') {
      try { ex.images = JSON.parse(ex.images) } catch { ex.images = [] }
    }
    ex.exhibitItems = data.items || []
    ex.virtualTours = data.tours || []
    ex.timeSlots = data.slots || []
    exhibition.value = ex

    // Check favorite
    if (userStore.isLoggedIn && exhibition.value) {
      try {
        const favRes = await checkFavorite(exhibition.value.id)
        isFavorited.value = favRes.data?.favorited || false
      } catch {}
      // Check reservation
      await checkMyReservation()
    }

    loadComments()
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.loading-center { display: flex; justify-content: center; }

/* ---- Hero ---- */
.detail-hero {
  position: relative;
  min-height: 420px;
  display: flex;
  align-items: flex-end;
  overflow: hidden;
}
.hero-bg {
  position: absolute;
  inset: 0;
  background-size: cover;
  background-position: center;
  filter: brightness(0.5) blur(2px);
  transform: scale(1.05);
}
.hero-mask {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(27,40,56,0.3) 0%, rgba(27,40,56,0.9) 100%);
}
.hero-content {
  position: relative;
  z-index: 2;
  padding-bottom: 48px;
}
.detail-title {
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 700;
  margin: 16px 0;
  color: #FFFFFF;
}
.detail-meta {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
  color: rgba(255,255,255,0.7);
  font-size: 0.95rem;
  margin-bottom: 24px;
}
.price { color: var(--color-accent); font-weight: 700; font-size: 1.2rem; }
.price.free { color: #52c41a; }
.detail-actions { display: flex; gap: 12px; }
.hero-btn {
  background: var(--color-accent) !important;
  color: var(--color-primary) !important;
}
.hero-btn:hover {
  background: var(--color-accent-hover) !important;
}
.hero-outline {
  color: #FFFFFF !important;
  border-color: rgba(255,255,255,0.4) !important;
}
.hero-outline:hover {
  background: rgba(255,255,255,0.1) !important;
  color: #FFFFFF !important;
}
.fav-btn { font-size: 0.9rem !important; }

/* ---- Body ---- */
.detail-body {
  padding-top: 48px;
}
.detail-body > section {
  margin-bottom: 64px;
}
.desc-text {
  color: var(--color-text-secondary);
  line-height: 1.8;
  font-size: 1rem;
  white-space: pre-line;
}
.image-gallery {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
  margin-top: 24px;
}
.gallery-item { border-radius: var(--radius-md); overflow: hidden; }
.gallery-item img { width: 100%; height: 200px; object-fit: cover; }

/* ---- Items ---- */
.items-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
}
.item-card { overflow: hidden; }
.item-image { aspect-ratio: 4/3; overflow: hidden; }
.item-image img { width:100%; height:100%; object-fit:cover; transition: transform 0.5s var(--ease-out); }
.item-card:hover .item-image img { transform: scale(1.06); }
.item-body { padding: 16px; }
.item-name { font-size: 1rem; font-weight: 600; margin-bottom: 4px; color: var(--color-text-primary); }
.item-artist { font-size: 0.85rem; color: var(--color-accent); margin-bottom: 8px; }
.item-desc { font-size: 0.82rem; color: var(--color-text-secondary); line-height: 1.5; }

/* ---- Tours ---- */
.tours-list { display: flex; flex-direction: column; gap: 24px; }
.tour-card-full { overflow: hidden; padding: 24px; }
.tour-header { margin-bottom: 16px; }
.tour-info { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.tour-info h3 { font-size: 1.1rem; font-weight: 600; margin: 0; color: var(--color-text-primary); }
.tour-desc { font-size: 0.88rem; color: var(--color-text-secondary); line-height: 1.5; margin: 0; }
.tour-iframe-wrap { border-radius: 8px; overflow: hidden; background: #f0f0f0; }
.tour-external { display: flex; flex-direction: column; align-items: flex-start; }
.tour-thumb { width: 100%; max-height: 240px; overflow: hidden; border-radius: 8px; }
.tour-thumb img { width: 100%; height: 100%; object-fit: cover; }

/* ---- Booking ---- */
.booking-section .login-hint { max-width: 480px; }

/* ---- Comments ---- */
.comment-list { display: flex; flex-direction: column; gap: 16px; }
.comment-item {
  padding: 20px;
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
}
.comment-header { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; }
.comment-info { flex: 1; display: flex; flex-direction: column; }
.comment-user { font-weight: 500; font-size: 0.95rem; color: var(--color-text-primary); }
.comment-time { font-size: 0.78rem; color: var(--color-text-muted); }
.comment-content { color: var(--color-text-secondary); line-height: 1.6; font-size: 0.95rem; }
.pagination-wrap { display: flex; justify-content: center; margin-top: 24px; }

/* ---- Booked ---- */
.booked-card { border: 1px solid #b7eb8f; background: #f6ffed; }
.booked-header { display: flex; align-items: center; gap: 16px; }

@media (max-width: 640px) {
  .detail-meta { flex-direction: column; gap: 8px; }
  .detail-actions { flex-direction: column; }
  .tour-card-full { padding: 16px; }
  .tour-iframe-wrap iframe { height: 300px; }
}
</style>
