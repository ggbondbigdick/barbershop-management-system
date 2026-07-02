<template>
  <el-dialog
      :model-value="customerBoxVisible"
      :title="customerBoxTitle"
      width="580px"
      :show-close="false"
      :close-on-click-modal="false"
      center
  >
    <div class="dialog-body">
      <el-form :model="customerBoxForm" :rules="customerBoxRules" ref="customerBoxFormRef" label-position="top">
        <el-form-item label="头像" prop="avatar">
          <avatar-upload ref="avatarUploadRef" :default-img-url="customerBoxForm.avatar" />
        </el-form-item>
        <el-form-item v-if="!customerBoxForm.id" label="账号" prop="account">
          <el-input v-model="customerBoxForm.account" clearable/>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="customerBoxForm.name" clearable/>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="customerBoxForm.sex" placeholder="请选择性别" clearable>
            <el-option label="男" value="1"></el-option>
            <el-option label="女" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="customerBoxForm.age" :min="1" step="1" :max="100" placeholder="请输入年龄" clearable/>
        </el-form-item>
        <el-form-item label="手机号" prop="contactTel">
          <el-input v-model="customerBoxForm.contactTel" clearable/>
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="submit(customerBoxFormRef)">提交</el-button>
      </span>
    </template>
  </el-dialog>
</template>


<script setup lang="ts">
import {ref, reactive} from 'vue'
import {ElMessage, FormInstance} from 'element-plus'
import AvatarUpload from '../../components/SingleUpload.vue'

defineProps(['customerBoxVisible', 'customerBoxTitle'])
const emit = defineEmits(['close'])
defineExpose({initCustomerBoxForm})

const customerBoxFormRef = ref()
const avatarUploadRef = ref()
let customerBoxForm = reactive<any>({
  id: '',
  avatar: '',
  account: '',
  name: '',
  sex: '',
  age: null,
  contactTel: '',
  role: '2',
  password: ''
})
const customerBoxRules = reactive({
  avatar: [{required: true, message: '请上传头像', trigger: 'blur'}],
  account: [{required: true, message: '请输入账号', trigger: 'blur'}],
  name: [{required: true, message: '请输入名称', trigger: 'blur'}],
  sex: [{required: true, message: '请选择性别', trigger: ['change', 'blur']}],
  age: [{required: true, message: '请输入年龄', trigger: 'blur'}],
  contactTel: [{required: true, message: '请输入手机号', trigger: 'blur'}]
})

function initCustomerBoxForm(id: string) {
  getCustomerBoxDataInfo(id)
}

const getCustomerBoxDataInfo = async (id: string) => {
  const res: any = await null
  if (res.success) {
    customerBoxForm = Object.assign(customerBoxForm, res.result)
    avatarUploadRef.value.setImageUrl(res.result.avatar)
  }
}

const submit = (forEl: FormInstance | undefined) => {
  customerBoxForm.avatar = avatarUploadRef.value.getUploadInfo().id
  if (!forEl) return
  forEl.validate(async (valid) => {
    if (valid) {
      cancel()
    }
  })
}

const cancel = () => {
  Object.keys(customerBoxForm).map((key) => customerBoxForm[key] = '')
  customerBoxFormRef.value.resetFields()
  customerBoxFormRef.value.clearValidate()
  avatarUploadRef.value.clearUploadFile()
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
