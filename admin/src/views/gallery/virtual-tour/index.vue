<!-- 虚拟导览管理页面 -->
<template>
  <div class="virtual-tour-page art-full-height">
    <ElCard class="art-table-card">
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElSpace wrap>
            <ElSelect v-model="searchExhibitionId" placeholder="选择展览" clearable filterable style="width: 220px"
              @change="handleSearch">
              <ElOption v-for="ex in exhibitionOptions" :key="ex.id" :label="ex.title" :value="ex.id" />
            </ElSelect>
            <ElButton type="primary" @click="handleSearch" v-ripple>查询</ElButton>
            <ElButton @click="showDialog('add')" v-ripple>新增导览</ElButton>
          </ElSpace>
        </template>
      </ArtTableHeader>

      <ArtTable :loading="loading" :data="data" :columns="columns" :pagination="pagination"
        @pagination:size-change="handleSizeChange" @pagination:current-change="handleCurrentChange">
      </ArtTable>

      <!-- 新增/编辑弹窗 -->
      <ElDialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增导览' : '编辑导览'" width="600px"
        destroy-on-close>
        <ElForm ref="formRef" :model="formData" :rules="formRules" label-width="100px">
          <ElFormItem label="所属展览" prop="exhibitionId">
            <ElSelect v-model="formData.exhibitionId" placeholder="选择展览" filterable style="width: 100%">
              <ElOption v-for="ex in exhibitionOptions" :key="ex.id" :label="ex.title" :value="ex.id" />
            </ElSelect>
          </ElFormItem>
          <ElFormItem label="导览标题" prop="title">
            <ElInput v-model="formData.title" placeholder="请输入导览标题" />
          </ElFormItem>
          <ElFormItem label="类型" prop="tourType">
            <ElSelect v-model="formData.tourType" style="width: 100%">
              <ElOption label="全景" :value="0" />
              <ElOption label="3D模型" :value="1" />
            </ElSelect>
          </ElFormItem>
          <ElFormItem label="全景资源URL" prop="panoramaUrl">
            <ElInput v-model="formData.panoramaUrl" placeholder="请输入全景图/3D资源URL" />
          </ElFormItem>
          <ElFormItem label="缩略图URL" prop="thumbnail">
            <ElInput v-model="formData.thumbnail" placeholder="请输入缩略图URL" />
          </ElFormItem>
          <ElFormItem label="排序" prop="sortOrder">
            <ElInputNumber v-model="formData.sortOrder" :min="0" />
          </ElFormItem>
          <ElFormItem label="导览描述" prop="description">
            <ElInput v-model="formData.description" type="textarea" :rows="3" placeholder="请输入描述" />
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
  import {
    fetchVirtualTourList, createVirtualTour, updateVirtualTour, deleteVirtualTour,
    fetchExhibitionList
  } from '@/api/gallery'
  import { ElTag, ElMessageBox, ElMessage } from 'element-plus'
  import { DialogType } from '@/types'

  defineOptions({ name: 'VirtualTourManage' })

  const TOUR_TYPE_MAP: Record<number, { type: string; text: string }> = {
    0: { type: 'primary', text: '全景' },
    1: { type: 'success', text: '3D模型' }
  }

  const searchExhibitionId = ref<number | undefined>()
  const exhibitionOptions = ref<any[]>([])
  const dialogType = ref<DialogType>('add')
  const dialogVisible = ref(false)
  const submitLoading = ref(false)
  const formRef = ref()
  const currentId = ref<number | null>(null)

  const formData = ref({
    exhibitionId: undefined as number | undefined,
    title: '', description: '', panoramaUrl: '', thumbnail: '',
    tourType: 0, sortOrder: 0
  })

  const formRules = {
    exhibitionId: [{ required: true, message: '请选择展览', trigger: 'change' }],
    title: [{ required: true, message: '请输入导览标题', trigger: 'blur' }],
    panoramaUrl: [{ required: true, message: '请输入资源URL', trigger: 'blur' }]
  }

  onMounted(async () => {
    try {
      const res = await fetchExhibitionList({ page: 1, size: 999 })
      exhibitionOptions.value = (res as any)?.records || []
    } catch (e) { /* ignore */ }
  })

  const {
    columns, columnChecks, data, loading, pagination, getData,
    handleSizeChange, handleCurrentChange, refreshData, replaceSearchParams
  } = useTable({
    core: {
      apiFn: fetchVirtualTourList,
      apiParams: { page: 1, size: 10, exhibitionId: undefined as any },
      columnsFactory: () => [
        { type: 'index', width: 60, label: '序号' },
        { prop: 'title', label: '导览标题', showOverflowTooltip: true },
        {
          prop: 'tourType', label: '类型', width: 90,
          formatter: (row: any) => {
            const cfg = TOUR_TYPE_MAP[row.tourType] || { type: 'info', text: '未知' }
            return h(ElTag, { type: cfg.type as any }, () => cfg.text)
          }
        },
        { prop: 'panoramaUrl', label: '资源URL', showOverflowTooltip: true },
        { prop: 'sortOrder', label: '排序', width: 70 },
        { prop: 'createTime', label: '创建时间', width: 170 },
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
    replaceSearchParams({ exhibitionId: searchExhibitionId.value })
    getData()
  }

  function showDialog(type: DialogType, row?: any) {
    dialogType.value = type
    if (type === 'edit' && row) {
      currentId.value = row.id
      formData.value = {
        exhibitionId: row.exhibitionId, title: row.title, description: row.description || '',
        panoramaUrl: row.panoramaUrl, thumbnail: row.thumbnail || '',
        tourType: row.tourType, sortOrder: row.sortOrder || 0
      }
    } else {
      currentId.value = null
      formData.value = {
        exhibitionId: searchExhibitionId.value, title: '', description: '',
        panoramaUrl: '', thumbnail: '', tourType: 0, sortOrder: 0
      }
    }
    dialogVisible.value = true
  }

  async function handleSubmit() {
    await formRef.value?.validate()
    submitLoading.value = true
    try {
      if (dialogType.value === 'add') {
        await createVirtualTour(formData.value)
        ElMessage.success('新增成功')
      } else {
        await updateVirtualTour(currentId.value!, formData.value)
        ElMessage.success('更新成功')
      }
      dialogVisible.value = false
      getData()
    } catch (e) { /* error handled by http */ } finally {
      submitLoading.value = false
    }
  }

  function handleDelete(row: any) {
    ElMessageBox.confirm(`确定要删除导览「${row.title}」吗？`, '删除确认', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    }).then(async () => {
      await deleteVirtualTour(row.id)
      ElMessage.success('删除成功')
      getData()
    }).catch(() => {})
  }
</script>
