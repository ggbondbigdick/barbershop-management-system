<template>
  <el-header class="header-container">
    <div class="header-content">
      <el-tooltip
          class="box-item"
          effect="dark"
          content="点击伸缩侧边栏菜单"
          placement="bottom"
          auto-close="300"
      >
        <div class="title" @click="onClickHeader">
          <img class="logo" src="@/assets/img/haircut.svg" alt="logo" />
          {{ '小小理发店管理系统' }}
        </div>
      </el-tooltip>
      <div class="user-info">
        <el-dropdown>
        <span class="el-dropdown-link">
          {{ `登录人: ${user.name}` }}
        </span>
          <template #dropdown>
            <el-dropdown-menu>
<!--              <el-dropdown-item @click.native="clickFrontDesk">{{ '前往前台' }}</el-dropdown-item>-->
              <el-dropdown-item @click.native="clickQuit">{{ '注销登录' }}</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </el-header>
</template>


<script setup lang="ts">
import {getUserInfo, clearAll} from '@/tool/loginTool'
import {logout} from '@/api/UserApi'
import router from '@/router/index'
import {ElMessage, ElNotification} from 'element-plus'
import {useMenuShrink} from '@/store/menuShrink'

let user: any = getUserInfo()
const menuShrink: any = useMenuShrink()

const onClickHeader = () => {
  menuShrink.toggleMenuShrink()
}

const clickFrontDesk = () => {
  router.push({path: '/'})
}

const clickQuit = async () => {
  const res: any = await logout()
  if (res.success) {
    let msg = '注销登录成功'
    ElMessage.success(msg)
    router.push({path: '/'})
    clearAll()
  }
}
</script>

<style lang="less" scoped>
.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  color: #fff;
  background: rgba(255, 250, 245, 0.88);
  backdrop-filter: blur(10px);
  height: 80px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  width: 100%;
  align-items: center;
}

.title {
  font-size: 20px;
  font-weight: bold;
  white-space: nowrap;
  color: var(--brand-dark);
  cursor: pointer;
  display: flex;
  align-items: center;
}

.logo {
  width: 35px;
  height: 35px;
  margin-right: 8px;
  margin-left: 10px;
  margin-bottom: 0;
}

.user-info {
  display: flex;
  align-items: center;
  color: var(--text-main);
}

.el-dropdown-link {
  cursor: pointer;
  color: var(--text-main);
}

:focus {
  outline: 0;
}

@media (max-width: 768px) {
  .header-container {
    padding: 0 12px;
  }

  .title {
    font-size: 16px;
    max-width: 62vw;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .user-info {
    font-size: 12px;
  }
}
</style>
