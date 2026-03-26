<template>
  <div class="art-card h-128 p-5 mb-5 max-sm:mb-4">
    <div class="art-card-header">
      <div class="title">
        <h4>最新评论</h4>
        <p>用户反馈动态</p>
      </div>
    </div>

    <div class="h-9/10 mt-2 overflow-hidden">
      <ElScrollbar>
        <div
          class="h-17.5 leading-17.5 border-b border-g-300 text-sm overflow-hidden last:border-b-0"
          v-for="(item, index) in list"
          :key="index"
        >
          <span class="text-g-800 font-medium">{{ item.nickname }}</span>
          <span class="mx-2 text-g-600">评价了</span>
          <span class="text-theme">{{ item.exhibitionTitle }}</span>
        </div>
        <div
          v-if="list.length === 0"
          class="h-full flex-cc text-g-500 text-sm"
        >
          暂无评论
        </div>
      </ElScrollbar>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { fetchCommentList } from '@/api/gallery'

  interface CommentItem {
    nickname: string
    exhibitionTitle: string
  }

  const list = reactive<CommentItem[]>([])

  onMounted(async () => {
    try {
      const res = await fetchCommentList({ page: 1, size: 8 })
      if (res?.records) {
        res.records.forEach((item: any) => {
          list.push({
            nickname: item.nickname || '匿名用户',
            exhibitionTitle: item.exhibitionTitle || '未知展览'
          })
        })
      }
    } catch (e) {
      console.error('Failed to fetch comments:', e)
    }
  })
</script>
