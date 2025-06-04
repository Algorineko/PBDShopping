<template>
  <div class="merchant-info-container">
    <!-- 顶部品牌导航 -->
    <div class="brand-header">
      <div class="brand-section">
        <div class="brand-icon">
          🛒
        </div>
        <div class="brand-info">
          <h1>拼宝东商城</h1>
          <p class="page-subtitle">商家信息管理</p>
        </div>
      </div>
      <div class="user-info">
        <div class="user-avatar">👤</div>
        <span class="username">{{ shopInfo.username || '商家' }}</span>
      </div>
    </div>

    <!-- 信息管理卡片 -->
    <div class="info-management-card">
      <div class="section-header">
        <div class="title-icon">👤</div>
        <h2>商家信息管理</h2>
      </div>

      <div class="info-card-content">
        <!-- 表单部分 -->
        <div class="form-container">
          <div class="form-item">
            <label class="form-label">商家用户名</label>
            <input 
              v-model="shopInfo.username" 
              class="form-input disabled"
              disabled
            />
            <div class="form-tip">商家用户名不可更改</div>
          </div>

          <div class="form-item">
            <label class="form-label">旧密码</label>
            <div class="password-input-wrapper">
              <input 
                v-model="oldPassword" 
                type="password" 
                placeholder="请输入当前密码"
                class="form-input"
              />
              <span class="password-toggle"></span>
            </div>
          </div>

          <div class="form-item">
            <label class="form-label">新密码</label>
            <div class="password-input-wrapper">
              <input 
                v-model="shopInfo.password" 
                type="password" 
                placeholder="请输入新密码"
                class="form-input"
              />
              <span class="password-toggle"></span>
            </div>
            <div class="form-tip">留空则不修改密码</div>
          </div>

          <div class="form-item">
            <label class="form-label">商家地址</label>
            <input 
              v-model="shopInfo.address" 
              placeholder="请输入商家地址"
              class="form-input"
            />
          </div>

          <div class="form-item">
            <label class="form-label">联系电话</label>
            <input 
              v-model="shopInfo.phoneNumber" 
              placeholder="请输入联系电话"
              class="form-input"
            />
          </div>

          <div class="form-item">
            <label class="form-label">商家头像</label>
            <div class="avatar-uploader">
              <div class="avatar-preview" :class="{'has-avatar': shopInfo.avatar}">
                <div v-if="shopInfo.avatar" class="avatar-image-wrapper">
                  <img :src="shopInfo.avatar" alt="商家头像" />
                </div>
                <div v-else class="avatar-placeholder">
                  👤
                </div>
              </div>
              
              <div class="avatar-actions">
                <label class="avatar-upload-btn">
                  <input 
                    type="file" 
                    @change="handleAvatarChange" 
                    class="file-input" 
                    accept="image/*" 
                  />
                  <span class="btn-text">📤 上传头像</span>
                </label>
                
                <button 
                  v-if="shopInfo.avatar" 
                  class="avatar-remove-btn"
                  @click="removeAvatar"
                >
                  🗑️ 移除头像
                </button>
              </div>
            </div>
          </div>
          
          <!-- 安全提示 -->
          <div class="security-note">
            <h3>安全提示</h3>
            <ul>
              <li>修改密码后请务必妥善保存</li>
              <li>联系电话请填写有效的手机号码</li>
              <li>头像图片支持JPG、PNG格式，大小不超过2MB</li>
            </ul>
          </div>

          <!-- 表单操作按钮 -->
          <div class="form-actions">
            <button 
              class="save-btn"
              @click="saveShopInfo"
              :disabled="loading"
            >
              <span v-if="loading">⏳</span>
              <span v-else>✅</span>
              保存修改
            </button>
            
            <button class="reset-btn" @click="resetForm">
              🔄 重置修改
            </button>
          </div>
        </div>
      </div>
      
      <div class="card-footer">
        <p>拼宝东商城 © 2023 商家信息管理系统</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const shopInfo = ref({
  username: '',
  password: '',
  address: '',
  phoneNumber: '',
  avatar: ''
})
const oldPassword = ref('')
const loading = ref(false)
const originalShopInfo = ref({})

// 检查Token有效性
const checkTokenValidity = () => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.error('用户未登录，请重新登录')
    redirectToLogin()
    return false
  }
  
  return true
}

// 重定向到登录页
const redirectToLogin = () => {
  localStorage.removeItem('token')
  setTimeout(() => {
    window.location.href = '/login'
  }, 1500)
}

// 获取商家信息
const fetchShopInfo = async () => {
  if (!checkTokenValidity()) return
  
  try {
    const token = localStorage.getItem('token')
    const response = await axios.get('http://algorineko.top:8080/api/merchants/profile', {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      timeout: 10000
      
    })
    
    console.log('response.data', response.data)
    // 根据实际API响应结构调整
    const data = response.data.data || response.data
    
    // 映射字段到本地数据结构
    shopInfo.value = {
      username: data.merchantName || data.username || '',
      password: '',
      address: data.merchantAddress || data.address || '',
      phoneNumber: data.phoneNumber || '',
      avatar: data.headPicture || data.avatar || ''
    }
   
    if (data.merchantId) {
      localStorage.setItem('userId', data.merchantId.toString())
    } else if (data.id) {
      localStorage.setItem('userId', data.id.toString())
    }
    
    // 从本地存储获取头像（如果存在）
    const userId = localStorage.getItem('userId')
    if (userId) {
      const localAvatar = localStorage.getItem(`merchant_avatar_${userId}`)
      if (localAvatar) {
        shopInfo.value.avatar = localAvatar
      }
    }

    originalShopInfo.value = { ...shopInfo.value }
  } catch (error) {
    console.error('获取商家信息失败:', error)
    
    let errorMessage = '获取商家信息失败，请重试'
    if (error.response) {
      if (error.response.status === 401) {
        errorMessage = '身份验证失败，请重新登录'
        redirectToLogin()
      } else if (error.response.data && error.response.data.message) {
        errorMessage = error.response.data.message
      }
    }
    
    ElMessage.error(errorMessage)
  }
}

// 保存商家信息
const saveShopInfo = async () => {
  if (!checkTokenValidity()) return
  
  loading.value = true
  const token = localStorage.getItem('token')
  
  try {
    // 1. 更新联系信息
    await axios.put('http://algorineko.top:8080/api/merchants/putProfile', {
      merchantAddress: shopInfo.value.address, // 使用后端需要的字段名
      phoneNumber: shopInfo.value.phoneNumber
    }, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      timeout: 10000
    })

    // 2. 如果有新密码则更新密码
    if (shopInfo.value.password) {
      if (!oldPassword.value) {
        ElMessage.warning('修改密码需要提供旧密码')
        loading.value = false
        return
      }
      
      await axios.put('http://algorineko.top:8080/api/merchants/change-password', {
        oldPassword: oldPassword.value,
        newPassword: shopInfo.value.password
      }, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        timeout: 10000
      })
    }

    // 3. 更新成功后重置表单状态
    originalShopInfo.value = { ...shopInfo.value }
    oldPassword.value = ''
    shopInfo.value.password = ''
    
    // 4. 重新获取最新数据确保一致性
    await fetchShopInfo()
    
    ElMessage.success({
      message: '商家信息已更新成功',
      duration: 2000
    })
  } catch (error) {
    console.error('保存失败:', error)
    
    let errorMessage = '保存失败，请重试'
    if (error.response) {
      if (error.response.status === 400) {
        errorMessage = error.response.data.message || '请求参数错误'
      } else if (error.response.status === 401) {
        errorMessage = '身份验证失败，请重新登录'
        redirectToLogin()
      } else if (error.response.status === 403) {
        errorMessage = '旧密码不正确'
      } else if (error.response.data && error.response.data.message) {
        errorMessage = error.response.data.message
      }
    }
    
    ElMessage.error(errorMessage)
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  shopInfo.value = { ...originalShopInfo.value }
  oldPassword.value = ''
  shopInfo.value.password = ''
  ElMessage.info('已重置修改内容')
}

// 头像上传处理
const handleAvatarChange = (e) => {
  const file = e.target.files[0];
  if (!file) return;
  
  // 检查文件大小，限制为2MB
  const maxSize = 2 * 1024 * 1024;
  if (file.size > maxSize) {
    ElMessage.error('图片大小不能超过2MB');
    return;
  }
  
  const reader = new FileReader();
  reader.onload = (e) => {
    shopInfo.value.avatar = e.target.result;
    
    // 将头像保存到本地存储
    const userId = localStorage.getItem('userId');
    if (userId) {
      localStorage.setItem(`merchant_avatar_${userId}`, e.target.result);
    }
    
    ElMessage.success('头像上传成功');
  };
  reader.readAsDataURL(file);
  
  // 重置文件输入，允许再次选择相同的文件
  e.target.value = null;
}

// 移除头像
const removeAvatar = () => {
  shopInfo.value.avatar = '';
  
  // 从本地存储移除头像
  const userId = localStorage.getItem('userId');
  if (userId) {
    localStorage.removeItem(`merchant_avatar_${userId}`);
  }
  
  ElMessage.info('头像已移除');
}

// 组件挂载时获取商家信息
onMounted(() => {
  fetchShopInfo();
})
</script>

<style scoped>
.merchant-info-container {
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4ecf4 100%);
  min-height: 100vh;
  padding: 25px;
}

.brand-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(48, 49, 51, 0.08);
}

.brand-section {
  display: flex;
  align-items: center;
  gap: 15px;
}

.brand-icon {
  background: linear-gradient(135deg, #e53935, #e35d5b);
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(229, 57, 53, 0.3);
  font-size: 28px;
  color: white;
}

.brand-info h1 {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin: 0;
  letter-spacing: 1px;
}

.page-subtitle {
  color: #909399;
  font-size: 14px;
  margin-top: 5px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  background: #e1e4e8;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.username {
  font-weight: 500;
  color: #606266;
}

.info-management-card {
  background: white;
  border-radius: 18px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  margin: 0 auto;
  max-width: 900px;
  animation: fadeIn 0.6s ease;
}

.section-header {
  padding: 25px 30px;
  background: linear-gradient(to right, #f8fafc, #f0f5ff);
  border-bottom: 1px solid #ebeef5;
  display: flex;
  align-items: center;
  gap: 15px;
}

.section-header h2 {
  font-size: 22px;
  color: #303133;
  font-weight: 600;
  margin: 0;
}

.title-icon {
  font-size: 24px;
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #e6f7ff, #d1e9ff);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #1890ff;
}

.info-card-content {
  padding: 35px 40px;
}

.form-container {
  max-width: 700px;
  margin: 0 auto;
}

.form-item {
  margin-bottom: 30px;
}

.form-label {
  display: block;
  font-weight: 600;
  color: #409EFF;
  font-size: 16px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.form-input {
  width: 100%;
  padding: 14px 18px;
  border: 1px solid #dcdfe6;
  border-radius: 12px;
  font-size: 15px;
  transition: all 0.3s;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
}

.form-input:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 3px rgba(24, 144, 255, 0.15);
}

.form-input.disabled {
  background-color: #f8f9fa;
  color: #909399;
  cursor: not-allowed;
}

.password-input-wrapper {
  position: relative;
}

.password-toggle {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  font-size: 18px;
  opacity: 0.6;
}

.form-tip {
  font-size: 13px;
  color: #909399;
  margin-top: 8px;
  padding-left: 8px;
  font-style: italic;
}

.avatar-uploader {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.avatar-preview {
  width: 130px;
  height: 130px;
  border-radius: 16px;
  overflow: hidden;
  position: relative;
  border: 3px solid #f0f5ff;
  transition: all 0.3s;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.avatar-preview.has-avatar {
  border-color: #d9ecff;
}

.avatar-preview:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4ecf4 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #a0a0a0;
  font-size: 45px;
}

.avatar-image-wrapper {
  width: 100%;
  height: 100%;
}

.avatar-image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.avatar-actions {
  display: flex;
  gap: 15px;
}

.avatar-upload-btn {
  position: relative;
  display: inline-block;
}

.avatar-upload-btn .btn-text {
  display: inline-block;
  padding: 10px 18px;
  background: linear-gradient(to right, #1890ff, #47a6ff);
  color: white;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 10px rgba(24, 144, 255, 0.3);
}

.avatar-upload-btn:hover .btn-text {
  background: linear-gradient(to right, #0082e6, #2a96ff);
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(24, 144, 255, 0.4);
}

.file-input {
  position: absolute;
  left: 0;
  top: 0;
  opacity: 0;
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.avatar-remove-btn {
  padding: 10px 18px;
  background: linear-gradient(to right, #ff4d4f, #ff7875);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 10px rgba(255, 77, 79, 0.2);
}

.avatar-remove-btn:hover {
  background: linear-gradient(to right, #e64244, #ff6b6b);
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(255, 77, 79, 0.3);
}

.security-note {
  padding: 20px;
  background: #f0f9ff;
  border-radius: 12px;
  border-left: 4px solid #1890ff;
  margin: 35px 0 25px;
}

.security-note h3 {
  margin-bottom: 12px;
  color: #303133;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.security-note ul {
  padding-left: 20px;
}

.security-note li {
  margin-bottom: 8px;
  line-height: 1.6;
  color: #606266;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 25px;
  margin-top: 30px;
}

.save-btn, .reset-btn {
  padding: 14px 40px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 15px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 10px;
  border: none;
}

.save-btn {
  background: linear-gradient(to right, #e53935, #e35d5b);
  color: white;
  box-shadow: 0 6px 18px rgba(229, 57, 53, 0.4);
}

.save-btn:hover {
  background: linear-gradient(to right, #d63031, #e05553);
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(229, 57, 53, 0.5);
}

.save-btn:disabled {
  background: linear-gradient(to right, #bdc3c7, #95a5a6);
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
  box-shadow: 0 4px 10px rgba(189, 195, 199, 0.4);
}

.reset-btn {
  background: #ffffff;
  color: #606266;
  border: 1px solid #dcdfe6;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
}

.reset-btn:hover {
  background: #f0f5ff;
  color: #1890ff;
  border-color: #1890ff;
  transform: translateY(-3px);
  box-shadow: 0 6px 15px rgba(24, 144, 255, 0.15);
}

.card-footer {
  text-align: center;
  padding: 25px 0;
  color: #909399;
  font-size: 14px;
  border-top: 1px solid #ebeef5;
  background-color: #f8fafc;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .merchant-info-container {
    padding: 15px;
  }
  
  .brand-header {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }
  
  .brand-section {
    flex-direction: column;
  }
  
  .info-card-content {
    padding: 25px 20px;
  }
  
  .section-header {
    padding: 20px;
  }
  
  .avatar-actions {
    flex-direction: column;
    gap: 10px;
  }
  
  .form-actions {
    flex-direction: column;
    gap: 15px;
  }
  
  .save-btn, .reset-btn {
    width: 100%;
    justify-content: center;
  }
  
  .avatar-preview {
    margin: 0 auto;
  }
}
</style>