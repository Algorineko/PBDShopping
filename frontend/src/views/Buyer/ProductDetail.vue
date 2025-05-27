<template>
  <div class="product-detail">
    <el-button type="text" @click="$router.go(-1)">返回</el-button>
    
    <div class="detail-content">
      <el-carousel :interval="4000" height="400px">
        <el-carousel-item 
          v-for="(img, index) in product.mainPictures" 
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

      <div class="product-info">
        <h1>{{ product.name }}</h1>
        <p class="price">¥{{ product.price }}</p>
        <el-tag type="success">库存 {{ product.inventory }} 件</el-tag>
        
        <div class="action-group">
          <el-input-number 
            v-model="quantity" 
            :min="product.inventory > 0 ? 1 : 0"
            :max="product.inventory"
            :disabled="product.inventory === 0"
          />
          <el-button 
            type="primary" 
            @click="addToCart"
            :disabled="product.inventory === 0"
          >
            {{ product.inventory > 0 ? '加入购物车' : '已售罄' }}
          </el-button>
        </div>

        <el-divider />
        <div class="description">
          <h3>商品详情</h3>
          <p>{{ product.detailDesc || '暂无详情描述' }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/stores/cart'


const route = useRoute()
const cartStore = useCartStore()
const reviews = ref([])

onMounted(() => {
  const savedReviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
  reviews.value = savedReviews.filter(r => r.productId === product.value.id)
})
const product = ref({
  id: '',
  name: '',
  price: 0,
  inventory: 0,
  mainPictures: [],
  detailDesc: ''
})

const quantity = ref(1)

const fetchProductDetail = async (id) => {
  try {
    const response = await fetch(
      `http://pcapi-xiaotuxian-front-devtest.itheima.net/goods?id=${id}`
    )
    const res = await response.json()
    if (res.code !== '1') throw new Error(res.msg || '商品加载失败')
    
    product.value = {
      id: res.result.id,
      name: res.result.name,
      price: Number(res.result.price).toFixed(2),
      inventory: res.result.inventory || 0,
      mainPictures: res.result.mainPictures || [],
      detailDesc: res.result.details?.properties?.[0]?.value || ''
    }

    // 根据库存设置初始数量
    quantity.value = product.value.inventory > 0 ? 1 : 0
  } catch (error) {
    ElMessage.error(error.message)
  }
}

onMounted(() => {
  const productId = route.params.id
  if (productId) fetchProductDetail(productId)
})

const addToCart = () => {
  cartStore.addItem({
    id: product.value.id,
    name: product.value.name,
    price: product.value.price,
    quantity: quantity.value,
    image: product.value.mainPictures[0]
  })
  ElMessage.success('已加入购物车')
}
</script>

<style scoped>
.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
}

.el-carousel__item {
  background-color: #f5f7fa;
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
.reviews {
  margin-top: 30px;
}
.review-item {
  padding: 15px;
  border-bottom: 1px solid #ebeef5;
}
.review-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 10px;
}
.date {
  color: #909399;
  font-size: 0.9em;
}
.content {
  color: #606266;
}
</style>