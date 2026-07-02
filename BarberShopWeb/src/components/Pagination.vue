<template>
  <div
      class="pagination flex-center"
      :style="{margin: margin}"
  >
    <el-pagination
        class="pagination-control"
        background
        layout="total, prev, pager, next, jumper"
        :current-page="pageParams.page"
        :page-size="pageParams.size"
        :total="total"
        @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup lang="ts">
import {defineProps, defineEmits, reactive} from 'vue'

const emits = defineEmits(['onChange'])
// 接收父组件传值
defineProps({
  margin: {
    type: String,
    default: () => '5px 0 0'
  },
  // 总数
  total: {
    type: Number,
    default: null
  }
})

const pageParams = reactive<any>({
  page: 1,
  size: 10
})

const handleCurrentChange = (page: number) => {
  pageParams.page = page
  emits('onChange', pageParams)
}

</script>

<style lang="less" scoped>
.pagination-control ::v-deep .btn-prev,
.pagination-control ::v-deep .number,
.pagination-control ::v-deep .btn-next,
.pagination-control ::v-deep .el-pagination__editor {
  min-width: 25px !important;
  width: 35px !important;
  height: 35px !important;
  border-radius: 15px !important;
  background-color: #fff !important;
  font-size: 12px !important;
  color: #969696 !important;
  overflow: hidden;
}

.pagination-control ::v-deep .number.is-active {
  background-color: #12BCFF !important;
  font-weight: normal !important;
  color: #fff !important;
}

.pagination-control ::v-deep .el-input__wrapper {
  box-shadow: none !important;
}
</style>
