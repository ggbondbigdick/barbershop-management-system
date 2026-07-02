<template>
  <el-dialog
      :model-value="reserveViewBoxVisible"
      title="预约信息"
      width="580px"
      :show-close="false"
      :close-on-click-modal="false"
      center
  >
    <div class="dialog-body">
      <el-skeleton v-if="loading" animated :rows="6" />

      <div v-else class="content-wrapper">
        <el-image
            :src="reserveViewBoxForm.relatedThumbnail"
            fit="cover"
            class="project-poster"
        >
          <template #error>
            <div class="image-slot">加载失败</div>
          </template>
        </el-image>

        <el-descriptions :column="1" border class="info-table">
          <el-descriptions-item label="预约人" label-class-name="info-label">
            {{ reserveViewBoxForm.reservePerson }}
          </el-descriptions-item>

          <el-descriptions-item label="项目名称" label-class-name="info-label">
            {{ reserveViewBoxForm.relatedName }}
          </el-descriptions-item>

          <el-descriptions-item label="价格" label-class-name="info-label">
            ￥ {{ reserveViewBoxForm.relatedPrice }} RMB
          </el-descriptions-item>

          <el-descriptions-item label="预约时间" label-class-name="info-label">
            {{ reserveViewBoxForm.reserveTime }}
          </el-descriptions-item>

          <el-descriptions-item label="指定理发师" label-class-name="info-label">
            {{ reserveViewBoxForm.reserveStaffName || '门店安排' }}
          </el-descriptions-item>

          <el-descriptions-item label="状态" label-class-name="info-label">
            <el-tag :type="stateTagType">{{ stateLabel }}</el-tag>
          </el-descriptions-item>

          <el-descriptions-item
              v-if="reserveViewBoxForm.remark"
              label="备注"
              label-class-name="info-label"
          >
            {{ reserveViewBoxForm.remark }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancel">关闭</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import ReserveApi from '@/api/ReserveApi'

interface ReserveForm {
  id: string
  relatedThumbnail: string
  reservePerson: string
  relatedName: string
  relatedPrice: string | number
  reserveTime: string
  reserveStaffName?: string
  state: number | string
  remark: string
}

defineProps(['reserveViewBoxVisible'])
const emit = defineEmits(['close'])

const loading = ref(false)
const reserveViewBoxForm = reactive<ReserveForm>({
  id: '',
  relatedThumbnail: '',
  reservePerson: '',
  relatedName: '',
  relatedPrice: '',
  reserveTime: '',
  reserveStaffName: '',
  state: 0,
  remark: ''
})

const stateLabel = computed(() => {
  const stateMap: Record<string, string> = {
    1: '预约中',
    2: '预约成功',
    3: '待支付',
    4: '正在服务',
    5: '服务完成',
    6: '预约失败',
    7: '已取消'
  }
  return stateMap[String(reserveViewBoxForm.state)] || '未知状态'
})

const stateTagType = computed(() => {
  const typeMap: Record<string, string> = {
    1: 'info',
    2: 'success',
    3: 'warning',
    4: 'primary',
    5: 'success',
    6: 'danger',
    7: 'info'
  }
  return typeMap[String(reserveViewBoxForm.state)] || 'default'
})

const initReserveViewBoxForm = (id: string) => {
  getReserveViewBoxDataInfo(id)
}

const getReserveViewBoxDataInfo = async (id: string) => {
  loading.value = true
  try {
    const res: any = await ReserveApi.GetReserveInfo(id)
    if (res.success) {
      Object.assign(reserveViewBoxForm, res.result)
    } else {
      ElMessage.error('数据加载失败')
    }
  } catch (error) {
    ElMessage.error('网络异常，请重试')
  } finally {
    loading.value = false
  }
}

const cancel = () => {
  // 重置表单数据
  Object.assign(reserveViewBoxForm, {
    id: '',
    relatedThumbnail: '',
    reservePerson: '',
    relatedName: '',
    relatedPrice: '',
    reserveTime: '',
    reserveStaffName: '',
    state: 0,
    remark: ''
  })

  emit('close')
}

defineExpose({ initReserveViewBoxForm })
</script>

<style lang="less" scoped>
@primary-color: #409EFF;

.el-dialog {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 36px rgba(0, 0, 0, 0.2);
  }

  &__header {
    padding: 20px 24px;
    background: linear-gradient(135deg, @primary-color 0%, #337ecc 100%);
    margin: 0;

    .el-dialog__title {
      color: white;
      font-size: 18px;
      font-weight: 600;
      letter-spacing: 0.5px;
    }
  }

  &__body {
    padding: 0;
  }
}

.dialog-body {
  padding: 10px;
  background-color: #f9f9f9;
}

.content-wrapper {
  animation: fadeIn 0.5s ease-in-out;
}

.project-poster {
  width: 100%;
  height: 200px;
  border-radius: 8px;
  margin-bottom: 20px;
  transition: all 0.3s ease;

  &:hover {
    transform: scale(1.02);
  }
}

.info-table {
  :deep(.el-descriptions__label) {
    background-color: #f5f7fa !important;
    font-weight: 600;
    color: #333;
    width: 120px;
  }

  :deep(.el-descriptions__label + .el-descriptions__label) {
    border-top: none;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px;
  background-color: white;
  border-top: 1px solid #e8e8e8;

  .el-button {
    border-radius: 8px;
    padding: 10px 24px;
    font-weight: 500;
    letter-spacing: 0.5px;
    transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    &:active {
      transform: translateY(0);
    }

    &--primary {
      background: linear-gradient(135deg, @primary-color 0%, #337ecc 100%);
      border: none;

      &:hover {
        background: linear-gradient(135deg, @primary-color 0%, #2a6eb7 100%);
      }
    }

    &--default {
      background: linear-gradient(135deg, #f4f4f5 0%, #e9e9eb 100%);
      border: none;
      color: #606266;

      &:hover {
        background: linear-gradient(135deg, #f4f4f5 0%, #e0e0e2 100%);
        color: #333;
      }
    }
  }
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 600px) {
  .el-dialog {
    width: 95% !important;
  }
}
</style>
