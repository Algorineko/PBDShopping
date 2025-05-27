<template>
  <!-- 模板部分完全保持不变 -->
  <div class="buyer-view">
    <h1>商品列表</h1>
    <div class="product-list">
      <div v-for="product in products" :key="product._id" class="product-card">
        <div class="product-image">
          <img :src="product.image || '/placeholder-product.jpg'" alt="商品图片">
        </div>
        <div class="product-info">
          <h3>{{ product.name }}</h3>
          <p class="price">¥{{ product.price }}</p>
          <p class="business">商家：{{ product.businessId.username }}</p>
          <p class="stock">库存：{{ product.stock }}</p>
          <el-button type="primary" @click="addToCart(product)">加入购物车</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import productApi from '../services/product'

export default {
  data() {
    return {
      products: []
    }
  },
  async mounted() {
    await this.loadProducts()
  },
  methods: {
    async loadProducts() {
      try {
        const res = await productApi.getAllProducts()
        this.products = res.data
      } catch (error) {
        ElMessage.error('加载商品失败')
      }
    },
    addToCart(product) {
      // 保留点击事件参数，添加基础日志验证功能
      console.log('尝试添加商品到购物车:', product._id)
      // 后续可在此处添加购物车逻辑
      // 示例：this.$store.dispatch('cart/addItem', product)
    }
  }
}
</script>

<style scoped>
.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  padding: 20px;
}

.product-card {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
  transition: box-shadow 0.3s;
}

.product-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.product-image img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 15px;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
  margin: 10px 0;
}

.business {
  color: #909399;
  font-size: 12px;
}
</style>