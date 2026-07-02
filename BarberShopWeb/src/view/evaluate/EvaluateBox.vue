<template>
  <el-dialog
      :model-value="evaluateVisible"
      title="评价"
      width="580px"
      :show-close="false"
      :close-on-click-modal="false"
      center
  >
    <div class="dialog-body">
      <el-form :model="evaluateForm" :rules="evaluateRules" ref="evaluateFormRef" label-position="top">
        <el-form-item label="此次服务项目评价" prop="content">
          <el-input v-model="evaluateForm.content" type="textarea" :rows="3" placeholder="请输入评价内容" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="submit(evaluateFormRef)">提交</el-button>
      </span>
    </template>
  </el-dialog>
</template>


<script setup lang="ts">
import {ref, reactive} from 'vue'
import {ElMessage, FormInstance} from 'element-plus'
import EvaluateApi from '@/api/EvaluateApi'

defineProps(['evaluateVisible'])
const emit = defineEmits(['close'])
defineExpose({initEvaluateForm})

const evaluateFormRef = ref()
let evaluateForm = reactive<any>({
  id: '',
  type: '1',
  reserveObject: '',
  content: ''
})
const evaluateRules = reactive({
  content: [
    { required: true, message: '请输入评价内容', trigger: 'blur' }
  ]
})

function initEvaluateForm(id: string) {
  evaluateForm.reserveObject = id
}

const submit = (forEl: FormInstance | undefined) => {
  if (!forEl) return
  forEl.validate(async (valid) => {
    if (valid) {
      const res: any = await EvaluateApi.AddEvaluate(evaluateForm)
      if (res.success) {
        ElMessage.success('评价成功')
        cancel()
      }
    }
  })
}

const cancel = () => {
  Object.keys(evaluateForm).map((key) => evaluateForm[key] = '')
  evaluateFormRef.value.resetFields()
  evaluateFormRef.value.clearValidate()
  emit('close')
}
</script>

<style lang="less" scoped>
.el-dialog {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);

  &__header {
    padding: 20px 24px;
    background: linear-gradient(135deg, #409EFF 0%, #337ecc 100%);
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

  .el-form {
    display: flex;
    flex-direction: column;
    gap: 20px;

    &-item {
      margin-bottom: 0;
      transition: all 0.3s ease;

      &:hover {
        transform: translateX(5px);
      }

      &__label {
        font-weight: 300;
        color: #5a6c84;
        display: block;
      }
    }
  }

  .el-input, .el-select {
    width: 100%;

    &:deep(.el-input__wrapper) {
      border-radius: 8px;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
      transition: all 0.3s ease;
      height: 40px;

      &:hover {
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      }

      &.is-focus {
        box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
      }
    }
  }

  .el-input-number {
    width: 100%;

    &:deep(.el-input__wrapper) {
      padding-right: 0;
    }
  }

  .el-alert {
    margin-top: 8px;
    border-radius: 6px;
    background-color: #f0f7ff;

    &:deep(.el-alert__content) {
      padding: 6px 0;
    }
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
      background: linear-gradient(135deg, #409EFF 0%, #337ecc 100%);
      border: none;

      &:hover {
        background: linear-gradient(135deg, #409EFF 0%, #2a6eb7 100%);
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

/* 动画效果 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.el-dialog {
  animation: fadeIn 0.4s cubic-bezier(0.22, 0.61, 0.36, 1) forwards;
}

/* 响应式调整 */
@media (max-width: 600px) {
  .el-dialog {
    width: 90% !important;
  }

  .dialog-body {
    padding: 16px;
  }
}
</style>