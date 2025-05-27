<template>
  <div class="product-list">
    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="请输入商品名称或关键词"
        clearable
        @input="handleSearch"
        style="max-width: 400px"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>

    <!-- 分类区块 -->
    <div 
      class="category-section" 
      v-for="category in filteredCategories" 
      :key="category.id"
    >
      <div class="category-header">
        <h2 class="category-title">{{ category.name }}</h2>
        <el-image 
          :src="formatImageUrl(category.picture)" 
          style="width: 120px; height: 40px; margin-left: 15px"
          fit="contain"
        />
      </div>

      <!-- 商品网格 -->
      <div class="product-grid">
        <div 
          v-for="product in category.goods"
          :key="product.id"
          class="product-card"
          @click="viewDetail(product.id)"
        >
          <!-- 商品图片 -->
          <div class="image-container">
            <el-image 
              :src="formatImageUrl(product.picture)" 
              class="product-image"
              :preview-src-list="[formatImageUrl(product.picture)]"
              style="width: 100%; height: 200px;"
              fit="cover"
            />
          </div>
          
          <!-- 商品信息 -->
          <div class="product-info">
            <h3 class="name">{{ product.name }}</h3>
            <p class="desc">{{ product.desc }}</p>
            <div class="price-section">
              <span class="price">¥{{ product.price }}</span>
              <el-tag type="success" size="small">销量 {{ product.orderNum || 0 }}</el-tag>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-empty
      v-if="!loading && filteredCategories.length === 0"
      description="没有找到相关商品"
      class="empty-tip"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

const router = useRouter()

const categories = ref([])
const searchKeyword = ref('')
const loading = ref(true)

// 图片URL格式化（移除尺寸参数）
const formatImageUrl = (url) => {
  if (!url) return ''
  // 示例输入：http://xxx.com/pic.jpg?quality=95&imageView
  // 处理后：http://xxx.com/pic.jpg
  return url.split('?')[0]
}

const fetchCategories = async () => {
  try {
    const response = await fetch(
      'http://pcapi-xiaotuxian-front-devtest.itheima.net/home/category/head'
    )
    const res = await response.json()
    
    if (res.code !== '1') throw new Error(res.msg || '数据加载失败')
    
    // 数据结构映射
    categories.value = res.result.map(category => ({
      id: category.id,
      name: category.name,
      picture: category.picture,
      goods: (category.goods || []).map(g => ({
        id: g.id,
        name: g.name,
        desc: g.desc,
        price: Number(g.price).toFixed(2),
        picture: g.picture,
        orderNum: g.orderNum
      }))
    })).filter(c => c.goods.length > 0)
    
  } catch (error) {
    ElMessage.error(error.message)
    categories.value = []
  } finally {
    loading.value = false
  }
}

const filteredCategories = computed(() => {
  const keyword = searchKeyword.value.toLowerCase()
  return categories.value
    .map(category => ({
      ...category,
      goods: category.goods.filter(product => 
        product.name?.toLowerCase().includes(keyword) ||
        product.desc?.toLowerCase().includes(keyword)
      )
    }))
    .filter(category => category.goods.length > 0)
})

const viewDetail = (productId) => {
  router.push(`/buyer/product/${productId}`)
}

onMounted(fetchCategories)
</script>

<style scoped>
.product-list {
  padding: 20px;
}

.category-section {
  margin-bottom: 40px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.category-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.product-card {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.image-container {
  width: 100%;
  height: 200px;
  background: #f5f7fa;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.product-info {
  padding: 15px;
}

.name {
  font-size: 16px;
  margin-bottom: 8px;
  color: #333;
}

.desc {
  font-size: 12px;
  color: #999;
  height: 36px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.price-section {
  margin-top: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 18px;
  color: #f56c6c;
  font-weight: bold;
}
</style>