<template>
  <div class="order-section">
    <h2><i class="el-icon-tickets"></i> 订单管理</h2>
    <div class="filter-bar">
      <div class="filter-group">
        <el-select 
          v-model="orderFilter.status" 
          placeholder="订单状态" 
          style="width: 150px"
        >
          <el-option label="全部" value="all" />
          <el-option label="待付款" value="pending" />
          <el-option label="已付款" value="paid" />
          <el-option label="已发货" value="shipped" />
          <el-option label="已完成" value="completed" />
        </el-select>
        <el-date-picker
          v-model="orderFilter.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 350px"
        />
      </div>
      <el-button type="primary" icon="el-icon-search" @click="loadOrders">搜索</el-button>
    </div>

    <el-table :data="flattenedOrders" border style="width: 100%" class="data-table">
      <el-table-column prop="orderId" label="订单号" width="180" />
      <el-table-column prop="createTime" label="下单时间" width="180" />
      
      <el-table-column label="商品信息">
        <template #default="{ row }">
          <div class="product-row">
            <el-image 
              :src="row.item.image || '/placeholder-product.jpg'"
              style="width: 60px; height: 60px; border-radius: 4px;"
              fit="cover"
            />
            <div class="product-details">
              <div class="product-name">{{ row.item.name }}</div>
              <div class="product-meta">
                <span class="product-price">¥{{ (row.item.price || 0).toFixed(2) }} × {{ row.item.quantity || 1 }}</span>
                <span class="product-subtotal">小计: ¥{{ ((row.item.price || 0) * (row.item.quantity || 1)).toFixed(2) }}</span>
              </div>
            </div>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column label="金额" width="120" align="right">
        <template #default="{ row }">
          ¥{{ ((row.item.price || 0) * (row.item.quantity || 1)).toFixed(2) }}
        </template>
      </el-table-column>
      
      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="statusMap[row?.status]?.type || 'info'">
            {{ statusMap[row?.status]?.text || '未知状态' }}
          </el-tag>
        </template>
      </el-table-column>
      
      <!-- 操作列：增加发货按钮 -->
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <div v-if="row">
            <!-- 发货按钮：只对已付款订单显示 -->
            <el-button 
              v-if="row.status === 'paid' && row.isFirstItem"
              size="small" 
              type="success"
              icon="el-icon-truck"
              @click="handleShip(row.orderId)"
            >发货</el-button>
            <!-- 已发货状态显示 -->
            <span v-else-if="row.status === 'shipped' && row.isFirstItem">
              <el-tag type="success">已发货</el-tag>
            </span>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const orderFilter = ref({ status: 'all', dateRange: [] })
const orders = ref([])

// 从本地存储加载所有商品数据
const allProducts = ref([])

const statusMap = {
  pending: { text: '待付款', type: 'warning' },
  paid: { text: '已付款', type: 'primary' },
  shipped: { text: '已发货', type: 'success' },
  completed: { text: '已完成', type: 'info' }
}

// 创建扁平化的订单数据结构（每个商品一行）
const flattenedOrders = computed(() => {
  const myProductIds = allProducts.value.map(p => p.id)
  const result = []
  
  filteredOrders.value.forEach(order => {
    // 过滤出属于当前商家的商品
    const myItems = order.items?.filter(item => myProductIds.includes(item.id)) || []
    
    // 为每个商品创建一行
    myItems.forEach((item, index) => {
      result.push({
        ...order,
        item,
        isFirstItem: index === 0  // 标记是否是订单中的第一个商品
      })
    })
  })
  
  return result
})

// 过滤订单（保留原有功能）
const filteredOrders = computed(() => {
  const myProductIds = allProducts.value.map(p => p.id)
  
  return (orders.value || [])
    .filter(o => {
      // 检查订单是否包含当前商家的商品
      const hasMyProduct = o.items?.some(item => myProductIds.includes(item.id)) ?? false
      
      // 状态筛选
      const statusMatch = orderFilter.value.status === 'all' || 
                       o.status === orderFilter.value.status
      
      // 日期筛选
      const dateMatch = !orderFilter.value.dateRange?.length || (
        new Date(o.createTime) >= new Date(orderFilter.value.dateRange[0]) &&
        new Date(o.createTime) <= new Date(orderFilter.value.dateRange[1]))
      
      return statusMatch && dateMatch && hasMyProduct
    })
})

// 发货操作 - 只能对已付款订单进行发货
const handleShip = (orderId) => {
  const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
  const order = savedOrders.find(o => o.orderId === orderId)
  
  if (order && order.status === 'paid') {
    // 更新订单状态
    const updatedOrders = savedOrders.map(o => {
      if (o.orderId === orderId) {
        return {
          ...o,
          status: 'shipped',
          shipTime: new Date().toLocaleString()
        }
      }
      return o
    })
    
    localStorage.setItem('orders', JSON.stringify(updatedOrders))
    
    ElMessage.success('订单状态已更新为已发货')
    loadOrders() // 重新加载订单以更新状态显示
  } else {
    ElMessage.error('只能对已付款的订单进行发货操作')
  }
}

const loadOrders = () => {
  const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
  
  // 应用筛选条件
  let filteredOrders = [...savedOrders]
  
  // 状态筛选
  if (orderFilter.value.status !== 'all') {
    filteredOrders = filteredOrders.filter(order => order.status === orderFilter.value.status)
  }
  
  // 日期筛选
  if (orderFilter.value.dateRange && orderFilter.value.dateRange.length === 2) {
    const startDate = new Date(orderFilter.value.dateRange[0])
    const endDate = new Date(orderFilter.value.dateRange[1])
    endDate.setHours(23, 59, 59, 999) // 包含结束日期的全天
    
    filteredOrders = filteredOrders.filter(order => {
      const orderDate = new Date(order.createTime)
      return orderDate >= startDate && orderDate <= endDate
    })
  }
  
  orders.value = filteredOrders
}

// 从本地存储加载当前商家的商品
const loadProducts = () => {
  const businessId = localStorage.getItem('userId') || 'default'
  const storageKey = `businessProducts_${businessId}`
  const savedProducts = localStorage.getItem(storageKey)
  if (savedProducts) {
    allProducts.value = JSON.parse(savedProducts)
  } else {
    allProducts.value = []
  }
}

onMounted(() => {
  loadProducts() // 加载当前商家的商品
  loadOrders()   // 加载订单数据
})
</script>

<style scoped>
.order-section h2 {
  margin-top: 0;
  margin-bottom: 25px;
  color: #303133;
  font-size: 22px;
  font-weight: 600;
  display: flex;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.order-section h2 i {
  margin-right: 12px;
  font-size: 24px;
  color: #409eff;
}

.filter-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.filter-group {
  display: flex;
  gap: 15px;
}

.data-table {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.product-row {
  display: flex;
  align-items: center;
  gap: 15px;
}

.product-details {
  flex: 1;
}

.product-name {
  font-weight: 500;
  margin-bottom: 8px;
  font-size: 14px;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-price {
  color: #666;
  font-size: 13px;
}

.product-subtotal {
  font-weight: 600;
  color: #f56c6c;
  font-size: 14px;
}
</style>