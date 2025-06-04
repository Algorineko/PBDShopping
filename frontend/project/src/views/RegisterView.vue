<template>
  <div class="register-container">
    <h2>用户注册</h2>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label>用户名:</label>
        <input type="text" v-model="registerForm.username" required>
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
        <label>手机号:</label>
        <input type="text" v-model="registerForm.phone" required>
      </div>
      <div class="form-group">
        <label>地址:</label>
        <input type="text" v-model="registerForm.address" required>
      </div>
      <div class="form-group">
        <label>身份:</label>
        <select v-model="registerForm.role" required>
          <option value="">请选择身份</option>
          <option value="merchant">商家</option>
          <option value="customer">顾客</option>
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
        username: '',
        password: '',
        confirmPassword: '',
        phone: '',
        address: '',
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
        await authService.register(this.registerForm)
        alert('注册成功，请登录')
        this.$router.push('/login')
      } catch (error) {
        console.error('注册错误详情:', error.response);
        if (error.response?.data?.message) {
          alert(`注册失败: ${error.response.data.message}`);
        } else {
          alert(error.message || '注册失败，请重试')
        }
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