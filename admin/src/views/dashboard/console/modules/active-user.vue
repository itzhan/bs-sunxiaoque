<template>
  <div class="art-card h-105 p-4 box-border mb-5 max-sm:mb-4">
    <ArtBarChart
      class="box-border p-2"
      barWidth="50%"
      height="13.7rem"
      :showAxisLine="false"
      :data="chartData"
      :xAxisData="xAxisLabels"
    />
    <div class="ml-1">
      <h3 class="mt-5 text-lg font-medium">预约概览</h3>
      <p class="mt-1 text-sm">展览预约情况统计</p>
    </div>
    <div class="flex-b mt-2">
      <div class="flex-1" v-for="(item, index) in list" :key="index">
        <p class="text-2xl text-g-900">{{ item.num }}</p>
        <p class="text-xs text-g-500">{{ item.name }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { fetchDashboard } from '@/api/gallery'

  interface StatItem {
    name: string
    num: string | number
  }

  // 近9个月预约趋势（模拟数据，与项目时间线吻合 2025.7~2026.3）
  const xAxisLabels = ['7月', '8月', '9月', '10月', '11月', '12月', '1月', '2月', '3月']
  const chartData = [45, 62, 78, 95, 130, 105, 88, 110, 156]

  const list = reactive<StatItem[]>([
    { name: '总预约数', num: '-' },
    { name: '总评论数', num: '-' },
    { name: '总浏览量', num: '-' },
    { name: '今日预约', num: '-' }
  ])

  onMounted(async () => {
    try {
      const res = await fetchDashboard()
      if (res) {
        list[0].num = res.totalReservations ?? 0
        list[1].num = res.totalComments ?? 0
        list[2].num = res.totalVisits ?? 0
        list[3].num = res.todayReservations ?? 0
      }
    } catch (e) {
      console.error('Failed to fetch dashboard:', e)
    }
  })
</script>
