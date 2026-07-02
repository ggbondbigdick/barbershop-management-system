<template>
  <div class="registerForm">
    <h1>注册账号</h1>
    <el-form class="register_form" :model="registerForm" :rules="registerRules" ref="registerFormRef">
      <el-form-item prop="account">
        <el-input v-model="registerForm.account" placeholder="账号" size="large" clearable />
      </el-form-item>
      <el-form-item prop="name">
        <el-input v-model="registerForm.name" placeholder="名称" size="large" clearable />
      </el-form-item>
      <el-form-item prop="sex">
        <el-select v-model="registerForm.sex" placeholder="性别" size="large" clearable>
          <el-option label="男" value="1"></el-option>
          <el-option label="女" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="age">
        <el-input-number style="width: 100%;height: 42px;" v-model="registerForm.age" :min="1" step="1" :max="100" placeholder="年龄" />
      </el-form-item>
      <el-form-item prop="contactTel">
        <el-input v-model="registerForm.contactTel" placeholder="联系方式" size="large" clearable />
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="registerForm.password" placeholder="密码" size="large" clearable />
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input type="password" v-model="registerForm.confirmPassword" placeholder="确认密码" size="large" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="registerBtn(registerFormRef)">注 册 账 号</el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="goToHomeBtn">前 往 首 页</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>


<script setup lang="ts">
import {reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {register} from '@/api/UserApi'
import {encryptByPublicKey} from '@/tool/passwordTool'
import {Lock, User} from '@element-plus/icons-vue'
import {FormInstance, FormRules, ElMessage} from 'element-plus'
import { userAgreeStore } from '@/store'

const registerAgreeStore: any = userAgreeStore()
const router = useRouter()
const registerFormRef = ref<FormInstance>()

const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (registerForm.confirmPassword !== '') {
      if (!registerFormRef.value) return
      registerFormRef.value.validateField('confirmPassword')
    }
    callback()
  }
}
const validateCheckPass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error("两次输入密码不匹配！"))
  } else {
    callback()
  }
}

const defaultRegisterForm = () => ({
  account: '',
  name: '',
  sex: '',
  age: null,
  contactTel: '',
  role: '2',
  password: '',
  confirmPassword: '',
})

let registerForm = reactive<any>(defaultRegisterForm())

let registerRules = reactive<FormRules> ({
  account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  sex: [{ required: true, message: '请选择性别', trigger: ['blur', 'change'] }],
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
  contactTel: [{ required: true, message: '请输入联系方式', trigger: 'blur' }],
  password: [{ validator: validatePass, trigger: 'blur' }],
  confirmPassword: [{ validator: validateCheckPass, trigger: 'blur' }],
})

// Methods for register
const registerBtn = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate(async (valid: any) => {
    if (valid) {
      let data = {
        ...registerForm,
        password: encryptByPublicKey(registerForm.password)
      }
      const res: any = await register(data)
      if (res.success) {
        registerAgreeStore.setUserAgree(false)
        Object.assign(registerForm, defaultRegisterForm())
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      }
    }
  })
}

// Methods for go to login
const goToHomeBtn = () => {
  Object.assign(registerForm, defaultRegisterForm())
  router.push('/')
}
</script>

<style lang="less" scoped>
.registerForm {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 24px 16px;
  background:
    radial-gradient(circle at top, rgba(222, 179, 140, 0.28), transparent 35%),
    linear-gradient(180deg, #fbf5ef 0%, #f3e7da 42%, #efe2d5 100%);
  animation: fadeIn 0.6s ease-out;

  h1 {
    color: var(--brand-dark);
    font-size: 28px;
    margin-bottom: 14px;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  }

  .el-form {
    width: 100%;
    max-width: 520px;
    padding: 30px;
    background: rgba(255, 252, 248, 0.88);
    border-radius: 24px;
    box-shadow: 0 18px 40px rgba(86, 57, 35, 0.12);
    backdrop-filter: blur(10px);
    transition: all 0.3s ease;

    &:hover {
      box-shadow: 0 15px 35px rgba(0, 0, 0, 0.12);
    }

    .el-form-item {
      margin-bottom: 24px;
      transition: all 0.3s ease;

      &:hover {
        transform: translateX(5px);
      }
    }

    .el-input, .el-select {
      width: 100%;

      &:deep(.el-input__wrapper) {
        height: 48px;
        border-radius: 16px;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
        transition: all 0.3s ease;
        padding: 0 16px;

        &:hover {
          box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        &.is-focus {
          box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.3);
        }
      }

      &:deep(.el-input__inner::placeholder) {
        color: #a8abb2;
      }
    }

    .el-input-number {
      width: 100%;
      height: 48px;
    }

    .el-button {
      width: 100%;
      height: 46px;
      border-radius: 12px;
      font-size: 16px;
      font-weight: 500;
      letter-spacing: 1px;
      transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);

      &--primary {
        background: linear-gradient(135deg, #b56a3b 0%, #7b4221 100%);
        border: none;
        color: white;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 20px rgba(64, 158, 255, 0.3);
        }

        &:active {
          transform: translateY(0);
        }
      }

      &--default {
        background: linear-gradient(135deg, #f4f4f5 0%, #e9e9eb 100%);
        border: none;
        color: #606266;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
          color: #333;
        }

        &:active {
          transform: translateY(0);
        }
      }
    }
  }
}

/* 动画效果 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 响应式设计 */
@media (max-width: 600px) {
  .registerForm {
    justify-content: flex-start;
    padding: 18px 12px calc(24px + env(safe-area-inset-bottom));

    .el-form {
      padding: 24px 16px;
      border-radius: 20px;
    }
  }
}
</style>
