<template>
  <div class="user-center">
    <!-- 左侧导航 -->
    <div class="side-nav">
      <div 
        v-for="nav in navItems"
        :key="nav.path"
        class="nav-item"
        :class="{ active: activePath === nav.path }"
        @click="goToNav(nav.path)"
      >
        {{ nav.name }}
      </div>
    </div>

    <!-- 右侧内容 -->
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref, watch } from 'vue'

const route = useRoute()
const router = useRouter()
const username = localStorage.getItem('username') || ''
const activePath = ref('')

const navItems = [
  { path: 'profile', name: '我的信息' },
  { path: 'cart', name: '我的购物车' },
  { path: 'orders', name: '我的订单' },
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
/* 保持原有样式不变 */
.user-center {
  display: flex;
  min-height: calc(100vh - 60px);
}

.side-nav {
  width: 200px;
  padding: 20px;
  background: #fff;
  border-right: 1px solid #ebeef5;
}

.nav-item {
  padding: 15px;
  margin-bottom: 10px;
  cursor: pointer;
  border-radius: 4px;
  transition: background 0.3s;
}

.nav-item:hover {
  background: #f5f7fa;
}

.nav-item.active {
  background: #ecf5ff;
  color: #409eff;
}

.content {
  flex: 1;
  padding: 20px;
  background: #f5f7fa;
}
</style>