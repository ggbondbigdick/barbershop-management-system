<template>
  <div class="appointment-page">
    <section class="page-header">
      <p>Orders</p>
      <h2>我的预约</h2>
      <span>订单状态、支付、评价和删除操作都收进了移动端卡片底部。</span>
    </section>

    <div v-if="appointments.length" class="appointment-list">
      <article v-for="item in appointments" :key="item.id" class="appointment-card mobile-page-card">
        <div class="card-top">
          <img :src="item.relatedThumbnail" alt="服务项目" loading="lazy" />
          <div class="card-info">
            <div>
              <h3>{{ item.relatedName }}</h3>
              <p class="price">¥ {{ item.relatedPrice }} RMB</p>
            </div>
            <el-tag :type="statusTagType(item.state)" size="small" :effect="statusTagEffect(item.state)">
              {{ statusText(item.state) }}
            </el-tag>
          </div>
        </div>

        <div class="meta-list">
          <div class="meta-item">
            <span>预约时间</span>
            <strong>{{ item.reserveTime }}</strong>
          </div>
          <div class="meta-item">
            <span>指定理发师</span>
            <strong>{{ item.reserveStaffName || '门店安排' }}</strong>
          </div>
          <div class="meta-item">
            <span>支付时间</span>
            <strong>{{ item.payTime ? item.payTime : '---' }}</strong>
          </div>
        </div>

        <div class="card-actions">
          <el-button
            v-if="item.state === '3'"
            type="primary"
            @click="handlePay(item)"
            class="action-btn"
            :icon="payIcon"
            :loading="payingId === item.id"
            :disabled="!!payingId"
          >
            立即支付
          </el-button>

          <el-button
            v-if="item.state === '5' && item.evaluate === '2'"
            type="primary"
            @click="handleEvaluation(item)"
            class="action-btn"
          >
            评价
          </el-button>

          <el-button
            v-if="item.state === '1' || item.state === '3'"
            type="danger"
            @click="handleCancel(item)"
            class="action-btn danger-btn"
          >
            取消预约
          </el-button>
        </div>
      </article>
    </div>

    <div v-else class="empty-wrap mobile-page-card">
      <el-empty description="暂无预约记录" />
    </div>

    <div class="pagination" v-if="total > 6">
      <el-pagination
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="currentPage"
        background
        @current-change="handleCurrentChange"
      />
    </div>

    <EvaluateBox
      ref="evaluationBoxRef"
      v-model:evaluate-visible="evaluationBoxVisible"
      @close="closeEvaluationBox"
    />
  </div>
</template>

<script setup lang="ts">
import {nextTick, onMounted, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {DocumentChecked} from '@element-plus/icons-vue'
import ReserveApi from '@/api/ReserveApi'
import EvaluateBox from '@/view/evaluate/EvaluateBox.vue'

const evaluationBoxRef = ref()
const evaluationBoxVisible = ref(false)
const currentPage = ref(1)
const total = ref(0)
const pageSize = 6
const appointments = ref<any[]>([])
const payingId = ref('')

const loadData = async () => {
  const res: any = await ReserveApi.GetReservePaginationList({
    page: currentPage.value,
    size: pageSize
  })
  if (res.success) {
    appointments.value = res.rows
    total.value = res.total
  }
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadData()
}

const statusText = (status: string): string => {
  const map: Record<string, string> = {
    '1': '预约中',
    '2': '预约成功',
    '3': '待支付',
    '4': '正在服务',
    '5': '服务完成',
    '6': '预约失败',
    '7': '已取消'
  }
  return map[status] || '未知状态'
}

const statusTagType = (status: string): string => {
  const map: Record<string, string> = {
    '1': 'info',
    '2': 'success',
    '3': 'warning',
    '4': 'primary',
    '5': 'success',
    '6': 'danger',
    '7': 'info'
  }
  return map[status] || 'default'
}

const statusTagEffect = (status: string): string => {
  const map: Record<string, string> = {
    '1': 'dark',
    '2': 'light',
    '3': 'plain',
    '4': 'light',
    '5': 'light',
    '6': 'plain',
    '7': 'plain'
  }
  return map[status] || 'light'
}

const payIcon = DocumentChecked

const handlePay = async (item: any) => {
  if (payingId.value) return
  payingId.value = item.id
  try {
    const res: any = await ReserveApi.ReservePay(item.id)
    if (res.success) {
      ElMessage.success('支付成功')
      loadData()
    }
  } finally {
    payingId.value = ''
  }
}

const handleEvaluation = async (item: any) => {
  ElMessageBox.confirm('你确定要评价该预约吗？', '评价', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning'
  }).then(async () => {
    evaluationBoxVisible.value = true
    nextTick(() => evaluationBoxRef.value.initEvaluateForm(item.id))
  })
}

const closeEvaluationBox = () => {
  evaluationBoxVisible.value = false
  loadData()
}

const handleCancel = async (item: any) => {
  ElMessageBox.confirm(
    '你确定取消预约吗？',
    '取消预约',
    {
      confirmButtonText: '是',
      cancelButtonText: '否',
      type: 'warning'
    }
  ).then(async () => {
    const res: any = await ReserveApi.CancelReserve(item.id)
    if (res.success) {
      ElMessage.success('操作成功')
      loadData()
    }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style lang="less" scoped>
.appointment-page {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.page-header {
  p,
  h2,
  span {
    margin: 0;
  }

  p {
    color: var(--brand);
    font-size: 12px;
    font-weight: 700;
    letter-spacing: 0.18em;
    text-transform: uppercase;
  }

  h2 {
    margin-top: 8px;
    font-size: clamp(24px, 4vw, 32px);
  }

  span {
    display: block;
    margin-top: 10px;
    color: var(--text-muted);
    line-height: 1.7;
  }
}

.appointment-list {
  display: grid;
  gap: 16px;
}

.appointment-card {
  padding: 18px;
  border-radius: 24px;
}

.card-top {
  display: grid;
  grid-template-columns: 112px 1fr;
  gap: 14px;

  img {
    width: 112px;
    height: 112px;
    border-radius: 18px;
    object-fit: cover;
  }
}

.card-info {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 12px;

  h3,
  p {
    margin: 0;
  }
}

.price {
  margin-top: 8px !important;
  color: var(--brand-dark);
  font-size: 18px;
  font-weight: 700;
}

.meta-list {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
  margin-top: 16px;
}

.meta-item {
  padding: 12px 14px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.6);

  span,
  strong {
    display: block;
  }

  span {
    margin-bottom: 6px;
    color: var(--text-muted);
    font-size: 13px;
  }

  strong {
    line-height: 1.5;
  }
}

.card-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 16px;
}

.action-btn {
  min-height: 42px;
  margin: 0;
  border-radius: 14px;
}

.empty-wrap {
  border-radius: 24px;
  padding: 20px 0;
}

.pagination {
  display: flex;
  justify-content: center;
  padding-bottom: 6px;
}

@media (max-width: 768px) {
  .card-top,
  .meta-list {
    grid-template-columns: 1fr;
  }

  .card-top img {
    width: 100%;
    height: 180px;
  }

  .card-actions :deep(.el-button) {
    flex: 1 1 calc(50% - 5px);
  }
}
</style>
