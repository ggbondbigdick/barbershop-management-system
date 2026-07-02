<template>
  <div class="app-shell">
    <router-view v-if="isRouteAlive"></router-view>
  </div>
</template>


<script setup lang="ts">
import {nextTick, provide, ref} from 'vue'

// 定义刷新变量
const isRouteAlive = ref(true)

// 页面刷新
const reload = () => {
  isRouteAlive.value = false
  nextTick(() => isRouteAlive.value = true)
}

provide('reload', reload)

</script>

<style lang="less">
:root {
  color-scheme: light;
  --app-bg: #f5efe7;
  --card-bg: rgba(255, 252, 248, 0.92);
  --card-shadow: 0 18px 40px rgba(86, 57, 35, 0.12);
  --brand: #b56a3b;
  --brand-dark: #7b4221;
  --text-main: #2d221b;
  --text-muted: #7d6a5d;
  --line-soft: rgba(123, 66, 33, 0.12);
}

* {
  box-sizing: border-box;
}

html,
body,
#app {
  min-height: 100%;
  background:
    radial-gradient(circle at top, rgba(222, 179, 140, 0.28), transparent 35%),
    linear-gradient(180deg, #fbf5ef 0%, #f3e7da 42%, #efe2d5 100%);
  color: var(--text-main);
}

body {
  margin: 0;
  -webkit-font-smoothing: antialiased;
  text-rendering: optimizeLegibility;
}

button,
input,
textarea,
select {
  font: inherit;
}

a {
  color: inherit;
}

.app-shell {
  min-height: 100vh;
}

.mobile-page-card {
  background: var(--card-bg);
  border: 1px solid rgba(255, 255, 255, 0.6);
  box-shadow: var(--card-shadow);
  backdrop-filter: blur(12px);
}

@media (max-width: 768px) {
  html {
    font-size: 15px;
  }

  body {
    overscroll-behavior-y: none;
  }
}
</style>
