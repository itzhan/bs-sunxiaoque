<!-- 时间段管理页面 -->
<template>
  <div class="time-slot-page art-full-height">
    <ElCard class="art-table-card">
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElSpace wrap>
            <ElSelect v-model="searchExhibitionId" placeholder="选择展览" clearable filterable style="width: 220px"
              @change="handleSearch">
              <ElOption v-for="ex in exhibitionOptions" :key="ex.id" :label="ex.title" :value="ex.id" />
            </ElSelect>
            <ElButton type="primary" @click="handleSearch" v-ripple>查询</ElButton>
            <ElButton @click="showDialog('add')" v-ripple>新增时间段</ElButton>
          </ElSpace>
        </template>
      </ArtTableHeader>

      <ArtTable :loading="loading" :data="tableData" :columns="columns">
      </ArtTable>

      <!-- 新增/编辑弹窗 -->
      <ElDialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增时间段' : '编辑时间段'" width="500px"
        destroy-on-close>
        <ElForm ref="formRef" :model="formData" :rules="formRules" label-width="100px">
          <ElFormItem label="所属展览" prop="exhibitionId">
            <ElSelect v-model="formData.exhibitionId" placeholder="选择展览" filterable style="width: 100%">
              <ElOption v-for="ex in exhibitionOptions" :key="ex.id" :label="ex.title" :value="ex.id" />
            </ElSelect>
          </ElFormItem>

          <ElFormItem label="开始时间" prop="startTime">
            <ElTimePicker v-model="formData.startTime" format="HH:mm" value-format="HH:mm:ss"
              placeholder="选择开始时间" style="width: 100%" />
          </ElFormItem>
          <ElFormItem label="结束时间" prop="endTime">
            <ElTimePicker v-model="formData.endTime" format="HH:mm" value-format="HH:mm:ss"
              placeholder="选择结束时间" style="width: 100%" />
          </ElFormItem>
          <ElFormItem label="最大人数" prop="maxCapacity">
            <ElInputNumber v-model="formData.maxCapacity" :min="1" style="width: 100%" />
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
  import {
    fetchTimeSlotList, createTimeSlot, updateTimeSlot, deleteTimeSlot,
    fetchExhibitionList
  } from '@/api/gallery'
  import { ElMessageBox, ElMessage } from 'element-plus'
  import { DialogType } from '@/types'

  defineOptions({ name: 'TimeSlotManage' })

  const searchExhibitionId = ref<number | undefined>()
  const exhibitionOptions = ref<any[]>([])
  const tableData = ref<any[]>([])
  const loading = ref(false)
  const dialogType = ref<DialogType>('add')
  const dialogVisible = ref(false)
  const submitLoading = ref(false)
  const formRef = ref()
  const currentId = ref<number | null>(null)

  const formData = ref({
    exhibitionId: undefined as number | undefined,
    startTime: '', endTime: '', maxCapacity: 50
  })

  const formRules = {
    exhibitionId: [{ required: true, message: '请选择展览', trigger: 'change' }],
    startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
    endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
  }

  // 列定义（不使用 useTable，因为时间段接口不分页）
  const columns = ref([
    { type: 'index' as const, width: 60, label: '序号' },
    { prop: 'slotName', label: '时段名称', formatter: (row: any) => `${(row.startTime || '').slice(0,5)} - ${(row.endTime || '').slice(0,5)}` },
    { prop: 'startTime', label: '开始时间', width: 120 },
    { prop: 'endTime', label: '结束时间', width: 120 },
    { prop: 'maxCapacity', label: '最大人数', width: 100 },
    { prop: 'createTime', label: '创建时间', width: 170 },
    {
      prop: 'operation', label: '操作', width: 120, fixed: 'right' as const,
      formatter: (row: any) => h('div', [
        h(ArtButtonTable, { type: 'edit', onClick: () => showDialog('edit', row) }),
        h(ArtButtonTable, { type: 'delete', onClick: () => handleDelete(row) })
      ])
    }
  ])
  const columnChecks = ref(columns.value.map((_, i) => ({ key: String(i), checked: true })))

  onMounted(async () => {
    try {
      const res = await fetchExhibitionList({ page: 1, size: 999 })
      exhibitionOptions.value = (res as any)?.records || []
    } catch (e) { /* ignore */ }
  })

  async function loadData() {
    if (!searchExhibitionId.value) {
      tableData.value = []
      return
    }
    loading.value = true
    try {
      const res = await fetchTimeSlotList({ exhibitionId: searchExhibitionId.value })
      tableData.value = (res as any) || []
    } catch (e) { tableData.value = [] } finally {
      loading.value = false
    }
  }

  function refreshData() { loadData() }

  function handleSearch() { loadData() }

  function showDialog(type: DialogType, row?: any) {
    dialogType.value = type
    if (type === 'edit' && row) {
      currentId.value = row.id
      formData.value = {
        exhibitionId: row.exhibitionId,
        startTime: row.startTime, endTime: row.endTime, maxCapacity: row.maxCapacity
      }
    } else {
      currentId.value = null
      formData.value = {
        exhibitionId: searchExhibitionId.value,
        startTime: '', endTime: '', maxCapacity: 50
      }
    }
    dialogVisible.value = true
  }

  async function handleSubmit() {
    await formRef.value?.validate()
    submitLoading.value = true
    try {
      const payload = {
        ...formData.value,
        slotName: `${(formData.value.startTime || '').slice(0,5)}-${(formData.value.endTime || '').slice(0,5)}`
      }
      if (dialogType.value === 'add') {
        await createTimeSlot(payload)
        ElMessage.success('新增成功')
      } else {
        await updateTimeSlot(currentId.value!, payload)
        ElMessage.success('更新成功')
      }
      dialogVisible.value = false
      loadData()
    } catch (e) { /* error handled by http */ } finally {
      submitLoading.value = false
    }
  }

  function handleDelete(row: any) {
    ElMessageBox.confirm(`确定要删除时间段「${(row.startTime || '').slice(0,5)} - ${(row.endTime || '').slice(0,5)}」吗？`, '删除确认', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    }).then(async () => {
      await deleteTimeSlot(row.id)
      ElMessage.success('删除成功')
      loadData()
    }).catch(() => {})
  }
</script>
