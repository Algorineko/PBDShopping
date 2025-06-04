<template>
  <div class="business-page">
    <h1>管理员后台</h1>
    <p>欢迎管理员 {{ username }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: localStorage.getItem('username') || '未知用户'
    }
  }
}
</script>