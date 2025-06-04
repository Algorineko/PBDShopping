<template>
  <div class="product-detail">
    <el-button type="text" @click="$router.go(-1)">返回</el-button>
    
    <div class="detail-content">
      <el-carousel :interval="4000" height="400px" v-if="product.images?.length > 0">
        <el-carousel-item 
          v-for="(img, index) in product.images" 
          :key="index"
          style="height: 400px;"
        >
          <el-image 
            :src="img" 
            fit="cover" 
            style="width: 100%; height: 100%;"
          />
        </el-carousel-item>
      </el-carousel>
      
      <!-- 无图片提示 -->
      <div v-else class="no-images">
        <el-empty description="暂无商品图片" />
      </div>

      <div class="product-info">
        <h1>{{ product.name }}</h1>
        <p class="price">¥{{ product.price }}</p>
        
        <div class="action-group">
          <el-input-number 
            v-model="quantity" 
            :min="1"
          />
          <el-button 
            type="primary" 
            @click="addToCart"
            :loading="isAddingToCart"
          >
            加入购物车
          </el-button>
        </div>

        <el-divider />
        <div class="description">
          <h3>商品详情</h3>
          <p>{{ product.description || '暂无详情描述' }}</p>
        </div>
        
        <!-- 商品评价区域 -->
        <el-divider />
        <div class="reviews-section">
          <h3>商品评价</h3>
          
          <div v-if="reviewsLoading" class="reviews-loading">
            <el-skeleton :rows="3" animated />
          </div>
          
          <div v-else>
            <div v-if="reviews.length === 0" class="no-reviews">
              <el-empty description="暂无评价" />
            </div>
            
            <div v-else class="review-list">
              <div v-for="review in reviews" :key="review.reviewId" class="review-item">
                <div class="review-header">
                  <el-rate 
                    v-model="review.rating" 
                    disabled 
                    show-score 
                    text-color="#ff9900" 
                    score-template="{value} 分"
                  />
                  <span class="customer-id">用户ID: {{ review.customerId }}</span>
                </div>
                <div class="review-content">
                  <p>{{ review.comment }}</p>
                </div>
                <el-divider class="review-divider" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/stores/cart'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

const product = ref({
  id: '',
  name: '',
  price: 0,
  images: [],
  description: ''
})

// 新增评价相关变量
const reviews = ref([])
const reviewsLoading = ref(false)

const quantity = ref(1)
const isAddingToCart = ref(false) // 加载状态

// 从API获取商品详情
const fetchProductDetail = async (id) => {
  if (!id) {
    ElMessage.error('无效的商品ID')
    return
  }
  
  try {
    const response = await axios.get(
      `http://algorineko.top:8080/api/merchant/product/detail/${id}`
    )
    
    const apiData = response.data
    
    // 转换API数据结构以适应前端需求
    product.value = {
      id: apiData.productId,
      name: apiData.productName,
      price: apiData.price,
      description: apiData.description,
      // 确保images是数组，如果为null则转换为空数组
      images: apiData.images || []
    }
    
  } catch (error) {
    ElMessage.error('商品加载失败')
    console.error('商品加载错误:', error)
  }
}

// 新增：获取商品评价
const fetchProductReviews = async (productId) => {
  if (!productId) return
  
  reviewsLoading.value = true
  try {
    const response = await axios.get(
      `http://algorineko.top:8080/api/customer/review/product/${productId}`
    )
    reviews.value = response.data || []
  } catch (error) {
    console.error('获取评价失败:', error)
    ElMessage.error('评价加载失败')
  } finally {
    reviewsLoading.value = false
  }
}

onMounted(() => {
  const productId = route.params.id
  if (productId) {
    fetchProductDetail(productId)
    fetchProductReviews(productId) // 同时获取评价数据
  }
})

// 从JWT token解析payload
const addToCart = async () => {
  isAddingToCart.value = true // 开始加载
  
  try {
    await cartStore.addItemToCart({
      id: product.value.id,
      productName: product.value.name,
      price: product.value.price,
      quantity: quantity.value,
      image: product.value.images?.[0] || ''
    })
     // 新增：添加成功后刷新购物车数据
    await cartStore.fetchCart()
    
    ElMessage.success('已加入购物车')
  } catch (error) {
    // 处理错误
    console.error('加入购物车失败:', error)
    const errorMsg = error.message || '加入购物车失败'
    ElMessage.error(errorMsg)
    
    // 处理未登录情况
    if (error.response?.status === 401) {
      router.push('/login')
    }
  } finally {
    isAddingToCart.value = false // 结束加载
  }
}
</script>

<style scoped>
.product-detail {
  padding: 20px;
}

.detail-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
}

.action-group {
  margin-top: 30px;
  display: flex;
  gap: 20px;
  align-items: center;
}

.description {
  margin-top: 30px;
}

.price {
  font-size: 24px;
  color: #f56c6c;
  font-weight: bold;
}

.no-images {
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  border-radius: 8px;
}

/* 新增评价区域样式 */
.reviews-section {
  margin-top: 30px;
}

.review-list {
  margin-top: 20px;
}

.review-item {
  padding: 15px 0;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.customer-id {
  font-size: 14px;
  color: #909399;
}

.review-content {
  font-size: 15px;
  line-height: 1.6;
  color: #606266;
}

.review-divider {
  margin: 15px 0;
}

.no-reviews {
  padding: 40px 0;
}

.reviews-loading {
  padding: 20px 0;
}

@media (max-width: 768px) {
  .detail-content {
    grid-template-columns: 1fr;
  }
  
  .review-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}
</style>