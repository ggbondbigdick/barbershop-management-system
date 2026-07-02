<template>
  <div class="profile-page">
    <section class="page-header">
      <p>Profile</p>
      <h2>个人信息</h2>
      <span>查看资料、修改密码和充值都改成了适合手机操作的分段卡片。</span>
    </section>

    <el-tabs v-model="activeTab" type="card" class="profile-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="用户信息" name="info">
        <div class="info-card mobile-page-card">
          <div class="info-item">
            <span>昵称</span>
            <strong>{{ userInfoData.name }}</strong>
          </div>
          <div class="info-item">
            <span>年龄</span>
            <strong>{{ userInfoData.age }}</strong>
          </div>
          <div class="info-item">
            <span>性别</span>
            <strong>{{ userInfoData.sex === '1' ? '男' : '女' }}</strong>
          </div>
          <div class="info-item">
            <span>手机号</span>
            <strong>{{ userInfoData.contactTel }}</strong>
          </div>
          <div class="info-item highlight">
            <span>余额</span>
            <strong>¥ {{ userInfoData.balance }} RMB</strong>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="修改密码" name="password">
        <div class="form-card mobile-page-card">
          <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-position="top">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" show-password placeholder="请输入原密码" />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" show-password placeholder="请输入新密码" />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" show-password placeholder="请再次输入新密码" />
            </el-form-item>
            <el-button type="primary" @click="submitPasswordForm" class="submit-button" :loading="passwordLoading">
              {{ passwordLoading ? '提交中...' : '保存修改' }}
            </el-button>
          </el-form>
        </div>
      </el-tab-pane>

      <el-tab-pane label="余额充值" name="recharge">
        <div class="form-card mobile-page-card">
          <el-form ref="rechargeFormRef" :model="rechargeForm" :rules="rechargeRules" label-position="top">
            <el-form-item label="充值金额" prop="amount">
              <el-input-number
                v-model="rechargeForm.amount"
                :min="1"
                :step="10"
                placeholder="请输入充值金额"
                style="width: 100%"
              />
            </el-form-item>
            <el-button type="primary" @click="submitRechargeForm" class="submit-button" :loading="rechargeLoading">
              {{ rechargeLoading ? '充值中...' : '立即充值' }}
            </el-button>
          </el-form>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {modifyPassword, userInfo, userRecharge} from '@/api/UserApi'
import {encryptByPublicKey} from '@/tool/passwordTool'

const activeTab = ref('info')

const userInfoData = ref({
  name: '',
  age: null as number | null,
  gender: null as number | null,
  contactTel: '',
  balance: null as number | null,
  sex: ''
})

const getUserInfo = async () => {
  const res: any = await userInfo('')
  if (res.success) {
    userInfoData.value = Object.assign(userInfoData.value, res.result)
  }
}

const passwordForm = ref<any>({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordFormRef = ref()

const passwordRules = {
  oldPassword: [{required: true, message: '请输入原密码', trigger: 'blur'}],
  newPassword: [{required: true, message: '请输入新密码', trigger: 'blur'}],
  confirmPassword: [
    {required: true, message: '请再次输入新密码', trigger: 'blur'},
    {
      validator: (rule: any, value: string, callback: any) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const passwordLoading = ref(false)
const rechargeLoading = ref(false)

const submitPasswordForm = () => {
  passwordFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      passwordLoading.value = true
      const data = {
        oldPassword: encryptByPublicKey(passwordForm.value.oldPassword),
        password: encryptByPublicKey(passwordForm.value.newPassword)
      }
      const res: any = await modifyPassword(data)
      passwordLoading.value = false
      if (res.success) {
        ElMessage.success('密码修改成功')
        Object.keys(passwordForm.value).forEach((key) => {
          passwordForm.value[key] = ''
        })
      }
    }
  })
}

const rechargeForm = ref({
  amount: null
})

const rechargeFormRef = ref()

const rechargeRules = {
  amount: [
    {required: true, message: '请输入充值金额', trigger: 'blur'},
    {type: 'number', min: 1, message: '充值金额必须大于0', trigger: 'blur'}
  ]
}

const submitRechargeForm = () => {
  rechargeFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      rechargeLoading.value = true
      try {
        const res: any = await userRecharge(rechargeForm.value.amount)
        if (res.success) {
          if (res.result?.balance !== undefined) {
            userInfoData.value.balance = res.result.balance
          }
          ElMessage.success('充值成功')
          rechargeForm.value.amount = null
          getUserInfo()
        }
      } finally {
        rechargeLoading.value = false
      }
    }
  })
}

const handleTabChange = (tabName: string) => {
  activeTab.value = tabName
  if (tabName === 'recharge' && rechargeFormRef.value) {
    rechargeForm.value.amount = null
    rechargeFormRef.value.resetFields()
  } else if (tabName === 'password' && passwordFormRef.value) {
    passwordFormRef.value.resetFields()
    Object.keys(passwordForm.value).forEach((key) => {
      passwordForm.value[key] = ''
    })
  } else if (tabName === 'info') {
    getUserInfo()
  }
}

onMounted(() => getUserInfo())
</script>

<style lang="less" scoped>
.profile-page {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.page-header {
  p,
  h2,
  span {
    margin: 0;
  }

  p {
    color: var(--brand);
    font-size: 12px;
    font-weight: 700;
    letter-spacing: 0.18em;
    text-transform: uppercase;
  }

  h2 {
    margin-top: 8px;
    font-size: clamp(24px, 4vw, 32px);
  }

  span {
    display: block;
    margin-top: 10px;
    color: var(--text-muted);
    line-height: 1.7;
  }
}

.profile-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: 16px;
  }

  :deep(.el-tabs__nav-wrap) {
    overflow-x: auto;
  }

  :deep(.el-tabs__item) {
    border-radius: 999px !important;
  }

  :deep(.el-tabs__item.is-active) {
    background: linear-gradient(135deg, var(--brand), var(--brand-dark));
    color: #fff;
    border-color: transparent;
  }
}

.info-card,
.form-card {
  padding: 18px;
  border-radius: 24px;
}

.info-card {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}

.info-item {
  padding: 14px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.62);

  span,
  strong {
    display: block;
  }

  span {
    margin-bottom: 8px;
    color: var(--text-muted);
    font-size: 13px;
  }

  strong {
    line-height: 1.5;
    word-break: break-all;
  }
}

.highlight {
  background: linear-gradient(135deg, rgba(181, 106, 59, 0.14), rgba(123, 66, 33, 0.08));
}

.form-card :deep(.el-form-item__label) {
  font-weight: 700;
  color: var(--text-main);
}

.form-card :deep(.el-input__wrapper),
.form-card :deep(.el-input-number),
.form-card :deep(.el-input-number .el-input__wrapper) {
  border-radius: 16px;
  box-shadow: none;
}

.submit-button {
  width: 100%;
  min-height: 46px;
  border: none;
  border-radius: 16px;
  background: linear-gradient(135deg, var(--brand), var(--brand-dark));
}

@media (max-width: 768px) {
  .info-card {
    grid-template-columns: 1fr;
  }
}
</style>
