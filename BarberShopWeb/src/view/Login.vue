<template>
  <div class="login_container">
    <el-row>
      <el-col :span="11" :xs="0"/>
      <el-col :span="12" :xs="24">
        <el-form class="login_form" :model="loginForm" :rules="rules" ref="loginForms">
          <h1>欢迎 使用</h1>
          <h2>小小理发店管理系统</h2>
          <el-form-item prop="accountNum">
            <el-input :prefix-icon="User" v-model="loginForm.accountNum" placeholder="账号"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input type="password" :prefix-icon="Lock" v-model="loginForm.password" show-password
                      placeholder="密码"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button class="login_btn" type="primary" size="default" @click="loginBtn(loginForms)">
              登 录
            </el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>


<script setup lang="ts">
import {Lock, User} from '@element-plus/icons-vue'
import {reactive, ref} from 'vue'
import {FormInstance, ElMessage} from 'element-plus'
import {useRouter} from 'vue-router'
import {login, userInfo} from '@/api/UserApi'
import {encryptByPublicKey} from '@/tool/passwordTool'

//获取el-form组件
let loginForms = ref()
//获取路由器
let router = useRouter()
//收集账号与密码的数据
let loginForm = reactive({accountNum: '', password: ''})

//定义表单校验需要配置对象
const rules = {
  accountNum: [{required: true, message: '请输入账号', trigger: 'blur'}],
  password: [{required: true, message: '请输入密码', trigger: 'blur'}]
}

// 忘记密码按钮
const forgotPasswordBtn = () => {
  router.push('forgotPassword')
}

// 注册账号按钮
const registerBtn = () => {
  router.push('register')
}

//登录请求
const loginBtn = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate(async (valid: any) => {
    if (valid) {
      let data = {account: loginForm.accountNum, password: encryptByPublicKey(loginForm.password)}
      const res: any = await login(data)
      if (res.success) {
        localStorage.setItem('token', res.result.token)
        localStorage.setItem('device', res.result.device)
        localStorage.setItem('tokenExpire', res.result.expireTime)
        ElMessage.success('登陆成功......')
        // 登录成功后，跳转到内容页面
        setTimeout(async () => {
          // 获取登录用户信息
          const res: any = await userInfo("")
          if (res.success) localStorage.setItem('userInfo', JSON.stringify(res.result))
          if (res.result.role === '1' || res.result.role === '3') {
            router.push('/home')
          } else {
            router.push('/')
          }
        }, 500)
      }
    }
  })
}
</script>

<style lang="less" scoped>
.login_container {
  width: 100vw;
  height: 100vh;
  background: url('@/assets/img/Login_BJ.jpg') no-repeat center center fixed;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  top: 0;
  left: 0;
  overflow: hidden;

  /* 半透明遮罩层 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.2);
    z-index: 0;
  }

  .login_form {
    width: 380px;
    padding: 40px;
    background: rgba(255, 248, 242, 0.86);
    border-radius: 28px;
    box-shadow: 0 18px 40px rgba(61, 36, 18, 0.2);
    backdrop-filter: blur(8px);
    position: absolute;
    animation: float 25s ease-in-out infinite;
    z-index: 1;
    transition: all 0.4s ease;

    &:hover {
      transform: scale(1.02);
      box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
    }

    h1 {
      font-size: 32px;
      margin-bottom: 10px;
      text-align: center;
      color: #2c3e50;
      background: linear-gradient(135deg, #b56a3b, #7b4221);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    }

    h2 {
      font-size: 16px;
      margin: 20px 0 30px;
      text-align: center;
      color: #606266;
      font-weight: normal;
    }

    .el-form-item {
      margin-bottom: 22px;
      transition: all 0.3s ease;

      &:hover {
        transform: translateX(5px);
      }
    }

    .el-input {
      &:deep(.el-input__wrapper) {
        height: 48px;
        border-radius: 16px;
        background: rgba(255, 255, 255, 0.9);
        transition: all 0.3s ease;

        &:hover {
          box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
          background: rgba(255, 255, 255, 1);
        }

        &.is-focus {
          box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.3);
        }
      }

      &:deep(.el-input__prefix-inner) {
        color: #b56a3b;
      }
    }

    .login_btn {
      height: 48px;
      width: 100%;
      font-size: 16px;
      font-weight: 500;
      letter-spacing: 1px;
      background: linear-gradient(135deg, #b56a3b, #7b4221);
      border: none;
      border-radius: 16px;
      color: white;
      transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
      margin-top: 10px;

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 20px rgba(64, 158, 255, 0.3);
      }

      &:active {
        transform: translateY(0);
      }
    }
  }
}

/* 漂浮动画 */
@keyframes float {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  25% {
    transform: translate(15px, 10px) rotate(0.8deg);
  }
  50% {
    transform: translate(20px, 5px) rotate(-0.8deg);
  }
  75% {
    transform: translate(10px, 15px) rotate(0.8deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login_container {
    padding: 16px;
    align-items: flex-end;

    .login_form {
      width: 100%;
      max-width: 420px;
      margin-bottom: calc(24px + env(safe-area-inset-bottom));
      padding: 28px 20px;
      animation: none !important;
      position: relative;
      border-radius: 24px;

      &:hover {
        transform: none;
      }

      h1 {
        font-size: 28px;
      }

      h2 {
        margin-bottom: 24px;
      }
    }
  }
}
</style>
