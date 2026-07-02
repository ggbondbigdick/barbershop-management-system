<template>
  <div class="mobile-home-shell">
    <header class="mobile-header mobile-page-card">
      <div class="brand" @click="goTab('/hairstyle')">
        <img src="/src/assets/img/haircut.svg" alt="logo" />
        <div>
          <p>Little Barber</p>
          <h1>小小理发店</h1>
        </div>
      </div>
      <div class="header-actions">
        <el-button class="ghost-btn" plain @click="onRegister">
          {{ isLogin ? displayName : '注册' }}
        </el-button>
        <el-button class="brand-btn" @click="onLogin">
          {{ isLogin ? '注销' : '登录' }}
        </el-button>
      </div>
    </header>

    <main class="mobile-main">
      <section class="hero-banner mobile-page-card">
        <div>
          <span class="hero-tag">H5 预约体验</span>
          <h2>在手机里也顺手的理发预约</h2>
          <p>选项目、约时间、看订单、改资料，整个流程都压缩成了更适合触屏操作的单手体验。</p>
        </div>
        <el-button class="hero-button" @click="goTab('/reserve')">立即预约</el-button>
      </section>

      <section class="content-panel">
        <router-view />
      </section>
    </main>

    <nav class="bottom-nav mobile-page-card">
      <button
        v-for="item in visibleTabs"
        :key="item.path"
        :class="{ active: activePath === item.path }"
        @click="goTab(item.path)"
      >
        <span>{{ item.label }}</span>
      </button>
    </nav>
  </div>
</template>

<script setup lang="ts">
import {computed} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {logout} from '@/api/UserApi'
import {checkLogin, getUserInfo, clearAll} from '@/tool/loginTool'

const router = useRouter()
const route = useRoute()

const activePath = computed(() => route.path)
const isLogin = computed(() => checkLogin())
const displayName = computed(() => {
  const user = getUserInfo()
  return user?.name || '已登录'
})

const tabs = computed(() => [
  {label: '项目', path: '/hairstyle', auth: false},
  {label: '预约', path: '/reserve', auth: false},
  {label: '订单', path: '/myReserve', auth: true},
  {label: '我的', path: '/myInfo', auth: true}
])

const visibleTabs = computed(() => tabs.value.filter((item) => !item.auth || isLogin.value))

const goTab = (path: string) => {
  if (path === '/reserve' && route.path === '/reserve') {
    return
  }

  if ((path === '/myReserve' || path === '/myInfo') && !isLogin.value) {
    router.push('/login')
    return
  }

  router.push(path)
}

const onRegister = () => {
  if (!isLogin.value) {
    router.push('/register')
  }
}

const onLogin = async () => {
  if (!isLogin.value) {
    router.push('/login')
    return
  }

  const res: any = await logout()
  if (res.success) {
    clearAll()
    router.push('/hairstyle')
    window.location.reload()
  }
}
</script>

<style lang="less" scoped>
.mobile-home-shell {
  min-height: 100vh;
  padding: 16px 16px calc(92px + env(safe-area-inset-bottom));
}

.mobile-header {
  position: sticky;
  top: 12px;
  z-index: 30;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 14px 18px;
  border-radius: 24px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;

  img {
    width: 44px;
    height: 44px;
    border-radius: 14px;
    padding: 8px;
    background: linear-gradient(135deg, rgba(181, 106, 59, 0.16), rgba(123, 66, 33, 0.08));
  }

  p,
  h1 {
    margin: 0;
  }

  p {
    font-size: 12px;
    color: var(--text-muted);
    text-transform: uppercase;
    letter-spacing: 0.14em;
  }

  h1 {
    font-size: 20px;
    color: var(--brand-dark);
  }
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.ghost-btn,
.brand-btn,
.hero-button {
  border-radius: 999px;
  border: none;
  padding-inline: 18px;
}

.ghost-btn {
  color: var(--brand-dark);
  background: rgba(181, 106, 59, 0.08);
}

.brand-btn,
.hero-button {
  background: linear-gradient(135deg, var(--brand), var(--brand-dark));
  color: #fff;
}

.mobile-main {
  width: min(100%, 1120px);
  margin: 20px auto 0;
}

.hero-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  padding: 22px;
  border-radius: 28px;
}

.hero-tag {
  display: inline-flex;
  padding: 6px 12px;
  margin-bottom: 12px;
  border-radius: 999px;
  background: rgba(181, 106, 59, 0.12);
  color: var(--brand-dark);
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.08em;
}

.hero-banner h2 {
  margin: 0 0 8px;
  font-size: clamp(24px, 3.2vw, 34px);
}

.hero-banner p {
  margin: 0;
  max-width: 620px;
  color: var(--text-muted);
  line-height: 1.7;
}

.content-panel {
  margin-top: 18px;
}

.bottom-nav {
  position: fixed;
  left: 50%;
  bottom: calc(12px + env(safe-area-inset-bottom));
  transform: translateX(-50%);
  z-index: 40;
  display: flex;
  gap: 10px;
  width: min(calc(100% - 20px), 560px);
  padding: 10px;
  border-radius: 999px;
}

.bottom-nav button {
  flex: 1;
  min-height: 50px;
  border: none;
  border-radius: 999px;
  background: transparent;
  color: var(--text-muted);
  font-weight: 600;
}

.bottom-nav button.active {
  background: linear-gradient(135deg, var(--brand), var(--brand-dark));
  color: #fff;
  box-shadow: 0 10px 24px rgba(123, 66, 33, 0.24);
}

@media (min-width: 1024px) {
  .mobile-home-shell {
    padding-inline: 24px;
  }
}

@media (max-width: 768px) {
  .mobile-home-shell {
    padding-inline: 12px;
  }

  .mobile-header {
    top: 8px;
    flex-direction: column;
    align-items: stretch;
  }

  .header-actions {
    width: 100%;
  }

  .header-actions :deep(.el-button) {
    flex: 1;
  }

  .hero-banner {
    flex-direction: column;
    align-items: flex-start;
    padding: 18px;
    border-radius: 24px;
  }

  .hero-button {
    width: 100%;
    min-height: 44px;
  }
}
</style>
