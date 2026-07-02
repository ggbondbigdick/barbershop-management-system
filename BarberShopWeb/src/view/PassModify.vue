<template>
  <div class="password-change-container">
    <el-card class="password-card">
      <template #header>
        <div class="card-header">
          <div class="icon-wrapper">
            <i class="icon el-icon-lock"></i>
          </div>
          <h2 class="card-title">密码修改</h2>
          <p class="card-subtitle">为您的账户设置更安全的密码</p>
        </div>
      </template>

      <el-form ref="formRef" :model="formData" :rules="rules" label-position="top">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
              v-model="formData.oldPassword"
              type="password"
              show-password
              placeholder="请输入当前密码"
              @focus="animateInput($event, true)"
              @blur="animateInput($event, false)"
              clearable
          />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
              v-model="formData.newPassword"
              type="password"
              show-password
              placeholder="请输入新密码"
              @focus="animateInput($event, true)"
              @blur="animateInput($event, false)"
              clearable
          />
        </el-form-item>

        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input
              v-model="formData.confirmPassword"
              type="password"
              show-password
              placeholder="请再次输入新密码"
              @focus="animateInput($event, true)"
              @blur="animateInput($event, false)"
              clearable
          />
        </el-form-item>

        <el-button
            type="primary"
            @click="submitForm"
            class="submit-btn"
            block
            round
        >
          {{ '确认修改' }}
        </el-button>
      </el-form>

      <div class="tips-section">
        <h4 class="tip-title">密码安全建议</h4>
        <ul class="tips-list">
          <li class="tip-item">
            <i class="el-icon-check"></i>
            密码长度至少8位
          </li>
          <li class="tip-item">
            <i class="el-icon-check"></i>
            包含大小写字母和数字
          </li>
          <li class="tip-item">
            <i class="el-icon-check"></i>
            避免使用个人信息
          </li>
          <li class="tip-item">
            <i class="el-icon-check"></i>
            定期更换密码
          </li>
        </ul>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { modifyPassword } from '@/api/UserApi'
import { encryptByPublicKey } from '@/tool/passwordTool'

// 表单引用
const formRef = ref()

// 表单数据
const formData = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 验证规则
const rules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: any) => {
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/
        if (!regex.test(value)) {
          callback(new Error('需包含大小写字母和数字，至少8位'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: any) => {
        if (value !== formData.value.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 表单提交
const submitForm = async () => {
  await formRef.value.validate()
  let data = {
    oldPassword: encryptByPublicKey(formData.value.oldPassword),
    password: encryptByPublicKey(formData.value.newPassword)
  }
  const res: any = await modifyPassword(data)
  if (res.success) {
    ElMessage.success('密码修改成功！')
    // 重置表单
    formRef.value.resetFields()
  }
}

// 输入框动画
const animateInput = (event: Event, isFocus: boolean) => {
  const inputEl = (event.target as HTMLElement).closest('.el-input') as HTMLElement
  if (inputEl) {
    inputEl.style.transition = 'all 0.3s ease'
    inputEl.style.transform = isFocus ? 'scale(1.02)' : 'scale(1)'
  }
}
</script>

<style scoped lang="less">
@primary-color: #6C5CE7;
@secondary-color: #FF7675;
@light-bg: #f5f7ff;
@white: #ffffff;

.password-change-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.password-card {
  width: 100%;
  max-width: 500px;
  border-radius: 20px;
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
  background: white;
  overflow: hidden;
  transition: all 0.4s ease;
  position: relative;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 20px 50px rgba(0, 0, 0, 0.15);
  }

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 5px;
    background: linear-gradient(90deg, @primary-color, @secondary-color);
  }
}

.card-header {
  text-align: center;
  padding: 30px 20px 15px;
  position: relative;
  background: radial-gradient(circle at top, @white 70%, transparent 30%);
}

.icon-wrapper {
  width: 60px;
  height: 60px;
  margin: 0 auto 15px;
  background: linear-gradient(135deg, @primary-color, @secondary-color);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);

  .icon {
    color: white;
    font-size: 24px;
    transition: transform 0.3s ease;
  }

  &:hover .icon {
    transform: rotate(12deg);
  }
}

.card-title {
  margin: 0;
  font-size: 26px;
  color: #333;
  font-weight: 700;
  letter-spacing: 1px;
  margin-bottom: 8px;
}

.card-subtitle {
  margin: 0;
  font-size: 14px;
  color: #777;
  font-weight: 400;
}

.el-form {
  padding: 0 25px;
}

.el-form-item {
  margin-bottom: 20px;
}

.submit-btn {
  width: 100px;
  margin-top: 25px;
  background: linear-gradient(135deg, @primary-color, @secondary-color);
  border: none;
  color: white;
  font-weight: 450;
  letter-spacing: 1px;
  transition: all 0.3s ease;
  font-size: 16px;
  padding: 12px 0;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
  }

  &:active {
    transform: translateY(0);
  }

  &.is-loading {
    background: #b3aee3;
  }
}

.tips-section {
  margin-top: 35px;
  padding: 25px 25px 30px;
  background: #f8f9fa;
  border-top: 1px solid #eee;
}

.tip-title {
  margin: 0 0 15px;
  font-size: 16px;
  color: #333;
  font-weight: 600;
}

.tips-list {
  list-style: none;
  padding-left: 0;
  margin: 0;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.tip-item {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #555;
  padding-left: 5px;

  i {
    margin-right: 8px;
    color: @primary-color;
    transition: transform 0.3s ease;
  }

  &:hover i {
    transform: translateX(3px);
  }
}

@media (max-width: 500px) {
  .tips-list {
    grid-template-columns: 1fr;
  }
}
</style>