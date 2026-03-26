<!-- 公告管理页面 -->
<template>
  <div class="announcement-page art-full-height">
    <ElCard class="art-table-card">
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElSpace wrap>
            <ElSelect v-model="searchStatus" placeholder="状态" clearable style="width: 120px" @change="handleSearch">
              <ElOption label="草稿" :value="0" />
              <ElOption label="已发布" :value="1" />
            </ElSelect>
            <ElButton type="primary" @click="handleSearch" v-ripple>查询</ElButton>
            <ElButton @click="showDialog('add')" v-ripple>新增公告</ElButton>
          </ElSpace>
        </template>
      </ArtTableHeader>

      <ArtTable :loading="loading" :data="data" :columns="columns" :pagination="pagination"
        @pagination:size-change="handleSizeChange" @pagination:current-change="handleCurrentChange">
      </ArtTable>

      <!-- 新增/编辑弹窗 -->
      <ElDialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增公告' : '编辑公告'" width="650px"
        destroy-on-close>
        <ElForm ref="formRef" :model="formData" :rules="formRules" label-width="80px">
          <ElFormItem label="公告标题" prop="title">
            <ElInput v-model="formData.title" placeholder="请输入公告标题" />
          </ElFormItem>
          <ElFormItem label="封面图" prop="coverImage">
            <ElInput v-model="formData.coverImage" placeholder="输入封面图URL" />
          </ElFormItem>
          <ElFormItem label="状态" prop="status">
            <ElSelect v-model="formData.status" style="width: 100%">
              <ElOption label="草稿" :value="0" />
              <ElOption label="已发布" :value="1" />
            </ElSelect>
          </ElFormItem>
          <ElFormItem label="是否置顶" prop="topFlag">
            <ElSwitch v-model="formData.topFlag" :active-value="1" :inactive-value="0" active-text="是"
              inactive-text="否" />
          </ElFormItem>
          <ElFormItem label="公告内容" prop="content">
            <ElInput v-model="formData.content" type="textarea" :rows="6" placeholder="请输入公告内容" />
          </ElFormItem>
        </ElForm>
        <template #footer>
          <ElButton @click="dialogVisible = false">取消</ElButton>
          <ElButton type="primary" :loading="submitLoading" @click="handleSubmit">确定</ElButton>
        </template>
      </ElDialog>
    </ElCard>
  </div>
</template>

<script setup lang="ts">
  import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
  import { useTable } from '@/hooks/core/useTable'
  import { fetchAnnouncementList, createAnnouncement, updateAnnouncement, deleteAnnouncement } from '@/api/gallery'
  import { ElTag, ElMessageBox, ElMessage, ElSwitch } from 'element-plus'
  import { DialogType } from '@/types'

  defineOptions({ name: 'AnnouncementManage' })

  const searchStatus = ref<number | undefined>()
  const dialogType = ref<DialogType>('add')
  const dialogVisible = ref(false)
  const submitLoading = ref(false)
  const formRef = ref()
  const currentId = ref<number | null>(null)

  const formData = ref({
    title: '', content: '', coverImage: '', status: 0, topFlag: 0
  })

  const formRules = {
    title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
    content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }]
  }

  const {
    columns, columnChecks, data, loading, pagination, getData,
    handleSizeChange, handleCurrentChange, refreshData, replaceSearchParams
  } = useTable({
    core: {
      apiFn: fetchAnnouncementList,
      apiParams: { page: 1, size: 10 },
      columnsFactory: () => [
        { type: 'index', width: 60, label: '序号' },
        { prop: 'title', label: '公告标题', showOverflowTooltip: true },
        {
          prop: 'status', label: '状态', width: 90,
          formatter: (row: any) => h(ElTag, { type: row.status === 1 ? 'success' : 'info' }, () => row.status === 1 ? '已发布' : '草稿')
        },
        {
          prop: 'topFlag', label: '置顶', width: 70,
          formatter: (row: any) => h(ElTag, { type: row.topFlag === 1 ? 'warning' : 'info' }, () => row.topFlag === 1 ? '是' : '否')
        },
        { prop: 'createTime', label: '创建时间', width: 170 },
        { prop: 'updateTime', label: '更新时间', width: 170 },
        {
          prop: 'operation', label: '操作', width: 120, fixed: 'right',
          formatter: (row: any) => h('div', [
            h(ArtButtonTable, { type: 'edit', onClick: () => showDialog('edit', row) }),
            h(ArtButtonTable, { type: 'delete', onClick: () => handleDelete(row) })
          ])
        }
      ]
    }
  })

  function handleSearch() {
    replaceSearchParams({ status: searchStatus.value })
    getData()
  }

  function showDialog(type: DialogType, row?: any) {
    dialogType.value = type
    if (type === 'edit' && row) {
      currentId.value = row.id
      formData.value = {
        title: row.title, content: row.content || '',
        coverImage: row.coverImage || '', status: row.status, topFlag: row.topFlag || 0
      }
    } else {
      currentId.value = null
      formData.value = { title: '', content: '', coverImage: '', status: 0, topFlag: 0 }
    }
    dialogVisible.value = true
  }

  async function handleSubmit() {
    await formRef.value?.validate()
    submitLoading.value = true
    try {
      if (dialogType.value === 'add') {
        await createAnnouncement(formData.value)
        ElMessage.success('新增成功')
      } else {
        await updateAnnouncement(currentId.value!, formData.value)
        ElMessage.success('更新成功')
      }
      dialogVisible.value = false
      getData()
    } catch (e) { /* error handled by http */ } finally {
      submitLoading.value = false
    }
  }

  function handleDelete(row: any) {
    ElMessageBox.confirm(`确定要删除公告「${row.title}」吗？`, '删除确认', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    }).then(async () => {
      await deleteAnnouncement(row.id)
      ElMessage.success('删除成功')
      getData()
    }).catch(() => {})
  }
</script>
