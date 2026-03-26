<template>
  <ElRow :gutter="20" class="flex">
    <ElCol v-for="(item, index) in dataList" :key="index" :sm="12" :md="6" :lg="6">
      <div class="art-card relative flex flex-col justify-center h-35 px-5 mb-5 max-sm:mb-4">
        <span class="text-g-700 text-sm">{{ item.des }}</span>
        <ArtCountTo class="text-[26px] font-medium mt-2" :target="item.num" :duration="1300" />
        <div class="flex-c mt-1">
          <span class="text-xs text-g-600">{{ item.subText }}</span>
        </div>
        <div
          class="absolute top-0 bottom-0 right-5 m-auto size-12.5 rounded-xl flex-cc bg-theme/10"
        >
          <ArtSvgIcon :icon="item.icon" class="text-xl text-theme" />
        </div>
      </div>
    </ElCol>
  </ElRow>
</template>

<script setup lang="ts">
  import { fetchDashboard } from '@/api/gallery'

  interface CardDataItem {
    des: string
    icon: string
    num: number
    subText: string
  }

  const dataList = reactive<CardDataItem[]>([
    {
      des: '展览总数',
      icon: 'ri:gallery-line',
      num: 0,
      subText: '正在展出及已结束'
    },
    {
      des: '注册用户',
      icon: 'ri:group-line',
      num: 0,
      subText: '平台注册用户总量'
    },
    {
      des: '预约总数',
      icon: 'ri:calendar-check-line',
      num: 0,
      subText: '累计预约记录'
    },
    {
      des: '今日预约',
      icon: 'ri:fire-line',
      num: 0,
      subText: '今日新增预约'
    }
  ])

  onMounted(async () => {
    try {
      const res = await fetchDashboard()
      if (res) {
        dataList[0].num = res.totalExhibitions ?? 0
        dataList[1].num = res.totalUsers ?? 0
        dataList[2].num = res.totalReservations ?? 0
        dataList[3].num = res.todayReservations ?? 0
      }
    } catch (e) {
      console.error('Failed to fetch dashboard data:', e)
    }
  })
</script>
