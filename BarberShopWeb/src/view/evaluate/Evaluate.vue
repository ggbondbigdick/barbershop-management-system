<template>
  <div class="index_info">
    <div class="search_from">
      <el-form :inline="true" ref="searchFormRef" :model="searchForm" class="search_form">
        <el-form-item label="关键词" prop="keyword">
          <el-input v-model="searchForm.keyword" placeholder="请输入关键词搜索" clearable/>
        </el-form-item>
      </el-form>
      <div class="btn_group">
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
<!--        <el-button type="success" @click="handleAdd">新增</el-button>-->
      </div>
    </div>
    <div class="data_table">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column align="center" prop="evaluator" label="评价用户"/>
        <el-table-column align="center" prop="projectName" label="评价项目"/>
        <el-table-column align="center" label="评价内容" prop="content" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.content }}
          </template>
        </el-table-column>
        <el-table-column align="center" prop="createTime" label="评价时间"/>
        <el-table-column align="center" label="操作" width="180">
          <template #default="scope">
<!--            <el-button link type="primary" size="small" @click="handleEdit(scope.row.id)">编辑</el-button>-->
            <el-button link type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="data_table_pagination">
      <Pagination v-model:total="searchForm.total" @onChange="handleCurrentChange"/>
    </div>
  </div>
</template>


<script setup lang="ts">
import {ref, reactive, onMounted, nextTick} from 'vue'
import Pagination from '@/components/Pagination.vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import EvaluateApi from '@/api/EvaluateApi'

const searchFormRef = ref()
const searchForm = reactive({
  queryParam: '',
  keyword: '',
  page: 1,
  size: 10,
  total: 0
})
const tableData = ref<any>([])


onMounted(() => {
  getTableDataList()
})

const getTableDataList = async () => {
  const res: any = await  EvaluateApi.GetEvaluatePaginationList(searchForm)
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
  getTableDataList()
}

const handleCurrentChange = (val: any) => {
  searchForm.page = val.page
  searchForm.size = val.size
  getTableDataList()
}

const handleAdd = () => {
}

const handleEdit = (id: string) => {
}

const handleDelete = (id: string) => {
  ElMessageBox.confirm(
      '你确定删除这个评价信息吗 ?',
      '删除',
      {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning',
      }
  ).then(async () => {
    const res: any = await EvaluateApi.DeleteEvaluate(id)
    if (res.success) {
      ElMessage.success('删除成功')
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