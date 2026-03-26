<!-- 展览管理页面 -->
<template>
  <div class="exhibition-page art-full-height">
    <ElCard class="art-table-card">
      <ArtTableHeader v-model:columns="columnChecks" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElSpace wrap>
            <ElInput v-model="searchTitle" placeholder="搜索展览标题" clearable style="width: 180px"
              @keyup.enter="handleSearch" />
            <ElSelect v-model="searchCategoryId" placeholder="选择分类" clearable style="width: 140px">
              <ElOption v-for="cat in categoryOptions" :key="cat.id" :label="cat.name" :value="cat.id" />
            </ElSelect>
            <ElSelect v-model="searchStatus" placeholder="状态" clearable style="width: 120px">
              <ElOption label="草稿" :value="0" />
              <ElOption label="即将开展" :value="1" />
              <ElOption label="展出中" :value="2" />
              <ElOption label="已结束" :value="3" />
            </ElSelect>
            <ElButton type="primary" @click="handleSearch" v-ripple>查询</ElButton>
            <ElButton @click="showDialog('add')" v-ripple>新增展览</ElButton>
          </ElSpace>
        </template>
      </ArtTableHeader>

      <ArtTable :loading="loading" :data="data" :columns="columns" :pagination="pagination"
        @pagination:size-change="handleSizeChange" @pagination:current-change="handleCurrentChange">
      </ArtTable>

      <!-- 新增/编辑弹窗 -->
      <ElDialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增展览' : '编辑展览'" width="750px"
        destroy-on-close>
        <ElForm ref="formRef" :model="formData" :rules="formRules" label-width="100px">
          <ElRow :gutter="16">
            <ElCol :span="12">
              <ElFormItem label="展览标题" prop="title">
                <ElInput v-model="formData.title" placeholder="请输入标题" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="分类" prop="categoryId">
                <ElSelect v-model="formData.categoryId" placeholder="选择分类" style="width: 100%">
                  <ElOption v-for="cat in categoryOptions" :key="cat.id" :label="cat.name" :value="cat.id" />
                </ElSelect>
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="展览地点" prop="location">
                <ElInput v-model="formData.location" placeholder="请输入地点" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="展厅名称" prop="hallName">
                <ElInput v-model="formData.hallName" placeholder="请输入展厅" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="开始日期" prop="startDate">
                <ElDatePicker v-model="formData.startDate" type="date" value-format="YYYY-MM-DD"
                  placeholder="选择日期" style="width: 100%" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="结束日期" prop="endDate">
                <ElDatePicker v-model="formData.endDate" type="date" value-format="YYYY-MM-DD"
                  placeholder="选择日期" style="width: 100%" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="票价" prop="ticketPrice">
                <ElInputNumber v-model="formData.ticketPrice" :min="0" :precision="2" :step="10"
                  style="width: 100%" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="封面图" prop="coverImage">
                <ElInput v-model="formData.coverImage" placeholder="输入封面图URL" />
              </ElFormItem>
            </ElCol>
            <ElCol :span="12">
              <ElFormItem label="状态" prop="status">
                <ElSelect v-model="formData.status" style="width: 100%">
                  <ElOption label="草稿" :value="0" />
                  <ElOption label="即将开展" :value="1" />
                  <ElOption label="展出中" :value="2" />
                  <ElOption label="已结束" :value="3" />
                </ElSelect>
              </ElFormItem>
            </ElCol>
            <ElCol :span="24">
              <ElFormItem label="展览描述" prop="description">
                <ElInput v-model="formData.description" type="textarea" :rows="3" placeholder="请输入描述" />
              </ElFormItem>
            </ElCol>
          </ElRow>

          <!-- 时间段管理区域 -->
          <ElDivider content-position="left">参观时间段</ElDivider>
          <div style="margin-bottom: 16px;">
            <ElTable :data="formData.timeSlots" border size="small" style="width: 100%">
              <ElTableColumn label="开始时间" min-width="140">
                <template #default="{ row }">
                  <ElTimePicker v-model="row.startTime" format="HH:mm" value-format="HH:mm:ss"
                    placeholder="开始时间" style="width: 100%" size="small" />
                </template>
              </ElTableColumn>
              <ElTableColumn label="结束时间" min-width="140">
                <template #default="{ row }">
                  <ElTimePicker v-model="row.endTime" format="HH:mm" value-format="HH:mm:ss"
                    placeholder="结束时间" style="width: 100%" size="small" />
                </template>
              </ElTableColumn>
              <ElTableColumn label="最大人数" width="120">
                <template #default="{ row }">
                  <ElInputNumber v-model="row.maxCapacity" :min="1" size="small" style="width: 100%" controls-position="right" />
                </template>
              </ElTableColumn>
              <ElTableColumn label="操作" width="60" align="center">
                <template #default="{ $index }">
                  <ElButton type="danger" link size="small" @click="removeTimeSlot($index)">
                    <ElIcon><Delete /></ElIcon>
                  </ElButton>
                </template>
              </ElTableColumn>
            </ElTable>
            <ElButton style="margin-top: 8px;" size="small" @click="addTimeSlot">
              <ElIcon style="margin-right: 4px;"><Plus /></ElIcon> 添加时间段
            </ElButton>
          </div>
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
    fetchExhibitionList, createExhibition, updateExhibition, deleteExhibition,
    fetchAllCategories, fetchTimeSlotList, createTimeSlot, updateTimeSlot, deleteTimeSlot
  } from '@/api/gallery'
  import { ElTag, ElMessageBox, ElMessage } from 'element-plus'
  import { Delete, Plus } from '@element-plus/icons-vue'
  import { DialogType } from '@/types'

  defineOptions({ name: 'ExhibitionManage' })

  const STATUS_MAP: Record<number, { type: string; text: string }> = {
    0: { type: 'info', text: '草稿' },
    1: { type: 'warning', text: '即将开展' },
    2: { type: 'success', text: '展出中' },
    3: { type: 'danger', text: '已结束' }
  }

  interface SlotItem {
    id?: number
    startTime: string
    endTime: string
    maxCapacity: number
    _isNew?: boolean  // 标记新增的时间段
  }

  const searchTitle = ref('')
  const searchCategoryId = ref<number | undefined>()
  const searchStatus = ref<number | undefined>()
  const categoryOptions = ref<any[]>([])
  const dialogType = ref<DialogType>('add')
  const dialogVisible = ref(false)
  const submitLoading = ref(false)
  const formRef = ref()
  const currentId = ref<number | null>(null)
  // 用于编辑时记录原始时间段ID列表，以便计算需要删除的
  const originalSlotIds = ref<number[]>([])

  const formData = ref({
    title: '', description: '', coverImage: '', categoryId: undefined as number | undefined,
    location: '', hallName: '', startDate: '', endDate: '',
    ticketPrice: 0, status: 0,
    timeSlots: [] as SlotItem[]
  })

  const formRules = {
    title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
    startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
    endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
  }

  function addTimeSlot() {
    formData.value.timeSlots.push({ startTime: '', endTime: '', maxCapacity: 50, _isNew: true })
  }

  function removeTimeSlot(index: number) {
    formData.value.timeSlots.splice(index, 1)
  }

  onMounted(async () => {
    try {
      const res = await fetchAllCategories()
      categoryOptions.value = res || []
    } catch (e) { /* ignore */ }
  })

  const {
    columns, columnChecks, data, loading, pagination, getData,
    handleSizeChange, handleCurrentChange, refreshData, replaceSearchParams
  } = useTable({
    core: {
      apiFn: fetchExhibitionList,
      apiParams: { page: 1, size: 10 },
      columnsFactory: () => [
        { type: 'index', width: 60, label: '序号' },
        { prop: 'title', label: '展览标题', showOverflowTooltip: true },
        {
          prop: 'categoryId', label: '分类', width: 100,
          formatter: (row: any) => {
            const cat = categoryOptions.value.find((c: any) => c.id === row.categoryId)
            return cat ? cat.name : '-'
          }
        },
        { prop: 'location', label: '地点', width: 120, showOverflowTooltip: true },
        { prop: 'startDate', label: '开始日期', width: 110 },
        { prop: 'endDate', label: '结束日期', width: 110 },
        { prop: 'ticketPrice', label: '票价', width: 80, formatter: (row: any) => `¥${row.ticketPrice}` },
        { prop: 'viewCount', label: '浏览量', width: 80 },
        {
          prop: 'status', label: '状态', width: 100,
          formatter: (row: any) => {
            const cfg = STATUS_MAP[row.status] || { type: 'info', text: '未知' }
            return h(ElTag, { type: cfg.type as any }, () => cfg.text)
          }
        },
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
    replaceSearchParams({
      title: searchTitle.value || undefined,
      categoryId: searchCategoryId.value,
      status: searchStatus.value
    })
    getData()
  }

  async function showDialog(type: DialogType, row?: any) {
    dialogType.value = type
    if (type === 'edit' && row) {
      currentId.value = row.id
      formData.value = {
        title: row.title, description: row.description || '', coverImage: row.coverImage || '',
        categoryId: row.categoryId, location: row.location || '', hallName: row.hallName || '',
        startDate: row.startDate, endDate: row.endDate,
        ticketPrice: row.ticketPrice, status: row.status,
        timeSlots: []
      }
      // 加载该展览已有的时间段
      try {
        const slots = await fetchTimeSlotList({ exhibitionId: row.id })
        const list = (slots as any) || []
        originalSlotIds.value = list.map((s: any) => s.id)
        formData.value.timeSlots = list.map((s: any) => ({
          id: s.id,
          startTime: s.startTime,
          endTime: s.endTime,
          maxCapacity: s.maxCapacity,
          _isNew: false
        }))
      } catch (e) { /* ignore */ }
    } else {
      currentId.value = null
      originalSlotIds.value = []
      formData.value = {
        title: '', description: '', coverImage: '', categoryId: undefined,
        location: '', hallName: '', startDate: '', endDate: '',
        ticketPrice: 0, status: 0,
        timeSlots: []
      }
    }
    dialogVisible.value = true
  }

  async function handleSubmit() {
    await formRef.value?.validate()
    submitLoading.value = true
    try {
      const { timeSlots, ...exhibitionData } = formData.value
      let exhibitionId: number

      if (dialogType.value === 'add') {
        const res = await createExhibition(exhibitionData)
        exhibitionId = (res as any)?.id || (res as any)?.data?.id
        ElMessage.success('新增成功')
      } else {
        exhibitionId = currentId.value!
        await updateExhibition(exhibitionId, exhibitionData)
        ElMessage.success('更新成功')
      }

      // 同步时间段
      if (exhibitionId) {
        await syncTimeSlots(exhibitionId, timeSlots)
      }

      dialogVisible.value = false
      getData()
    } catch (e) { /* error handled by http */ } finally {
      submitLoading.value = false
    }
  }

  async function syncTimeSlots(exhibitionId: number, slots: SlotItem[]) {
    // 1. 计算需要删除的时间段（原始有但现在没有了）
    const currentIds = slots.filter(s => s.id).map(s => s.id!)
    const toDelete = originalSlotIds.value.filter(id => !currentIds.includes(id))

    // 2. 执行删除
    for (const id of toDelete) {
      try { await deleteTimeSlot(id) } catch {}
    }

    // 3. 新增或更新
    for (const slot of slots) {
      const slotName = `${(slot.startTime || '').slice(0, 5)}-${(slot.endTime || '').slice(0, 5)}`
      const payload = {
        exhibitionId,
        slotName,
        startTime: slot.startTime,
        endTime: slot.endTime,
        maxCapacity: slot.maxCapacity
      }
      if (slot.id && !slot._isNew) {
        // 更新已有的
        try { await updateTimeSlot(slot.id, payload) } catch {}
      } else {
        // 新增
        try { await createTimeSlot(payload) } catch {}
      }
    }
  }

  function handleDelete(row: any) {
    ElMessageBox.confirm(`确定要删除展览「${row.title}」吗？`, '删除确认', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    }).then(async () => {
      await deleteExhibition(row.id)
      ElMessage.success('删除成功')
      getData()
    }).catch(() => {})
  }
</script>
