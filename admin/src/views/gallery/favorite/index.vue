<!-- 收藏记录管理页面（只读） -->
<template>
  <div class="favorite-page art-full-height">
    <ElCard class="art-table-card">
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElSpace wrap>
            <ElText type="info">收藏记录（所有用户）</ElText>
          </ElSpace>
        </template>
      </ArtTableHeader>

      <ArtTable :loading="loading" :data="data" :columns="columns" :pagination="pagination"
        @pagination:size-change="handleSizeChange" @pagination:current-change="handleCurrentChange">
      </ArtTable>
    </ElCard>
  </div>
</template>

<script setup lang="ts">
  import { useTable } from '@/hooks/core/useTable'
  import { fetchFavoriteList } from '@/api/gallery'
  import { ElImage } from 'element-plus'

  defineOptions({ name: 'FavoriteManage' })

  const {
    columns, columnChecks, data, loading, pagination, getData,
    handleSizeChange, handleCurrentChange, refreshData
  } = useTable({
    core: {
      apiFn: fetchFavoriteList,
      apiParams: { page: 1, size: 10 },
      columnsFactory: () => [
        { type: 'index', width: 60, label: '序号' },
        { prop: 'userId', label: '用户ID', width: 80 },
        { prop: 'exhibitionTitle', label: '展览名称', showOverflowTooltip: true },
        {
          prop: 'coverImage', label: '封面', width: 80,
          formatter: (row: any) => row.coverImage
            ? h(ElImage, { src: row.coverImage, style: 'width:50px;height:50px', fit: 'cover', previewSrcList: [row.coverImage], previewTeleported: true })
            : '-'
        },
        { prop: 'createTime', label: '收藏时间', width: 170 }
      ]
    }
  })
</script>
