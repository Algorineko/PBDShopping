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
      <div class="form-group">
        <label>身份:</label>
        <select v-model="loginForm.role" required>
          <option value="customer">顾客</option>
          <option value="merchant">商家</option>
          <option value="admin">管理员</option>
        </select>
      </div>
      <div class="button-group">
        <button type="submit">登录</button>
        <button type="button" @click="goToRegister">注册</button>
      </div>
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
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
        password: '',
        role: 'customer'
      },
      errorMessage: ''
    }
  },
  methods: {
    async handleLogin() {
      this.errorMessage = ''
      
      try {
        console.log('发送登录请求:', this.loginForm)
        const response = await authService.login(this.loginForm)
        console.log('完整登录响应:', response)
        
        // 根据实际响应结构调整
        const responseData = response.data || {}
        
        // 提取token - 直接位于响应顶层
        const token = responseData.token
        console.log('提取的token:', token)
        
        // 提取用户信息 - 位于user对象内
        const userInfo = responseData.user || {}
        
        // 根据角色提取用户名
        let username = ''
        if (this.loginForm.role === 'merchant') {
          username = userInfo.merchantName
        } else {
          // 其他角色处理（根据实际API调整）
          username = userInfo.username || this.loginForm.username
        }
        console.log('提取的用户名:', username)
        
        // 使用登录时选择的角色
        const role = this.loginForm.role
        
        // 调试输出
        console.log('最终登录信息:', { token, username, role })
        
        if (!token) {
          throw new Error('登录响应缺少token字段')
        }
        
        // 存储认证信息
        localStorage.setItem('token', token)
        localStorage.setItem('username', username)
        localStorage.setItem('role', role)

        // 根据角色跳转到对应页面
        switch (role) {
          case 'merchant':
            this.$router.push(`/business/${username}`)
            break
          case 'customer':
            this.$router.push(`/buyer/${username}`)
            break
          case 'admin':
            this.$router.push('/admin')
            break
          default:
            console.warn('未知用户角色:', role)
            this.$router.push('/')
        }
      } catch (error) {
        console.error('登录错误详情:', error)
        
        let message = '登录失败，请稍后再试'
        
        if (error.response) {
          // 服务器返回了错误响应
          const status = error.response.status
          const data = error.response.data || {}
          
          // 尝试从错误响应中获取更具体的消息
          message = data.message || 
                    data.error || 
                    data.msg || 
                    `服务器返回错误: ${status}`
          
          // 添加特定状态码的默认消息
          if (status === 400) {
            message = '请求参数错误: ' + message
          } else if (status === 401) {
            message = '认证失败: ' + message
          } else if (status === 403) {
            message = '无权限访问: ' + message
          } else if (status === 404) {
            message = 'API接口不存在: ' + message
          } else if (status >= 500) {
            message = '服务器错误: ' + message
          }
        } else if (error.request) {
          // 请求已发送但无响应
          message = '服务器无响应，请检查网络连接'
        } else {
          // 请求未发送或处理逻辑错误
          message = error.message || '请求处理失败'
        }
        
        this.errorMessage = message
      }
    },
    goToRegister() {
      this.$router.push('/register')
    }
  }
}
</script>

<style scoped>
/* 样式保持不变 */
.login-container {
  max-width: 400px;
  margin: 2rem auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  position: relative;
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

.error-message {
  margin-top: 1rem;
  padding: 0.75rem;
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
  border-radius: 4px;
}
</style>