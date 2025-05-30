<template>
  <div class="register-container">
    <h2>用户注册</h2>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label>用户ID:</label>
        <input type="text" v-model="registerForm.userId" required>
      </div>
      <div class="form-group">
        <label>用户名称:</label>
        <input type="text" v-model="registerForm.userName" required>
      </div>
      <div class="form-group">
        <label>密码:</label>
        <input type="password" v-model="registerForm.password" required>
      </div>
      <div class="form-group">
        <label>确认密码:</label>
        <input type="password" v-model="registerForm.confirmPassword" required>
      </div>
      <div class="form-group">
        <label>身份:</label>
        <select v-model="registerForm.role" required>
          <option value="">请选择身份</option>
          <option value="business">商家</option>
          <option value="buyer">买家</option>
          <option value="admin">管理员</option>
        </select>
      </div>
      <button type="submit">确认注册</button>
    </form>
  </div>
</template>

<script>
import authService from '../services/auth'

export default {
  data() {
    return {
      registerForm: {
        userId: '',
        userName: '',
        password: '',
        confirmPassword: '',
        role: ''
      }
    }
  },
  methods: {
    async handleRegister() {
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        alert('两次输入的密码不一致')
        return
      }

      try {
        await authService.register({
          userId: this.registerForm.userId,
          userName: this.registerForm.userName,
          password: this.registerForm.password,
          role: this.registerForm.role
        })
        alert('注册成功，请登录')
        this.$router.push('/login')
      } catch (error) {
        alert(error.message || '注册失败，请重试')
      }
    }
  }
}
</script>

<style scoped>
.register-container {
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

.form-group input,
.form-group select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 0.5rem;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 1rem; 
}

button:hover {
  background-color: #218838;
}
</style>