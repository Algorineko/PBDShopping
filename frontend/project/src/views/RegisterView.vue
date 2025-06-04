<template>
  <div class="register-bg">
    <div class="register-container">
      <div class="register-header">
        <div class="brand-logo">
          <div class="logo-circle">拼</div>
          <h1 class="brand-title">拼宝东商城</h1>
        </div>
        <h2 class="welcome-text">创建您的账户，开启品质购物之旅</h2>
      </div>
      
      <div class="register-card">
        <form @submit.prevent="handleRegister">
          <div class="form-group floating-input">
            <input 
              type="text" 
              v-model="registerForm.username" 
              required
              placeholder=" "
              id="username"
              class="input-with-icon"
            >
            <label for="username">用户名</label>
            <i class="input-icon">👤</i>
          </div>
          
          <div class="form-group floating-input">
            <input 
              type="password" 
              v-model="registerForm.password" 
              required
              placeholder=" "
              id="password"
              class="input-with-icon"
            >
            <label for="password">密码</label>
            <i class="input-icon">🔒</i>
          </div>
          
          <div class="form-group floating-input">
            <input 
              type="password" 
              v-model="registerForm.confirmPassword" 
              required
              placeholder=" "
              id="confirmPassword"
              class="input-with-icon"
            >
            <label for="confirmPassword">确认密码</label>
            <i class="input-icon">✅</i>
          </div>
          
          <div class="form-group floating-input">
            <input 
              type="text" 
              v-model="registerForm.phone" 
              required
              placeholder=" "
              id="phone"
              class="input-with-icon"
            >
            <label for="phone">手机号</label>
            <i class="input-icon">📱</i>
          </div>
          
          <div class="form-group floating-input">
            <input 
              type="text" 
              v-model="registerForm.address" 
              required
              placeholder=" "
              id="address"
              class="input-with-icon"
            >
            <label for="address">地址</label>
            <i class="input-icon">🏠</i>
          </div>
          
          <div class="form-group floating-input">
            <select 
              v-model="registerForm.role" 
              required
              id="role"
              class="input-with-icon"
            >
              <option value="" disabled selected hidden></option>
              <option value="customer">顾客</option>
              <option value="merchant">商家</option>
            </select>
            <label for="role">身份</label>
            <i class="input-icon">👑</i>
          </div>
          
          <div class="terms-agreement">
            <input type="checkbox" id="terms" required>
            <label for="terms">我已阅读并同意<a href="#" @click.prevent="showAlert">《用户协议》</a>和<a href="#" @click.prevent="showAlert">《隐私政策》</a></label>
          </div>
          
          <button type="submit" class="register-btn">确认注册</button>
          
          <div class="login-link">
            <p>已有账户？<a href="#" @click.prevent="goToLogin">立即登录</a></p>
          </div>
        </form>
      </div>
      
      <footer class="register-footer">
        <div class="footer-links">
          <a href="#" @click.prevent="showAlert">隐私政策</a> | 
          <a href="#" @click.prevent="showAlert">使用条款</a> | 
          <a href="#" @click.prevent="showAlert">帮助中心</a>
        </div>
        <p class="copyright">© 2023 拼宝东商城 - 品质生活的最佳选择</p>
      </footer>
    </div>
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
        alert('两次输入的密码不一致');
        return;
      }

      try {
        await authService.register(this.registerForm);
        alert('注册成功，请登录');
        this.$router.push('/login');
      } catch (error) {
        console.error('注册错误详情:', error.response);
        if (error.response?.data?.message) {
          alert(`注册失败: ${error.response.data.message}`);
        } else {
          alert(error.message || '注册失败，请重试');
        }
      }
    },
    showAlert() {
      alert('该服务暂未开放');
    },
    goToLogin() {
      this.$router.push('/login');
    }
  }
}
</script>

<style scoped>
/* 使用登录页面的基本样式 */
.register-bg {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7eb 100%);
  padding: 20px;
}

.register-container {
  width: 100%;
  max-width: 500px;
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  position: relative;
  z-index: 1;
}

.register-header {
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

.register-card {
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

.terms-agreement {
  display: flex;
  align-items: flex-start;
  margin-bottom: 25px;
}

.terms-agreement input {
  margin-right: 10px;
  margin-top: 5px;
}

.terms-agreement label {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
}

.terms-agreement a {
  color: #e53935;
  text-decoration: none;
}

.terms-agreement a:hover {
  text-decoration: underline;
}

.register-btn {
  width: 100%;
  padding: 15px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(to right, #28a745, #2bc44c);
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(40, 167, 69, 0.4);
}

.login-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.login-link a {
  color: #e53935;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}

.register-footer {
  padding: 20px 30px;
  background-color: #f8f9fa;
  border-top: 1px solid #e9ecef;
  text-align: center;
  color: #666;
  font-size: 14px;
}

.footer-links {
  margin-bottom: 8px;
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

.copyright {
  color: #999;
  font-size: 13px;
}

@media (max-width: 500px) {
  .register-container {
    border-radius: 15px;
  }
  
  .register-header, .register-card {
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
  
  .register-footer {
    padding: 15px;
    font-size: 13px;
  }
  
  .terms-agreement label {
    font-size: 13px;
  }
}
</style>