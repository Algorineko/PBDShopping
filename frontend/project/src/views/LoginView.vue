<template>
  <div class="login-bg">
    <div class="login-container">
      <div class="login-header">
        <div class="brand-logo">
          <div class="logo-circle">拼</div>
          <h1 class="brand-title">拼宝东商城</h1>
        </div>
        <h2 class="welcome-text">欢迎回来！请登录您的账户</h2>
      </div>
      
      <div class="login-card">
        <form @submit.prevent="handleLogin">
          <div class="form-group floating-input">
            <input 
              type="text" 
              v-model="loginForm.username" 
              required
              placeholder=""
              id="username"
              class="input-with-icon"
            >
            <label for="username">用户名</label>
            <i class="input-icon">👤</i>
          </div>
          
          <div class="form-group floating-input">
            <input 
              type="password" 
              v-model="loginForm.password" 
              required
              placeholder=""
              id="password"
              class="input-with-icon"
            >
            <label for="password">密码</label>
            <i class="input-icon">🔒</i>
          </div>
          
          <div class="form-group floating-input">
            <select 
              v-model="loginForm.role" 
              required
              id="role"
              class="input-with-icon"
            >
              <option value="" disabled selected hidden></option>
              <option value="customer">顾客</option>
              <option value="merchant">商家</option>
              <option value="admin">管理员</option>
            </select>
            <label for="role">身份</label>
            <i class="input-icon">👑</i>
          </div>
          
          <div class="remember-forgot">
            <div class="remember-me">
              <input type="checkbox" id="remember">
              <label for="remember">记住我</label>
            </div>
            <a href="#" class="forgot-password" @click.prevent="showAlert">忘记密码?</a>
          </div>
          
          <div class="button-group">
            <button type="submit" class="login-btn">登录</button>
            <button type="button" @click="goToRegister" class="register-btn">注册新账户</button>
          </div>
          
          <div v-if="errorMessage" class="error-message">
            <i class="error-icon">⚠️</i> {{ errorMessage }}
          </div>
        </form>
        
        <div class="social-login">
          <p class="divider">或使用其他方式登录</p>
          <div class="social-icons">
            <button class="social-btn wechat" @click.prevent="showAlert">微</button>
            <button class="social-btn qq" @click.prevent="showAlert">Q</button>
            <button class="social-btn weibo" @click.prevent="showAlert">微</button>
          </div>
        </div>
      </div>
      
      <footer class="login-footer">
        <p>© 2025 拼宝东商城 - 品质生活的最佳选择</p>
        <div class="footer-links">
          <a href="#" @click.prevent="showAlert">隐私政策</a> | <a href="#" @click.prevent="showAlert">使用条款</a> | <a href="#" @click.prevent="showAlert">帮助中心</a>
        </div>
      </footer>
    </div>
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
    },
    showAlert() {
      alert('该服务暂未开放')
    }
  }
}
</script>

<style scoped>
/* 基础样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.login-bg {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7eb 100%);
  padding: 20px;
}

.login-container {
  width: 100%;
  max-width: 480px;
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  position: relative;
  z-index: 1;
}

.login-header {
  padding: 40px 30px 20px;
  background: linear-gradient(to right, #e53935, #e35d5b);
  text-align: center;
  position: relative;
}

.brand-logo {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.logo-circle {
  width: 70px;
  height: 70px;
  background: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  font-weight: bold;
  color: #e53935;
  box-shadow: 0 8px 20px rgba(227, 65, 56, 0.3);
  margin-bottom: 15px;
}

.brand-title {
  font-size: 28px;
  color: white;
  font-weight: bold;
  letter-spacing: 1px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.welcome-text {
  color: rgba(255, 255, 255, 0.9);
  font-size: 16px;
  font-weight: normal;
  margin-top: 5px;
}

.login-card {
  padding: 30px;
}

.form-group {
  margin-bottom: 25px;
  position: relative;
}

.input-with-icon {
  position: relative;
  width: 100%;
  padding: 18px 20px 18px 50px;
  font-size: 16px;
  border: 1px solid #e0e0e0;
  background-color: #fafafa;
  transition: all 0.3s ease;
  border-radius: 10px;
  outline: none;
}

.input-with-icon:focus {
  border-color: #e53935;
  box-shadow: 0 0 0 2px rgba(229, 57, 53, 0.2);
  background-color: white;
}

.input-icon {
  position: absolute;
  left: 16px;
  top: 18px;
  font-size: 18px;
  z-index: 2;
}

.floating-input {
  position: relative;
}

.floating-input label {
  position: absolute;
  left: 50px;
  top: 18px;
  color: #999;
  font-size: 16px;
  transition: all 0.3s ease;
  pointer-events: none;
  background: transparent;
}

.floating-input input:focus + label,
.floating-input input:not(:placeholder-shown) + label,
.floating-input select:focus + label,
.floating-input select:not([value=""]) + label {
  top: -8px;
  left: 45px;
  font-size: 12px;
  color: #e53935;
  background: white;
  padding: 0 5px;
  z-index: 3;
}

.remember-forgot {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.remember-me {
  display: flex;
  align-items: center;
}

.remember-me input {
  margin-right: 8px;
}

.forgot-password {
  color: #666;
  text-decoration: none;
  font-size: 14px;
}

.forgot-password:hover {
  color: #e53935;
  text-decoration: underline;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.login-btn, .register-btn {
  padding: 15px;
  font-size: 16px;
  font-weight: 600;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.login-btn {
  background: linear-gradient(to right, #e53935, #e35d5b);
  color: white;
  box-shadow: 0 4px 15px rgba(229, 57, 53, 0.3);
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(229, 57, 53, 0.4);
}

.register-btn {
  background-color: transparent;
  color: #666;
  border: 1px solid #ddd;
}

.register-btn:hover {
  background-color: #f5f5f5;
  border-color: #ccc;
}

.error-message {
  margin-top: 20px;
  padding: 12px;
  background-color: #ffebee;
  color: #e53935;
  border-radius: 8px;
  display: flex;
  align-items: center;
  animation: fadeIn 0.3s ease;
}

.error-icon {
  margin-right: 8px;
  font-size: 18px;
}

.social-login {
  margin-top: 30px;
  text-align: center;
}

.divider {
  color: #999;
  position: relative;
  margin-bottom: 25px;
}

.divider::before,
.divider::after {
  content: "";
  position: absolute;
  top: 50%;
  width: 38%;
  height: 1px;
  background-color: #e0e0e0;
}

.divider::before {
  left: 0;
}

.divider::after {
  right: 0;
}

.social-icons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.social-btn {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  border: none;
  font-size: 18px;
  font-weight: bold;
  color: white;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.social-btn:hover {
  transform: translateY(-3px);
}

.wechat {
  background-color: #09bb07;
}

.qq {
  background-color: #12b7f5;
}

.weibo {
  background-color: #e6162d;
}

.login-footer {
  padding: 20px 30px;
  background-color: #f8f9fa;
  border-top: 1px solid #e9ecef;
  text-align: center;
  color: #666;
  font-size: 14px;
}

.footer-links {
  margin-top: 8px;
}

.footer-links a {
  color: #666;
  text-decoration: none;
  margin: 0 8px;
}

.footer-links a:hover {
  color: #e53935;
  text-decoration: underline;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 500px) {
  .login-container {
    border-radius: 15px;
  }
  
  .login-header, .login-card {
    padding: 25px 20px;
  }
  
  .logo-circle {
    width: 60px;
    height: 60px;
    font-size: 28px;
  }
  
  .brand-title {
    font-size: 24px;
  }
  
  .welcome-text {
    font-size: 14px;
  }
  
  .login-footer {
    padding: 15px;
    font-size: 13px;
  }
}
</style>