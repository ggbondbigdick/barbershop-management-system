<template>
  <el-aside :style="dynamicStyle">
    <el-menu
        :router="true"
        :default-active="router.currentRoute.value.name"
        text-color="rgb(12, 10, 15)"
        :collapse-transition="false"
        :collapse="menuShrink.isMenuShrink"
    >
      <el-menu-item
          v-for="item in noChildren()"
          :index="item.name"
          :key="item.name"
          @click="clickMenu(item)"
      >
        <!-- <font-awesome-icon class="icon" :icon="item.icon"></font-awesome-icon>-->
        <span>{{ item.title }}</span>
      </el-menu-item>
      <el-sub-menu
          v-for="item in hasChildren()"
          :index="item.name"
          :key="item.name"
      >
        <template #title>
          <!--<font-awesome-icon class="icon" :icon="item.icon"></font-awesome-icon>-->
          <span>{{ item.title }}</span>
        </template>
        <el-menu-item-group>
          <el-menu-item v-for="(subItem,subIndex) in item.children" :index="subItem.name" :key="subIndex"
                        @click="clickMenu(subItem)">
            <span>{{ subItem.title }}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
    </el-menu>
  </el-aside>
</template>


<script setup lang="ts">
import {computed, onMounted, onUnmounted, ref} from 'vue'
import {getUserMenus} from '@/tool/menuTool'
import {getUserInfo} from '@/tool/loginTool'
import router from '@/router/index'
import {useMenuShrink} from '@/store/menuShrink'

let user: any = getUserInfo()
const menuShrink: any = useMenuShrink()

const handleResize = () => {
  menuShrink.isMenuShrink = window.innerWidth <= 768
}

onMounted(() => {
  handleResize()
  window.addEventListener("resize", handleResize)
})

onUnmounted(() => {
  window.removeEventListener("resize", handleResize)
})

const dynamicStyle = computed(() => {
  return {
    width: menuShrink.isMenuShrink ? '0px' : '210px',
    height: 'calc(100vh - 80px)',
    cursor: 'pointer',
    overflowX: 'hidden',
    overflowY: 'auto',
  }
})

// 获取用户菜单
const menuList = ref(getUserMenus(user.role))

const noChildren = () => {
  return menuList.value.filter((item: any) => !item.children)
}

const hasChildren = () => {
  return menuList.value.filter((item: any) => item.children)
}

const clickMenu = (item: any) => {
  router.push({name: item.name})
}
</script>

<style lang="less" scoped>
.el-aside {
  background: rgba(255, 250, 245, 0.94);
  border-right: 1px solid rgba(123, 66, 33, 0.08);
  transition: width 0.25s ease;
}

::v-deep(.el-menu) {
  text-align: center;
  background: transparent;
  border: none;
  height: 100%;
}

.icon {
  width: 23px;
  height: 23px;
  margin-right: 8px;
}

@media (max-width: 768px) {
  .el-aside {
    position: fixed;
    top: 80px;
    left: 0;
    z-index: 25;
    box-shadow: 0 14px 36px rgba(61, 36, 18, 0.16);
  }
}
</style>
