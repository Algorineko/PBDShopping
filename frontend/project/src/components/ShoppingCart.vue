<template>
  <div class="shopping-cart">
    <h2>购物车 ({{ cartItems?.length || 0 }})</h2>
    
    <div v-if="loading" class="loading-overlay">
      <el-progress :percentage="50" :indeterminate="true" />
      <p>加载购物车中...</p>
    </div>
    
    <div v-if="error" class="error-message">
      <el-alert :title="error" type="error" show-icon />
      <el-button type="primary" @click="retryFetch">重试</el-button>
    </div>
    
    <template v-else>
      <div class="cart-actions" v-if="cartItems?.length > 0">
        <el-checkbox 
          :model-value="allSelected"
          @change="handleToggleSelectAll"
        >
          全选
        </el-checkbox>
        <el-button 
          type="danger" 
          size="small"
          :disabled="selectedItems.size === 0 || cartStore.loading"
          :loading="cartStore.loading"
          @click="removeSelectedItems"
        >
          删除选中
        </el-button>
      </div>
      
      <el-table 
        :data="cartItems"
        v-if="cartItems?.length > 0"
        border
        style="width: 100%"
      >
        <!-- 选择列 -->
        <el-table-column width="50" align="center">
          <template #header>
            <el-checkbox 
              :model-value="allSelected"
              @change="handleToggleSelectAll" 
            />
          </template>
          <template #default="{ row }">
            <el-checkbox 
              :model-value="selectedItems.has(row.cartItemId)" 
              @change="handleToggleSelection(row.cartItemId)"
            />
          </template>
        </el-table-column>
        
        <!-- 商品列 -->
        <el-table-column label="商品" width="300">
          <template #default="{ row }">
            <div class="product-info" v-if="row">
              <router-link :to="`/buyer/${username}/product/${row.id}`">
                <el-image 
                  :src="row.image || '/placeholder-product.jpg'"
                  width="80"
                  style="cursor: pointer"
                />
              </router-link>
              <router-link 
                :to="`/buyer/${username}/product/${row.id}`"
                class="product-link"
              >
                {{ row.name || '未知商品' }}
              </router-link>
            </div>
          </template>
        </el-table-column>

        <!-- 单价列 -->
        <el-table-column label="单价" width="120">
          <template #default="{ row }">
            ¥{{ (row.price || 0).toFixed(2) }}
          </template>
        </el-table-column>

        <!-- 数量列 -->
        <el-table-column label="数量" width="150">
          <template #default="{ row }">
            <el-input-number 
              v-model="row.quantity"
              :min="1"
              :max="99"
              :disabled="cartStore.loading"
              @change="updateQuantity(row)"
            />
          </template>
        </el-table-column>

        <!-- 小计列 -->
        <el-table-column label="小计" width="120">
          <template #default="{ row }">
            ¥{{ ((row.price || 0) * (row.quantity || 1)).toFixed(2) }}
          </template>
        </el-table-column>
        
        <!-- 操作列 -->
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button 
              type="danger" 
              :disabled="cartStore.loading"
              :loading="cartStore.loading"
              @click="removeItem(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空购物车提示 -->
      <el-empty
        v-else
        description="您的购物车还是空的，快去选购商品吧！"
        class="empty-tip"
      />

      <!-- 结算区域 -->
      <div class="checkout" v-if="cartItems?.length > 0">
        <div class="selected-info">
          已选 <span class="highlight">{{ selectedItems.size }}</span> 件商品
        </div>
        <div class="total">
          合计：<span class="highlight">¥{{ (selectedTotalPrice || 0).toFixed(2) }}</span>
        </div>
        <el-button 
          type="primary" 
          size="large"
          :disabled="selectedItems.size === 0 || cartStore.loading || checkoutLoading"
          :loading="checkoutLoading"
          @click="checkout"
        >
          去结算
        </el-button>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/stores/cart'
import { storeToRefs } from 'pinia'
import { computed, onMounted, ref } from 'vue'
import axios from 'axios'

const cartStore = useCartStore()
const { 
  items: cartItems, 
  selectedItems,
  selectedTotalPrice,
  selectedItemsList,
  loading,
  error
} = storeToRefs(cartStore)

// 添加结算加载状态
const checkoutLoading = ref(false)
// 从token获取用户名
const token = localStorage.getItem('token')
const username = computed(() => {
  if (token) {
    const payload = parseJwt(token)
    return payload.sub || 'guest'
  }
  return 'guest'
})

// 计算是否全选
const allSelected = computed(() => {
  return selectedItems.value.size === cartItems.value.length && cartItems.value.length > 0
})

// 初始化获取购物车数据
onMounted(async () => {
  if (!cartItems.value.length) {
    await cartStore.fetchCart()
  }
})

// 重试获取购物车
const retryFetch = async () => {
  await cartStore.fetchCart()
}

// 处理全选/取消全选
const handleToggleSelectAll = () => {
  cartStore.toggleSelectAll()
}

// 处理单个选择项切换
const handleToggleSelection = (id) => {
  cartStore.toggleSelection(id)
}

const updateQuantity = async (item) => {
  if (!item?.cartItemId) {
    ElMessage.error('无效的商品数据')
    return
  }
  
  try {
    await cartStore.updateQuantity(item.cartItemId, item.quantity)
    ElMessage.success('数量更新成功')
  } catch (err) {
    ElMessage.error(`数量更新失败: ${err.message || '未知错误'}`)
  }
}

const removeItem = async (item) => {
  if (!item?.cartItemId) {
    ElMessage.error('无效操作')
    return
  }
  
  try {
    await cartStore.removeItem(item.cartItemId)
    ElMessage.success('已移除商品')
  } catch (err) {
    ElMessage.error(`移除商品失败: ${err.message || '未知错误'}`)
  }
}

// 删除选中的商品
const removeSelectedItems = async () => {
  if (selectedItems.value.size === 0) return
  
  const selectedIds = [...selectedItems.value]
  try {
    await cartStore.removeItems(selectedIds)
    ElMessage.success(`已删除 ${selectedIds.length} 件商品`)
  } catch (err) {
    ElMessage.error(`删除商品失败: ${err.message || '未知错误'}`)
  }
}

// 获取商品商家信息
const fetchProductMerchant = async (productId) => {
  try {
    const response = await axios.get(
      `http://algorineko.top:8080/api/merchant/product/detail/${productId}`
    )
    if (response.data && response.data.merchantId) {
      return response.data.merchantId
    }
    
    throw new Error('未获取到商家信息')
  } catch (error) {
    console.error(`获取商品 ${productId} 的商家信息失败:`, error)
    throw new Error(`获取商品商家信息失败: ${error.message || '未知错误'}`)
  }
}

// JWT 解析函数
const parseJwt = (token) => {
  try {
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    return JSON.parse(jsonPayload)
  } catch (e) {
    console.error('Token解析失败:', e)
    return null
  }
}

// 结算功能
const checkout = async () => {
  if (selectedItems.value.size === 0) {
    ElMessage.warning('请选择要结算的商品')
    return
  }
  
  checkoutLoading.value = true
  
  try {
    // 1. 获取所有商品的商家ID
    const merchantPromises = selectedItemsList.value.map(item => 
      fetchProductMerchant(item.id)
    )
    
    const merchantIds = await Promise.all(merchantPromises)
    
    // 2. 检查所有商品是否属于同一商家
    const uniqueMerchantIds = [...new Set(merchantIds)]
    
    if (uniqueMerchantIds.length === 0) {
      throw new Error('无法确定商家信息')
    }
    
    if (uniqueMerchantIds.length > 1) {
      throw new Error('您选择的商品来自不同商家，请分开结算')
    }
    
    const merchantId = uniqueMerchantIds[0]
    
    // 3. 调用后端API创建订单
    const token = localStorage.getItem('token')
    if (!token) {
      throw new Error('请先登录后再结算')
    }
    
    // 解析token获取用户ID
    const payload = parseJwt(token)
    if (!payload || !payload.customerId) {
      throw new Error('用户信息不完整，请重新登录')
    }
    
    // 构造请求数据
    const orderItems = selectedItemsList.value.map(item => ({
      productId: item.id,
      quantity: item.quantity
    }))
    
    const requestData = {
      customerId: Number(payload.customerId),
      merchantId: merchantId,
      items: orderItems
    }
    
    // 调用创建订单API
    const response = await axios.post(
      'http://algorineko.top:8080/api/order/create',
      requestData,
      {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    )
    
    if (!response.data) {
      throw new Error('订单创建失败')
    }
    
    // 4. 从购物车中移除已结算商品
    const selectedIds = [...selectedItems.value]
    await cartStore.removeItems(selectedIds)
    
    ElMessage.success('订单创建成功！')
    
    // 跳转到订单页面
    // router.push(`/buyer/user/orders`)
  } catch (error) {
    let errorMsg = '订单创建失败'
    
    // 处理特定错误类型
    if (error.message.includes('不同商家')) {
      errorMsg = error.message
    } else if (error.message.includes('商家信息')) {
      errorMsg = error.message
    } else if (error.response) {
      if (error.response.status === 400) {
        errorMsg = error.response.data?.message || '请求参数错误'
      } else if (error.response.status === 401) {
        errorMsg = '身份验证失败，请重新登录'
      } else if (error.response.status === 500) {
        errorMsg = error.response.data?.message || '服务器内部错误'
      } else {
        errorMsg = error.response.data?.message || '未知错误'
      }
    } else {
      errorMsg = error.message || '网络错误，请检查连接'
    }
    
    ElMessage.error(errorMsg)
    console.error('结算失败:', error)
  } finally {
    checkoutLoading.value = false
  }
}
</script>

<style scoped>
.product-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.product-link {
  color: #606266;
  text-decoration: none;
  &:hover {
    color: #409eff;
    text-decoration: underline;
  }
}

.checkout {
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 30px;
}

.total {
  font-size: 20px;
  color: #f56c6c;
}

.empty-tip {
  margin-top: 50px;
  padding: 40px 0;
  background: #fff;
  border-radius: 8px;
}

.cart-actions {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
  padding: 10px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.selected-info {
  font-size: 14px;
  color: #606266;
}

.highlight {
  color: #f56c6c;
  font-weight: bold;
  font-size: 18px;
}

.loading-overlay {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 8px;
  margin-bottom: 20px;
}

.error-message {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  margin-bottom: 20px;
}
</style>