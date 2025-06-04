<template>
  <div class="business-dashboard">
    <!-- 顶部导航栏 -->
    <div class="dashboard-header">
      <div class="header-container">
        <div class="brand-section">
          <div class="logo-container">
            <div class="logo-circle">
              <span>商</span>
            </div>
            <h1 class="brand-name">商家管理中心</h1>
          </div>
          
          <div class="user-info">
            <div class="user-avatar">
              <span>{{ shopInfo.name?.charAt(0) || '商' }}</span>
            </div>
            <div class="user-details">
              <span class="welcome-text">欢迎您，{{ shopInfo.name || '商家' }}</span>
              <span class="username">@{{ shopInfo.id }}</span>
            </div>
          </div>
        </div>
        
        <button class="logout-btn" @click="logout">
          <span class="logout-icon">🚪</span>
          <span class="logout-text">退出登录</span>
        </button>
      </div>
    </div>
    
    <div class="dashboard-body">
      <!-- 功能导航面板 -->
      <div class="function-panel">
        <div class="panel-header">
          <span class="panel-title">管理中心</span>
        </div>
        
        <nav class="function-nav">
          <router-link 
            v-for="item in functions" 
            :key="item.key"
            :to="{ name: item.routeName }"
            class="nav-item"
            :class="{ active: isActive(item.routeName) }"
          >
            <div class="nav-icon">
              <span v-if="item.key === 'info'">👤</span>
              <span v-if="item.key === 'products'">📦</span>
              <span v-if="item.key === 'orders'">📊</span>
            </div>
            <span class="nav-label">{{ item.label }}</span>
            <div class="active-indicator"></div>
          </router-link>
        </nav>
        
        <div class="panel-footer">
          <span class="support-info">客服支持: 400-888-8888</span>
        </div>
      </div>
      
      <!-- 主内容区域 -->
      <div class="content-container">
        <router-view :shop-info="shopInfo" @update-shop-info="updateShopInfo" class="content-area" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const functions = ref([
  { key: 'info', label: '商家信息', routeName: 'BusinessInfo' },
  { key: 'products', label: '商品管理', routeName: 'BusinessProducts' },
  { key: 'orders', label: '订单管理', routeName: 'BusinessOrders' }
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
  localStorage.removeItem('username')
  localStorage.removeItem('role')
  router.push('/login')
}

const updateShopInfo = (newInfo) => {
  shopInfo.value = { ...shopInfo.value, ...newInfo }
}

onMounted(() => {
  const username = localStorage.getItem('username') || ''
  
  // 使用用户名作为商家ID和名称
  shopInfo.value.id = username
  shopInfo.value.name = username
  
  // 从本地存储获取商家信息
  const businessUsers = JSON.parse(localStorage.getItem('businessUsers') || '[]')
  const currentBusiness = businessUsers.find(u => u.username === username)
  
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
.business-dashboard {
  display: flex;
  flex-direction: column;
  height: 100vh;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background: linear-gradient(135deg, #f8fafc 0%, #eef4fa 100%);
  overflow: hidden;
}

/* 顶部导航栏样式 */
.dashboard-header {
  background: linear-gradient(to right, #1e3a8a, #3b82f6);
  box-shadow: 0 4px 15px rgba(30, 58, 138, 0.3);
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1400px;
  margin: 0 auto;
  padding: 15px 30px;
}

.brand-section {
  display: flex;
  align-items: center;
  gap: 25px;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 15px;
}

.logo-circle {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  background: linear-gradient(to right, #f0f9ff, #d1e9ff);
  color: #1e40af;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.3);
}

.brand-name {
  font-size: 24px;
  font-weight: 700;
  color: white;
  margin: 0;
  letter-spacing: 1px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.15);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50px;
  backdrop-filter: blur(5px);
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(to right, #7e57c2, #5e35b1);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 18px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.user-details {
  display: flex;
  flex-direction: column;
}

.welcome-text {
  font-size: 16px;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.95);
}

.username {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 3px;
}

.logout-btn {
  background: linear-gradient(to right, #f87171, #ef4444);
  border: none;
  color: white;
  padding: 12px 25px;
  border-radius: 30px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.3);
}

.logout-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(239, 68, 68, 0.4);
}

/* 主内容区域布局 */
.dashboard-body {
  display: flex;
  flex: 1;
  overflow: hidden;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  padding: 0 20px 30px;
}

/* 功能面板样式 */
.function-panel {
  width: 260px;
  min-width: 260px;
  background: white;
  border-radius: 18px;
  margin-right: 25px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  animation: slideInLeft 0.5s ease;
}

.panel-header {
  padding: 20px 25px;
  border-bottom: 1px solid #f1f5f9;
}

.panel-title {
  font-size: 18px;
  font-weight: 700;
  color: #1e3a8a;
  display: flex;
  align-items: center;
  gap: 10px;
}

.function-nav {
  padding: 20px 0;
  flex: 1;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 18px 28px;
  position: relative;
  cursor: pointer;
  transition: all 0.3s;
  color: #4b5563;
  text-decoration: none;
}

.nav-item:hover {
  background: #f0f9ff;
  color: #3b82f6;
}

.nav-item.active {
  background: #eff6ff;
  color: #2563eb;
}

.nav-item.active .active-indicator {
  opacity: 1;
}

.nav-icon {
  font-size: 24px;
  width: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-label {
  font-size: 16px;
  font-weight: 500;
  margin-left: 18px;
  transition: all 0.3s;
}

.active-indicator {
  position: absolute;
  right: 20px;
  width: 8px;
  height: 8px;
  background: #2563eb;
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.3s;
}

.panel-footer {
  padding: 20px 25px;
  background: #f8fafc;
  border-top: 1px solid #eef2f6;
  font-size: 13px;
  color: #64748b;
  text-align: center;
}

/* 内容区域样式 */
.content-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.content-area {
  flex: 1;
  background: white;
  border-radius: 18px;
  padding: 30px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
  overflow-y: auto;
  animation: fadeIn 0.6s ease;
}

/* 动画效果 */
@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(15px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 992px) {
  .function-panel {
    width: 220px;
    min-width: 220px;
  }
  
  .nav-item {
    padding: 16px 24px;
  }
}

@media (max-width: 768px) {
  .dashboard-body {
    flex-direction: column;
    padding: 0;
  }
  
  .function-panel {
    width: 100%;
    margin: 0 0 20px 0;
    border-radius: 0 0 20px 20px;
  }
  
  .header-container {
    flex-direction: column;
    gap: 15px;
    padding: 15px 20px;
  }
  
  .brand-section {
    width: 100%;
    justify-content: space-between;
  }
  
  .content-area {
    border-radius: 18px 18px 0 0;
  }
  
  .logout-btn {
    width: 100%;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .nav-item {
    padding: 14px 20px;
  }
  
  .content-area {
    padding: 20px;
  }
}
</style>