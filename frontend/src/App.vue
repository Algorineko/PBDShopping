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
    logout() {
      localStorage.clear()
      this.$router.push('/login')
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