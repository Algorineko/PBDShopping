<template>
  <div class="shopping-cart">
    <h2>购物车 ({{ cartItems?.length || 0 }})</h2>
    
    <el-table 
      :data="cartItems"
      v-if="cartItems?.length > 0"
      border
      style="width: 100%"
    >
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
      <div class="total">总计：¥{{ (totalPrice || 0).toFixed(2) }}</div>
      <el-button type="primary" size="large">去结算</el-button>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/stores/cart'
import { storeToRefs } from 'pinia'

const cartStore = useCartStore()
const { items: cartItems, totalPrice } = storeToRefs(cartStore)

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
</style>