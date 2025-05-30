<template>
  <div class="shopping-cart">
    <h2>购物车 ({{ cartItems?.length || 0 }})</h2>
    
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
        :disabled="selectedItems.size === 0"
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
            :model-value="selectedItems.has(row.id)" 
            @change="handleToggleSelection(row.id)"
          />
        </template>
      </el-table-column>
      
      <!-- 商品列 -->
      <el-table-column label="商品" width="300">
        <template #default="{ row }">
          <div class="product-info" v-if="row">
            <router-link :to="`/buyer/product/${row.id}`">
              <el-image 
                :src="row.image || '/placeholder-product.jpg'"
                width="80"
                style="cursor: pointer"
              />
            </router-link>
            <router-link 
              :to="`/buyer/product/${row.id}`"
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
            @click="removeItem(row?.id)"
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
        :disabled="selectedItems.size === 0"
        @click="checkout"
      >
        去结算
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/stores/cart'
import { storeToRefs } from 'pinia'
import { computed } from 'vue'

const cartStore = useCartStore()

// 使用 storeToRefs 只解构响应式属性
const { 
  items: cartItems, 
  selectedItems,
  selectedTotalPrice,
  selectedItemsList
} = storeToRefs(cartStore)

// 计算是否全选
const allSelected = computed(() => {
  return selectedItems.value.size === cartItems.value.length && cartItems.value.length > 0
})

// 处理全选/取消全选
const handleToggleSelectAll = () => {
  cartStore.toggleSelectAll()
}

// 处理单个选择项切换
const handleToggleSelection = (id) => {
  cartStore.toggleSelection(id)
}

const updateQuantity = (item) => {
  if (!item?.id) {
    ElMessage.error('无效的商品数据')
    return
  }
  if (item.quantity < 1) {
    cartStore.removeItem(item.id)
    ElMessage.warning('商品已移除')
  }
}

const removeItem = (itemId) => {
  if (!itemId) {
    ElMessage.error('无效操作')
    return
  }
  cartStore.removeItem(itemId)
  ElMessage.success('已移除商品')
}

// 删除选中的商品
const removeSelectedItems = () => {
  const selectedIds = [...selectedItems.value]
  cartStore.removeItems(selectedIds)
  ElMessage.success(`已删除 ${selectedIds.length} 件商品`)
}

// 结算功能
const checkout = () => {
  if (selectedItems.value.size === 0) {
    ElMessage.warning('请选择要结算的商品')
    return
  }
  
  try {
    // 获取现有订单数据
    let orders = JSON.parse(localStorage.getItem('orders') || '[]')
    
    // 清理旧订单：只保留最近50个订单
    const MAX_ORDER_COUNT = 50;
    if (orders.length > MAX_ORDER_COUNT) {
      orders = orders.slice(-MAX_ORDER_COUNT);
      localStorage.setItem('orders', JSON.stringify(orders));
    }
    
    // 获取下一个订单ID
    let nextOrderId = 1
    if (orders.length > 0) {
      const maxId = Math.max(...orders.map(order => order.orderId))
      nextOrderId = maxId + 1
    }
    
    // 创建新订单
    const newOrder = {
      orderId: nextOrderId,
      createTime: new Date().toLocaleString(),
      items: [...selectedItemsList.value],
      totalAmount: selectedTotalPrice.value,
      status: 'pending', // 待付款
      reviewed: false,
      isPaid: false
    }
    
    // 保存订单
    orders.push(newOrder)
    localStorage.setItem('orders', JSON.stringify(orders))
    
    // 从购物车移除已结算商品
    const selectedIds = [...selectedItems.value]
    cartStore.removeItems(selectedIds)
    
    ElMessage.success(`订单 #${nextOrderId} 创建成功！`)
    
    // 跳转到订单页面
    // router.push(`/buyer/user/orders`)
  } catch (error) {
    if (error.name === 'QuotaExceededError') {
      ElMessage.error('订单存储失败：存储空间不足，请清理浏览器数据');
    } else {
      ElMessage.error(`订单创建失败：${error.message}`);
    }
    console.error('结算失败:', error);
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
</style>