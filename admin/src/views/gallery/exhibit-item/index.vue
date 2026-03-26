<!-- 展品管理页面 -->
<template>
  <div class="exhibit-item-page art-full-height">
    <ElCard class="art-table-card">
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElSpace wrap>
            <ElSelect v-model="searchExhibitionId" placeholder="选择展览" clearable filterable style="width: 220px"
              @change="handleSearch">
              <ElOption v-for="ex in exhibitionOptions" :key="ex.id" :label="ex.title" :value="ex.id" />
            </ElSelect>
            <ElButton type="primary" @click="handleSearch" v-ripple>查询</ElButton>
            <ElButton @click="showDialog('add')" v-ripple>新增展品</ElButton>
          </ElSpace>
        </template>
      </ArtTableHeader>

      <ArtTable :loading="loading" :data="data" :columns="columns" :pagination="pagination"
        @pagination:size-change="handleSizeChange" @pagination:current-change="handleCurrentChange">
      </ArtTable>

      <!-- 新增/编辑弹窗 -->
      <ElDialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增展品' : '编辑展品'" width="600px"
        destroy-on-close>
        <ElForm ref="formRef" :model="formData" :rules="formRules" label-width="100px">
          <ElFormItem label="所属展览" prop="exhibitionId">
            <ElSelect v-model="formData.exhibitionId" placeholder="选择展览" filterable style="width: 100%">
              <ElOption v-for="ex in exhibitionOptions" :key="ex.id" :label="ex.title" :value="ex.id" />
            </ElSelect>
          </ElFormItem>
          <ElFormItem label="展品名称" prop="name">
            <ElInput v-model="formData.name" placeholder="请输入展品名称" />
          </ElFormItem>
          <ElFormItem label="艺术家" prop="artist">
            <ElInput v-model="formData.artist" placeholder="请输入艺术家/作者" />
          </ElFormItem>
          <ElFormItem label="年代" prop="era">
            <ElInput v-model="formData.era" placeholder="请输入年代" />
          </ElFormItem>
          <ElFormItem label="展品图片" prop="coverImage">
            <ElInput v-model="formData.coverImage" placeholder="输入图片URL" />
          </ElFormItem>

          <ElFormItem label="排序" prop="sortOrder">
            <ElInputNumber v-model="formData.sortOrder" :min="0" />
          </ElFormItem>
          <ElFormItem label="展品描述" prop="description">
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
    fetchExhibitItemList, createExhibitItem, updateExhibitItem, deleteExhibitItem,
    fetchExhibitionList
  } from '@/api/gallery'
  import { ElMessageBox, ElMessage, ElImage } from 'element-plus'
  import { DialogType } from '@/types'

  defineOptions({ name: 'ExhibitItemManage' })

  const searchExhibitionId = ref<number | undefined>()
  const exhibitionOptions = ref<any[]>([])
  const dialogType = ref<DialogType>('add')
  const dialogVisible = ref(false)
  const submitLoading = ref(false)
  const formRef = ref()
  const currentId = ref<number | null>(null)

  const formData = ref({
    exhibitionId: undefined as number | undefined,
    name: '', artist: '', era: '', description: '',
    coverImage: '', sortOrder: 0
  })

  const formRules = {
    exhibitionId: [{ required: true, message: '请选择展览', trigger: 'change' }],
    name: [{ required: true, message: '请输入展品名称', trigger: 'blur' }]
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
      apiFn: fetchExhibitItemList,
      apiParams: { page: 1, size: 10, exhibitionId: undefined as any },
      columnsFactory: () => [
        { type: 'index', width: 60, label: '序号' },
        {
          prop: 'coverImage', label: '图片', width: 80,
          formatter: (row: any) => row.coverImage
            ? h(ElImage, { src: row.coverImage, style: 'width:50px;height:50px', fit: 'cover', previewSrcList: [row.coverImage], previewTeleported: true })
            : '-'
        },
        { prop: 'name', label: '展品名称', showOverflowTooltip: true },
        { prop: 'artist', label: '艺术家', width: 120 },
        { prop: 'era', label: '年代', width: 100 },
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
        exhibitionId: row.exhibitionId, name: row.name, artist: row.artist || '',
        era: row.era || '', description: row.description || '',
        coverImage: row.coverImage || '', sortOrder: row.sortOrder || 0
      }
    } else {
      currentId.value = null
      formData.value = {
        exhibitionId: searchExhibitionId.value, name: '', artist: '', era: '',
        description: '', coverImage: '', sortOrder: 0
      }
    }
    dialogVisible.value = true
  }

  async function handleSubmit() {
    await formRef.value?.validate()
    submitLoading.value = true
    try {
      if (dialogType.value === 'add') {
        await createExhibitItem(formData.value)
        ElMessage.success('新增成功')
      } else {
        await updateExhibitItem(currentId.value!, formData.value)
        ElMessage.success('更新成功')
      }
      dialogVisible.value = false
      getData()
    } catch (e) { /* error handled by http */ } finally {
      submitLoading.value = false
    }
  }

  function handleDelete(row: any) {
    ElMessageBox.confirm(`确定要删除展品「${row.name}」吗？`, '删除确认', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    }).then(async () => {
      await deleteExhibitItem(row.id)
      ElMessage.success('删除成功')
      getData()
    }).catch(() => {})
  }
</script>
