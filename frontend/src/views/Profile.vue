<template>
  <div class="profile-page">
    <section class="page-header">
      <div class="container">
        <h1 class="page-title font-display">个人中心</h1>
        <div class="gold-line"></div>
      </div>
    </section>

    <section class="section">
      <div class="container">
        <n-tabs v-model:value="activeTab" type="line" animated>
          <!-- 个人资料 -->
          <n-tab-pane name="profile" tab="个人资料">
            <div class="profile-card card" style="padding:32px; max-width:600px;">
              <n-form :model="profileForm" label-placement="left" label-width="80">
                <n-form-item label="用户名">
                  <n-input :value="userStore.username" disabled />
                </n-form-item>
                <n-form-item label="昵称">
                  <n-input v-model:value="profileForm.nickname" />
                </n-form-item>
                <n-form-item label="手机号">
                  <n-input v-model:value="profileForm.phone" />
                </n-form-item>
                <n-form-item label="邮箱">
                  <n-input v-model:value="profileForm.email" />
                </n-form-item>
                <n-form-item label="性别">
                  <n-radio-group v-model:value="profileForm.gender">
                    <n-space>
                      <n-radio :value="0">未知</n-radio>
                      <n-radio :value="1">男</n-radio>
                      <n-radio :value="2">女</n-radio>
                    </n-space>
                  </n-radio-group>
                </n-form-item>
                <n-form-item>
                  <n-button type="primary" :loading="profileLoading" @click="saveProfile">保存修改</n-button>
                </n-form-item>
              </n-form>
            </div>

            <div class="password-card card" style="padding:32px; max-width:600px; margin-top:24px;">
              <h3 style="margin-bottom:20px; font-size:1.1rem;">修改密码</h3>
              <n-form :model="pwdForm" label-placement="left" label-width="80">
                <n-form-item label="原密码">
                  <n-input v-model:value="pwdForm.oldPassword" type="password" show-password-on="mousedown" />
                </n-form-item>
                <n-form-item label="新密码">
                  <n-input v-model:value="pwdForm.newPassword" type="password" show-password-on="mousedown" />
                </n-form-item>
                <n-form-item>
                  <n-button type="primary" :loading="pwdLoading" @click="savePwd">修改密码</n-button>
                </n-form-item>
              </n-form>
            </div>
          </n-tab-pane>

          <!-- 我的预约 -->
          <n-tab-pane name="reservations" tab="我的预约">
            <div v-if="resLoading" class="loading-center"><n-spin /></div>
            <template v-else>
              <div v-if="reservations.length > 0" class="reservation-list">
                <div v-for="r in reservations" :key="r.id" class="card res-card">
                  <div class="res-info">
                    <h4>{{ r.exhibitionTitle }}</h4>
                    <div class="res-meta">
                      <span><Calendar :size="14" /> {{ r.reservationDate }}</span>
                      <span><Clock :size="14" /> {{ r.slotName }}</span>
                      <span><Users :size="14" /> {{ r.numVisitors }}人</span>
                    </div>
                    <div class="res-contact">联系人: {{ r.contactName }} · {{ r.contactPhone }}</div>
                  </div>
                  <div class="res-actions">
                    <n-tag :type="statusType(r.status)" size="medium">{{ statusText(r.status) }}</n-tag>
                    <n-button v-if="r.status === 0" size="small" quaternary type="error" @click="handleCancel(r.id)">
                      取消预约
                    </n-button>
                  </div>
                </div>
              </div>
              <n-empty v-else description="暂无预约" style="padding:60px 0;" />
              <div class="pagination-wrap" v-if="resTotal > resPageSize">
                <n-pagination v-model:page="resPage" :page-count="Math.ceil(resTotal / resPageSize)" @update:page="loadReservations" />
              </div>
            </template>
          </n-tab-pane>

          <!-- 我的收藏 -->
          <n-tab-pane name="favorites" tab="我的收藏">
            <div v-if="favLoading" class="loading-center"><n-spin /></div>
            <template v-else>
              <div v-if="favorites.length > 0" class="fav-grid">
                <div v-for="f in favorites" :key="f.id" class="card fav-card" @click="$router.push(`/exhibitions/${f.exhibitionId}`)">
                  <div class="fav-cover">
                    <img :src="f.coverImage || defaultCover" :alt="f.exhibitionTitle" />
                  </div>
                  <div class="fav-body">
                    <h4>{{ f.exhibitionTitle }}</h4>
                    <span class="fav-time">收藏于 {{ f.createTime?.slice(0, 10) }}</span>
                  </div>
                </div>
              </div>
              <n-empty v-else description="暂无收藏" style="padding:60px 0;" />
              <div class="pagination-wrap" v-if="favTotal > favPageSize">
                <n-pagination v-model:page="favPage" :page-count="Math.ceil(favTotal / favPageSize)" @update:page="loadFavorites" />
              </div>
            </template>
          </n-tab-pane>
        </n-tabs>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Calendar, Clock, Users } from 'lucide-vue-next'
import { useMessage } from 'naive-ui'
import { useUserStore } from '@/stores/user'
import { getProfile, updateProfile, changePassword } from '@/api/user'
import { getMyReservations, cancelReservation, getMyFavorites } from '@/api/user'

const message = useMessage()
const userStore = useUserStore()
const defaultCover = 'https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400&h=250&fit=crop'

const activeTab = ref('profile')

// Profile
const profileForm = reactive({ nickname: '', phone: '', email: '', gender: 0 })
const profileLoading = ref(false)
const pwdForm = reactive({ oldPassword: '', newPassword: '' })
const pwdLoading = ref(false)

// Reservations
const resLoading = ref(true)
const reservations = ref<any[]>([])
const resPage = ref(1)
const resPageSize = 10
const resTotal = ref(0)

// Favorites
const favLoading = ref(true)
const favorites = ref<any[]>([])
const favPage = ref(1)
const favPageSize = 12
const favTotal = ref(0)

function statusText(s: number) {
  return ['待确认', '已确认', '已取消', '已完成', '已过期'][s] || '未知'
}
function statusType(s: number): 'warning' | 'success' | 'error' | 'default' | 'info' {
  return (['warning', 'success', 'error', 'default', 'info'] as const)[s] || 'default'
}

async function loadProfile() {
  try {
    const res = await getProfile()
    Object.assign(profileForm, {
      nickname: res.data?.nickname || '',
      phone: res.data?.phone || '',
      email: res.data?.email || '',
      gender: res.data?.gender ?? 0
    })
  } catch {}
}

async function saveProfile() {
  profileLoading.value = true
  try {
    await updateProfile(profileForm)
    message.success('资料已更新')
    userStore.refreshInfo()
  } catch (e: any) { message.error(e.message || '更新失败') }
  finally { profileLoading.value = false }
}

async function savePwd() {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) { message.warning('请填写完整'); return }
  pwdLoading.value = true
  try {
    await changePassword(pwdForm)
    message.success('密码已修改')
    pwdForm.oldPassword = ''
    pwdForm.newPassword = ''
  } catch (e: any) { message.error(e.message || '修改失败') }
  finally { pwdLoading.value = false }
}

async function loadReservations() {
  resLoading.value = true
  try {
    const res = await getMyReservations({ page: resPage.value, size: resPageSize })
    reservations.value = res.data?.records || []
    resTotal.value = res.data?.total || 0
  } catch {} finally { resLoading.value = false }
}

async function handleCancel(id: number) {
  try {
    await cancelReservation(id)
    message.success('已取消预约')
    loadReservations()
  } catch (e: any) { message.error(e.message || '取消失败') }
}

async function loadFavorites() {
  favLoading.value = true
  try {
    const res = await getMyFavorites({ page: favPage.value, size: favPageSize })
    favorites.value = res.data?.records || []
    favTotal.value = res.data?.total || 0
  } catch {} finally { favLoading.value = false }
}

onMounted(() => {
  loadProfile()
  loadReservations()
  loadFavorites()
})
</script>

<style scoped>
.page-header { text-align:center; padding:60px 0 40px; }
.page-title { font-size:2.5rem; font-weight:700; }
.section { padding: 0 0 80px; }
.loading-center { display:flex; justify-content:center; padding:40px 0; }

/* Reservations */
.reservation-list { display:flex; flex-direction:column; gap:16px; }
.res-card { display:flex; justify-content:space-between; align-items:center; padding:24px; gap:16px; flex-wrap:wrap; }
.res-info h4 { font-size:1.05rem; margin-bottom:4px; }
.res-meta { display:flex; gap:16px; font-size:0.85rem; color:var(--color-text-secondary); margin-bottom:4px; }
.res-contact { font-size:0.82rem; color:var(--color-text-muted); }
.res-actions { display:flex; flex-direction:column; align-items:flex-end; gap:8px; }

/* Favorites */
.fav-grid { display:grid; grid-template-columns:repeat(auto-fill, minmax(260px, 1fr)); gap:20px; }
.fav-card { cursor:pointer; overflow:hidden; }
.fav-cover { aspect-ratio:16/10; overflow:hidden; }
.fav-cover img { width:100%; height:100%; object-fit:cover; transition: transform 0.5s var(--ease-out); }
.fav-card:hover .fav-cover img { transform:scale(1.06); }
.fav-body { padding:16px; }
.fav-body h4 { font-size:1rem; margin-bottom:4px; }
.fav-time { font-size:0.82rem; color:var(--color-text-muted); }
.pagination-wrap { display:flex; justify-content:center; margin-top:32px; }
</style>
