<!-- 预约管理页面 -->
<template>
  <div class="reservation-page art-full-height">
    <ElCard class="art-table-card">
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElSpace wrap>
            <ElInput v-model="searchUsername" placeholder="搜索用户名" clearable style="width: 160px"
              @keyup.enter="handleSearch" />
            <ElSelect v-model="searchStatus" placeholder="状态" clearable style="width: 120px">
              <ElOption label="待确认" :value="0" />
              <ElOption label="已确认" :value="1" />
              <ElOption label="已取消" :value="2" />
              <ElOption label="已完成" :value="3" />
              <ElOption label="已过期" :value="4" />
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
  import { useTable } from '@/hooks/core/useTable'
  import { fetchReservationList, confirmReservation, completeReservation } from '@/api/gallery'
  import { ElTag, ElMessageBox, ElMessage, ElButton as ElBtn } from 'element-plus'

  defineOptions({ name: 'ReservationManage' })

  const STATUS_MAP: Record<number, { type: string; text: string }> = {
    0: { type: 'warning', text: '待确认' },
    1: { type: 'primary', text: '已确认' },
    2: { type: 'info', text: '已取消' },
    3: { type: 'success', text: '已完成' },
    4: { type: 'danger', text: '已过期' }
  }

  const searchUsername = ref('')
  const searchStatus = ref<number | undefined>()

  const {
    columns, columnChecks, data, loading, pagination, getData,
    handleSizeChange, handleCurrentChange, refreshData, replaceSearchParams
  } = useTable({
    core: {
      apiFn: fetchReservationList,
      apiParams: { page: 1, size: 10 },
      columnsFactory: () => [
        { type: 'index', width: 60, label: '序号' },
        { prop: 'contactName', label: '联系人', width: 100 },
        { prop: 'contactPhone', label: '联系电话', width: 120 },
        { prop: 'exhibitionTitle', label: '展览名称', showOverflowTooltip: true },
        { prop: 'reservationDate', label: '预约日期', width: 110 },
        { prop: 'numVisitors', label: '人数', width: 70 },
        {
          prop: 'status', label: '状态', width: 90,
          formatter: (row: any) => {
            const cfg = STATUS_MAP[row.status] || { type: 'info', text: '未知' }
            return h(ElTag, { type: cfg.type as any }, () => cfg.text)
          }
        },
        { prop: 'cancelReason', label: '取消原因', showOverflowTooltip: true },
        { prop: 'createTime', label: '预约时间', width: 170 },
        {
          prop: 'operation', label: '操作', width: 160, fixed: 'right',
          formatter: (row: any) => {
            const buttons: any[] = []
            if (row.status === 0) {
              buttons.push(h(ElBtn, { size: 'small', type: 'primary', link: true, onClick: () => handleConfirm(row) }, () => '确认'))
            }
            if (row.status === 1) {
              buttons.push(h(ElBtn, { size: 'small', type: 'success', link: true, onClick: () => handleComplete(row) }, () => '完成'))
            }
            return h('div', buttons)
          }
        }
      ]
    }
  })

  function handleSearch() {
    replaceSearchParams({
      username: searchUsername.value || undefined,
      status: searchStatus.value
    })
    getData()
  }

  function handleConfirm(row: any) {
    ElMessageBox.confirm(`确定要确认该预约吗？`, '确认预约', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'info'
    }).then(async () => {
      await confirmReservation(row.id)
      ElMessage.success('已确认')
      getData()
    }).catch(() => {})
  }

  function handleComplete(row: any) {
    ElMessageBox.confirm(`确定要标记该预约为已完成吗？`, '完成预约', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'info'
    }).then(async () => {
      await completeReservation(row.id)
      ElMessage.success('已完成')
      getData()
    }).catch(() => {})
  }
</script>
