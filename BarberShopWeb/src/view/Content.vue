<template>
  <div class="content" style="text-align: center; margin-top: 50px; font-family: Arial, sans-serif;">
    <h1 style="color: #4CAF50; font-size: 36px;">Hello, {{ name }} 👋</h1>
    <div style="color: #555; font-size: 20px; margin-top: 20px;">
      欢 迎 来 到 <span style="font-weight: bold; color: #007BFF;">理 发 店 管 理 系 统 后 台</span> !
    </div>
  </div>
</template>



<script setup lang="ts">
import {onMounted, reactive} from 'vue'
import {getUserInfo} from '@/tool/loginTool'
import {getStatisticsData} from '@/api/UserApi'

const {accountNum, name} = getUserInfo()
let statisticsData = reactive<any>({
  userBalance: 0,
  orderCount: 0,
  userCount: 0
})

onMounted(async () => {
  getUserStatistics()
})

const getUserStatistics = async () => {
  const res: any = await getStatisticsData()
  if (res.success) {
    statisticsData = Object.assign(statisticsData, res.result)
  }
}
</script>
