<template>
  <div class="reserve-page">
    <div class="reserve-card mobile-page-card">
      <section class="reserve-header">
        <p>Appointment</p>
        <h2>服务项目预约</h2>
        <span>确认项目和日期后即可提交，整个表单已压缩成移动端单列操作流。</span>
      </section>

      <section class="service-card">
        <div class="image-container">
          <img :src="serviceData.thumbnailUrl" alt="服务项目" loading="lazy" />
          <div class="category-badge">精选服务</div>
        </div>

        <div class="info-container">
          <h3>{{ serviceData.name }}</h3>
          <p class="description">{{ serviceData.introduce }}</p>
          <div class="price">¥ {{ serviceData.price }} RMB</div>
        </div>
      </section>

      <el-form ref="formRef" :model="formData" :rules="rules" label-position="top" class="reserve-form">
        <el-form-item label="预约日期" prop="date">
          <el-date-picker
            v-model="formData.date"
            type="date"
            placeholder="请选择日期"
            :disabled-date="disabledPastDates"
            popper-class="custom-datepicker"
            style="width: 100%"
            value-format="YYYY-MM-DD"
            format="YYYY 年 MM 月 DD 日"
            clearable
          />
        </el-form-item>

        <el-form-item label="选择理发师" prop="reserveStaff">
          <div class="staff-choice">
            <button
              type="button"
              class="staff-card recommend-card"
              :class="{ active: formData.reserveStaff === '' }"
              @click="selectStaff('')"
            >
              <strong>不指定理发师</strong>
              <span>由门店根据项目和当日安排为你分配</span>
            </button>

            <div v-if="staffLoading" class="staff-loading">正在加载可预约理发师...</div>

            <div v-else-if="formData.date && staffOptions.length" class="staff-list">
              <button
                v-for="staff in staffOptions"
                :key="staff.id"
                type="button"
                :disabled="!staff.available"
                class="staff-card"
                :class="{ active: formData.reserveStaff === staff.id, disabled: !staff.available }"
                @click="selectStaff(staff.id)"
              >
                <div class="staff-top">
                  <img v-if="staff.avatarUrl" :src="staff.avatarUrl" :alt="staff.name" />
                  <div v-else class="staff-avatar-placeholder">{{ staff.name?.slice(0, 1) || '理' }}</div>
                  <div>
                    <strong>{{ staff.name }}</strong>
                    <p>{{ staff.goodTypeLabel }}</p>
                  </div>
                </div>
                <span class="staff-status" :class="staff.status.toLowerCase()">{{ staff.statusLabel }}</span>
              </button>
            </div>

            <div v-else-if="formData.date" class="staff-empty">
              暂无符合该项目的理发师，请先到员工管理里补充擅长项目。
            </div>

            <div v-else class="staff-empty">
              先选择预约日期，再查看理发师当前可预约情况。
            </div>
          </div>
        </el-form-item>

        <el-form-item label="备注信息" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注（如特殊要求）"
            :autosize="{ minRows: 3, maxRows: 5 }"
          />
        </el-form-item>

        <el-button type="primary" @click="submitForm" class="submit-button">
          预约提交
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref, watch} from 'vue'
import {ElMessage} from 'element-plus'
import {checkLogin} from '@/tool/loginTool'
import {useRoute, useRouter} from 'vue-router'
import HaircuttingProjectApi from '@/api/HaircuttingProjectApi'
import ReserveApi from '@/api/ReserveApi'
import {getAvailableStaff} from '@/api/UserApi'

const route = useRoute()
const router = useRouter()
const formRef = ref()

const formData = ref({
  date: '',
  reserveStaff: '',
  remark: ''
})

const staffLoading = ref(false)
const staffOptions = ref<any[]>([])

const serviceData = reactive<any>({
  thumbnailUrl: 'https://via.placeholder.com/300x200?text=专业剪发',
  name: '男士剪发套餐',
  introduce: '包含洗头、剪发、造型、肩颈按摩，专业造型师为您打造时尚发型',
  price: 98
})

const rules = {
  date: [{required: true, message: '请选择预约日期', trigger: 'change'}]
}

let id = ''

onMounted(() => {
  id = route.query.id as string
  if (id) {
    getServiceProjectInfo(id)
  }
})

watch(() => formData.value.date, async (val) => {
  formData.value.reserveStaff = ''
  if (val && serviceData.type) {
    await loadAvailableStaff()
  } else {
    staffOptions.value = []
  }
})

const getServiceProjectInfo = async (serviceId: string) => {
  const res: any = await HaircuttingProjectApi.GetHaircuttingProjectInfo(serviceId)
  if (res.success) {
    Object.assign(serviceData, res.result)
    if (formData.value.date) {
      loadAvailableStaff()
    }
  }
}

const loadAvailableStaff = async () => {
  if (!serviceData.type || !formData.value.date) return
  staffLoading.value = true
  const res: any = await getAvailableStaff(serviceData.type, formData.value.date)
  staffLoading.value = false
  if (res.success) {
    staffOptions.value = res.result || []
  }
}

const selectStaff = (staffId: string) => {
  formData.value.reserveStaff = staffId
}

const disabledPastDates = (time: Date) => time.getTime() < Date.now() - 86400000

const submitForm = () => {
  if (!checkLogin()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      const data = {
        type: '1',
        reserveObject: id,
        reserveTime: formData.value.date,
        reserveStaff: formData.value.reserveStaff,
        remark: formData.value.remark
      }
      const res: any = await ReserveApi.AddReserve(data)
      if (res.success) {
        ElMessage.success('预约成功')
        router.push('/myReserve')
      }
    }
  })
}
</script>

<style lang="less" scoped>
.reserve-page {
  padding-bottom: 8px;
}

.reserve-card {
  max-width: 760px;
  margin: 0 auto;
  padding: 22px;
  border-radius: 28px;
}

.reserve-header {
  margin-bottom: 18px;

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

.service-card {
  display: grid;
  grid-template-columns: 220px 1fr;
  gap: 18px;
  padding: 14px;
  margin-bottom: 22px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.72);
  border: 1px solid rgba(123, 66, 33, 0.08);
}

.image-container {
  position: relative;
  min-height: 190px;

  img {
    width: 100%;
    height: 100%;
    display: block;
    object-fit: cover;
    border-radius: 18px;
  }
}

.category-badge {
  position: absolute;
  left: 12px;
  top: 12px;
  padding: 6px 10px;
  border-radius: 999px;
  background: rgba(45, 34, 27, 0.78);
  color: #fff;
  font-size: 12px;
}

.info-container {
  display: flex;
  flex-direction: column;
  justify-content: center;

  h3,
  p {
    margin: 0;
  }

  h3 {
    font-size: 22px;
  }
}

.description {
  margin-top: 10px !important;
  color: var(--text-muted);
  line-height: 1.7;
}

.price {
  margin-top: 18px;
  font-size: 22px;
  font-weight: 700;
  color: var(--brand-dark);
}

.reserve-form :deep(.el-form-item__label) {
  font-weight: 700;
  color: var(--text-main);
}

.staff-choice {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.staff-list {
  display: grid;
  grid-template-columns: 1fr;
  gap: 12px;
}

.staff-card {
  width: 100%;
  padding: 14px;
  border: 1px solid rgba(123, 66, 33, 0.12);
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.72);
  text-align: left;
}

.staff-card.active {
  border-color: var(--brand);
  box-shadow: 0 0 0 2px rgba(181, 106, 59, 0.12);
}

.staff-card.disabled {
  opacity: 0.72;
}

.recommend-card strong,
.staff-top strong {
  display: block;
  font-size: 16px;
}

.recommend-card span,
.staff-top p,
.staff-empty,
.staff-loading {
  display: block;
  margin-top: 6px;
  color: var(--text-muted);
  line-height: 1.6;
}

.staff-top {
  display: flex;
  align-items: center;
  gap: 12px;
}

.staff-top img,
.staff-avatar-placeholder {
  width: 52px;
  height: 52px;
  border-radius: 16px;
  object-fit: cover;
}

.staff-avatar-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(181, 106, 59, 0.14);
  color: var(--brand-dark);
  font-weight: 700;
}

.staff-status {
  display: inline-flex;
  margin-top: 10px;
  padding: 5px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
}

.staff-status.available {
  background: rgba(103, 194, 58, 0.14);
  color: #4b8c2e;
}

.staff-status.booked,
.staff-status.leave {
  background: rgba(245, 108, 108, 0.12);
  color: #c45656;
}

.staff-status.off_duty {
  background: rgba(144, 147, 153, 0.14);
  color: #73767a;
}

.reserve-form :deep(.el-input__wrapper),
.reserve-form :deep(.el-textarea__inner) {
  border-radius: 18px;
  padding: 12px 14px;
  box-shadow: none;
  background: rgba(255, 255, 255, 0.8);
}

.submit-button {
  width: 100%;
  min-height: 48px;
  margin-top: 6px;
  border: none;
  border-radius: 16px;
  background: linear-gradient(135deg, var(--brand), var(--brand-dark));
}

@media (max-width: 768px) {
  .reserve-card {
    padding: 18px;
    border-radius: 24px;
  }

  .service-card {
    grid-template-columns: 1fr;
  }

  .image-container {
    min-height: 200px;
  }
}
</style>
