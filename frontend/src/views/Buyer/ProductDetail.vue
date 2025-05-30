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
        <el-tag type="success">库存 {{ product.stock }} 件</el-tag>
        
        <div class="action-group">
          <el-input-number 
            v-model="quantity" 
            :min="product.stock > 0 ? 1 : 0"
            :max="product.stock"
            :disabled="product.stock === 0"
          />
          <el-button 
            type="primary" 
            @click="addToCart"
            :disabled="product.stock === 0"
          >
            {{ product.stock > 0 ? '加入购物车' : '已售罄' }}
          </el-button>
        </div>

        <el-divider />
        <div class="description">
          <h3>商品详情</h3>
          <p>{{ product.description || '暂无详情描述' }}</p>
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
//const reviews = ref([])

const product = ref({
  id: '',
  name: '',
  price: 0,
  stock: 0,
  images: [],
  description: ''
})

const quantity = ref(1)

// 从本地存储获取商品详情
const fetchProductDetail = (id) => {
  if (!id) {
    ElMessage.error('无效的商品ID')
    return
  }
  
  try {
    // 从所有商家的商品中查找
    let foundProduct = null
    
    // 获取所有商家商品存储的key
    const keys = Object.keys(localStorage).filter(key => 
      key.startsWith('businessProducts_')
    )
    
    // 遍历所有商家商品
    for (const key of keys) {
      const products = JSON.parse(localStorage.getItem(key) || '[]')
      const p = products.find(p => p.id === id)
      if (p) {
        foundProduct = {
          ...p,
          price: Number(p.price) || 0,
          stock: Number(p.stock) || 0
        }
        break
      }
    }
    
    if (foundProduct) {
      product.value = foundProduct
      
      // 根据库存设置初始数量
      quantity.value = foundProduct.stock > 0 ? 1 : 0
    } else {
      ElMessage.error('未找到商品信息')
    }
  } catch (error) {
    ElMessage.error('商品加载失败')
    console.error('商品加载错误:', error)
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
    image: product.value.images?.[0] || ''
  })
  ElMessage.success('已加入购物车')
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

@media (max-width: 768px) {
  .detail-content {
    grid-template-columns: 1fr;
  }
}
</style>