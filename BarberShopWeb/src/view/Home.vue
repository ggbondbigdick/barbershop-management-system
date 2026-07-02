<template>
  <div class="admin-shell">
    <el-container class="flex-container">
      <Header class="header"/>
      <el-container class="flex-container-main">
        <Aside class="aside"/>
        <Main class="main"/>
      </el-container>
    </el-container>
  </div>
</template>


<script setup lang="ts">
import Header from '@/view/components/Header.vue'
import Aside from '@/view/components/Aside.vue'
import Main from '@/view/components/Main.vue'
</script>

<style lang="less" scoped>
.admin-shell {
  min-height: 100vh;
  background: rgba(255, 252, 248, 0.35);
}

.flex-container {
  display: block;
  min-height: 100vh;
}

.flex-container-main {
  position: relative;
  display: flex;
  min-height: calc(100vh - 80px);
}

.main {
  flex: 1;
}

@media (max-width: 768px) {
  .flex-container-main {
    display: block;
  }
}
</style>
