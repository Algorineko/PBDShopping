<template>
  <div class="user-center-container">
    <!-- 顶部用户信息卡片 -->
    <div class="user-card">
      <div class="user-header">
        <div class="user-avatar">
          <div class="avatar-circle">
            {{ username.charAt(0).toUpperCase() }}
          </div>
        </div>
        <div class="user-info">
          <h2 class="username">{{ username }}</h2>
          <div class="user-status">
            <span class="status-icon">🔵</span>
            <span class="status-text">在线</span>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 主内容区 -->
    <div class="user-center-main">
      <!-- 左侧导航 -->
      <div class="side-nav">
        <div 
          v-for="nav in navItems"
          :key="nav.path"
          class="nav-item"
          :class="{ active: activePath === nav.path }"
          @click="goToNav(nav.path)"
        >
          <div class="nav-icon">
            <span v-if="nav.path === 'profile'">👤</span>
            <span v-if="nav.path === 'cart'">🛒</span>
            <span v-if="nav.path === 'orders'">📝</span>
            <span v-if="nav.path === 'reviews'">⭐</span>
          </div>
          <div class="nav-text">{{ nav.name }}</div>
          <div class="nav-indicator"></div>
        </div>
      </div>

      <!-- 右侧内容 -->
      <div class="content-container">
        <div class="content-card">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref, watch } from 'vue'

const route = useRoute()
const router = useRouter()
const username = localStorage.getItem('username') || '用户'
const activePath = ref('')

const navItems = [
  { path: 'profile', name: '我的信息' },
  { path: 'cart', name: '购物车' },
  { path: 'orders', name: '订单记录' },
  { path: 'reviews', name: '评价管理' }
]

// 更新激活路径
watch(() => route.path, (newPath) => {
  activePath.value = newPath.split('/').pop()
}, { immediate: true })

const goToNav = (path) => {
  router.push(`/buyer/${username}/user/${path}`)
}
</script>

<style scoped>
.user-center-container {
  background: linear-gradient(to right, #f0f5ff 0%, #f7f9fc 100%);
  min-height: 100vh;
  padding: 30px;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

/* 用户卡片样式 */
.user-card {
  background: white;
  border-radius: 20px;
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.08);
  margin-bottom: 30px;
  padding: 30px;
  background: linear-gradient(to right, #4a6fa5, #3a5a80);
  color: white;
}

.user-header {
  display: flex;
  align-items: center;
  padding-bottom: 25px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  margin-bottom: 25px;
}

.user-avatar {
  margin-right: 20px;
}

.avatar-circle {
  width: 85px;
  height: 85px;
  border-radius: 50%;
  background: linear-gradient(to right, #ff9a9e, #fad0c4);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  font-weight: bold;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
}

.user-info {
  flex: 1;
}

.username {
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 10px 0;
  letter-spacing: 1px;
}

.user-status {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  opacity: 0.9;
}

.status-icon {
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% { opacity: 0.7; }
  50% { opacity: 1; }
  100% { opacity: 0.7; }
}

.user-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.stat-item {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  border-radius: 15px;
  padding: 20px;
  text-align: center;
  transition: all 0.3s;
}

.stat-item:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateY(-5px);
}

.stat-number {
  font-size: 32px;
  font-weight: 800;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 15px;
  opacity: 0.9;
}

/* 主内容区 */
.user-center-main {
  display: flex;
  gap: 30px;
}

.side-nav {
  width: 280px;
  min-width: 280px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.06);
  padding: 20px;
  animation: slideIn 0.5s ease;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 18px 22px;
  border-radius: 14px;
  margin-bottom: 12px;
  cursor: pointer;
  position: relative;
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
  overflow: hidden;
  color: #5a6575;
}

.nav-item:hover {
  background: #f0f7ff;
  color: #4a6fa5;
}

.nav-item.active {
  background: linear-gradient(to right, #4a6fa5, #3a5a80);
  color: white;
  box-shadow: 0 8px 20px rgba(74, 111, 165, 0.25);
}

.nav-icon {
  font-size: 22px;
  min-width: 36px;
}

.nav-text {
  font-size: 17px;
  font-weight: 500;
  letter-spacing: 0.5px;
}

.nav-indicator {
  position: absolute;
  right: 20px;
  width: 12px;
  height: 12px;
  border: 2px solid #8ba3c7;
  border-radius: 50%;
}

.nav-item.active .nav-indicator {
  background: white;
  border-color: white;
}

/* 内容区域 */
.content-container {
  flex: 1;
}

.content-card {
  background: white;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.06);
  padding: 35px;
  height: 100%;
  min-height: 600px;
  animation: fadeIn 0.6s ease;
}

/* 动画效果 */
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
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
  .user-center-main {
    flex-direction: column;
  }
  
  .side-nav {
    width: 100%;
  }
  
  .user-stats {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .user-card {
    padding: 25px;
  }
  
  .content-card {
    padding: 25px;
  }
  
  .user-header {
    flex-direction: column;
    text-align: center;
  }
  
  .user-avatar {
    margin-right: 0;
    margin-bottom: 20px;
  }
}

@media (max-width: 480px) {
  .user-center-container {
    padding: 20px;
  }
  
  .nav-item {
    padding: 16px 18px;
  }
}
</style>