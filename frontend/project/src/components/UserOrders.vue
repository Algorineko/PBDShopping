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
              <router-link :to="`/buyer/${username}/product/${row.item.id}`">
                <el-image 
                  :src="row.item.image"
                  style="width: 60px; height: 60px; cursor: pointer"
                  fit="cover"
                />
              </router-link>
              <div class="product-details">
                <router-link 
                  :to="`/buyer/${username}/product/${row.item.id}`"
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
      <el-table-column label="操作" width="250">
        <template #default="scope">
          <!-- 订单操作按钮（仅在第一行显示） -->
          <div v-if="scope.row.isFirst">
            <!-- 付款按钮：仅待付款状态显示 -->
            <el-button 
              v-if="scope.row.status === 'pending' || scope.row.status === 'PENDING'"
              type="success" 
              size="small"
              @click="payOrder(scope.row.orderId)"
            >
              付款
            </el-button>
            
            <!-- 确认收货按钮：仅已发货状态显示 -->
            <el-button 
              v-if="scope.row.status === 'shipped' || scope.row.status === 'SHIPPED'"
              type="warning" 
              size="small"
              @click="confirmReceipt(scope.row.orderId)"
            >
              确认收货
            </el-button>
            
            <!-- 物流详情按钮：仅已发货和已完成状态显示 -->
            <el-button 
              v-if="(scope.row.status === 'shipped' || scope.row.status === 'SHIPPED' || scope.row.status === 'completed' || scope.row.status === 'COMPLETED')"
              type="info" 
              size="small"
              @click="showTrackingInfo(scope.row)"
            >
              物流详情
            </el-button>
          </div>
          
          <!-- 商品评价按钮（每个商品行都显示） -->
          <el-button 
            v-if="(scope.row.status === 'completed' || scope.row.status === 'COMPLETED') && scope.row.isPaid && !scope.row.item.reviewed"
            type="warning" 
            size="small"
            @click="openReviewDialog(scope.row)"
          >
            评价
          </el-button>
          
          <!-- 已评价标记 -->
          <el-tag v-if="(scope.row.status === 'completed' || scope.row.status === 'COMPLETED') && scope.row.isPaid && scope.row.item.reviewed" type="success">
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
    
    <!-- 物流详情对话框（修改后） -->
    <el-dialog v-model="trackingDialogVisible" title="物流详情">
      <el-form label-width="100px">
        <el-form-item label="物流公司：">
          <span>{{ currentTrackingInfo.logisticsCompany }}</span>
        </el-form-item>
        <el-form-item label="物流单号：">
          <span>{{ currentTrackingInfo.trackingNumber }}</span>
        </el-form-item>
        <!-- 移除了物流状态显示 -->
      </el-form>
      <template #footer>
        <el-button type="primary" @click="trackingDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// 图片格式化函数
const formatImageUrl = (url) => {
  if (!url || url.trim() === '') return '';
  
  if (url.startsWith('http') || url.startsWith('data:')) {
    return url;
  }
  
  // 使用固定基础URL
  const baseUrl = 'http://algorineko.top:8081';
  
  if (url.startsWith('/')) {
    return `${baseUrl}${url}`;
  }
  
  return `${baseUrl}/${url}`;
}

// 订单状态配置
const statusText = {
  PENDING: '待付款',
  PAID: '待发货',
  SHIPPED: '已发货',
  COMPLETED: '已完成',
  CANCELED: '已取消',
  // 添加小写状态映射以兼容原有逻辑
  pending: '待付款',
  paid: '待发货',
  shipped: '已发货',
  completed: '已完成',
  canceled: '已取消'
}

const statusType = {
  PENDING: 'warning',
  PAID: 'primary',
  SHIPPED: 'primary',
  COMPLETED: 'success',
  CANCELED: 'info',
  // 添加小写状态映射以兼容原有逻辑
  pending: 'warning',
  paid: 'primary',
  shipped: 'primary',
  completed: 'success',
  canceled: 'info'
}

// JWT 解析函数
const parseJwt = (token) => {
  try {
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    return JSON.parse(jsonPayload)
  } catch (e) {
    console.error('Token解析失败:', e)
    return null
  }
}

// 从token获取用户名
const token = localStorage.getItem('token')
const username = computed(() => {
  if (token) {
    const payload = parseJwt(token)
    return payload.sub|| 'guest'
  }
  return 'guest'
})

// 筛选选项
const statusOptions = ref([
  { value: 'all', label: '全部状态' },
  { value: 'PENDING', label: '待付款' },
  { value: 'PAID', label: '待发货' },
  { value: 'SHIPPED', label: '已发货' },
  { value: 'COMPLETED', label: '已完成' }
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
  orderItemId: '', // 新增：订单项ID
  customerId: '', // 新增：用户ID
  rating: 5,
  content: ''
})
const currentReviewOrderLine = ref(null)

// 物流详情相关
const trackingDialogVisible = ref(false)
const currentTrackingInfo = ref({
  logisticsCompany: '',
  trackingNumber: ''
})

// 从Token获取用户ID
const getCustomerIdFromToken = () => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.error('用户未登录，请先登录')
    return null
  }
  try {
    // 使用自定义函数解析JWT Token
    const decoded = parseJwt(token)
    // 假设用户ID存储在decoded.id字段中
    return decoded.customerId || null
  } catch (error) {
    console.error('Token解析失败:', error)
    ElMessage.error('用户信息解析失败')
    return null
  }
}

// 更新订单状态（直接使用大写的英文状态字符串）
const updateOrderStatus = async (orderId, newStatus) => {
  try {
    const response = await axios.put(
      `http://algorineko.top:8080/api/order/updateStatus?orderId=${orderId}&status=${newStatus}`,
      {},
      {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      }
    )
    
    return response.data === true
  } catch (error) {
    console.error('更新订单状态失败:', error)
    ElMessage.error('更新订单状态失败')
    return false
  }
}

// 从后端API加载订单数据
const loadOrders = async () => {
  try {
    // 从Token获取用户ID
    const customerId = getCustomerIdFromToken()
    if (!customerId) {
      ElMessage.error('无法获取用户信息')
      return
    }

    // 调用后端API获取订单
    const response = await axios.get(
      `http://algorineko.top:8080/api/order/customer/${customerId}`,
      {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      }
    )
    
    // 转换数据结构
    const backendOrders = response.data.map(order => ({
      orderId: order.orderId,
      merchantId: order.merchantId,
      totalPrice: order.totalPrice,
      status: order.status, // 保持大写状态
      createTime: new Date().toLocaleString(), // 添加时间字段（后端未提供）
      items: order.items.map(item => ({
        id: item.productId,
        name: `商品 ${item.productId}`, // 默认商品名称
        price: item.price,
        quantity: item.quantity,
        image: 'https://via.placeholder.com/60', // 默认图片
        orderItemId: item.orderItemId // 新增：保存订单项ID
      }))
    }))

    // 获取所有唯一商品ID
    const productIds = [...new Set(
      backendOrders.flatMap(order => 
        order.items.map(item => item.id)
      )
    )]

    // 并发获取所有商品详情
    const productRequests = productIds.map(id => 
      axios.get(`http://algorineko.top:8080/api/merchant/product/detail/${id}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      }).catch(error => {
        console.error(`获取商品${id}详情失败:`, error)
        return null
      })
    )
    console.log('productRequests:', productIds)
    // 等待所有商品详情请求完成
    const productResponses = await Promise.all(productRequests)
    
    // 构建商品信息映射表
    const productMap = {}
    productResponses.forEach((res, index) => {
      if (res && res.data) {
        const product = res.data
        // 处理商品图片
        let formattedImage = 'https://via.placeholder.com/60';
        if (product.images && product.images.length > 0) {
          // 过滤空字符串并处理图片URL
          const images = (product.images || [])
            .filter(img => img && img.trim() !== '')
            .map(img => formatImageUrl(img));
          
          if (images.length > 0) {
            formattedImage = images[0];
          }
        }
        
        productMap[productIds[index]] = {
          name: product.productName,
          image: formattedImage
        }
      }
    })

    // 更新订单中的商品信息
    backendOrders.forEach(order => {
      order.items.forEach(item => {
        const productInfo = productMap[item.id]
        if (productInfo) {
          item.name = productInfo.name
          item.image = productInfo.image
        }
      })
    })

    // 添加评价状态逻辑
    const reviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
    
    backendOrders.forEach(order => {
      // 设置订单支付状态
      order.isPaid = ['PAID', 'SHIPPED', 'COMPLETED'].includes(order.status)
      
      // 设置每个商品项的评价状态
      order.items.forEach(item => {
        // 检查该商品是否已有评价
        const hasReview = reviews.some(review => 
          review.orderItemId === item.orderItemId
        )
        item.reviewed = hasReview
      })
    })

    // 保存到localStorage（保持原有功能）
    localStorage.setItem('orders', JSON.stringify(backendOrders))
    
    // 应用筛选条件（原有逻辑）
    let filteredOrders = [...backendOrders]
    
    // 状态筛选
    if (filterStatus.value !== 'all') {
      filteredOrders = filteredOrders.filter(order => 
        order.status === filterStatus.value
      )
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
    
    // 更详细的错误信息
    let errorMessage = '订单加载失败'
    if (error.response) {
      // 服务器响应了错误状态码
      if (error.response.status === 401) {
        errorMessage = '用户未认证，请重新登录'
      } else if (error.response.status === 403) {
        errorMessage = '没有权限访问订单数据'
      } else if (error.response.data && error.response.data.message) {
        errorMessage = error.response.data.message
      }
    } else if (error.request) {
      // 请求已发出但没有收到响应
      errorMessage = '无法连接到服务器，请检查网络连接'
    }
    
    ElMessage.error(errorMessage)
    orderLines.value = []
  }
}

const openReviewDialog = (orderLine) => {
  currentReviewOrderLine.value = orderLine
  const customerId = getCustomerIdFromToken()
  
  reviewForm.value = {
    orderItemId: orderLine.item.orderItemId, // 使用订单项ID
    customerId: customerId, // 设置用户ID
    rating: 5,
    content: ''
  }
  reviewDialogVisible.value = true
}

const submitReview = async () => {
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      ElMessage.error('用户未登录')
      return
    }
    
    // 调用后端评价接口
    const response = await axios.post(
      'http://algorineko.top:8080/api/customer/review/add',
      {
        orderItemId: reviewForm.value.orderItemId,
        customerId: reviewForm.value.customerId,
        rating: reviewForm.value.rating,
        comment: reviewForm.value.content
      },
      {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    )
    
    if (response.data === '评论提交成功') {
      // 获取本地存储的评价记录
      const reviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
      
      // 添加新的评价记录（使用订单项ID作为标识）
      reviews.push({
        orderItemId: reviewForm.value.orderItemId,
        rating: reviewForm.value.rating,
        content: reviewForm.value.content,
        date: new Date().toLocaleString(),
        itemSnapshot: {
          id: currentReviewOrderLine.value.item.id,
          name: currentReviewOrderLine.value.item.name,
          price: currentReviewOrderLine.value.item.price,
          quantity: currentReviewOrderLine.value.item.quantity,
          image: currentReviewOrderLine.value.item.image
        }
      })
      
      // 更新本地存储
      localStorage.setItem('productReviews', JSON.stringify(reviews))
      
      ElMessage.success('评价提交成功')
      reviewDialogVisible.value = false
      
      // 重新加载订单数据更新评价状态
      loadOrders()
    } else {
      ElMessage.error('评价提交失败：' + response.data)
    }
  } catch (error) {
    console.error('评价提交失败:', error)
    let errorMessage = '评价提交失败'
    if (error.response) {
      if (error.response.data) {
        errorMessage += `: ${error.response.data}`
      }
    }
    ElMessage.error(errorMessage)
  }
}

// 支付订单
const payOrder = async (orderId) => {
  if (!orderId) {
    ElMessage.warning('无效的订单号')
    return
  }
  
  // 调用API更新订单状态为已付款（使用大写状态字符串）
  const success = await updateOrderStatus(orderId, 'PAID')
  
  if (success) {
    // 更新本地存储
    const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
    const updatedOrders = savedOrders.map(order => {
      if (order.orderId === orderId) {
        return {
          ...order,
          status: 'PAID', // 状态变为待发货
          isPaid: true
        }
      }
      return order
    })
    
    localStorage.setItem('orders', JSON.stringify(updatedOrders))
    
    ElMessage.success(`订单 #${orderId} 支付成功，等待商家发货`)
    loadOrders()
  }
}

// 确认收货
const confirmReceipt = async (orderId) => {
  if (!orderId) {
    ElMessage.warning('无效的订单号')
    return
  }
  
  // 调用API更新订单状态为已完成（使用大写状态字符串）
  const success = await updateOrderStatus(orderId, 'COMPLETED')
  
  if (success) {
    // 更新本地存储
    const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
    const updatedOrders = savedOrders.map(order => {
      if (order.orderId === orderId) {
        return {
          ...order,
          status: 'COMPLETED'
        }
      }
      return order
    })
    
    localStorage.setItem('orders', JSON.stringify(updatedOrders))
    
    ElMessage.success(`订单 #${orderId} 确认收货成功`)
    loadOrders()
  }
}

// 分页处理
const handlePageChange = (page) => {
  currentPage.value = page
  loadOrders()
}

// 显示物流详情
const showTrackingInfo = async (orderLine) => {
  try {
    // 重置物流信息
    currentTrackingInfo.value = {
      logisticsCompany: '',
      trackingNumber: ''
    }
    
    // 调用物流接口
    const response = await axios.get(
      `http://algorineko.top:8080/api/order/tracking/${orderLine.orderId}`,
      {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      }
    )
    
    // 获取当前订单项的物流信息
    const itemLogistics = response.data.itemLogistics
    const orderItemId = orderLine.item.orderItemId
    
    if (itemLogistics && itemLogistics[orderItemId]) {
      const logisticsInfo = itemLogistics[orderItemId]
      currentTrackingInfo.value = {
        logisticsCompany: logisticsInfo.logisticsCompany || '未知',
        trackingNumber: logisticsInfo.trackingNumber || '暂无'
      }
    } else {
      ElMessage.warning('未找到该订单项的物流信息')
    }
    
    trackingDialogVisible.value = true
  } catch (error) {
    console.error('获取物流信息失败:', error)
    ElMessage.error('获取物流信息失败')
  }
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