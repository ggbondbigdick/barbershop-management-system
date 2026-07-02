<template>
  <el-main>
    <router-view/>
  </el-main>
</template>


<script setup lang="ts">

</script>

<style lang="less" scoped>
.el-main {
  width: 100%;
  height: calc(100vh - 80px);
  padding: 18px;
  scrollbar-width: thin; /* 适用于 Firefox */
  scrollbar-color: rgba(0, 0, 0, 0.3) transparent; /* 适用于 Firefox */
  overflow-y: auto; /* 确保垂直滚动条可见 */
  background: transparent;
}

.el-main::-webkit-scrollbar {
  width: 8px; /* 滚动条宽度 */
}

.el-main::-webkit-scrollbar-track {
  background: transparent; /* 滚动条轨道背景透明 */
}

.el-main::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.3); /* 滚动条滑块颜色，半透明 */
  border-radius: 4px; /* 滚动条滑块圆角 */
  border: 2px solid transparent; /* 边框透明，增加悬浮感 */
  background-clip: padding-box; /* 背景裁剪到内边距框，避免边框影响 */
}

.el-main::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 0, 0, 0.5); /* 滚动条滑块悬停时的颜色，更深一些 */
}

@media (max-width: 768px) {
  .el-main {
    padding: 12px;
  }
}
</style>
