<template>
  <div class="user-reviews">
    <h2>评价管理</h2>
    
    <el-table :data="reviews" border style="width: 100%">
      <el-table-column prop="orderItemId" label="订单项ID" width="120" />
      
      <el-table-column label="商品信息">
        <template #default="{ row }">
          <div class="product-list">
            <div class="product-item">
              <router-link :to="`/buyer/${username}/product/${row.item.id}`">
                <el-image 
                  :src="row.item.image || '/placeholder-product.jpg'"
                  style="width: 60px; height: 60px;"
                  fit="cover"
                />
              </router-link>
              <div class="product-details">
                <router-link :to="`/buyer/${username}/product/${row.item.id}`" class="product-name">
                  {{ row.item.name || '商品信息缺失' }}
                </router-link>
                <div class="product-price">¥{{ (row.item.price || 0).toFixed(2) }} × {{ row.item.quantity || 1 }}</div>
              </div>
            </div>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column label="评分" width="120">
        <template #default="scope">
          <el-rate 
            v-model="scope.row.rating"
            disabled
            show-score
            text-color="#ff9900"
          />
        </template>
      </el-table-column>
      
      <el-table-column prop="comment" label="评价内容" />
      
      <el-table-column prop="date" label="评价时间" width="180" />
      
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button type="danger" size="small" @click="deleteReview(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty 
      v-if="reviews.length === 0" 
      description="暂无评价记录"
      class="empty-tip"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const reviews = ref([])

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
    return payload.sub || 'guest'
  }
  return 'guest'
})

// 从Token获取用户ID
const getCustomerIdFromToken = () => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.error('用户未登录，请先登录')
    return null
  }
  try {
    const decoded = parseJwt(token)
    return decoded.customerId || null
  } catch (error) {
    console.error('Token解析失败:', error)
    ElMessage.error('用户信息解析失败')
    return null
  }
}

// 加载评价数据
const loadReviews = async () => {
  try {
    const customerId = getCustomerIdFromToken()
    if (!customerId) {
      ElMessage.error('无法获取用户信息')
      return
    }
    
    // 从后端获取评价列表
    const response = await axios.get(
      `http://algorineko.top:8080/api/customer/review/customer/${customerId}`,
      {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      }
    )
    
    // 获取本地存储的商品快照
    const savedReviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
    
    // 合并数据：后端返回的评价数据 + 本地存储的商品快照
    reviews.value = response.data.map(apiReview => {
      // 查找本地存储中对应的评价（如果有）
      const localReview = savedReviews.find(r => 
        r.orderItemId === apiReview.orderItemId
      )
      
      // 使用本地存储的商品快照（如果存在）
      const itemSnapshot = localReview?.itemSnapshot || {}
      
      return {
        ...apiReview,
        content: apiReview.comment, // 为了兼容原有模板
        date: localReview?.date || new Date().toLocaleString(), // 保留原有日期
        item: {
          id: itemSnapshot.id || 0,
          name: itemSnapshot.name || '商品信息缺失',
          price: itemSnapshot.price || 0,
          quantity: itemSnapshot.quantity || 1,
          image: itemSnapshot.image || '/placeholder-product.jpg'
        }
      }
    })
    
  } catch (error) {
    console.error('加载评价失败:', error)
    ElMessage.error('加载评价失败')
    reviews.value = []
  }
}

const deleteReview = async (review) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个评价吗？删除后无法恢复',
      '警告',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 调用后端API删除评价 - 使用正确的URL格式
    await axios.delete(
      `http://algorineko.top:8080/api/customer/review/${review.reviewId}`,
      {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      }
    )
    
    // 从本地存储中移除商品快照
    const savedReviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
    const newReviews = savedReviews.filter(r => 
      r.orderItemId !== review.orderItemId
    )
    localStorage.setItem('productReviews', JSON.stringify(newReviews))
    
    // 重新加载评价
    loadReviews()
    ElMessage.success('删除成功')
    
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除评价失败:', error)
      ElMessage.error('删除评价失败')
    }
  }
}

onMounted(loadReviews)
</script>

<style scoped>
.user-reviews {
  padding: 20px;
  background: #fff;
}

.empty-tip {
  margin-top: 50px;
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
  font-weight: 500;
  margin-bottom: 5px;
  color: #606266;
  text-decoration: none;
  display: block;
  cursor: pointer;
  transition: color 0.3s;
}

.product-name:hover {
  color: #409eff;
  text-decoration: underline;
}

.product-price {
  color: #f56c6c;
  font-size: 14px;
}

/* 图片链接样式 */
a {
  text-decoration: none;
  color: inherit;
}

/* 图片悬停效果 */
.el-image {
  transition: transform 0.3s;
}

.el-image:hover {
  transform: scale(1.05);
}
</style>