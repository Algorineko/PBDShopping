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
      :data="orders"
      v-if="orders.length > 0"
      border 
      style="width: 100%"
    >
      <el-table-column prop="orderId" label="订单号" width="200" />
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column label="商品信息" width="220">
        <template #default="{ row }">
          <div class="product-info">
            <router-link :to="`/buyer/product/${row.productId}`">
              <el-image 
                :src="row.productImage"
                style="width: 60px; height: 60px; cursor: pointer"
                fit="cover"
              />
            </router-link>
            <router-link 
              :to="`/buyer/product/${row.productId}`"
              class="product-name"
            >
              {{ row.productName }}
            </router-link>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="金额" width="120" align="right">
        <template #default="scope">
          ¥{{ scope.row.amount?.toFixed(2) || '0.00' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="120">
        <template #default="scope">
          <el-tag :type="statusType[scope.row.status] || 'info'">
            {{ statusText[scope.row.status] || '未知状态' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button 
            type="primary" 
            size="small" 
            @click="viewDetail(scope.row?.orderId)"
          >
            详情
          </el-button>
          <el-button 
            v-if="scope.row?.status === 'pending'"
            type="success" 
            size="small"
            @click="payOrder(scope.row?.orderId)"
          >
            付款
          </el-button>
          <el-button 
            v-if="scope.row?.status === 'completed' && !scope.row?.reviewed"
            type="warning" 
            size="small"
            @click="openReviewDialog(scope.row)"
          >
            评价
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态提示 -->
    <el-empty v-else description="暂无订单数据" class="empty-placeholder" />

    <!-- 分页 -->
    <div class="pagination" v-if="orders.length > 0">
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
import { ElMessage } from 'element-plus'

// 订单状态配置
const statusText = {
  pending: '待付款',
  shipped: '已发货',
  completed: '已完成',
  canceled: '已取消'
}

const statusType = {
  pending: 'warning',
  shipped: 'primary',
  completed: 'success',
  canceled: 'info'
}

// 筛选选项
const statusOptions = ref([
  { value: 'all', label: '全部状态' },
  { value: 'pending', label: '待付款' },
  { value: 'shipped', label: '已发货' },
  { value: 'completed', label: '已完成' }
])

// 订单数据
const orders = ref([])
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
const currentReviewOrder = ref(null)

// 模拟订单数据加载
const loadOrders = async () => {
  try {
    const mockData = []
    const start = (currentPage.value - 1) * pageSize.value
    for (let i = 0; i < pageSize.value; i++) {
      mockData.push({
        orderId: `2023100${start + i + 1}`,
        productId: `100${start + i + 1}`,
        productName: `商品 ${start + i + 1}`,
        productImage: `https://dummyimage.com/100x100/ccc/fff&text=Product+${start + i + 1}`,
        createTime: `2023-10-${String(start + i + 1).padStart(2, '0')} 14:30:00`,
        amount: Math.random() * 500 + 100,
        status: Object.keys(statusText)[Math.floor(Math.random() * 4)],
        reviewed: Math.random() > 0.5
      })
    }
    orders.value = mockData
    total.value = 50
  } catch (error) {
    ElMessage.error('订单加载失败')
    orders.value = []
  }
}

const openReviewDialog = (order) => {
  currentReviewOrder.value = order
  reviewForm.value = {
    orderId: order.orderId,
    productId: order.productId,
    rating: 5,
    content: ''
  }
  reviewDialogVisible.value = true
}

const submitReview = () => {
  const reviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
  reviews.push({
    ...reviewForm.value,
    date: new Date().toLocaleString()
  })
  localStorage.setItem('productReviews', JSON.stringify(reviews))
  
  // 标记订单已评价
  orders.value = orders.value.map(order => 
    order.orderId === currentReviewOrder.value.orderId 
      ? { ...order, reviewed: true } 
      : order
  )
  
  ElMessage.success('评价提交成功')
  reviewDialogVisible.value = false
}

// 查看订单详情（添加参数验证）
const viewDetail = (orderId) => {
  if (!orderId) {
    ElMessage.warning('无效的订单号')
    return
  }
  console.log('查看订单详情:', orderId)
  // router.push(`/order/${orderId}`)
}

// 支付订单（添加安全验证）
const payOrder = (orderId) => {
  if (!orderId) {
    ElMessage.warning('无效的订单号')
    return
  }
  ElMessage.success(`模拟支付订单 ${orderId} 成功`)
  orders.value = orders.value.map(order => 
    order.orderId === orderId ? { ...order, status: 'shipped' } : order
  )
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
.product-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.product-name {
  color: #606266;
  text-decoration: none;
  &:hover {
    color: #409eff;
    text-decoration: underline;
  }
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