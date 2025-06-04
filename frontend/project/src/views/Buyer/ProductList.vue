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

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-section">
      <el-skeleton :rows="6" animated />
    </div>

    <!-- 分类区块 -->
    <template v-else>
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
                <!-- 已移除库存显示 -->
              </div>
            </div>
          </div>
        </div>
        
        <!-- 无商品提示 -->
        <div v-else class="empty-products">
          <el-empty description="该分类下暂无商品" />
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import axios from 'axios'

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

// 获取第一张图片
const getFirstImage = (images) => {
  return images?.length > 0 ? images[0] : '/placeholder-product.jpg'
}

// 从API获取分类商品
const fetchCategoryProducts = async (categoryId) => {
  try {
    const response = await axios.get(
      `http://algorineko.top:8080/api/merchant/product/category/${categoryId}`
    )
    
    // 转换API数据结构以适应前端需求
    return response.data.map(product => ({
      id: product.productId,
      name: product.productName,
      description: product.description,
      price: product.price,
      images: product.images,
      categoryId: String(product.categoryId) // 确保类型一致
    }))
  } catch (error) {
    console.error(`获取分类 ${categoryId} 商品失败:`, error)
    return [] // 出错时返回空数组
  }
}

// 初始化数据
const initData = async () => {
  try {
    loading.value = true
    
    // 加载分类
    const categoryList = loadCategories()
    
    // 创建分类映射
    const categoryMap = new Map()
    
    // 初始化所有分类
    categoryList.forEach(category => {
      categoryMap.set(category.id, {
        ...category,
        products: []
      })
    })
    
    // 并发请求所有分类的商品
    const requests = categoryList.map(category => 
      fetchCategoryProducts(parseInt(category.id))
    )
    
    const results = await Promise.all(requests)
    
    // 将商品分配到对应分类
    results.forEach((products, index) => {
      const categoryId = categoryList[index].id
      if (categoryMap.has(categoryId)) {
        categoryMap.get(categoryId).products = products
      }
    })
    
    // 确保所有分类都显示
    categories.value = Array.from(categoryMap.values())
    
  } catch (error) {
    console.error('初始化数据失败:', error)
    // 可以在这里添加用户提示
  } finally {
    loading.value = false
  }
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

const handleSearch = () => {
  // 搜索逻辑保持不变
}

const viewDetail = (productId) => {
  // 从localStorage获取用户名
  const username = localStorage.getItem('username') || '';
  // 使用正确的路由格式：/buyer/:username/product/:id
  router.push(`/buyer/${username}/product/${productId}`);
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

.loading-section {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}
</style>