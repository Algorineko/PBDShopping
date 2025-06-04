<template>
  <div class="business-container">
    <!-- 顶部操作栏 -->
    <div class="action-bar">
      <div class="user-info">
        <el-avatar :size="40" :src="shopInfo.avatar" />
        <div class="user-details">
          <span class="welcome-text">欢迎，{{ shopInfo.name }}</span>
          <span class="shop-id">商家ID: {{ shopInfo.id }}</span>
        </div>
      </div>
      <el-button class="logout-btn" type="danger" @click="logout">
        <i class="el-icon-switch-button"></i> 退出登录
      </el-button>
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
        <i :class="item.icon" class="function-icon"></i>
        <span>{{ item.label }}</span>
      </div>
    </div>

    <!-- 右侧内容区 -->
    <div class="content-area">
      <!-- 商家信息 -->
      <div v-if="currentView === 'info'" class="info-section">
        <h2><i class="el-icon-user"></i> 商家信息管理</h2>
        <el-card class="info-card">
          <el-form label-width="120px">
            <el-form-item label="商家ID">
              <el-input v-model="shopInfo.id" disabled />
              <div class="info-tip">商家ID不可更改</div>
            </el-form-item>
            <el-form-item label="商家名称">
              <el-input v-model="shopInfo.name" />
            </el-form-item>
            <el-form-item label="登录密码">
              <el-input 
                v-model="shopInfo.password" 
                type="password" 
                show-password
                placeholder="请输入新密码" 
              />
              <div class="info-tip">留空则不修改密码</div>
            </el-form-item>
            <el-form-item label="商家地址">
              <el-input v-model="shopInfo.address" placeholder="请输入商家地址" />
            </el-form-item>
            <el-form-item label="联系方式">
              <el-input v-model="shopInfo.contact" placeholder="请输入联系电话" />
            </el-form-item>
            <el-form-item label="商家头像">
              <div class="avatar-upload">
                <el-avatar 
                  :size="120" 
                  :src="shopInfo.avatar" 
                  class="avatar-preview"
                />
                <div class="avatar-actions">
                  <el-upload
                    action="#"
                    :show-file-list="false"
                    :auto-upload="false"
                    :on-change="handleAvatarChange"
                  >
                    <el-button type="primary" icon="el-icon-upload" size="small">上传头像</el-button>
                  </el-upload>
                  <el-button 
                    v-if="shopInfo.avatar" 
                    type="danger" 
                    icon="el-icon-delete" 
                    size="small"
                    @click="shopInfo.avatar = ''"
                  >
                    移除
                  </el-button>
                </div>
              </div>
            </el-form-item>
            <div class="form-actions">
              <el-button type="primary" @click="saveShopInfo">
                <i class="el-icon-check"></i> 保存修改
              </el-button>
              <el-button @click="resetForm">
                <i class="el-icon-refresh"></i> 重置修改
              </el-button>
            </div>
          </el-form>
        </el-card>
      </div>

      <!-- 商品管理 -->
      <div v-if="currentView === 'products'" class="product-section">
        <h2><i class="el-icon-goods"></i> 商品管理</h2>
        <div class="table-header">
          <el-input 
            v-model="productFilter.keyword" 
            placeholder="输入商品名称搜索" 
            style="width: 300px"
            clearable
            prefix-icon="el-icon-search"
          />
          <el-button type="primary" icon="el-icon-plus" @click="showAddDialog">新增商品</el-button>
        </div>
        
        <el-table :data="filteredProducts" border style="width: 100%" class="data-table">
          <el-table-column label="商品图片" width="120">
            <template #default="scope">
              <el-image 
                v-if="scope.row.images && scope.row.images.length > 0"
                :src="scope.row.images[0]" 
                fit="cover" 
                style="width: 80px; height: 80px; border-radius: 4px;"
              >
                <template #error>
                  <div class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </template>
              </el-image>
              <div v-else class="no-image">无图片</div>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="商品名称" width="180" />
          <el-table-column prop="id" label="商品ID" width="120" />
          <el-table-column prop="categoryId" label="分类ID" width="120" />
          <el-table-column label="价格" width="120" align="right">
            <template #default="scope">
              ¥{{ (scope?.row?.price ?? 0).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="stock" label="库存" width="120" />
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <div v-if="scope?.row">
                <el-button 
                  size="small" 
                  icon="el-icon-edit"
                  @click="editProduct(scope.row)"
                >编辑</el-button>
                <el-button 
                  size="small" 
                  type="danger" 
                  icon="el-icon-delete"
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
        <h2><i class="el-icon-tickets"></i> 订单管理</h2>
        <div class="filter-bar">
          <div class="filter-group">
            <el-select 
              v-model="orderFilter.status" 
              placeholder="订单状态" 
              style="width: 150px"
            >
              <el-option label="全部" value="all" />
              <el-option label="待付款" value="pending" />
              <el-option label="已付款" value="paid" />
              <el-option label="已发货" value="shipped" />
              <el-option label="已完成" value="completed" />
            </el-select>
            <el-date-picker
              v-model="orderFilter.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 350px"
            />
          </div>
          <el-button type="primary" icon="el-icon-search" @click="loadOrders">搜索</el-button>
        </div>

        <el-table :data="filteredOrders" border style="width: 100%" class="data-table">
          <el-table-column prop="orderId" label="订单号" width="180" />
          <el-table-column prop="createTime" label="下单时间" width="180" />
          
          <!-- 商品信息列 -->
          <el-table-column label="商品信息">
            <template #default="{ row }">
              <div class="product-list">
                <div 
                  v-for="(item, index) in filteredOrderItems(row)" 
                  :key="index"
                  class="product-item"
                >
                  <el-image 
                    :src="item.image || '/placeholder-product.jpg'"
                    style="width: 60px; height: 60px; border-radius: 4px;"
                    fit="cover"
                  />
                  <div class="product-details">
                    <div class="product-name">{{ item.name }}</div>
                    <div class="product-price">¥{{ (item.price || 0).toFixed(2) }} × {{ item.quantity || 1 }}</div>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          
          <!-- 金额列（只计算当前商家的商品金额） -->
          <el-table-column label="金额" width="120" align="right">
            <template #default="{ row }">
              ¥{{ calculateMerchantTotal(row).toFixed(2) }}
            </template>
          </el-table-column>
          
          <el-table-column label="状态" width="120">
            <template #default="{ row }">
              <el-tag :type="statusMap[row?.status]?.type || 'info'">
                {{ statusMap[row?.status]?.text || '未知状态' }}
              </el-tag>
            </template>
          </el-table-column>
          
          <!-- 操作列：增加发货按钮 -->
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <div v-if="row">
                <!-- 发货按钮：只对已付款订单显示 -->
                <el-button 
                  v-if="row.status === 'paid' && hasMyProducts(row)"
                  size="small" 
                  type="success"
                  icon="el-icon-truck"
                  @click="handleShip(row)"
                >发货</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 评价管理 -->
      <div v-if="currentView === 'reviews'" class="review-section">
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
          <el-table-column prop="content" label="评价内容" min-width="200" />
          <el-table-column prop="createTime" label="评价时间" width="180" />
          <el-table-column label="操作" width="120">
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
      </div>
    </div>

    <!-- 添加/编辑商品对话框 -->
    <el-dialog v-model="productDialogVisible" :title="dialogTitle" width="800px">
      <el-form :model="currentProduct" label-width="100px">
        <el-form-item label="商品ID">
          <el-input v-model="currentProduct.id" placeholder="请输入商品ID" :disabled="isEditing" />
        </el-form-item>
        <el-form-item label="分类ID">
          <el-input v-model="currentProduct.categoryId" placeholder="请输入分类ID" />
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="currentProduct.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number 
            v-model="currentProduct.price" 
            :min="0" 
            :precision="2" 
            :controls="false"
            style="width: 200px"
          />
          <span class="input-unit">元</span>
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number 
            v-model="currentProduct.stock" 
            :min="0" 
            :controls="false"
            style="width: 200px"
          />
          <span class="input-unit">件</span>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="currentProduct.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="商品图片">
          <div class="image-upload-container">
            <div class="image-preview-list">
              <div v-for="(image, index) in currentProduct.images" :key="index" class="image-preview-item">
                <el-image 
                  :src="image" 
                  fit="cover" 
                    class="preview-image"
                />
                <div class="image-actions">
                  <el-button 
                    type="danger" 
                    icon="el-icon-delete" 
                    size="small"
                    circle
                    @click="removeImage(index)"
                  />
                </div>
              </div>
            </div>
            <el-upload
              action="#"
              :multiple="true"
              :show-file-list="false"
              :auto-upload="false"
              :on-change="handleImageChange"
              class="image-upload-btn"
            >
              <el-button type="primary" icon="el-icon-plus">添加图片</el-button>
              <div class="upload-tip">支持多图上传</div>
            </el-upload>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="productDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveProduct">保存</el-button>
      </template>
    </el-dialog>

    <!-- 回复评价对话框 -->
    <el-dialog v-model="replyDialogVisible" title="回复评价" width="600px">
      <div class="review-dialog">
        <div class="review-info">
          <div class="review-product">{{ currentReview?.productName }}</div>
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
    import { useRouter } from 'vue-router'
    import { ElMessage, ElNotification } from 'element-plus'

    const router = useRouter()

    // 功能列表
    const functions = ref([
      { key: 'info', label: '商家信息', icon: 'el-icon-user' },
      { key: 'products', label: '商品管理', icon: 'el-icon-goods' },
      { key: 'orders', label: '订单管理', icon: 'el-icon-tickets' },
      { key: 'reviews', label: '评价管理', icon: 'el-icon-chat-dot-round' }
    ])

    // 当前视图
    const currentView = ref('info')

    // 商家信息 - 初始值从登录信息获取
    const shopInfo = ref({
      id: '',
      name: '',
      address: '',
      contact: '',
      password: '',
      avatar: '',
      credit: 'AAAA级',
      balance: 12580.60
    })

    // 备份初始商家信息用于重置
    const originalShopInfo = ref({})

    // 商品数据（从localStorage加载）
    const products = ref([])

    // 订单数据
    const orders = ref([])

    // 评价数据
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
    ])

    const productFilter = ref({ keyword: '' })
    const productDialogVisible = ref(false)
    const currentProduct = ref({
      id: '',
      categoryId: '',
      name: '',
      price: 0,
      stock: 0,
      description: '',
      images: []
    })
    const isEditing = ref(false)

    const orderFilter = ref({ status: 'all', dateRange: [] })
    const statusMap = {
      pending: { text: '待付款', type: 'warning' },
      paid: { text: '已付款', type: 'primary' },
      shipped: { text: '已发货', type: 'success' },
      completed: { text: '已完成', type: 'info' }
    }

    const reviewFilter = ref({ keyword: '', rating: '0' })
    const replyDialogVisible = ref(false)
    const replyContent = ref('')
    const currentReview = ref(null)

    // 计算属性 - 过滤商品
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
          stock: Math.max(Number(p.stock) || 0, 0)
        }))
    })

    // 过滤订单 - 只显示当前商家的订单
    const filteredOrders = computed(() => {
      // 获取当前商家的所有商品ID
      const myProductIds = products.value.map(p => p.id)
      
      return (orders.value || [])
        .filter(o => {
          // 检查订单是否包含当前商家的商品
          const hasMyProduct = o.items?.some(item => myProductIds.includes(item.id)) ?? false
          
          const statusMatch = orderFilter.value.status === 'all' || 
                           o.status === orderFilter.value.status
          const dateMatch = !orderFilter.value.dateRange?.length || (
            new Date(o.createTime) >= new Date(orderFilter.value.dateRange[0]) &&
            new Date(o.createTime) <= new Date(orderFilter.value.dateRange[1]))
          
          return statusMatch && dateMatch && hasMyProduct
        })
    })

    // 过滤订单中的商品（只显示当前商家的商品）
    const filteredOrderItems = (row) => {
      if (!row.items) return []
      const myProductIds = products.value.map(p => p.id)
      return row.items.filter(item => myProductIds.includes(item.id))
    }

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

    // 检查商品是否属于当前商家
    const isMyProduct = (productId) => {
      return products.value.some(p => p.id === productId)
    }

    // 检查订单是否有当前商家的商品
    const hasMyProducts = (order) => {
      return order.items?.some(item => isMyProduct(item.id)) || false
    }

    // 计算当前商家在订单中的总金额
    const calculateMerchantTotal = (order) => {
      if (!order.items) return 0
      
      return order.items.reduce((total, item) => {
        if (isMyProduct(item.id)) {
          return total + (item.price || 0) * (item.quantity || 1)
        }
        return total
      }, 0)
    }

    // 方法
    const switchView = (view) => {
      if (['info', 'products', 'orders', 'reviews'].includes(view)) {
        currentView.value = view
        
        // 切换到订单管理时加载订单
        if (view === 'orders') {
          loadOrders()
        }
      }
    }

    const logout = () => {
      // 只清除认证信息，保留用户数据
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('userName')
      localStorage.removeItem('role')
      router.push('/login')
    }

    const saveShopInfo = () => {
      // 更新本地存储中的商家信息
      const businessUsers = JSON.parse(localStorage.getItem('businessUsers') || '[]')
      const userId = localStorage.getItem('userId')
      
      if (!userId) {
        ElMessage.error('用户信息异常，请重新登录')
        return
      }
      
      const index = businessUsers.findIndex(u => u.userId === userId)
      
      if (index !== -1) {
        // 更新商家信息
        const updatedUser = {
          ...businessUsers[index],
          userName: shopInfo.value.name,
          address: shopInfo.value.address,
          contact: shopInfo.value.contact,
          avatar: shopInfo.value.avatar
        }
        
        // 如果输入了新密码，则更新密码
        if (shopInfo.value.password) {
          updatedUser.password = shopInfo.value.password
        }
        
        businessUsers[index] = updatedUser
        localStorage.setItem('businessUsers', JSON.stringify(businessUsers))
        
        // 更新本地存储中的用户名
        localStorage.setItem('userName', shopInfo.value.name)
        
        // 更新原始信息备份
        originalShopInfo.value = { ...shopInfo.value }
        
        ElNotification.success({
          title: '保存成功',
          message: '商家信息已更新',
          duration: 2000
        })
        
        // 清空密码字段
        shopInfo.value.password = ''
      } else {
        ElMessage.error('未找到商家信息，请重新登录')
      }
    }

    const resetForm = () => {
      shopInfo.value = { ...originalShopInfo.value }
      shopInfo.value.password = ''
      ElMessage.info('已重置修改内容')
    }

    // 处理商家头像上传
    const handleAvatarChange = (file) => {
      if (!file) return
      
      const rawFile = file.raw
      if (rawFile) {
        const reader = new FileReader()
        reader.onload = (e) => {
          shopInfo.value.avatar = e.target.result
        }
        reader.readAsDataURL(rawFile)
      }
    }

    // 处理商品图片上传
    const handleImageChange = (file) => {
      if (!file) return
      
      const rawFile = file.raw
      if (rawFile) {
        const reader = new FileReader()
        reader.onload = (e) => {
          currentProduct.value.images.push(e.target.result)
        }
        reader.readAsDataURL(rawFile)
      }
    }

    // 移除商品图片
    const removeImage = (index) => {
      currentProduct.value.images.splice(index, 1)
    }

    const showAddDialog = () => {
      currentProduct.value = {
        id: '',
        categoryId: '',
        name: '',
        price: 0,
        stock: 0,
        description: '',
        images: []
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

    // 保存商品到localStorage
    const saveProductToLocalStorage = () => {
      const businessId = localStorage.getItem('userId') || 'default'
      const storageKey = `businessProducts_${businessId}`
      localStorage.setItem(storageKey, JSON.stringify(products.value))
    }

    // 从localStorage加载商品
    const loadProductsFromLocalStorage = () => {
      const businessId = localStorage.getItem('userId') || 'default'
      const storageKey = `businessProducts_${businessId}`
      const savedProducts = localStorage.getItem(storageKey)
      if (savedProducts) {
        products.value = JSON.parse(savedProducts)
      }
    }

    // 从localStorage加载订单
    const loadOrders = () => {
      const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
      
      // 应用筛选条件
      let filteredOrders = [...savedOrders]
      
      // 状态筛选
      if (orderFilter.value.status !== 'all') {
        filteredOrders = filteredOrders.filter(order => order.status === orderFilter.value.status)
      }
      
      // 日期筛选
      if (orderFilter.value.dateRange && orderFilter.value.dateRange.length === 2) {
        const startDate = new Date(orderFilter.value.dateRange[0])
        const endDate = new Date(orderFilter.value.dateRange[1])
        endDate.setHours(23, 59, 59, 999) // 包含结束日期的全天
        
        filteredOrders = filteredOrders.filter(order => {
          const orderDate = new Date(order.createTime)
          return orderDate >= startDate && orderDate <= endDate
        })
      }
      
      orders.value = filteredOrders
    }

    const saveProduct = () => {
      if (!currentProduct.value.id) {
        ElMessage.error('商品ID不能为空')
        return
      }
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
              price: Number(currentProduct.value.price) || 0,
              stock: Math.max(Number(currentProduct.value.stock), 0)
            })
          }
        } else {
          // 检查商品ID是否已存在
          if (products.value.some(p => p.id === currentProduct.value.id)) {
            ElMessage.error('商品ID已存在，请使用不同的ID')
            return
          }
          
          products.value.push({
            ...currentProduct.value,
            name: currentProduct.value.name || '新商品',
            price: Number(currentProduct.value.price) || 0,
            stock: Math.max(Number(currentProduct.value.stock), 0)
          })
        }
        
        // 保存到localStorage
        saveProductToLocalStorage()
        
        productDialogVisible.value = false
        ElMessage.success('商品保存成功')
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
        
        // 保存到localStorage
        saveProductToLocalStorage()
        
        ElMessage.success('商品删除成功')
      }
    }

    // 发货操作 - 只能对已付款订单进行发货
    const handleShip = (order) => {
      if (order?.orderId && order.status === 'paid') {
        // 更新订单状态
        const savedOrders = JSON.parse(localStorage.getItem('orders') || '[]')
        const updatedOrders = savedOrders.map(o => {
          if (o.orderId === order.orderId) {
            return {
              ...o,
              status: 'shipped',
              shipTime: new Date().toLocaleString()
            }
          }
          return o
        })
        
        localStorage.setItem('orders', JSON.stringify(updatedOrders))
        
        ElMessage.success('订单状态已更新为已发货')
        loadOrders() // 重新加载订单以更新状态显示
      } else {
        ElMessage.error('只能对已付款的订单进行发货操作')
      }
    }

    const showReplyDialog = (review) => {
      if (review) {
        currentReview.value = review
        replyDialogVisible.value = true
        replyContent.value = ''
      }
    }

    const submitReply = () => {
      if (currentReview.value && replyContent.value.trim()) {
        currentReview.value.replied = true
        replyDialogVisible.value = false
        replyContent.value = ''
        ElMessage.success('回复成功')
      }
    }

    // 页面加载时从本地存储获取商家信息和商品数据
    onMounted(() => {
      // 设置商家ID为登录时的用户ID
      const userId = localStorage.getItem('userId')
      const userName = localStorage.getItem('userName')
      
      shopInfo.value.id = userId || 'BUS2023VIP'
      shopInfo.value.name = userName || '商家名称'
      
      // 尝试从本地存储获取完整商家信息
      const businessUsers = JSON.parse(localStorage.getItem('businessUsers') || '[]')
      const currentBusiness = businessUsers.find(u => u.userId === userId)
      
      if (currentBusiness) {
        shopInfo.value = {
          ...shopInfo.value,
          address: currentBusiness.address || '',
          contact: currentBusiness.contact || '',
          avatar: currentBusiness.avatar || ''
        }
      }
      
      // 保存原始信息用于重置
      originalShopInfo.value = { ...shopInfo.value }
      
      // 从localStorage加载商品数据
      loadProductsFromLocalStorage()
    })
    </script>

    <style scoped>
    .business-container {
      display: grid;
      grid-template-columns: 220px 1fr;
      grid-template-rows: 80px 1fr;
      height: 100vh;
      background: #f5f7fa;
      font-family: 'Helvetica Neue', Arial, sans-serif;
      overflow: hidden;
    }

    .action-bar {
      grid-column: 1 / -1;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 30px;
      background: #fff;
      border-bottom: 1px solid #ebeef5;
      box-shadow: 0 2px 8px rgba(0,0,0,0.05);
      z-index: 10;
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 15px;
    }

    .user-details {
      display: flex;
      flex-direction: column;
    }

    .welcome-text {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }

    .shop-id {
      font-size: 13px;
      color: #909399;
    }

    .logout-btn {
      padding: 10px 20px;
      border-radius: 6px;
      font-weight: 500;
    }

    .function-panel {
      padding: 25px 0;
      background: #fff;
      border-right: 1px solid #ebeef5;
      overflow-y: auto;
      box-shadow: 2px 0 5px rgba(0,0,0,0.03);
    }

    .function-card {
      display: flex;
      align-items: center;
      padding: 16px 30px;
      cursor: pointer;
      transition: all 0.3s;
      font-size: 15px;
      color: #606266;
      border-left: 4px solid transparent;
    }

    .function-card:hover {
      background-color: #f5f7fa;
      color: #409eff;
    }

    .function-card.active {
      background-color: #ecf5ff;
      color: #409eff;
      font-weight: 500;
      border-left: 4px solid #409eff;
    }

    .function-icon {
      margin-right: 15px;
      font-size: 20px;
      width: 24px;
      text-align: center;
    }

    .content-area {
      padding: 30px;
      background: white;
      overflow-y: auto;
      max-height: calc(100vh - 80px);
    }

    .content-area h2 {
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

    .content-area h2 i {
      margin-right: 12px;
      font-size: 24px;
      color: #409eff;
    }

    .info-card {
      border-radius: 10px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.05);
      border: none;
    }

    .table-header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 25px;
    }

    .filter-bar {
      display: flex;
      gap: 15px;
      margin-bottom: 20px;
    }

    .avatar-upload {
      display: flex;
      align-items: center;
      gap: 25px;
    }

    .avatar-preview {
      border: 1px dashed #dcdfe6;
      border-radius: 8px;
      background-color: #f8f9fa;
    }

    .avatar-actions {
      display: flex;
      flex-direction: column;
      gap: 10px;
    }

    .data-table {
      border-radius: 8px;
      overflow: hidden;
      box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    }

    .error-text {
      color: #f56c6c;
      font-size: 12px;
      padding: 4px 8px;
    }

    .replied-text {
      color: #67C23A;
      font-size: 12px;
    }

    .el-form-item {
      margin-bottom: 25px;
    }

    .info-tip {
      font-size: 12px;
      color: #909399;
      margin-top: 5px;
    }

    .form-actions {
      display: flex;
      justify-content: center;
      gap: 20px;
      margin-top: 30px;
    }

    .form-actions .el-button {
      padding: 12px 30px;
      font-size: 16px;
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

    .review-product {
      font-weight: 600;
      margin-bottom: 10px;
      font-size: 16px;
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

    .input-unit {
      margin-left: 10px;
      color: #909399;
    }

    .image-upload-container {
      display: flex;
      flex-direction: column;
      gap: 15px;
    }

    .image-preview-list {
      display: flex;
      flex-wrap: wrap;
      gap: 15px;
    }

    .image-preview-item {
      position: relative;
      width: 120px;
      height: 120px;
      border: 1px dashed #dcdfe6;
      border-radius: 6px;
      overflow: hidden;
      transition: all 0.3s;
    }

    .image-preview-item:hover {
      border-color: #409eff;
    }

    .preview-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .image-actions {
      position: absolute;
      top: 5px;
      right: 5px;
    }

    .image-upload-btn {
      width: 120px;
    }

    .upload-tip {
      font-size: 12px;
      color: #909399;
      margin-top: 5px;
    }

    .no-image {
      width: 80px;
      height: 80px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f5f7fa;
      border: 1px dashed #dcdfe6;
      border-radius: 4px;
      color: #909399;
      font-size: 12px;
    }

    .image-slot {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 100%;
      background: #f5f7fa;
      color: #909399;
      font-size: 30px;
    }

    /* 新增的商品信息样式 */
    .product-list {
      padding: 10px 0;
    }

    .product-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 10px 0;
      border-bottom: 1px solid #eee;
    }

    .product-item:last-child {
      border-bottom: none;
    }

    .product-details {
      flex: 1;
    }

    .product-name {
      font-weight: 500;
      margin-bottom: 5px;
    }

    .product-price {
      color: #f56c6c;
      font-size: 14px;
    }
    </style>