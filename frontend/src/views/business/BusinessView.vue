<template>
  <div class="business-container">
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

    <div class="function-panel">
      <router-link 
        v-for="item in functions" 
        :key="item.key"
        :to="{ name: item.routeName }"
        class="function-card"
        :class="{ active: isActive(item.routeName) }"
      >
        <i :class="item.icon" class="function-icon"></i>
        <span>{{ item.label }}</span>
      </router-link>
    </div>

    <div class="content-area">
      <router-view :shop-info="shopInfo" @update-shop-info="updateShopInfo" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const functions = ref([
  { key: 'info', label: '商家信息', icon: 'el-icon-user', routeName: 'BusinessInfo' },
  { key: 'products', label: '商品管理', icon: 'el-icon-goods', routeName: 'BusinessProducts' },
  { key: 'orders', label: '订单管理', icon: 'el-icon-tickets', routeName: 'BusinessOrders' },
  { key: 'reviews', label: '评价管理', icon: 'el-icon-chat-dot-round', routeName: 'BusinessReviews' }
])

const shopInfo = ref({
  id: '',
  name: '',
  address: '',
  contact: '',
  password: '',
  avatar: ''
})

const isActive = computed(() => (routeName) => {
  return router.currentRoute.value.name === routeName
})

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userId')
  localStorage.removeItem('userName')
  localStorage.removeItem('role')
  router.push('/login')
}

const updateShopInfo = (newInfo) => {
  shopInfo.value = { ...shopInfo.value, ...newInfo }
}

onMounted(() => {
  const userId = localStorage.getItem('userId')
  const userName = localStorage.getItem('userName')
  
  shopInfo.value.id = userId || 'BUS2023VIP'
  shopInfo.value.name = userName || '商家名称'
  
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
  text-decoration: none;
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
</style>