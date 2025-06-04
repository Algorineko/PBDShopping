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
              v-if="(row.status === 'paid' || row.status === 'PAID') && row.isFirstItem"
              size="small" 
              type="success"
              icon="el-icon-truck"
              @click="handleShip(row.orderId)"
            >发货</el-button>
            <!-- 已发货状态显示 -->
            <span v-else-if="(row.status === 'shipped' || row.status === 'SHIPPED') && row.isFirstItem">
              <el-tag type="success">已发货</el-tag>
            </span>
          </div>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 发货对话框 -->
    <el-dialog v-model="shipDialogVisible" title="订单发货" width="500px">
      <el-form :model="shipForm" label-width="100px">
        <el-form-item label="订单号">
          <el-input v-model="currentOrderId" disabled />
        </el-form-item>
        <el-form-item label="物流公司" required>
          <el-input v-model="shipForm.trackingCompany" placeholder="请输入物流公司名称" />
        </el-form-item>
        <el-form-item label="物流单号">
          <div class="tracking-input">
            <el-input 
              v-model="shipForm.trackingNumber" 
              placeholder="请输入物流单号" 
              clearable
            />
            <el-button 
              type="primary" 
              plain 
              size="small" 
              @click="shipForm.trackingNumber = generateTrackingNumber()"
            >
              生成单号
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="shipDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmShip">确定发货</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const orderFilter = ref({ status: 'all', dateRange: [] })
const orders = ref([])

// 发货相关状态
const shipDialogVisible = ref(false)
const currentOrderId = ref('')
const shipForm = ref({
  trackingCompany: '',
  trackingNumber: ''
})

// 从token解析商家ID
const getMerchantIdFromToken = () => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.error('用户未登录，请先登录')
    return null
  }
  
  try {
    // 解析JWT Token
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    const payload = JSON.parse(jsonPayload)
    return payload.merchantId || null
  } catch (error) {
    console.error('Token解析失败:', error)
    ElMessage.error('商家信息解析失败')
    return null
  }
}

// 状态映射（支持大小写状态）
const statusMap = {
  pending: { text: '待付款', type: 'warning' },
  PENDING: { text: '待付款', type: 'warning' },
  paid: { text: '已付款', type: 'primary' },
  PAID: { text: '已付款', type: 'primary' },
  shipped: { text: '已发货', type: 'success' },
  SHIPPED: { text: '已发货', type: 'success' },
  completed: { text: '已完成', type: 'info' },
  COMPLETED: { text: '已完成', type: 'info' }
}

// 更新订单状态 - 使用新的发货API
const shipOrder = async (orderId, trackingCompany, trackingNumber) => {
  try {
    const merchantId = getMerchantIdFromToken()
    if (!merchantId) {
      ElMessage.error('无法获取商家信息')
      return false
    }
    
    // 查找当前订单的所有订单项
    const order = orders.value.find(o => o.orderId === orderId)
    if (!order || !order.items) {
      ElMessage.error('未找到订单项信息')
      return false
    }
    
    // 构建物流列表 - 使用正确的订单项ID
    const logisticsList = order.items.map(item => ({
      orderItemId: item.orderItemId, // 使用正确的订单项ID
      logisticsCompany: trackingCompany,
      trackingNumber: trackingNumber,
      status: "SHIPPED" // 发货状态
    }))
    console.log('物流列表:', logisticsList)
    // 调用发货API
    const response = await axios.post(
      `http://algorineko.top:8080/api/order/shipping`,
      {
        orderId: orderId,
        logisticsList: logisticsList
      },
      {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      }
    )
    
    return response.status === 200
  } catch (error) {
    console.error('发货操作失败:', error)
    
    let errorMessage = '发货失败'
    if (error.response) {
      if (error.response.status === 400) {
        errorMessage = '请求参数错误'
      } else if (error.response.status === 401) {
        errorMessage = '未授权操作'
      } else if (error.response.data && error.response.data.message) {
        errorMessage = error.response.data.message
      }
    }
    
    ElMessage.error(errorMessage)
    return false
  }
}

// 创建扁平化的订单数据结构（每个商品一行）
const flattenedOrders = computed(() => {
  const result = []
  
  filteredOrders.value.forEach(order => {
    order.items.forEach((item, index) => {
      result.push({
        ...order,
        item,
        isFirstItem: index === 0  // 标记是否是订单中的第一个商品
      })
    })
  })
  
  return result
})

// 过滤订单
const filteredOrders = computed(() => {
  return (orders.value || [])
    .filter(o => {
      // 状态筛选
      const statusMatch = orderFilter.value.status === 'all' || 
                       o.status.toLowerCase() === orderFilter.value.status.toLowerCase()
      
      // 日期筛选
      const dateMatch = !orderFilter.value.dateRange?.length || (
        new Date(o.createTime) >= new Date(orderFilter.value.dateRange[0]) &&
        new Date(o.createTime) <= new Date(orderFilter.value.dateRange[1]))
      
      return statusMatch && dateMatch
    })
})

// 生成递增的物流单号
const generateTrackingNumber = () => {
  let lastNumber = localStorage.getItem('lastTrackingNumber') || 0
  lastNumber = parseInt(lastNumber) + 1
  localStorage.setItem('lastTrackingNumber', lastNumber.toString())
  return lastNumber.toString().padStart(10, '0') // 生成10位数字，不足前面补0
}

// 发货操作 - 打开发货对话框
const handleShip = (orderId) => {
  currentOrderId.value = orderId
  // 生成物流单号（作为默认值）
  shipForm.value.trackingNumber = generateTrackingNumber()
  shipForm.value.trackingCompany = '' // 清空物流公司
  shipDialogVisible.value = true
}

// 确认发货
const confirmShip = async () => {
  if (!shipForm.value.trackingCompany) {
    ElMessage.warning('请输入物流公司')
    return
  }
  
  if (!shipForm.value.trackingNumber) {
    ElMessage.warning('请输入物流单号')
    return
  }
  
  // 使用发货API
  const success = await shipOrder(
    currentOrderId.value,
    shipForm.value.trackingCompany,
    shipForm.value.trackingNumber
  )
  
  if (success) {
    // 更新本地存储
    const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
    const updatedOrders = savedOrders.map(o => {
      if (o.orderId === currentOrderId.value) {
        return {
          ...o,
          status: 'SHIPPED', // 使用大写状态保持一致性
          shipTime: new Date().toLocaleString(),
          trackingCompany: shipForm.value.trackingCompany,
          trackingNumber: shipForm.value.trackingNumber
        }
      }
      return o
    })
    
    localStorage.setItem('orders', JSON.stringify(updatedOrders))
    
    ElMessage.success('发货成功！')
    shipDialogVisible.value = false
    loadOrders() // 重新加载订单
  }
}

// 从后端加载订单数据
const loadOrders = async () => {
  try {
    const merchantId = getMerchantIdFromToken()
    if (!merchantId) {
      ElMessage.error('无法获取商家信息')
      return
    }
    
    // 调用后端API获取订单
    const response = await axios.get(
      `http://algorineko.top:8080/api/order/merchant/${merchantId}`,
      {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      }
    )
    
    // 转换数据结构 - 保留订单项ID
    const backendOrders = response.data.map(order => ({
      orderId: order.orderId,
      merchantId: order.merchantId,
      totalPrice: order.totalPrice,
      status: order.status, // 保持大写状态
      createTime: new Date().toLocaleString(), // 添加时间字段
      items: order.items.map(item => ({
        orderItemId: item.orderItemId, // 保留订单项ID (后端返回的订单项ID)
        productId: item.productId, // 商品ID
        name: `商品 ${item.productId}`, // 默认商品名称（稍后会替换）
        price: item.price,
        quantity: item.quantity,
        image: '/placeholder-product.jpg' // 默认图片（稍后会替换）
      }))
    }))
    console.log('订单列表:', backendOrders)
    // 获取所有唯一商品ID
    const productIds = [...new Set(
      backendOrders.flatMap(order => 
        order.items.map(item => item.productId) // 使用商品ID获取详情
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

    // 等待所有商品详情请求完成
    const productResponses = await Promise.all(productRequests)
    
    // 构建商品信息映射表
    const productMap = {}
    productResponses.forEach((res, index) => {
      if (res && res.data) {
        const product = res.data
        productMap[productIds[index]] = {
          name: product.productName,
          // 使用第一张图片或默认图片
          image: product.images && product.images.length > 0 
            ? product.images[0] 
            : '/placeholder-product.jpg'
        }
      }
    })

    // 更新订单中的商品信息
    backendOrders.forEach(order => {
      order.items.forEach(item => {
        const productInfo = productMap[item.productId] // 使用商品ID获取详情
        if (productInfo) {
          item.name = productInfo.name
          item.image = productInfo.image
        }
      })
    })

    // 保存到本地存储（保持原有功能）
    localStorage.setItem('orders', JSON.stringify(backendOrders))
    
    orders.value = backendOrders
  } catch (error) {
    console.error('加载订单失败:', error)
    
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
    orders.value = []
  }
}

onMounted(() => {
  loadOrders() // 加载订单数据
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

/* 物流单号输入框样式 */
.tracking-input {
  display: flex;
  gap: 10px;
}

.tracking-input .el-input {
  flex: 1;
}
</style>