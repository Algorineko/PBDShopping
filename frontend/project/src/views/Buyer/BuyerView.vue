<template>
  <div class="buyer-layout">
    <!-- 顶部导航栏 -->
    <div class="app-header">
      <div class="header-container">
        <div class="header-content">
          <!-- 左侧品牌和导航 -->
          <div class="header-left">
            <div class="brand-section">
              <div class="logo-container">
                <div class="logo-circle"></div>
                <span class="logo-text">拼宝东</span>
              </div>
              <div class="brand-name">拼宝东商城</div>
            </div>
            
            <div class="navigation">
              <div class="nav-item">
                <router-link :to="`/buyer/${username}/products`" class="nav-link">
                  <div class="nav-icon">🏠</div>
                  <div class="nav-text">首页</div>
                  <div class="active-indicator"></div>
                </router-link>
              </div>
              <div class="nav-item">
                <router-link :to="`/buyer/${username}/user`" class="nav-link">
                  <div class="nav-icon">👤</div>
                  <div class="nav-text">个人中心</div>
                  <div class="active-indicator"></div>
                </router-link>
              </div>
            </div>
          </div>
          
          <!-- 右侧用户操作区 -->
          <div class="header-right">
            <div class="user-actions">
              <div class="user-info">
                <div class="user-avatar">
                  {{ username.charAt(0).toUpperCase() }}
                </div>
                <span class="welcome-text">您好, {{ username }}</span>
              </div>
              
              <el-button 
                class="logout-btn" 
                @click="logout"
              >
                <div class="logout-icon">🚪</div>
                <span>退出登录</span>
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 主内容区 -->
    <main class="main-content">
      <div class="content-container">
        <router-view></router-view>
      </div>
    </main>
    
    <!-- 底部区域 -->
    <footer class="app-footer">
      <div class="footer-content">
        <div class="footer-logo">
          <div class="logo-circle"></div>
          <span>拼宝东商城</span>
        </div>
        
        <div class="footer-links">
          <div class="footer-column">
            <h4>购物指南</h4>
            <ul>
              <li>购物流程</li>
              <li>发票说明</li>
              <li>会员介绍</li>
              <li>常见问题</li>
            </ul>
          </div>
          
          <div class="footer-column">
            <h4>支付方式</h4>
            <ul>
              <li>微信支付</li>
              <li>支付宝支付</li>
              <li>银行卡支付</li>
              <li>货到付款</li>
            </ul>
          </div>
          
          <div class="footer-column">
            <h4>售后服务</h4>
            <ul>
              <li>退换货政策</li>
              <li>取消订单</li>
              <li>价格保护</li>
              <li>联系我们</li>
            </ul>
          </div>
        </div>
      </div>
      
      <div class="copyright">
        © 2025 拼宝东商城 - 正品低价、品质保障、配送及时、轻松购物！
      </div>
    </footer>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue'

const router = useRouter()
const username = ref('')

onMounted(() => {
  username.value = localStorage.getItem('username') || '买家'
})

// 退出登录
const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.buyer-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: #f8fafc;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

/* 顶部导航栏 */
.app-header {
  background: linear-gradient(to right, #f56c6c, #e53935);
  box-shadow: 0 4px 20px rgba(229, 57, 53, 0.2);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 75px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 40px;
}

.brand-section {
  display: flex;
  align-items: center;
  gap: 15px;
}

.logo-container {
  position: relative;
}

.logo-circle {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(5px);
  position: relative;
  overflow: hidden;
  box-shadow: 0 0 0 3px rgba(255, 255, 255, 0.1);
}

.logo-circle::before {
  content: "";
  position: absolute;
  top: 20%;
  left: 20%;
  width: 60%;
  height: 60%;
  background: white;
  border-radius: 50%;
  box-shadow: 0 0 30px rgba(255, 255, 255, 0.6);
}

.logo-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #f56c6c;
  font-weight: bold;
  font-size: 22px;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
}

.brand-name {
  font-size: 24px;
  font-weight: bold;
  color: white;
  letter-spacing: 1px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

/* 导航菜单 */
.navigation {
  display: flex;
  gap: 10px;
}

.nav-item {
  margin: 0;
}

.nav-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px 25px;
  border-radius: 10px;
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-2px);
}

.nav-icon {
  font-size: 24px;
  margin-bottom: 5px;
}

.nav-text {
  font-size: 16px;
  font-weight: 500;
}

.active-indicator {
  position: absolute;
  bottom: 5px;
  width: 0;
  height: 3px;
  background: white;
  border-radius: 2px;
  transition: width 0.3s;
}

.nav-link.router-link-active {
  color: white;
  font-weight: 600;
}

.nav-link.router-link-active .active-indicator {
  width: 40px;
}

/* 右侧用户区 */
.header-right {
  display: flex;
  align-items: center;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-avatar {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  background: linear-gradient(to right, #ff9a9e, #fad0c4);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 18px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.welcome-text {
  color: white;
  font-size: 15px;
  font-weight: 500;
}

.logout-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 30px;
  padding: 9px 18px 9px 15px;
  color: white;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.logout-icon {
  font-size: 18px;
}

/* 主内容区 */
.main-content {
  flex: 1;
  padding: 30px 0;
}

.content-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 底部区域 */
.app-footer {
  background: linear-gradient(to right, #2d3748, #1a202c);
  color: rgba(255, 255, 255, 0.8);
  padding: 40px 0 20px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px 30px;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 22px;
  font-weight: bold;
  color: white;
}

.footer-logo .logo-circle {
  width: 40px;
  height: 40px;
  position: relative;
}

.footer-links {
  display: flex;
  gap: 60px;
}

.footer-column h4 {
  color: white;
  font-size: 18px;
  margin-bottom: 20px;
  position: relative;
  padding-bottom: 10px;
}

.footer-column h4::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 40px;
  height: 2px;
  background: #f56c6c;
}

.footer-column ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-column li {
  margin-bottom: 12px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.footer-column li:hover {
  color: #f56c6c;
  padding-left: 5px;
}

.copyright {
  text-align: center;
  padding: 20px 0;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

/* 响应式设计 */
@media (max-width: 992px) {
  .header-left {
    gap: 20px;
  }
  
  .footer-content {
    flex-direction: column;
    gap: 30px;
  }
  
  .footer-links {
    width: 100%;
    justify-content: space-between;
    gap: 30px;
  }
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    height: auto;
    padding: 15px 0;
  }
  
  .header-left {
    flex-direction: column;
    gap: 15px;
  }
  
  .user-actions {
    margin-top: 15px;
    flex-direction: column;
    gap: 15px;
  }
  
  .navigation {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .footer-links {
    flex-direction: column;
    gap: 30px;
  }
}
</style>