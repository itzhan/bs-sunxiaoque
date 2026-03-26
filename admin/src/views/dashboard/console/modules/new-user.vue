<template>
  <div class="art-card p-5 h-128 overflow-hidden mb-5 max-sm:mb-4">
    <div class="art-card-header">
      <div class="title">
        <h4>热门展览</h4>
        <p>按浏览量排序</p>
      </div>
    </div>
    <ArtTable
      class="w-full"
      :data="tableData"
      style="width: 100%"
      size="large"
      :border="false"
      :stripe="false"
      :header-cell-style="{ background: 'transparent' }"
    >
      <template #default>
        <ElTableColumn label="排名" width="70px">
          <template #default="scope">
            <span
              class="inline-flex items-center justify-center size-6 rounded-full text-xs font-bold"
              :class="scope.$index < 3 ? 'bg-theme/15 text-theme' : 'bg-g-200 text-g-600'"
            >
              {{ scope.$index + 1 }}
            </span>
          </template>
        </ElTableColumn>
        <ElTableColumn label="展览名称" prop="title" />
        <ElTableColumn label="浏览量" prop="viewCount" width="100px" sortable>
          <template #default="scope">
            <span class="font-medium">{{ scope.row.viewCount.toLocaleString() }}</span>
          </template>
        </ElTableColumn>
        <ElTableColumn label="状态" width="100px">
          <template #default="scope">
            <ElTag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </ElTag>
          </template>
        </ElTableColumn>
      </template>
    </ArtTable>
  </div>
</template>

<script setup lang="ts">
  import { ElTag } from 'element-plus'
  import { fetchExhibitionList } from '@/api/gallery'

  interface ExhibitionRow {
    title: string
    viewCount: number
    status: number
  }

  const tableData = reactive<ExhibitionRow[]>([])

  const getStatusType = (status: number) => {
    const map: Record<number, string> = { 0: 'info', 1: 'warning', 2: 'success', 3: '' }
    return (map[status] || 'info') as any
  }

  const getStatusText = (status: number) => {
    const map: Record<number, string> = { 0: '草稿', 1: '即将开展', 2: '展出中', 3: '已结束' }
    return map[status] || '未知'
  }

  onMounted(async () => {
    try {
      const res = await fetchExhibitionList({ page: 1, size: 6 })
      if (res?.records) {
        // 按浏览量排序
        const sorted = [...res.records].sort((a, b) => (b.viewCount || 0) - (a.viewCount || 0))
        sorted.forEach((item) => {
          tableData.push({
            title: item.title,
            viewCount: item.viewCount || 0,
            status: item.status
          })
        })
      }
    } catch (e) {
      console.error('Failed to fetch exhibitions:', e)
    }
  })
</script>

<style lang="scss" scoped>
  .art-card {
    :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
      color: var(--el-color-primary) !important;
      background: transparent !important;
    }
  }
</style>
