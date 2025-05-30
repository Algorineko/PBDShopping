<template>
  <div class="user-orders">
    <h2>我的订单</h2>
    
    <!-- 订单筛选 -->
    <div class="filter-section">
      <el-select v-model="filterStatus" placeholder="全部状态" @change="loadOrders">
        <el-option
          v-for="status in statusOptions"
          :key="status.value"
          :label="status.label"
          :value="status.value"
        />
      </el-select>
      <el-date-picker
        v-model="filterDate"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        @change="loadOrders"
      />
    </div>

    <!-- 订单列表 -->
    <el-table 
      :data="orderLines"
      v-if="orderLines.length > 0"
      border 
      style="width: 100%"
    >
      <el-table-column prop="orderId" label="订单号" width="200" />
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column label="商品信息">
        <template #default="{ row }">
          <div class="product-list">
            <div class="product-item">
              <router-link :to="`/buyer/product/${row.item.id}`">
                <el-image 
                  :src="row.item.image"
                  style="width: 60px; height: 60px; cursor: pointer"
                  fit="cover"
                />
              </router-link>
              <div class="product-details">
                <router-link 
                  :to="`/buyer/product/${row.item.id}`"
                  class="product-name"
                >
                  {{ row.item.name }}
                </router-link>
                <div class="product-price">
                  ¥{{ (row.item.price || 0).toFixed(2) }} × {{ row.item.quantity || 1 }}
                </div>
              </div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="金额" width="120" align="right">
        <template #default="scope">
          ¥{{ (scope.row.item.price * scope.row.item.quantity).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="120">
        <template #default="scope">
          <el-tag :type="statusType[scope.row.status] || 'info'">
            {{ statusText[scope.row.status] || '未知状态' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <!-- 订单操作按钮（仅在第一行显示） -->
          <div v-if="scope.row.isFirst">
            <!-- 付款按钮：仅待付款状态显示 -->
            <el-button 
              v-if="scope.row.status === 'pending'"
              type="success" 
              size="small"
              @click="payOrder(scope.row.orderId)"
            >
              付款
            </el-button>
            
            <!-- 确认收货按钮：仅已发货状态显示 -->
            <el-button 
              v-if="scope.row.status === 'shipped'"
              type="warning" 
              size="small"
              @click="confirmReceipt(scope.row.orderId)"
            >
              确认收货
            </el-button>
            
            <!-- 删除订单按钮：所有状态都可以删除 -->
            <el-button 
              type="danger" 
              size="small"
              icon="el-icon-delete"
              @click="deleteOrder(scope.row.orderId)"
            >
              删除
            </el-button>
          </div>
          
          <!-- 商品评价按钮（每个商品行都显示） -->
          <el-button 
            v-if="scope.row.status === 'completed' && scope.row.isPaid && !scope.row.item.reviewed"
            type="warning" 
            size="small"
            @click="openReviewDialog(scope.row)"
          >
            评价
          </el-button>
          
          <!-- 已评价标记 -->
          <el-tag v-if="scope.row.status === 'completed' && scope.row.isPaid && scope.row.item.reviewed" type="success">
            已评价
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态提示 -->
    <el-empty v-else description="暂无订单数据" class="empty-placeholder" />

    <!-- 分页 -->
    <div class="pagination" v-if="orderLines.length > 0">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        @current-change="handlePageChange"
      />
    </div>

    <!-- 评价对话框 -->
    <el-dialog v-model="reviewDialogVisible" title="商品评价">
      <el-form :model="reviewForm">
        <el-form-item label="评分">
          <el-rate v-model="reviewForm.rating" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input
            v-model="reviewForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入您的使用体验"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReview">提交评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 订单状态配置
const statusText = {
  pending: '待付款',
  paid: '待发货',
  shipped: '已发货',
  completed: '已完成',
  canceled: '已取消'
}

const statusType = {
  pending: 'warning',
  paid: 'primary',
  shipped: 'primary',
  completed: 'success',
  canceled: 'info'
}

// 筛选选项
const statusOptions = ref([
  { value: 'all', label: '全部状态' },
  { value: 'pending', label: '待付款' },
  { value: 'paid', label: '待发货' },
  { value: 'shipped', label: '已发货' },
  { value: 'completed', label: '已完成' }
])

// 订单数据
const orderLines = ref([]) // 用于存储拆分后的订单行
const filterStatus = ref('all')
const filterDate = ref([])
const total = ref(0)
const pageSize = ref(10)
const currentPage = ref(1)

// 评价相关
const reviewDialogVisible = ref(false)
const reviewForm = ref({
  orderId: '',
  productId: '',
  rating: 5,
  content: ''
})
const currentReviewOrderLine = ref(null)

// 从localStorage加载订单数据
const loadOrders = async () => {
  try {
    const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
    
    // 应用筛选条件
    let filteredOrders = [...savedOrders]
    
    // 状态筛选
    if (filterStatus.value !== 'all') {
      filteredOrders = filteredOrders.filter(order => order.status === filterStatus.value)
    }
    
    // 日期筛选
    if (filterDate.value && filterDate.value.length === 2) {
      const startDate = new Date(filterDate.value[0])
      const endDate = new Date(filterDate.value[1])
      endDate.setHours(23, 59, 59, 999) // 包含结束日期的全天
      
      filteredOrders = filteredOrders.filter(order => {
        const orderDate = new Date(order.createTime)
        return orderDate >= startDate && orderDate <= endDate
      })
    }
    
    // 拆分订单为商品行
    const lines = []
    filteredOrders.forEach(order => {
      order.items.forEach((item, index) => {
        lines.push({
          ...order,
          item, // 当前商品项
          isFirst: index === 0 // 标记是否是订单的第一行
        })
      })
    })
    
    // 分页处理
    const start = (currentPage.value - 1) * pageSize.value
    orderLines.value = lines.slice(start, start + pageSize.value)
    total.value = lines.length
    
  } catch (error) {
    console.error('订单加载失败:', error)
    ElMessage.error('订单加载失败')
    orderLines.value = []
  }
}

const openReviewDialog = (orderLine) => {
  currentReviewOrderLine.value = orderLine
  reviewForm.value = {
    orderId: orderLine.orderId,
    productId: orderLine.item.id || '', // 当前商品ID
    rating: 5,
    content: ''
  }
  reviewDialogVisible.value = true
}

const submitReview = () => {
  const reviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
  // 添加商品快照信息 - 这是关键修改
  const newReview = {
    ...reviewForm.value,
    date: new Date().toLocaleString(),
    itemSnapshot: {
      id: currentReviewOrderLine.value.item.id,
      name: currentReviewOrderLine.value.item.name,
      price: currentReviewOrderLine.value.item.price,
      quantity: currentReviewOrderLine.value.item.quantity,
      image: currentReviewOrderLine.value.item.image
    }
  }
  
  reviews.push(newReview)
  
  localStorage.setItem('productReviews', JSON.stringify(reviews))
  
  // 标记该商品已评价
  const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
  const updatedOrders = savedOrders.map(order => {
    if (order.orderId === currentReviewOrderLine.value.orderId) {
      // 更新订单中该商品的评价状态
      const updatedItems = order.items.map(item => {
        if (item.id === currentReviewOrderLine.value.item.id) {
          return { ...item, reviewed: true }
        }
        return item
      })
      return { ...order, items: updatedItems }
    }
    return order
  })
  
  localStorage.setItem('orders', JSON.stringify(updatedOrders))
  
  ElMessage.success('评价提交成功')
  reviewDialogVisible.value = false
  loadOrders()
}

// 支付订单
const payOrder = (orderId) => {
  if (!orderId) {
    ElMessage.warning('无效的订单号')
    return
  }
  
  // 更新订单状态为已付款（待发货）
  const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
  const updatedOrders = savedOrders.map(order => {
    if (order.orderId === orderId) {
      return {
        ...order,
        status: 'paid', // 状态变为待发货
        isPaid: true
      }
    }
    return order
  })
  
  localStorage.setItem('orders', JSON.stringify(updatedOrders))
  
  ElMessage.success(`订单 #${orderId} 支付成功，等待商家发货`)
  loadOrders()
}

// 确认收货
const confirmReceipt = (orderId) => {
  if (!orderId) {
    ElMessage.warning('无效的订单号')
    return
  }
  
  // 更新订单状态为已完成
  const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
  const updatedOrders = savedOrders.map(order => {
    if (order.orderId === orderId) {
      return {
        ...order,
        status: 'completed'
      }
    }
    return order
  })
  
  localStorage.setItem('orders', JSON.stringify(updatedOrders))
  
  ElMessage.success(`订单 #${orderId} 确认收货成功`)
  loadOrders()
}

// 删除订单
const deleteOrder = (orderId) => {
  ElMessageBox.confirm(
    '确定要删除这个订单吗？删除后无法恢复',
    '警告',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // 从本地存储中删除订单
    const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
    const filteredOrders = savedOrders.filter(order => order.orderId !== orderId)
    localStorage.setItem('orders', JSON.stringify(filteredOrders))
    
    ElMessage.success('订单删除成功')
    loadOrders()
  }).catch(() => {
    // 用户取消了删除操作
  })
}

// 分页处理
const handlePageChange = (page) => {
  currentPage.value = page
  loadOrders()
}

// 初始化加载
onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.user-orders {
  padding: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.filter-section {
  margin-bottom: 20px;
  display: flex;
  gap: 15px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.el-table {
  margin-top: 15px;
}

.empty-placeholder {
  margin-top: 50px;
}

.el-tag {
  margin: 2px 0;
}

.product-list {
  padding: 10px 0;
}

.product-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
}

.product-details {
  flex: 1;
}

.product-name {
  color: #606266;
  text-decoration: none;
  display: block;
  margin-bottom: 5px;
  &:hover {
    color: #409eff;
    text-decoration: underline;
  }
}

.product-price {
  color: #f56c6c;
  font-size: 14px;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .filter-section {
    flex-direction: column;
  }
  
  .el-table {
    overflow-x: auto;
  }
}
</style>