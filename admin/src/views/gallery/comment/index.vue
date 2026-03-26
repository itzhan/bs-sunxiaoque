<!-- 评论管理页面 -->
<template>
  <div class="comment-page art-full-height">
    <ElCard class="art-table-card">
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElSpace wrap>
            <ElSelect v-model="searchStatus" placeholder="审核状态" clearable style="width: 120px"
              @change="handleSearch">
              <ElOption label="待审核" :value="0" />
              <ElOption label="已通过" :value="1" />
              <ElOption label="已拒绝" :value="2" />
            </ElSelect>
            <ElButton type="primary" @click="handleSearch" v-ripple>查询</ElButton>
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
  import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
  import { useTable } from '@/hooks/core/useTable'
  import { fetchCommentList, approveComment, rejectComment, deleteComment } from '@/api/gallery'
  import { ElTag, ElMessageBox, ElMessage, ElButton as ElBtn, ElRate } from 'element-plus'

  defineOptions({ name: 'CommentManage' })

  const STATUS_MAP: Record<number, { type: string; text: string }> = {
    0: { type: 'warning', text: '待审核' },
    1: { type: 'success', text: '已通过' },
    2: { type: 'danger', text: '已拒绝' }
  }

  const searchStatus = ref<number | undefined>()

  const {
    columns, columnChecks, data, loading, pagination, getData,
    handleSizeChange, handleCurrentChange, refreshData, replaceSearchParams
  } = useTable({
    core: {
      apiFn: fetchCommentList,
      apiParams: { page: 1, size: 10 },
      columnsFactory: () => [
        { type: 'index', width: 60, label: '序号' },
        { prop: 'username', label: '用户', width: 100 },
        { prop: 'exhibitionTitle', label: '展览名称', width: 150, showOverflowTooltip: true },
        { prop: 'content', label: '评论内容', showOverflowTooltip: true },
        {
          prop: 'rating', label: '评分', width: 160,
          formatter: (row: any) => h(ElRate, { modelValue: row.rating, disabled: true, 'show-score': true })
        },
        {
          prop: 'status', label: '状态', width: 90,
          formatter: (row: any) => {
            const cfg = STATUS_MAP[row.status] || { type: 'info', text: '未知' }
            return h(ElTag, { type: cfg.type as any }, () => cfg.text)
          }
        },
        { prop: 'createTime', label: '评论时间', width: 170 },
        {
          prop: 'operation', label: '操作', width: 200, fixed: 'right',
          formatter: (row: any) => {
            const buttons: any[] = []
            if (row.status === 0) {
              buttons.push(h(ElBtn, { size: 'small', type: 'success', link: true, onClick: () => handleApprove(row) }, () => '通过'))
              buttons.push(h(ElBtn, { size: 'small', type: 'warning', link: true, onClick: () => handleReject(row) }, () => '拒绝'))
            }
            buttons.push(h(ArtButtonTable, { type: 'delete', onClick: () => handleDelete(row) }))
            return h('div', { style: 'display:flex;align-items:center;gap:4px' }, buttons)
          }
        }
      ]
    }
  })

  function handleSearch() {
    replaceSearchParams({ status: searchStatus.value })
    getData()
  }

  function handleApprove(row: any) {
    ElMessageBox.confirm(`确定要通过该评论吗？`, '审核确认', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'info'
    }).then(async () => {
      await approveComment(row.id)
      ElMessage.success('已通过')
      getData()
    }).catch(() => {})
  }

  function handleReject(row: any) {
    ElMessageBox.confirm(`确定要拒绝该评论吗？`, '审核确认', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    }).then(async () => {
      await rejectComment(row.id)
      ElMessage.success('已拒绝')
      getData()
    }).catch(() => {})
  }

  function handleDelete(row: any) {
    ElMessageBox.confirm(`确定要删除该评论吗？`, '删除确认', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    }).then(async () => {
      await deleteComment(row.id)
      ElMessage.success('删除成功')
      getData()
    }).catch(() => {})
  }
</script>
