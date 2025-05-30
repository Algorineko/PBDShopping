<template>
  <div id="app">
    <nav v-if="$route.path !== '/login' && $route.path !== '/register'">
      <button @click="logout">退出登录</button>
    </nav>
    <router-view/>
  </div>
</template>


<script>
export default {
  methods: {
    methods: {
    logout() {
      // 只清除认证信息，保留用户数据
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('userName')
     localStorage.removeItem('role')
      this.$router.push('/login')
      // 清空所有 localStorage 数据
      
    }
  }
  }
}
</script>
<style>
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 2rem;
  background-color: #f5f5f5;
}

</style>