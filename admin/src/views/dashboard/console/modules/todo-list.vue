<template>
  <div class="art-card h-128 p-5 mb-5 max-sm:mb-4">
    <div class="art-card-header">
      <div class="title">
        <h4>待处理事项</h4>
        <p>需管理员处理</p>
      </div>
    </div>

    <div class="h-[calc(100%-40px)] overflow-auto">
      <ElScrollbar>
        <div
          class="flex-cb h-17.5 border-b border-g-300 text-sm last:border-b-0"
          v-for="(item, index) in list"
          :key="index"
        >
          <div>
            <p class="text-sm">{{ item.title }}</p>
            <p class="text-g-500 mt-1">{{ item.desc }}</p>
          </div>
          <ElTag :type="item.tagType as any" size="small">{{ item.count }}</ElTag>
        </div>
        <div
          v-if="list.length === 0"
          class="h-full flex-cc text-g-500 text-sm"
        >
          暂无待处理事项
        </div>
      </ElScrollbar>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { fetchReservationList, fetchCommentList } from '@/api/gallery'
  import { ElTag } from 'element-plus'

  interface TodoItem {
    title: string
    desc: string
    count: number
    tagType: string
  }

  const list = reactive<TodoItem[]>([])

  onMounted(async () => {
    try {
      // 获取待确认预约
      const reservationRes = await fetchReservationList({ page: 1, size: 1, status: 0 })
      const pendingReservations = reservationRes?.total || 0

      // 获取待审核评论
      const commentRes = await fetchCommentList({ page: 1, size: 1, status: 0 })
      const pendingComments = commentRes?.total || 0

      if (pendingReservations > 0) {
        list.push({
          title: '待确认预约',
          desc: '需要确认的预约申请',
          count: pendingReservations,
          tagType: 'warning'
        })
      }
      if (pendingComments > 0) {
        list.push({
          title: '待审核评论',
          desc: '需要审核的用户评论',
          count: pendingComments,
          tagType: 'danger'
        })
      }
      if (list.length === 0) {
        list.push({
          title: '全部处理完毕',
          desc: '当前没有待处理的事项',
          count: 0,
          tagType: 'success'
        })
      }
    } catch (e) {
      console.error('Failed to fetch todo data:', e)
    }
  })
</script>
