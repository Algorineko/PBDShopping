<template>
  <div class="info-section">
    <h2><i class="el-icon-user"></i> 商家信息管理</h2>
    <el-card class="info-card">
      <el-form label-width="120px">
        <el-form-item label="商家用户名">
          <el-input v-model="shopInfo.username" disabled />
          <div class="info-tip">商家用户名不可更改</div>
        </el-form-item>
        <el-form-item label="旧密码">
          <el-input 
            v-model="oldPassword" 
            type="password" 
            show-password
            placeholder="请输入当前密码" 
          />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input 
            v-model="shopInfo.password" 
            type="password" 
            show-password
            placeholder="请输入新密码" 
          />
          <div class="info-tip">留空则不修改密码</div>
        </el-form-item>
        <el-form-item label="商家地址">
          <el-input v-model="shopInfo.address" placeholder="请输入商家地址" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="shopInfo.phoneNumber" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="商家头像">
          <div class="avatar-upload">
            <el-avatar 
              :size="120" 
              :src="shopInfo.avatar" 
              class="avatar-preview"
            />
            <div class="avatar-actions">
              <el-upload
                action="#"
                :show-file-list="false"
                :auto-upload="false"
                :on-change="handleAvatarChange"
              >
                <el-button type="primary" icon="el-icon-upload" size="small">上传头像</el-button>
              </el-upload>
              <el-button 
                v-if="shopInfo.avatar" 
                type="danger" 
                icon="el-icon-delete" 
                size="small"
                @click="removeAvatar"
              >
                移除
              </el-button>
            </div>
          </div>
        </el-form-item>
        <div class="form-actions">
          <el-button type="primary" @click="saveShopInfo" :loading="loading">
            <i class="el-icon-check"></i> 保存修改
          </el-button>
          <el-button @click="resetForm">
            <i class="el-icon-refresh"></i> 重置修改
          </el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElNotification } from 'element-plus'
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
    
    ElNotification.success({
      title: '保存成功',
      message: '商家信息已更新',
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
const handleAvatarChange = (file) => {
  if (!file) return
  const rawFile = file.raw
  if (rawFile) {
    const reader = new FileReader()
    reader.onload = (e) => {
      shopInfo.value.avatar = e.target.result
      
      // 将头像保存到本地存储
      const userId = localStorage.getItem('userId')
      if (userId) {
        localStorage.setItem(`merchant_avatar_${userId}`, e.target.result)
      }
    }
    reader.readAsDataURL(rawFile)
  }
}

// 移除头像
const removeAvatar = () => {
  shopInfo.value.avatar = ''
  
  // 从本地存储移除头像
  const userId = localStorage.getItem('userId')
  if (userId) {
    localStorage.removeItem(`merchant_avatar_${userId}`)
  }
}

// 组件挂载时获取商家信息
onMounted(() => {
  fetchShopInfo()
})
</script>

<style scoped>
.info-section h2 {
  margin-top: 0;
  margin-bottom: 25px;
  color: #303133;
  font-size: 22px;
  font-weight: 600;
  display: flex;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.info-section h2 i {
  margin-right: 12px;
  font-size: 24px;
  color: #409eff;
}

.info-card {
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  border: none;
}

.avatar-upload {
  display: flex;
  align-items: center;
  gap: 25px;
}

.avatar-preview {
  border: 1px dashed #dcdfe6;
  border-radius: 8px;
  background-color: #f8f9fa;
}

.avatar-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.info-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

.form-actions .el-button {
  padding: 12px 30px;
  font-size: 16px;
}
</style>