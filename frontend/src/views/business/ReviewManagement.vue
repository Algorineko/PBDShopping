<template>
  <div class="review-section">
    <h2><i class="el-icon-chat-dot-round"></i> 评价管理</h2>
    <div class="filter-bar">
      <el-input 
        v-model="reviewFilter.keyword" 
        placeholder="搜索商品名称" 
        style="width: 300px"
        prefix-icon="el-icon-search"
      />
      <el-select 
        v-model="reviewFilter.rating" 
        placeholder="评分筛选" 
        style="width: 150px"
      >
        <el-option label="全部" value="0" />
        <el-option label="5星" value="5" />
        <el-option label="4星" value="4" />
        <el-option label="3星及以下" value="3" />
      </el-select>
    </div>

    <el-table :data="filteredReviews" border style="width: 100%" class="data-table">
      <el-table-column prop="orderId" label="订单ID" width="120" />
      
      <el-table-column label="商品信息" min-width="200">
        <template #default="scope">
          <div class="product-info">
            <el-image 
              :src="scope.row.item?.image || '/placeholder-product.jpg'"
              style="width: 60px; height: 60px; border-radius: 4px;"
              fit="cover"
            />
            <div class="product-details">
              <div class="product-name">{{ scope.row.item?.name || '未知商品' }}</div>
              <div class="product-price">
                ¥{{ (scope.row.item?.price || 0).toFixed(2) }} × {{ scope.row.item?.quantity || 1 }}
              </div>
            </div>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column label="评分" width="120">
        <template #default="scope">
          <el-rate 
            :model-value="scope?.row?.rating ?? 0"
            disabled 
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']" 
          />
        </template>
      </el-table-column>
      <el-table-column prop="content" label="评价内容" min-width="200" />
      <el-table-column prop="date" label="评价时间" width="180" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <div v-if="scope?.row">
            <el-button 
              v-if="!scope.row.replied"
              type="primary" 
              size="small"
              icon="el-icon-chat-line-round"
              @click="showReplyDialog(scope.row)"
            >回复</el-button>
            <span v-else class="replied-text">已回复</span>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 回复评价对话框 -->
    <el-dialog v-model="replyDialogVisible" title="回复评价" width="600px">
      <div class="review-dialog">
        <div class="review-info">
          <div class="review-header">
            <span class="order-id">订单ID: {{ currentReview?.orderId }}</span>
          </div>
          <div class="review-product">{{ currentReview?.item?.name || '未知商品' }}</div>
          <div class="price-quantity">
            ¥{{ (currentReview?.item?.price || 0).toFixed(2) }} × {{ currentReview?.item?.quantity || 1 }}
          </div>
          <el-rate 
            :model-value="currentReview?.rating ?? 0"
            disabled 
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']" 
          />
          <div class="review-content">{{ currentReview?.content }}</div>
        </div>
        <div class="reply-section">
          <div class="reply-label">商家回复：</div>
          <el-input
            v-model="replyContent"
            type="textarea"
            :rows="4"
            placeholder="请输入回复内容"
          />
        </div>
      </div>
      <template #footer>
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReply">提交回复</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const reviewFilter = ref({ keyword: '', rating: '0' })
const reviews = ref([])

const replyDialogVisible = ref(false)
const replyContent = ref('')
const currentReview = ref(null)

// 加载当前商家的商品
const myProducts = ref([])

// 加载评价数据
const loadReviews = () => {
  const savedReviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
  const orders = JSON.parse(localStorage.getItem('orders') || '[]')
  
  // 获取当前商家的商品ID列表
  const businessId = localStorage.getItem('userId') || 'default'
  const storageKey = `businessProducts_${businessId}`
  const savedProducts = localStorage.getItem(storageKey)
  if (savedProducts) {
    myProducts.value = JSON.parse(savedProducts)
  } else {
    myProducts.value = []
  }
  
  const myProductIds = myProducts.value.map(p => p.id)
  
  // 为评价添加商品信息并过滤出当前商家的商品评价
  reviews.value = savedReviews
    .map(review => {
      const order = orders.find(o => o.orderId === review.orderId)
      let item = null
      
      if (order) {
        const orderItem = order.items.find(i => i.id === review.productId)
        if (orderItem) {
          item = { ...orderItem }
        }
      }
      
      return {
        ...review,
        item,
        replied: !!review.replyContent
      }
    })
    // 只保留属于当前商家的商品评价
    .filter(review => myProductIds.includes(review.productId))
}

const filteredReviews = computed(() => {
  return (reviews.value || [])
    .filter(r => {
      const keywordMatch = (r.item?.name?.toLowerCase() || '').includes(
        (reviewFilter.value.keyword?.toLowerCase() || '')
      )
      const ratingValue = Number(reviewFilter.value.rating)
      const ratingMatch = ratingValue === 0 || 
        (ratingValue === 5 && r.rating === 5) ||
        (ratingValue === 4 && r.rating >= 4 && r.rating < 5) ||
        (ratingValue === 3 && r.rating <= 3)
      return keywordMatch && ratingMatch
    })
})

const showReplyDialog = (review) => {
  if (review) {
    currentReview.value = review
    replyContent.value = review.replyContent || ''
    replyDialogVisible.value = true
  }
}

const submitReply = () => {
  if (currentReview.value && replyContent.value.trim()) {
    // 更新评价数据中的回复内容
    const savedReviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
    const index = savedReviews.findIndex(r => 
      r.orderId === currentReview.value.orderId && 
      r.productId === currentReview.value.productId
    )
    
    if (index !== -1) {
      savedReviews[index].replyContent = replyContent.value
      savedReviews[index].replyTime = new Date().toLocaleString()
      localStorage.setItem('productReviews', JSON.stringify(savedReviews))
      
      // 更新本地显示
      reviews.value = reviews.value.map(r => {
        if (r.orderId === currentReview.value.orderId && r.productId === currentReview.value.productId) {
          return {
            ...r,
            replyContent: replyContent.value,
            replied: true
          }
        }
        return r
      })
      
      ElMessage.success('回复成功')
      replyDialogVisible.value = false
    } else {
      ElMessage.error('评价数据不存在')
    }
  } else {
    ElMessage.warning('回复内容不能为空')
  }
}

onMounted(() => {
  loadReviews()
})
</script>

<style scoped>
.review-section h2 {
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

.review-section h2 i {
  margin-right: 12px;
  font-size: 24px;
  color: #409eff;
}

.filter-bar {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.data-table {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.replied-text {
  color: #67C23A;
  font-size: 12px;
}

.review-dialog {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.review-info {
  padding: 15px;
  background: #f9fafc;
  border-radius: 8px;
}

.review-header {
  display: flex;
  gap: 20px;
  margin-bottom: 10px;
  font-size: 13px;
  color: #606266;
}

.review-product {
  font-weight: 600;
  margin-bottom: 10px;
  font-size: 16px;
}

.price-quantity {
  margin-bottom: 10px;
  color: #f56c6c;
  font-weight: 500;
}

.review-content {
  margin-top: 10px;
  color: #606266;
  line-height: 1.6;
}

.reply-section {
  display: flex;
  flex-direction: column;
}

.reply-label {
  font-weight: 500;
  margin-bottom: 10px;
}

.product-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.product-details {
  flex: 1;
}

.product-name {
  font-weight: 500;
  margin-bottom: 5px;
}

.product-price {
  font-size: 13px;
  color: #f56c6c;
}
</style>