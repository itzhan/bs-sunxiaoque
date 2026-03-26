<!-- 分类管理页面 -->
<template>
  <div class="category-page art-full-height">
    <ElCard class="art-table-card">
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElSpace wrap>
            <ElInput v-model="searchKeyword" placeholder="搜索分类名称" clearable style="width: 200px"
              @keyup.enter="handleSearch" />
            <ElButton type="primary" @click="handleSearch" v-ripple>查询</ElButton>
            <ElButton @click="showDialog('add')" v-ripple>新增分类</ElButton>
          </ElSpace>
        </template>
      </ArtTableHeader>

      <ArtTable :loading="loading" :data="data" :columns="columns" :pagination="pagination"
        @pagination:size-change="handleSizeChange" @pagination:current-change="handleCurrentChange">
      </ArtTable>

      <!-- 新增/编辑弹窗 -->
      <ElDialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增分类' : '编辑分类'" width="500px" destroy-on-close>
        <ElForm ref="formRef" :model="formData" :rules="formRules" label-width="80px">
          <ElFormItem label="分类名称" prop="name">
            <ElInput v-model="formData.name" placeholder="请输入分类名称" />
          </ElFormItem>
          <ElFormItem label="分类描述" prop="description">
            <ElInput v-model="formData.description" type="textarea" :rows="3" placeholder="请输入分类描述" />
          </ElFormItem>
          <ElFormItem label="排序" prop="sortOrder">
            <ElInputNumber v-model="formData.sortOrder" :min="0" />
          </ElFormItem>
          <ElFormItem label="状态" prop="status">
            <ElSwitch v-model="formData.status" :active-value="1" :inactive-value="0" active-text="正常"
              inactive-text="禁用" />
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
  import { fetchCategoryList, createCategory, updateCategory, deleteCategory } from '@/api/gallery'
  import { ElTag, ElMessageBox, ElMessage, ElSwitch } from 'element-plus'
  import { DialogType } from '@/types'

  defineOptions({ name: 'CategoryManage' })

  const searchKeyword = ref('')
  const dialogType = ref<DialogType>('add')
  const dialogVisible = ref(false)
  const submitLoading = ref(false)
  const formRef = ref()
  const currentId = ref<number | null>(null)

  const formData = ref({
    name: '',
    description: '',
    sortOrder: 0,
    status: 1
  })

  const formRules = {
    name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
  }

  const {
    columns, columnChecks, data, loading, pagination, getData,
    handleSizeChange, handleCurrentChange, refreshData, replaceSearchParams
  } = useTable({
    core: {
      apiFn: fetchCategoryList,
      apiParams: { page: 1, size: 10 },
      columnsFactory: () => [
        { type: 'index', width: 60, label: '序号' },
        { prop: 'name', label: '分类名称' },
        { prop: 'description', label: '分类描述', showOverflowTooltip: true },
        { prop: 'sortOrder', label: '排序', width: 80 },
        {
          prop: 'status', label: '状态', width: 80,
          formatter: (row: any) => h(ElTag, { type: row.status === 1 ? 'success' : 'danger' }, () => row.status === 1 ? '正常' : '禁用')
        },
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
    replaceSearchParams({ keyword: searchKeyword.value })
    getData()
  }

  function showDialog(type: DialogType, row?: any) {
    dialogType.value = type
    if (type === 'edit' && row) {
      currentId.value = row.id
      formData.value = { name: row.name, description: row.description || '', sortOrder: row.sortOrder || 0, status: row.status }
    } else {
      currentId.value = null
      formData.value = { name: '', description: '', sortOrder: 0, status: 1 }
    }
    dialogVisible.value = true
  }

  async function handleSubmit() {
    await formRef.value?.validate()
    submitLoading.value = true
    try {
      if (dialogType.value === 'add') {
        await createCategory(formData.value)
        ElMessage.success('新增成功')
      } else {
        await updateCategory(currentId.value!, formData.value)
        ElMessage.success('更新成功')
      }
      dialogVisible.value = false
      getData()
    } catch (e) { /* error handled by http */ } finally {
      submitLoading.value = false
    }
  }

  function handleDelete(row: any) {
    ElMessageBox.confirm(`确定要删除分类「${row.name}」吗？`, '删除确认', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    }).then(async () => {
      await deleteCategory(row.id)
      ElMessage.success('删除成功')
      getData()
    }).catch(() => {})
  }
</script>
