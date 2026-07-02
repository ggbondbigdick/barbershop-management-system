<template>
  <el-dialog
      :model-value="attendanceViewVisible"
      :show-close="false"
      :close-on-click-modal="false"
      center
      class="attendance-dialog"
      width="620px"
  >
    <template #default>
      <div class="dialog-container">
        <!-- 装饰元素 -->
        <div class="decorative-border"></div>

        <!-- 头部 -->
        <div class="dialog-header">
          <div class="header-content">
            <div class="avatar-wrapper">
              <el-avatar :size="60" :src="attendanceViewForm.staffAvatar" class="pulse-animation">
                {{ attendanceViewForm.staffName?.slice(0, 1) || '员' }}
              </el-avatar>
              <span class="status-badge">{{ attendanceViewForm.staffRegisterTime || '注册日期未设置' }}</span>
            </div>
            <h2 class="employee-name">{{ attendanceViewForm.staffName || '张三' }}</h2>
          </div>
        </div>

        <!-- 主体内容 -->
        <div class="dialog-body">
          <el-row :gutter="20" class="stats-grid">
            <el-col :span="12" v-for="(value, key) in stats" :key="key">
              <div class="stat-card" :class="`card-${key}`">
                <div class="card-header">
                  <span class="card-label">{{ statLabels[key] }}</span>
                  <el-icon name="document" class="card-icon"></el-icon>
                </div>
                <div class="card-value">
                  {{ value }}
                  <span class="unit">{{ getUnit(key) }}</span>
                </div>
                <div class="progress-bar">
                  <div
                      class="progress-fill"
                      :style="{
                      width: getProgressWidth(key, value),
                      backgroundColor: getProgressColor(key)
                    }"
                  ></div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 底部 -->
        <div class="dialog-footer">
          <el-button @click="cancel" class="action-button">
            <i class="el-icon-close"></i> 关闭
          </el-button>
        </div>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, computed } from 'vue'
import AttendanceApi from '@/api/AttendanceApi'

defineProps(['attendanceViewVisible'])
const emit = defineEmits(['close'])
defineExpose({ initAttendanceViewForm })

// 表单数据
let attendanceViewForm = reactive({
  id: '',
  staffName: '',
  staffAvatar: '',
  staffRegisterTime: '',
  attendanceDays: 0,
  overTimeDays: 0,
  leaveDays: 0,
  salary: 0,
  period: ''
})

// 统计数据映射
const stats = computed(() => ({
  attendance: attendanceViewForm.attendanceDays,
  overtime: attendanceViewForm.overTimeDays,
  leave: attendanceViewForm.leaveDays,
  salary: attendanceViewForm.salary
}))

// 统计项标签
const statLabels = {
  attendance: '出勤天数',
  overtime: '加班天数',
  leave: '请假天数',
  salary: '实发工资'
}

function initAttendanceViewForm(id: string) {
  getAttendanceViewDataInfo(id)
}

// 数据请求
const getAttendanceViewDataInfo = async (id: string) => {
  const {success, result} = await AttendanceApi.GetAttendanceInfo(id) as any
  if (success) {
    Object.assign(attendanceViewForm, result)
  }
}

// 获取单位
function getUnit(key: string): string {
  switch(key) {
    case 'salary': return '元'
    case 'attendance':
    case 'overtime':
    case 'leave': return '天'
    default: return ''
  }
}

// 获取进度宽度
function getProgressWidth(key: string, value: number): string {
  switch(key) {
    case 'attendance': return `${Math.min((value / 22) * 100, 100)}%`
    case 'overtime': return `${Math.min(value * 10, 100)}%`
    case 'leave': return `${Math.min(value * 5, 100)}%`
    case 'salary': return `${Math.min((value / 12000) * 100, 100)}%`
    default: return '0%'
  }
}

// 获取进度条颜色
function getProgressColor(key: string): string {
  switch(key) {
    case 'attendance': return '#409EFF'
    case 'overtime': return '#67C23A'
    case 'leave': return '#F56C6C'
    case 'salary': return '#E6A23C'
    default: return '#999'
  }
}

// 关闭对话框
const cancel = () => {
  emit('close')
}
</script>

<style lang="less" scoped>
// 基础变量
@primary-color: #409EFF;
@success-color: #67C23A;
@warning-color: #E6A23C;
@danger-color: #F56C6C;
@text-color: #333;
@sub-text-color: #888;
@card-bg: #ffffff;
@border-radius: 12px;

.attendance-dialog {
  :deep(.el-dialog__header) { display: none; }
  :deep(.el-dialog__body) { padding: 0; }
}

.dialog-container {
  position: relative;
  background: linear-gradient(135deg, #ffffff 0%, #f8faff 100%);
  border-radius: @border-radius;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  font-family: 'Segoe UI', sans-serif;
}

// 装饰元素
.decorative-border {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, @primary-color, @success-color, @warning-color, @danger-color);
}

// 头部样式
.dialog-header {
  position: relative;
  background: linear-gradient(135deg, @primary-color 0%, #337ecc 100%);
  padding: 24px 32px;
  color: white;
  z-index: 1;
}

.header-content {
  position: relative;
  z-index: 2;
}

.avatar-wrapper {
  position: relative;
  display: inline-block;
  margin-bottom: 12px;
}

.pulse-animation {
  animation: pulse 2s infinite;
}

.status-badge {
  position: absolute;
  bottom: -4px;
  right: -4px;
  background: white;
  color: @primary-color;
  padding: 2px 8px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.employee-name {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

// 主体内容样式
.dialog-body {
  padding: 24px 32px;
}

.stats-grid {
  margin-bottom: 24px;
}

// 统计卡片
.stat-card {
  position: relative;
  background: @card-bg;
  border-radius: @border-radius;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border-left: 4px solid @primary-color;
  overflow: hidden;
  height: 120px;
  margin-bottom: 16px;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
    border-left-width: 6px;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.card-label {
  font-size: 14px;
  color: @sub-text-color;
  font-weight: 500;
}

.card-icon {
  color: @primary-color;
  font-size: 16px;
}

.card-value {
  font-size: 24px;
  font-weight: 700;
  color: @text-color;
  margin-bottom: 10px;
}

.unit {
  font-size: 14px;
  color: @sub-text-color;
  margin-left: 4px;
}

.progress-bar {
  height: 6px;
  background: #eef1f6;
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 0.5s ease;
}

// 底部样式
.dialog-footer {
  padding: 20px 32px;
  border-top: 1px solid #eee;
  background: #fff;
  text-align: right;
}

.action-button {
  border-radius: 8px;
  padding: 10px 24px;
  font-weight: 500;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  background: linear-gradient(135deg, #f0f3f8 0%, #e9e9eb 100%);
  border: none;
  color: #606266;

  &:hover {
    background: linear-gradient(135deg, #f0f3f8 0%, #dcdde1 100%);
    color: #333;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  &:active {
    transform: translateY(0);
  }
}

// 动画
@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.05); }
  100% { transform: scale(1); }
}

// 响应式设计
@media (max-width: 640px) {
  .dialog-container {
    width: 95vw;
    max-width: 95vw;
    border-radius: 8px;
  }

  .dialog-body {
    padding: 16px 24px;
  }

  .employee-name {
    font-size: 20px;
  }

  .stat-card {
    height: auto;
    padding: 16px;
  }

  .card-value {
    font-size: 20px;
  }
}
</style>
