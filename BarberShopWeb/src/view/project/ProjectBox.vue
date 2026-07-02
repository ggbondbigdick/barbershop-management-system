<template>
  <el-dialog
      :model-value="projectBoxVisible"
      :title="projectBoxTitle"
      width="580px"
      :show-close="false"
      :close-on-click-modal="false"
      center
  >
    <div class="dialog-body">
      <el-form :model="projectBoxForm" :rules="projectBoxRules" ref="projectBoxFormRef" label-position="top">
        <el-form-item label="缩略图" prop="thumbnail">
          <avatar-upload ref="thumbnailUploadRef" :default-img-url="projectBoxForm.thumbnail" />
        </el-form-item>
        <el-form-item label="项目类型" prop="type">
          <el-select v-model="projectBoxForm.type" placeholder="请选择项目类型" style="width: 100%" clearable>
            <el-option v-for="(item, index) in ['洗发', '理发', '烫发', '染发', '头发保养']" :key="index" :label="item" :value="index + 1" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="projectBoxForm.name" placeholder="请输入项目名称" clearable />
        </el-form-item>
        <el-form-item label="项目介绍" prop="introduce">
          <el-input type="textarea" v-model="projectBoxForm.introduce" :rows="2" placeholder="请输入项目介绍" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="projectBoxForm.price" :min="1" :max="10000" :step="0.1" placeholder="请输入价格" clearable />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="projectBoxForm.remark" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="submit(projectBoxFormRef)">提交</el-button>
      </span>
    </template>
  </el-dialog>
</template>


<script setup lang="ts">
import {ref, reactive} from 'vue'
import {ElMessage, FormInstance} from 'element-plus'
import AvatarUpload from '../../components/SingleUpload.vue'
import HaircuttingProjectApi from '@/api/HaircuttingProjectApi'

defineProps(['projectBoxVisible', 'projectBoxTitle'])
const emit = defineEmits(['close'])
defineExpose({initProjectBoxForm})

const projectBoxFormRef = ref()
const thumbnailUploadRef = ref()
let projectBoxForm = reactive<any>({
  id: '',
  type: '',
  thumbnail: '',
  name: '',
  introduce: '',
  price: null,
  remark: ''
})
const projectBoxRules = reactive({
  thumbnail: [{ required: true, message: '请上传项目缩略图', trigger: 'blur' }],
  type: [{ required: true, message: '请选择项目类型', trigger: ['change', 'blur'] }],
  name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  introduce: [{ required: true, message: '请输入项目介绍', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur'}]
})

function initProjectBoxForm(id: string) {
  getProjectBoxDataInfo(id)
}

const getProjectBoxDataInfo = async (id: string) => {
  const {success, result} = await HaircuttingProjectApi.GetHaircuttingProjectInfo(id) as any
  if (success) {
    projectBoxForm = Object.assign(projectBoxForm, result)
    thumbnailUploadRef.value.setImageUrl(result.thumbnail)
  }
}

const submit = (forEl: FormInstance | undefined) => {
  projectBoxForm.thumbnail = thumbnailUploadRef.value.getUploadInfo().id
  if (!forEl) return
  forEl.validate(async (valid) => {
    if (valid) {
      if (projectBoxForm.id) {
        const {success} = await HaircuttingProjectApi.ModifyHaircuttingProject(projectBoxForm) as any
        if (success) {
          ElMessage.success('项目信息编辑成功')
        }
      } else {
        const {success} = await HaircuttingProjectApi.AddHaircuttingProject(projectBoxForm) as any
        if (success) {
          ElMessage.success('新增项目信息成功')
        }
      }
      cancel()
    }
  })
}

const cancel = () => {
  Object.keys(projectBoxForm).map((key) => projectBoxForm[key] = '')
  projectBoxFormRef.value.resetFields()
  projectBoxFormRef.value.clearValidate()
  thumbnailUploadRef.value.clearUploadFile()
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