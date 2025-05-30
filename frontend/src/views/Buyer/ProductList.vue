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
          v-if="category.picture"
        />
      </div>

      <!-- 商品网格 -->
      <div class="product-grid" v-if="category.products.length > 0">
        <div 
          v-for="product in category.products"
          :key="product.id"
          class="product-card"
          @click="viewDetail(product.id)"
        >
          <!-- 商品图片 -->
          <div class="image-container">
            <el-image 
              :src="getFirstImage(product.images)" 
              class="product-image"
              style="width: 100%; height: 200px;"
              fit="cover"
            />
          </div>
          
          <!-- 商品信息 -->
          <div class="product-info">
            <h3 class="name">{{ product.name }}</h3>
            <p class="desc">{{ product.description || '暂无描述' }}</p>
            <div class="price-section">
              <span class="price">¥{{ product.price }}</span>
              <el-tag type="success" size="small">库存 {{ product.stock || 0 }}</el-tag>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 无商品提示 -->
      <div v-else class="empty-products">
        <el-empty description="该分类下暂无商品" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'

const router = useRouter()

const categories = ref([])
const searchKeyword = ref('')
const loading = ref(true)

// 加载分类数据
const loadCategories = () => {
  const savedCategories = localStorage.getItem('productCategories')
  if (savedCategories) {
    return JSON.parse(savedCategories)
  }
  
  // 默认分类数据
  return [
    { id: "1005000", name: "居家" },
    { id: "1005002", name: "美食" },
    { id: "1010000", name: "服饰" },
    { id: "1011000", name: "母婴" },
    { id: "1013001", name: "个护" },
    { id: "1019000", name: "严选" },
    { id: "1043000", name: "数码" },
    { id: "109243029", name: "运动" },
    { id: "19999999", name: "杂项" }
  ]
}

// 加载所有商品
const loadAllProducts = () => {
  const allProducts = []
  
  // 从localStorage加载所有商家的商品
  const keys = Object.keys(localStorage).filter(key => 
    key.startsWith('businessProducts_')
  )
  
  keys.forEach(key => {
    try {
      const products = JSON.parse(localStorage.getItem(key))
      allProducts.push(...products)
    } catch (e) {
      console.error(`Error parsing products from ${key}:`, e)
    }
  })
  
  return allProducts
}

// 获取第一张图片
const getFirstImage = (images) => {
  return images?.length > 0 ? images[0] : '/placeholder-product.jpg'
}

// 初始化数据
const initData = () => {
  // 加载分类
  const categoryList = loadCategories()
  
  // 加载所有商品
  const allProducts = loadAllProducts()
  
  // 创建分类映射
  const categoryMap = new Map()
  
  // 初始化所有分类（包括没有商品的）
  categoryList.forEach(category => {
    categoryMap.set(category.id, {
      ...category,
      products: []
    })
  })
  
  // 分配商品到分类
  allProducts.forEach(product => {
    if (product.categoryId && categoryMap.has(product.categoryId)) {
      categoryMap.get(product.categoryId).products.push(product)
    } else {
      // 如果杂项分类不存在则创建
      if (!categoryMap.has("19999999")) {
        categoryMap.set("19999999", {
          id: "19999999",
          name: "杂项",
          products: []
        })
      }
      categoryMap.get("19999999").products.push(product)
    }
  })
  
  // 确保所有分类都显示，即使没有商品
  categories.value = Array.from(categoryMap.values())
  
  loading.value = false
}

const filteredCategories = computed(() => {
  const keyword = searchKeyword.value.toLowerCase()
  
  // 没有搜索关键词时显示所有分类
  if (!keyword) return categories.value
  
  // 有搜索关键词时，只显示包含匹配商品的分类
  return categories.value
    .map(category => ({
      ...category,
      products: category.products.filter(product => 
        (product.name || '').toLowerCase().includes(keyword) ||
        (product.description || '').toLowerCase().includes(keyword)
      )
    }))
    .filter(category => category.products.length > 0)
})

const viewDetail = (productId) => {
  router.push(`/buyer/product/${productId}`)
}

onMounted(initData)
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

.empty-products {
  padding: 40px 0;
  background: #f9fafc;
  border-radius: 8px;
  margin-top: 20px;
}
</style>