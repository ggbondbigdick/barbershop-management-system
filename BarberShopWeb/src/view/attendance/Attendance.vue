<template>
  <div class="index_info">
    <div class="search_from">
      <el-form :inline="true" ref="searchFormRef" :model="searchForm" class="search_form">
        <el-form-item v-if="role === '1'" label="名称" prop="staffName">
          <el-input v-model="searchForm.staffName" placeholder="请输入名称搜索" clearable/>
        </el-form-item>
        <el-form-item label="考勤月份查询" prop="monthRangeArray">
          <el-date-picker
              v-model="monthRangeArray"
              type="monthrange"
              value-format="YYYY-MM"
              range-separator="至"
              start-placeholder="开始月份"
              end-placeholder="结束月份"
              format="YYYY 年 MM 月"
              placeholder="选择月份"
              @change="onSelectMonth"
              clearable
          />
        </el-form-item>
      </el-form>
      <div class="btn_group">
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
        <el-button v-if="role === '1'" type="success" @click="handleAdd">全员一键考勤</el-button>
      </div>
    </div>
    <div class="data_table">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column align="center" prop="staffName" label="名称"/>
        <el-table-column align="center" label="考勤状态">
          <template #default="scope">
            <el-tag :type="scope.row.type === '签到' ? 'success' : 'warning'">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="加班">
          <template #default="scope">
            {{ scope.row.overtime ? scope.row.overtime : '--' }}
          </template>
        </el-table-column>
        <el-table-column align="center" prop="createTime" label="考勤时间"/>
        <el-table-column align="center" label="操作" width="230">
          <template #default="scope">
            <el-button link type="info" size="small" @click="handleView(scope.row.id)">查看考勤记录</el-button>
            <el-button v-if="scope.row.type === '签到' && scope.row.overtime === '否' && role === '1' && isDatePassed(scope.row.createTime)" link type="warning" size="small" @click="handleEdit(scope.row.id)">请假</el-button>
            <el-button v-if="scope.row.type === '签到' && scope.row.overtime === '否' && role === '1' && isDatePassed(scope.row.createTime)" link type="success" size="small" @click="handleOvertime(scope.row.id)">加班</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="data_table_pagination">
      <Pagination v-model:total="searchForm.total" @onChange="handleCurrentChange"/>
    </div>

    <AttendanceView
        ref="attendanceViewRef"
        v-model:attendance-view-visible="attendanceViewVisible"
        @close="closeAttendanceView"
    />
  </div>
</template>


<script setup lang="ts">
import {ref, reactive, onMounted, nextTick} from 'vue'
import Pagination from '@/components/Pagination.vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import AttendanceApi from '@/api/AttendanceApi'
import AttendanceView from '@/view/attendance/AttendanceView.vue'
import {getUserInfo} from '@/tool/loginTool'

const {role} = getUserInfo()
const searchFormRef = ref()
const searchForm = reactive({
  staffName: '',
  startMonth: '',
  endMonth: '',
  page: 1,
  size: 10,
  total: 0
})
const tableData = ref<any>([])
const monthRangeArray = ref<string[]>([])
const attendanceViewRef = ref()
const attendanceViewVisible = ref(false)


onMounted(() => {
  getTableDataList()
})

const getTableDataList = async () => {
  const res: any = await AttendanceApi.GetAttendancePaginationList(searchForm)
  if (res.success) {
    tableData.value = res.rows
    searchForm.total = res.total
  }
}

const handleSearch = () => {
  getTableDataList()
}

const handleReset = () => {
  searchFormRef.value.resetFields()
  monthRangeArray.value = []
  searchForm.startMonth = ''
  searchForm.endMonth = ''
  getTableDataList()
}

const handleCurrentChange = (val: any) => {
  searchForm.page = val.page
  searchForm.size = val.size
  getTableDataList()
}

const onSelectMonth = (val: any) => {
  if (val) {
    searchForm.startMonth = val[0]
    searchForm.endMonth = val[1]
  } else {
    searchForm.startMonth = ''
    searchForm.endMonth = ''
  }
}

// 检查时间字符串
const isDatePassed = (dateTimeStr: string): boolean => {
  // 分割日期和时间部分
  const [dateStr, timeStr] = dateTimeStr.split(' ')

  if (!dateStr || !timeStr) {
    throw new Error('Invalid date-time format. Expected "YYYY-MM-DD HH:MM:SS".')
  }

  // 解析年、月、日
  const [year, month, day] = dateStr.split('-').map(Number)
  // 解析时、分、秒
  const [hours, minutes, seconds] = timeStr.split(':').map(Number)

  // 创建目标时间对象（基于本地时区）
  const targetDate = new Date(year, month - 1, day, hours, minutes, seconds)

  // 验证日期有效性
  if (isNaN(targetDate.getTime())) {
    throw new Error('Invalid date or time value.')
  }

  // 获取当前时间
  const now = new Date()

  // 比较年、月、日是否相同
  return (
      targetDate.getFullYear() === now.getFullYear() &&
      targetDate.getMonth() === now.getMonth() &&
      targetDate.getDate() === now.getDate()
  )
}

const handleAdd = () => {
  ElMessageBox.confirm(
      '你确定为全部员工执行一键考勤吗 ?',
      '全员一键考勤',
      {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning',
      }
  ).then(async () => {
    const res: any = await AttendanceApi.SignIn()
    if (res.success) {
      ElMessage.success('全员一键考勤成功')
      getTableDataList()
    }
  })
}

const handleView = (id: string) => {
  attendanceViewVisible.value = true
  nextTick(() => {
    attendanceViewRef.value.initAttendanceViewForm(id)
  })
}

const handleEdit = (id: string) => {
  ElMessageBox.confirm(
      '你确定更改员工考勤状态为：请假吗 ?',
      '考勤状态变更',
      {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning',
      }
  ).then(async () => {
    const res: any = await AttendanceApi.ModifyAttendance({id: id, type: '2', remark: '请假'})
    if (res.success) {
      ElMessage.success('考勤状态变更成功')
      getTableDataList()
    }
  })
}

const closeAttendanceView = () => {
  attendanceViewVisible.value = false
  getTableDataList()
}

const handleOvertime = (id: string) => {
  ElMessageBox.confirm(
      '你确定为员工加班打卡吗 ?',
      '删除',
      {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning',
      }
  ).then(async () => {
    const res: any = await AttendanceApi.ModifyAttendance({id: id, overtime: '1', remark: '加班打卡'})
    if (res.success) {
      ElMessage.success('加班打卡成功')
      getTableDataList()
    }
  })
}
</script>

<style lang="less" scoped>
.index_info {
  display: flex;
  flex-direction: column;
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  transition: all 0.3s ease;
}

.search_from {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 18px 24px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);

  &:hover {
    box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
    transform: translateY(-2px);
  }
}

.search_form {
  display: flex;
  align-items: center;
  gap: 16px;

  .el-form-item {
    margin-bottom: 0;
    transition: all 0.3s ease;

    &:hover {
      transform: scale(1.02);
    }
  }
}

.el-input {
  width: 240px;
  transition: all 0.3s ease;

  &:deep(.el-input__wrapper) {
    border-radius: 8px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
    transition: all 0.3s ease;

    &:hover {
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    &.is-focus {
      box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
    }
  }
}

.btn_group {
  display: flex;
  gap: 12px;

  .el-button {
    border-radius: 8px;
    padding: 10px 20px;
    font-weight: 500;
    letter-spacing: 0.5px;
    transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    &:active {
      transform: translateY(0);
    }

    &--primary {
      background: linear-gradient(135deg, #409EFF 0%, #337ecc 100%);
      border: none;
    }

    &--success {
      background: linear-gradient(135deg, #67C23A 0%, #529b2e 100%);
      border: none;
    }
  }
}

.data_table {
  flex: 1;
  margin-bottom: 20px;
  padding: 16px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
  }
}

.el-table {
  width: 100%;
  height: calc(100% - 10px);
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;

  &:deep {
    th {
      background-color: #f8fafc;
      color: #34495e;
      font-weight: 600;
      font-size: 14px;
      letter-spacing: 0.5px;
      transition: all 0.3s ease;
    }

    td {
      transition: all 0.3s ease;
    }

    tr {
      &:hover {
        td {
          background-color: #f0f7ff !important;
          transform: scale(1.005);
        }
      }
    }

    .el-table__row--striped {
      td {
        background-color: #f9f9f9;
      }

      &:hover {
        td {
          background-color: #f0f7ff !important;
        }
      }
    }

    .el-button {
      transition: all 0.2s ease;

      &:hover {
        transform: translateY(-2px) scale(1.1);
      }

      &:active {
        transform: translateY(0) scale(1);
      }
    }
  }
}

.data_table_pagination {
  display: flex;
  justify-content: center;
  padding: 16px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
  }

  &:deep(.el-pagination) {
    .btn-prev, .btn-next, .number {
      border-radius: 8px;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      }
    }

    .is-active {
      background: linear-gradient(135deg, #409EFF 0%, #337ecc 100%);
      color: white;
      font-weight: bold;
    }
  }
}

/* 动画效果 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.customer_table, .search_from, .customer_pagination {
  animation: fadeIn 0.5s ease-out forwards;
}

.customer_table {
  animation-delay: 0.1s;
}

.customer_pagination {
  animation-delay: 0.2s;
}
</style>
