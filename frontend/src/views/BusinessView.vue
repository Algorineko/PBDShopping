<template>
  <div class="business-container">
    <!-- 顶部操作栏 -->
    <div class="action-bar">
      <el-button class="logout-btn" type="text" @click="logout">退出登录</el-button>
    </div>

    <!-- 左侧功能面板 -->
    <div class="function-panel">
      <div 
        v-for="item in functions" 
        :key="item.key"
        class="function-card"
        :class="{ active: currentView === item.key }"
        @click="switchView(item.key)"
      >
        {{ item.label }}
      </div>
    </div>

    <!-- 右侧内容区 -->
    <div class="content-area">
      <!-- 商家信息 -->
      <div v-if="currentView === 'info'" class="info-section">
        <h2>商家信息</h2>
        <el-card>
          <el-form label-width="100px">
            <el-form-item label="商家ID">
              <el-input v-model="shopInfo.id" disabled />
            </el-form-item>
            <el-form-item label="商家名称">
              <el-input v-model="shopInfo.name" />
            </el-form-item>
            <el-form-item label="联系方式">
              <el-input v-model="shopInfo.contact" />
            </el-form-item>
            <el-form-item label="营业执照">
              <el-image 
                style="width: 300px"
                :src="shopInfo.license"
                :preview-src-list="[shopInfo.license]"
              />
            </el-form-item>
            <el-button type="primary" @click="saveShopInfo">保存修改</el-button>
          </el-form>
        </el-card>
      </div>

      <!-- 商品管理 -->
      <div v-if="currentView === 'products'" class="product-section">
        <div class="table-header">
          <el-input 
            v-model="productFilter.keyword" 
            placeholder="输入商品名称" 
            style="width: 200px"
            clearable
          />
          <el-button type="primary" icon="Plus" @click="showAddDialog">新增商品</el-button>
        </div>
        
        <el-table :data="filteredProducts" border style="width: 100%">
          <el-table-column prop="name" label="商品名称" width="180" />
          <el-table-column label="价格" width="120" align="right">
            <template #default="scope">
              ¥{{ (scope?.row?.price ?? 0).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="stock" label="库存" width="120" />
          <el-table-column label="状态" width="120">
            <template #default="scope">
              <el-tag :type="(scope?.row?.status === 'published') ? 'success' : 'info'">
                {{ scope?.row?.status === 'published' ? '已上架' : '未上架' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <div v-if="scope?.row">
                <el-button 
                  size="small" 
                  @click="editProduct(scope.row)"
                >编辑</el-button>
                <el-button 
                  size="small" 
                  type="danger" 
                  @click="deleteProduct(scope.row.id)"
                >删除</el-button>
              </div>
              <span v-else class="error-text">数据异常</span>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 订单管理 -->
      <div v-if="currentView === 'orders'" class="order-section">
        <div class="filter-bar">
          <div class="filter-group">
            <el-select 
              v-model="orderFilter.status" 
              placeholder="订单状态" 
              style="width: 120px"
            >
              <el-option label="全部" value="all" />
              <el-option label="待付款" value="pending" />
              <el-option label="已发货" value="shipped" />
              <el-option label="已完成" value="completed" />
            </el-select>
            <el-date-picker
              v-model="orderFilter.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            />
          </div>
          <el-button type="primary" icon="Search">搜索</el-button>
        </div>

        <el-table :data="filteredOrders" border style="width: 100%">
          <el-table-column prop="orderId" label="订单号" width="180" />
          <el-table-column prop="createTime" label="下单时间" width="180" />
          <el-table-column label="金额" width="120" align="right">
            <template #default="scope">
              ¥{{ (scope?.row?.amount ?? 0).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column label="状态" width="120">
            <template #default="scope">
              <el-tag :type="statusMap[scope?.row?.status]?.type || 'info'">
                {{ statusMap[scope?.row?.status]?.text || '未知状态' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <div v-if="scope?.row">
                <el-button 
                  size="small" 
                  @click="viewOrderDetail(scope.row)"
                >详情</el-button>
                <el-button 
                  v-if="scope.row.status === 'pending'" 
                  size="small" 
                  type="success"
                  @click="handleShip(scope.row)"
                >发货</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 评价管理 -->
      <div v-if="currentView === 'reviews'" class="review-section">
        <div class="filter-bar">
          <el-input 
            v-model="reviewFilter.keyword" 
            placeholder="搜索商品名称" 
            style="width: 200px"
          />
          <el-select 
            v-model="reviewFilter.rating" 
            placeholder="评分筛选" 
            style="width: 120px"
          >
            <el-option label="全部" value="0" />
            <el-option label="5星" value="5" />
            <el-option label="4星" value="4" />
            <el-option label="3星及以下" value="3" />
          </el-select>
        </div>

        <el-table :data="filteredReviews" border style="width: 100%">
          <el-table-column prop="productName" label="商品名称" width="180" />
          <el-table-column label="评分" width="120">
            <template #default="scope">
              <el-rate 
                :model-value="scope?.row?.rating ?? 0"
                disabled 
                :colors="['#99A9BF', '#F7BA2A', '#FF9900']" 
              />
            </template>
          </el-table-column>
          <el-table-column prop="content" label="评价内容" />
          <el-table-column prop="createTime" label="评价时间" width="180" />
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <div v-if="scope?.row">
                <el-button 
                  v-if="!scope.row.replied"
                  type="primary" 
                  size="small"
                  @click="showReplyDialog(scope.row)"
                >回复</el-button>
                <span v-else class="replied-text">已回复</span>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!-- 右下角店铺信息 -->
    <div class="shop-info">
      <h3>{{ shopInfo.name }}</h3>
      <p>商家ID: {{ shopInfo.id }}</p>
      <p>信用等级: {{ shopInfo.credit }}</p>
      <p>余额: ¥{{ (shopInfo.balance ?? 0).toFixed(2) }}</p>
    </div>

    <!-- 添加商品对话框 -->
    <el-dialog v-model="productDialogVisible" :title="dialogTitle">
      <el-form :model="currentProduct" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="currentProduct.name" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number 
            v-model="currentProduct.price" 
            :min="0" 
            :precision="2" 
          />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number 
            v-model="currentProduct.stock" 
            :min="0" 
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="currentProduct.status">
            <el-option label="已上架" value="published" />
            <el-option label="未上架" value="draft" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="currentProduct.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="productDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveProduct">保存</el-button>
      </template>
    </el-dialog>

    <!-- 回复评价对话框 -->
    <el-dialog v-model="replyDialogVisible" title="回复评价" width="500px">
      <el-input
        v-model="replyContent"
        type="textarea"
        :rows="4"
        placeholder="请输入回复内容"
      />
      <template #footer>
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReply">提交回复</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed,onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 功能列表
const functions = ref([
  { key: 'info', label: '商家信息' },
  { key: 'products', label: '商品管理' },
  { key: 'orders', label: '订单管理' },
  { key: 'reviews', label: '评价管理' }
])

// 当前视图
const currentView = ref('info')


// 商家信息（替换为你的数据）
const shopInfo = ref({
  id: 'BUS2023VIP',
  name: '优质食品专卖',
  contact: '199-1234-5678',
  license: 'https://example.com/new-license.jpg',
  credit: 'AAAA级',
  balance: 12580.60
})

// 商品数据（替换为你的数据）
const products = ref([
  {
    id: 1001,
    name: '有机大米 5kg',
    price: 68.90,
    stock: 150,
    status: 'published',
    description: '东北优质有机大米'
  },
  {
    id: 1002,
    name: '野生蜂蜜',
    price: 128.00,
    stock: 80,
    status: 'draft',
    description: '长白山天然野生蜂蜜'
  },
  {
    id: 1003,
    name: '特级橄榄油',
    price: 98.50,
    stock: 45,
    status: 'published',
    description: '进口冷压初榨橄榄油'
  }
])/*.map(p => ({
  id: p.id || Date.now(),
  name: p.name || '未命名商品',
  price: Number(p.price) || 0,
  stock: Math.max(Number(p.stock), 0) || 0,
  status: ['published', 'draft'].includes(p.status) ? p.status : 'draft',
  description: String(p.description || '')
})))
*/

// 订单数据（替换为你的数据）
const orders = ref([
  {
    orderId: '20231115001',
    createTime: '2023-11-15 09:30:00',
    amount: 256.40,
    status: 'pending'
  },
  {
    orderId: '20231115002',
    createTime: '2023-11-15 10:15:00',
    amount: 198.00,
    status: 'completed'
  },
  {
    orderId: '20231115003',
    createTime: '2023-11-15 11:00:00',
    amount: 385.90,
    status: 'shipped'
  }
])//.map(o => ({
//  orderId: o.orderId || `ORDER_${Date.now()}`,
//  createTime: o.createTime || new Date().toISOString(),
//  amount: Number(o.amount) || 0,
//  status: ['pending', 'shipped', 'completed'].includes(o.status) ? o.status : 'pending'
//})))

// 评价数据（替换为你的数据）
const reviews = ref([
  {
    productName: '有机大米 5kg',
    rating: 4.8,
    content: '米粒饱满，煮饭很香',
    createTime: '2023-11-14 16:30:00',
    replied: false
  },
  {
    productName: '特级橄榄油',
    rating: 5.0,
    content: '炒菜味道清香，品质很好',
    createTime: '2023-11-14 17:15:00',
    replied: true
  }
])/*.map(r => ({
  productName: r.productName || '未知商品',
  rating: Math.min(Math.max(Number(r.rating), 0), 5) || 0,
  content: r.content || '暂无评价内容',
  createTime: r.createTime || new Date().toISOString(),
  replied: Boolean(r.replied)
})))
*/

const productFilter = ref({ keyword: '' })
const productDialogVisible = ref(false)
const currentProduct = ref({})
const isEditing = ref(false)

const orderFilter = ref({ status: 'all', dateRange: [] })
const statusMap = {
  pending: { text: '待付款', type: 'warning' },
  shipped: { text: '已发货', type: 'primary' },
  completed: { text: '已完成', type: 'success' }
}

const reviewFilter = ref({ keyword: '', rating: '0' })
const replyDialogVisible = ref(false)
const replyContent = ref('')
let currentReview = null

// 计算属性（带空值保护）
const filteredProducts = computed(() => {
  return (products.value || [])
    .filter(p => 
      (p?.name?.toLowerCase() || '').includes(
        (productFilter.value.keyword?.toLowerCase() || '')
      )
    )
    .map(p => ({
      ...p,
      price: Number(p.price) || 0,
      stock: Math.max(Number(p.stock), 0)
    }))
})

const filteredOrders = computed(() => {
  return (orders.value || [])
    .filter(o => {
      const statusMatch = orderFilter.value.status === 'all' || 
                         o.status === orderFilter.value.status
      const dateMatch = !orderFilter.value.dateRange?.length || (
        new Date(o.createTime) >= new Date(orderFilter.value.dateRange[0]) &&
        new Date(o.createTime) <= new Date(orderFilter.value.dateRange[1])
      )
      return statusMatch && dateMatch
    })
})

const filteredReviews = computed(() => {
  return (reviews.value || [])
    .filter(r => {
      const keywordMatch = (r.productName?.toLowerCase() || '').includes(
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

const dialogTitle = computed(() => isEditing.value ? '编辑商品' : '新增商品')

// 方法（带参数验证）
const switchView = (view) => {
  if (['info', 'products', 'orders', 'reviews'].includes(view)) {
    currentView.value = view
  }
}

const logout = () => {
  localStorage.clear()
  router.push('/login')
}

const saveShopInfo = () => {
  ElMessage.success('商家信息已保存')
}

const showAddDialog = () => {
  currentProduct.value = {
    name: '',
    price: 0,
    stock: 0,
    status: 'draft',
    description: ''
  }
  isEditing.value = false
  productDialogVisible.value = true
}

const editProduct = (product) => {
  if (!product?.id) {
    ElMessage.warning('无效的商品数据')
    return
  }
  currentProduct.value = { ...product }
  isEditing.value = true
  productDialogVisible.value = true
}

const saveProduct = () => {
  if (!currentProduct.value.name?.trim()) {
    ElMessage.error('商品名称不能为空')
    return
  }
  if (currentProduct.value.price < 0) {
    ElMessage.error('价格不能为负数')
    return
  }

  try {
    if (isEditing.value) {
      const index = products.value.findIndex(p => p?.id === currentProduct.value?.id)
      if (index !== -1) {
        products.value.splice(index, 1, {
          ...currentProduct.value,
          name: currentProduct.value.name || '未命名商品',
          price: Number(currentProduct.value.price) || 0,
          stock: Math.max(Number(currentProduct.value.stock), 0)
        })
      }
    } else {
      products.value.push({
        ...currentProduct.value,
        id: Date.now(),
        name: currentProduct.value.name || '新商品',
        price: Number(currentProduct.value.price) || 0,
        stock: Math.max(Number(currentProduct.value.stock), 0)
      })
    }
    productDialogVisible.value = false
    ElMessage.success('操作成功')
  } catch (error) {
    ElMessage.error(`保存失败: ${error.message}`)
  }
}

const deleteProduct = (id) => {
  if (!id) {
    ElMessage.warning('无效的商品ID')
    return
  }
  const index = products.value.findIndex(p => p?.id === id)
  if (index !== -1) {
    products.value.splice(index, 1)
    ElMessage.success('商品删除成功')
  }
}

const viewOrderDetail = (order) => {
  if (order?.orderId) {
    console.log('查看订单详情:', order)
  }
}

const handleShip = (order) => {
  if (order?.orderId && order.status === 'pending') {
    order.status = 'shipped'
    ElMessage.success('订单状态已更新')
  }
}

const showReplyDialog = (review) => {
  if (review) {
    currentReview = review
    replyDialogVisible.value = true
  }
}

const submitReply = () => {
  if (currentReview && replyContent.value.trim()) {
    currentReview.replied = true
    replyDialogVisible.value = false
    replyContent.value = ''
    ElMessage.success('回复成功')
  }
}

// 异步数据加载示例
onMounted(async () => {
  try {
    // const response = await fetch('/api/shop-data')
    // const data = await response.json()
    // 实际使用时替换为你的API调用
  } catch (error) {
    ElMessage.error('数据加载失败')
  }
})

</script>

<style scoped>
/* 错误提示样式 */
.error-text {
  color: #f56c6c;
  font-size: 12px;
  padding: 4px 8px;
}
/* 原有样式保持不变 */
.business-container {
  display: grid;
  grid-template-columns: 200px 1fr;
  grid-template-rows: 60px 1fr;
  height: 100vh;
  background: #f5f7fa;
}


.action-bar {
  grid-column: 1 / -1;
  display: flex;
  justify-content: flex-end;
  padding: 10px 20px;
  background: #fff;
  border-bottom: 1px solid #ebeef5;
  z-index: 1;
}

.function-panel {
  padding: 20px;
  background: #fff;
  border-right: 1px solid #ebeef5;
  overflow-y: auto;
}

.function-card {
  padding: 20px;
  margin-bottom: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
}

.function-card:hover {
  border-color: #409eff;
  background-color: #f5f7fa;
}

.function-card.active {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.content-area {
  padding: 20px;
  background: white;
  margin: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
.table-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.filter-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.shop-info {
  position: fixed;
  right: 30px;
  bottom: 30px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.replied-text {
  color: #67C23A;
  font-size: 12px;
}

.el-table {
  margin-top: 15px;
}
</style>
