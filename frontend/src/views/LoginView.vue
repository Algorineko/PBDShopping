<template>
  <div class="login-container">
    <h2>用户登录</h2>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label>用户名:</label>
        <input type="text" v-model="loginForm.username" required>
      </div>
      <div class="form-group">
        <label>密码:</label>
        <input type="password" v-model="loginForm.password" required>
      </div>
      <div class="button-group">
        <button type="submit">登录</button>
        <button type="button" @click="goToRegister">注册</button>
      </div>
    </form>
  </div>
</template>

<script>
import authService from '../services/auth'

export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    async handleLogin() {
  try {
    const response = await authService.login(this.loginForm)
    
    // 存储用户信息
    localStorage.setItem('token', response.data.token)
    localStorage.setItem('username', this.loginForm.username)
    localStorage.setItem('role', response.data.role)

    // 根据角色跳转
    switch(response.data.role) {
      case 'business':
        this.$router.push('/business')
        break
      case 'buyer':
        this.$router.push('/buyer')
        break
      case 'admin':
        this.$router.push('/admin')
        break
      default:
        alert('未知用户角色')
    }
  } catch (error) {
    console.error('登录失败:', error)
    alert(error.response?.data?.message || '登录失败')
  }
},
    goToRegister() {
      this.$router.push('/register')
    }
  }
}
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 2rem auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}

.form-group input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.button-group {
  margin-top: 1.5rem;
  display: flex;
  gap: 1rem;
}

button {
  flex: 1;
  padding: 0.5rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>