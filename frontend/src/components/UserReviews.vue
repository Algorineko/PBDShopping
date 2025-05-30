<template>
  <div class="user-reviews">
    <h2>评价管理</h2>
    
    <el-table :data="reviews" border style="width: 100%">
      <el-table-column prop="orderId" label="订单ID" width="120" />
      
      <el-table-column label="商品信息">
        <template #default="{ row }">
          <div class="product-list">
            <div class="product-item">
              <router-link :to="`/buyer/product/${row.item.id}`">
                <el-image 
                  :src="row.item.image || '/placeholder-product.jpg'"
                  style="width: 60px; height: 60px;"
                  fit="cover"
                />
              </router-link>
              <div class="product-details">
                <router-link :to="`/buyer/product/${row.item.id}`" class="product-name">
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
      
      <el-table-column prop="content" label="评价内容" />
      
      <el-table-column prop="date" label="评价时间" width="180" />
      
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="editReview(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="deleteReview(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty 
      v-if="reviews.length === 0" 
      description="暂无评价记录"
      class="empty-tip"
    />

    <!-- 编辑对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑评价">
      <el-form :model="editingReview">
        <el-form-item label="评分">
          <el-rate v-model="editingReview.rating" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input
            v-model="editingReview.content"
            type="textarea"
            :rows="4"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const reviews = ref([])
const editDialogVisible = ref(false)
const editingReview = ref({})

const loadReviews = () => {
  const savedReviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
  
  // 为评价数据添加完整的商品信息 - 使用商品快照
  reviews.value = savedReviews.map(review => {
    // 确保商品信息存在
    const itemInfo = review.itemSnapshot || {}
    
    return {
      ...review,
      item: {
        id: itemInfo.id || review.productId || 0,
        name: itemInfo.name || '商品信息缺失',
        price: itemInfo.price || 0,
        quantity: itemInfo.quantity || 1,
        image: itemInfo.image || '/placeholder-product.jpg'
      }
    }
  })
}

const editReview = (review) => {
  // 仅克隆需要编辑的数据
  editingReview.value = { 
    orderId: review.orderId,
    productId: review.item.id,
    rating: review.rating,
    content: review.content
  }
  editDialogVisible.value = true
}

const deleteReview = (review) => {
  const newReviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
    .filter(r => !(r.orderId === review.orderId && r.productId === review.item.id))
  localStorage.setItem('productReviews', JSON.stringify(newReviews))
  
  // 更新订单中该商品的评价状态
  const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
  const updatedOrders = savedOrders.map(order => {
    if (order.orderId == review.orderId) {
      const updatedItems = order.items.map(item => {
        if (item.id == review.item.id) {
          return { ...item, reviewed: false }
        }
        return item
      })
      return { ...order, items: updatedItems }
    }
    return order
  })
  
  localStorage.setItem('orders', JSON.stringify(updatedOrders))
  
  loadReviews()
  ElMessage.success('删除成功')
}

const saveEdit = () => {
  const newReviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
  const index = newReviews.findIndex(r => 
    r.orderId === editingReview.value.orderId && 
    r.productId === editingReview.value.productId
  )
  
  if (index > -1) {
    // 只更新评价内容，保留商品快照
    newReviews[index] = {
      ...newReviews[index],
      rating: editingReview.value.rating,
      content: editingReview.value.content,
      date: new Date().toLocaleString()
    }
    
    localStorage.setItem('productReviews', JSON.stringify(newReviews))
    ElMessage.success('修改成功')
    editDialogVisible.value = false
    loadReviews()
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