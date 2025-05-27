<template>
  <div class="business-layout">
    <!-- 左侧导航 -->
    <div class="sidebar">
      <div class="business-info">
        <h3>{{ businessName }}</h3>
        <p>商家ID: {{ businessId }}</p>
      </div>
      
      <el-menu
        router
        :default-active="$route.path"
        class="nav-menu"
      >
        <el-menu-item index="/business/info">
          <el-icon><User /></el-icon>
          <span>商家信息</span>
        </el-menu-item>
        <el-menu-item index="/business/products">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/business/orders">
          <el-icon><Tickets /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/business/reviews">
          <el-icon><ChatDotRound /></el-icon>
          <span>评价管理</span>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 右侧内容区 -->
    <div class="content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import {
  User,
  Goods,
  Tickets,
  ChatDotRound
} from '@element-plus/icons-vue'

const businessName = ref('我的店铺')
const businessId = ref('BUS123456')
</script>

<style scoped>
.business-layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 240px;
  background: #fff;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
}

.business-info {
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.nav-menu {
  border-right: none;
}

.nav-menu :deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
}

.content {
  flex: 1;
  padding: 20px;
  background: #f5f7fa;
}
</style>