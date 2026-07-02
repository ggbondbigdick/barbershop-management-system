<template>
  <div class="hairstyle-page">
    <section class="page-head">
      <p>Service Menu</p>
      <h2>选一个今天想做的造型</h2>
      <span>先浏览门店全部项目，再按分类缩小范围，最后选择你想预约的具体服务。</span>
    </section>

    <div class="category-tabs">
      <button
        v-for="category in categories"
        :key="category.id"
        :class="{ active: activeCategory === category.id }"
        @click="activeCategory = category.id"
      >
        {{ category.name }}
      </button>
    </div>

    <div v-if="filteredServices.length" class="service-grid">
      <article v-for="service in filteredServices" :key="service.id" class="service-card mobile-page-card">
        <div class="image-container">
          <img :src="service.thumbnailUrl" :alt="service.name" />
          <span class="price-tag">¥{{ service.price }}</span>
        </div>
        <div class="service-content">
          <div>
            <h3>{{ service.name }}</h3>
            <p>{{ service.introduce }}</p>
          </div>
          <button class="book-btn" @click="onAppointment(service.id)">立即预约</button>
        </div>
      </article>
    </div>

    <el-empty v-else description="该分类下暂无服务项目" class="empty-state" />

    <div v-if="totalPages > 1" class="pagination">
      <button
        v-for="page in totalPages"
        :key="page"
        :class="{ active: currentPage === page }"
        @click="currentPage = page"
      >
        {{ page }}
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, ref, watch} from 'vue'
import {useRouter} from 'vue-router'
import HaircuttingProjectApi from '@/api/HaircuttingProjectApi'

const router = useRouter()

const categories = ref([
  {id: 'all', name: '全部项目'},
  {id: '2', name: '理发'},
  {id: '3', name: '烫发'},
  {id: '4', name: '染发'},
  {id: '1', name: '洗发'},
  {id: '5', name: '头发保养'}
])

const services = ref([
  {
    id: 13,
    type: '1',
    name: '深层清洁洗发',
    price: 88,
    thumbnailUrl: 'https://example.com/wash1.jpg',
    introduce: '专业头皮清洁，去除多余油脂'
  }
])

const activeCategory = ref('all')
const currentPage = ref(1)
const itemsPerPage = 6

onMounted(() => {
  getProjectList()
})

watch(activeCategory, () => {
  currentPage.value = 1
})

const getProjectList = async () => {
  const {success, result} = await HaircuttingProjectApi.GetHomeHaircuttingProjectList() as any
  if (success) {
    services.value = result
  }
}

const categoryServices = computed(() => {
  if (activeCategory.value === 'all') {
    return services.value
  }
  return services.value.filter((item) => item.type === activeCategory.value)
})

const filteredServices = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  return categoryServices.value.slice(start, start + itemsPerPage)
})

const totalPages = computed(() => Math.ceil(categoryServices.value.length / itemsPerPage))

const onAppointment = (id: string | number) => {
  router.push({
    path: '/reserve',
    query: {id: String(id)}
  })
}
</script>

<style lang="less" scoped>
.hairstyle-page {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.page-head {
  padding: 8px 4px 0;

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
    font-size: clamp(24px, 4vw, 34px);
  }

  span {
    display: block;
    margin-top: 10px;
    color: var(--text-muted);
    line-height: 1.7;
  }
}

.category-tabs {
  display: flex;
  gap: 10px;
  overflow-x: auto;
  padding-bottom: 4px;
}

.category-tabs button {
  flex: none;
  min-width: 88px;
  min-height: 42px;
  padding: 0 18px;
  border: 1px solid var(--line-soft);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.72);
  color: var(--text-muted);
  font-weight: 600;
}

.category-tabs button.active {
  background: linear-gradient(135deg, var(--brand), var(--brand-dark));
  color: #fff;
  border-color: transparent;
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 18px;
}

.service-card {
  overflow: hidden;
  border-radius: 24px;
}

.image-container {
  position: relative;
  height: 220px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
  }
}

.price-tag {
  position: absolute;
  left: 16px;
  bottom: 16px;
  padding: 8px 12px;
  border-radius: 999px;
  background: rgba(45, 34, 27, 0.78);
  color: #fff;
  font-weight: 700;
}

.service-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 18px;

  h3,
  p {
    margin: 0;
  }

  h3 {
    font-size: 20px;
  }

  p {
    margin-top: 8px;
    color: var(--text-muted);
    line-height: 1.7;
  }
}

.book-btn {
  width: 100%;
  min-height: 46px;
  border: none;
  border-radius: 14px;
  background: linear-gradient(135deg, var(--brand), var(--brand-dark));
  color: #fff;
  font-weight: 700;
}

.empty-state {
  border-radius: 24px;
  padding: 28px 0;
  background: rgba(255, 255, 255, 0.52);
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 10px;
  padding-bottom: 8px;
}

.pagination button {
  width: 42px;
  height: 42px;
  border: 1px solid var(--line-soft);
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.72);
  color: var(--text-muted);
  font-weight: 700;
}

.pagination button.active {
  background: linear-gradient(135deg, var(--brand), var(--brand-dark));
  color: #fff;
  border-color: transparent;
}

@media (max-width: 768px) {
  .service-grid {
    grid-template-columns: 1fr;
  }

  .image-container {
    height: 200px;
  }
}
</style>
