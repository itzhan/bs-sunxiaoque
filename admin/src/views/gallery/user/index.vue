<!-- 用户管理页面（管理端业务版） -->
<template>
  <div class="user-manage-page art-full-height">
    <ElCard class="art-table-card">
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElSpace wrap>
            <ElInput v-model="searchKeyword" placeholder="搜索用户名/昵称" clearable style="width: 180px"
              @keyup.enter="handleSearch" />
            <ElSelect v-model="searchStatus" placeholder="状态" clearable style="width: 120px">
              <ElOption label="正常" :value="1" />
              <ElOption label="禁用" :value="0" />
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
  import { fetchUserList, updateUserStatus, resetUserPassword } from '@/api/gallery'
  import { ElTag, ElMessageBox, ElMessage, ElButton as ElBtn, ElImage } from 'element-plus'

  defineOptions({ name: 'UserManage' })

  const GENDER_MAP: Record<number, string> = { 0: '未知', 1: '男', 2: '女' }
  const ROLE_MAP: Record<number, { type: string; text: string }> = {
    0: { type: 'danger', text: '管理员' },
    1: { type: 'primary', text: '普通用户' }
  }

  const searchKeyword = ref('')
  const searchStatus = ref<number | undefined>()

  const {
    columns, columnChecks, data, loading, pagination, getData,
    handleSizeChange, handleCurrentChange, refreshData, replaceSearchParams
  } = useTable({
    core: {
      apiFn: fetchUserList,
      apiParams: { page: 1, size: 10 },
      columnsFactory: () => [
        { type: 'index', width: 60, label: '序号' },
        {
          prop: 'username', label: '用户信息', width: 220,
          formatter: (row: any) => h('div', { style: 'display:flex;align-items:center;gap:8px' }, [
            row.avatar ? h(ElImage, { src: row.avatar, style: 'width:36px;height:36px;border-radius:6px', fit: 'cover', previewSrcList: [row.avatar], previewTeleported: true }) : null,
            h('div', [
              h('div', { style: 'font-weight:500' }, row.username),
              h('div', { style: 'font-size:12px;color:#999' }, row.nickname)
            ])
          ])
        },
        { prop: 'phone', label: '手机号', width: 120 },
        { prop: 'email', label: '邮箱', width: 160, showOverflowTooltip: true },
        {
          prop: 'gender', label: '性别', width: 70,
          formatter: (row: any) => GENDER_MAP[row.gender] || '未知'
        },
        {
          prop: 'role', label: '角色', width: 100,
          formatter: (row: any) => {
            const cfg = ROLE_MAP[row.role] || { type: 'info', text: '未知' }
            return h(ElTag, { type: cfg.type as any }, () => cfg.text)
          }
        },
        {
          prop: 'status', label: '状态', width: 80,
          formatter: (row: any) => h(ElTag, { type: row.status === 1 ? 'success' : 'danger' }, () => row.status === 1 ? '正常' : '禁用')
        },
        { prop: 'createTime', label: '注册时间', width: 170 },
        {
          prop: 'operation', label: '操作', width: 200, fixed: 'right',
          formatter: (row: any) => h('div', { style: 'display:flex;align-items:center;gap:4px' }, [
            h(ElBtn, {
              size: 'small', type: row.status === 1 ? 'warning' : 'success', link: true,
              onClick: () => handleToggleStatus(row)
            }, () => row.status === 1 ? '禁用' : '启用'),
            h(ElBtn, {
              size: 'small', type: 'info', link: true,
              onClick: () => handleResetPwd(row)
            }, () => '重置密码')
          ])
        }
      ]
    }
  })

  function handleSearch() {
    replaceSearchParams({
      keyword: searchKeyword.value || undefined,
      status: searchStatus.value
    })
    getData()
  }

  function handleToggleStatus(row: any) {
    const newStatus = row.status === 1 ? 0 : 1
    const action = newStatus === 0 ? '禁用' : '启用'
    ElMessageBox.confirm(`确定要${action}用户「${row.username}」吗？`, `${action}确认`, {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    }).then(async () => {
      await updateUserStatus(row.id, newStatus)
      ElMessage.success(`${action}成功`)
      getData()
    }).catch(() => {})
  }

  function handleResetPwd(row: any) {
    ElMessageBox.confirm(`确定要重置用户「${row.username}」的密码吗？`, '重置密码', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    }).then(async () => {
      await resetUserPassword(row.id)
      ElMessage.success('密码已重置')
    }).catch(() => {})
  }
</script>
