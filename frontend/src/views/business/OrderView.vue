<template>
  <div class="order-container">
    <div class="header">
      <h2>订单管理</h2>
      <div class="filters">
        <el-select v-model="filterStatus" placeholder="订单状态">
          <el-option label="全部" value="all" />
          <el-option label="待付款" value="pending" />
          <el-option label="已发货" value="shipped" />
          <el-option label="已完成" value="completed" />
        </el-select>
      </div>
    </div>

    <el-table :data="filteredOrders" style="width: 100%">
      <el-table-column prop="orderId" label="订单号" width="180" />
      <el-table-column prop="createTime" label="下单时间" />
      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="statusType[row.status]">
            {{ statusText[row.status] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="totalAmount" label="金额" width="120" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="viewDetail(row)">详情</el-button>
          <el-button 
            v-if="row.status === 'pending'" 
            size="small" 
            type="success"
            @click="shipOrder(row)"
          >
            发货
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      background
      layout="prev, pager, next"
      :total="totalOrders"
      :page-size="pageSize"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// 模拟数据
const orders = ref([
  { 
    orderId: '20231001001',
    createTime: '2023-10-01 14:30:00',
    status: 'pending',
    totalAmount: 299.00
  },
  { 
    orderId: '20231001002',
    createTime: '2023-10-01 15:00:00',
    status: 'shipped',
    totalAmount: 599.00
  }
])

const filterStatus = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)

const statusText = {
  pending: '待付款',
  shipped: '已发货',
  completed: '已完成'
}

const statusType = {
  pending: 'warning',
  shipped: 'primary',
  completed: 'success'
}

const filteredOrders = computed(() => {
  return orders.value.filter(order => 
    filterStatus.value === 'all' || order.status === filterStatus.value
  )
})

const totalOrders = computed(() => filteredOrders.value.length)

const viewDetail = (order) => {
  console.log('查看订单详情:', order)
}

const shipOrder = (order) => {
  console.log('订单发货:', order)
  order.status = 'shipped'
}

const handlePageChange = (page) => {
  currentPage.value = page
}
</script>

<style scoped>
.order-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filters {
  display: flex;
  gap: 10px;
}

.el-pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>