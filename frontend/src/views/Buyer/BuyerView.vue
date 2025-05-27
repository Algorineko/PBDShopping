<template>
  <div class="buyer-container">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="left">
        <router-link to="/buyer/products" class="logo">拼宝东商城</router-link>
        <nav class="nav">
          <router-link to="/buyer/products">首页</router-link>
          <router-link to="/buyer/user">我的淘宝</router-link>
        </nav>
      </div>
      <div class="right">
        <el-button type="text" @click="logout">退出登录</el-button>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="main-content">
      <router-view></router-view>
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 退出登录
const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.buyer-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  height: 60px;
  padding: 0 30px;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 1000;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: #f56c6c;
  text-decoration: none;
  margin-right: 40px;
}

.nav {
  display: flex;
  gap: 30px;
  
  a {
    color: #606266;
    text-decoration: none;
    font-size: 16px;
    transition: color 0.3s;

    &:hover {
      color: #f56c6c;
    }

    &.router-link-active {
      color: #f56c6c;
      border-bottom: 2px solid #f56c6c;
    }
  }
}

.main-content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f5f7fa;
}

.right {
  display: flex;
  align-items: center;
  gap: 20px;
}
</style>