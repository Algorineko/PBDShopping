<template>
  <div class="user-profile">
    <h2>我的信息</h2>
    
    <div class="info-card">
      <div class="info-item">
        <label>用户名：</label>
        <span class="info-content">{{ userData.username || '未设置' }}</span>
      </div>
      <div class="info-item">
        <label>收货地址：</label>
        <span class="info-content">{{ userData.address || '暂无收货地址' }}</span>
      </div>
      <div class="info-item">
        <label>手机号码：</label>
        <span class="info-content">{{ userData.contact || '暂无手机号码' }}</span>
      </div>
      <div class="info-item">
        <label>用户头像：</label>
        <div class="avatar-preview">
          <el-avatar 
            :size="80" 
            :src="userData.avatar || defaultAvatar" 
          />
          <div class="avatar-tip">头像仅本地保存</div>
        </div>
      </div>
      <el-button 
        type="primary" 
        @click="handleEditClick"
        class="edit-button"
      >
        编辑信息
      </el-button>
    </div>

    <el-dialog
      v-model="showDialog"
      title="修改个人信息"
      width="500px"
      @open="handleDialogOpen"
    >
      <el-form 
        ref="formRef" 
        :model="editForm" 
        :rules="formRules"
        label-width="100px"
      >
        <!-- 用户名展示（不可编辑） -->
        <div class="username-display">
          <label>用户名：</label>
          <span class="info-content">{{ userData.username || '未设置' }}</span>
          <div class="info-tip">用户名不可修改</div>
        </div>
        
        <el-form-item label="旧密码">
          <el-input
            v-model="editForm.oldPassword"
            type="password"
            show-password
            placeholder="请输入当前密码"
          />
        </el-form-item>
        
        <el-form-item label="新密码">
          <el-input
            v-model="editForm.password"
            type="password"
            show-password
            placeholder="请输入新密码"
          />
          <div class="info-tip">留空则不修改密码</div>
        </el-form-item>

        <el-form-item label="收货地址" prop="address">
          <el-input
            v-model="editForm.address"
            type="textarea"
            :rows="3"
            placeholder="请输入详细收货地址"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="手机号码" prop="contact">
          <el-input
            v-model="editForm.contact"
            placeholder="请输入手机号码"
          />
        </el-form-item>
        
        <el-form-item label="用户头像">
          <div class="avatar-upload">
            <el-avatar 
              :size="120" 
              :src="editForm.avatar || defaultAvatar" 
              class="avatar-preview"
            />
            <div class="avatar-actions">
              <el-upload
                action="#"
                :show-file-list="false"
                :auto-upload="false"
                :on-change="handleAvatarChange"
                accept="image/*"
              >
                <el-button type="primary" icon="el-icon-upload" size="small">上传头像</el-button>
              </el-upload>
              <el-button 
                v-if="editForm.avatar" 
                type="danger" 
                icon="el-icon-delete" 
                size="small"
                @click="editForm.avatar = ''"
              >
                移除
              </el-button>
            </div>
          </div>
          <div class="info-tip">头像仅本地保存，更换设备将丢失</div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showDialog = false">取消</el-button>
          <el-button 
            type="primary"
            @click="submitForm"
            :loading="loading"
          >
            确认修改
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import axios from 'axios'

// 使用可靠的在线默认头像URL
const DEFAULT_AVATAR_URL = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

export default {
  data() {
    // 手机号码验证规则
    const validatePhone = (rule, value, callback) => {
      if (value && !/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error('请输入正确的手机号码'))
      } else {
        callback()
      }
    }
    
    return {
      // 使用可靠的在线默认头像URL
      defaultAvatar: DEFAULT_AVATAR_URL,
      userData: {
        username: '',
        address: '',
        contact: '',
        avatar: ''
      },
      showDialog: false,
      loading: false,
      editForm: {
        oldPassword: '',
        password: '',
        address: '',
        contact: '',
        avatar: ''
      },
      formRules: {
        address: [
          { required: true, message: '收货地址不能为空', trigger: 'blur' },
          { max: 100, message: '最多可输入100个字符', trigger: 'blur' }
        ],
        contact: [
          { validator: validatePhone, trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.fetchUserProfile()
  },
  methods: {
    // 检查Token有效性
    checkTokenValidity() {
      const token = localStorage.getItem('token')
      if (!token) {
        ElMessage.error('用户未登录，请重新登录')
        this.redirectToLogin()
        return false
      }
      return true
    },

    // 重定向到登录页
    redirectToLogin() {
      localStorage.removeItem('token')
      setTimeout(() => {
        window.location.href = '/login'
      }, 1500)
    },

    // 获取用户信息 - 根据响应数据结构调整
    async fetchUserProfile() {
      if (!this.checkTokenValidity()) return
      
      try {
        const token = localStorage.getItem('token')
        const response = await axios.get('http://algorineko.top:8080/api/customer/profile', {
          headers: {
            'Authorization': `Bearer ${token}`
          },
          timeout: 10000
        })
        
        console.log('用户信息响应数据:', response.data)
        
        // 根据响应数据结构调整字段映射
        const data = response.data || {}
        
        this.userData = {
          username: data.customerName || '', // 使用customerName字段
          address: data.address || '',
          contact: data.phoneNumber || '',   // 使用phoneNumber字段
          // 头像从本地存储获取
          avatar: this.getAvatarFromStorage()
        }
        
        // 保存用户名到本地存储
        localStorage.setItem('userName', this.userData.username)
        
        // 保存用户ID
        if (data.customerId) {
          localStorage.setItem('userId', data.customerId.toString())
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        
        // 更详细的错误信息
        let errorMsg = '获取用户信息失败'
        if (error.response) {
          if (error.response.status === 401) {
            errorMsg = '身份验证失败，请重新登录'
            this.redirectToLogin()
          } else if (error.response.data?.message) {
            errorMsg = error.response.data.message
          }
        }
        
        ElMessage.error(errorMsg)
      }
    },

    // 从本地存储获取头像
    getAvatarFromStorage() {
      const profile = JSON.parse(localStorage.getItem('userProfile') || '{}')
      return profile.avatar || ''
    },

    handleEditClick() {
      this.showDialog = true
      // 初始化表单数据
      this.editForm = {
        address: this.userData.address,
        contact: this.userData.contact,
        avatar: this.userData.avatar,
        oldPassword: '',
        password: ''
      }
    },

    handleDialogOpen() {
      this.$nextTick(() => {
        if (this.$refs.formRef) {
          this.$refs.formRef.clearValidate()
        }
      })
    },
    
    // 处理头像上传
    handleAvatarChange(file) {
      if (!file) return
      
      // 检查文件类型和大小
      const validTypes = ['image/jpeg', 'image/png', 'image/gif']
      const isLt2M = file.size / 1024 / 1024 < 2
      
      if (!validTypes.includes(file.raw.type)) {
        ElMessage.error('只支持 JPG/PNG/GIF 格式!')
        return
      }
      
      if (!isLt2M) {
        ElMessage.error('头像大小不能超过 2MB!')
        return
      }
      
      const reader = new FileReader()
      reader.onload = (e) => {
        this.editForm.avatar = e.target.result
      }
      reader.readAsDataURL(file.raw)
    },

    // 提交表单
    async submitForm() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) {
          ElMessage.warning('请正确填写表单')
          return
        }
        
        try {
          this.loading = true
          const token = localStorage.getItem('token')
          if (!token) {
            ElMessage.error('用户未登录')
            return
          }
          
          // 1. 更新基本信息 - 使用后端需要的字段名
          await axios.put(
            'http://algorineko.top:8080/api/customer/putProfile',
            {
              address: this.editForm.address,
              phoneNumber: this.editForm.contact  // 使用phoneNumber字段
            },
            {
              headers: {
                'Authorization': `Bearer ${token}`
              },
              timeout: 10000
            }
          )
          
          // 2. 如果有新密码则更新密码
          if (this.editForm.password) {
            if (!this.editForm.oldPassword) {
              ElMessage.warning('修改密码需要提供旧密码')
              this.loading = false
              return
            }
            
            await axios.put(
              'http://algorineko.top:8080/api/customer/change-password',
              {
                oldPassword: this.editForm.oldPassword,
                newPassword: this.editForm.password
              },
              {
                headers: {
                  'Authorization': `Bearer ${token}`
                },
                timeout: 10000
              }
            )
          }
          
          // 3. 更新用户数据（包括头像）
          this.userData = {
            ...this.userData,
            address: this.editForm.address,
            contact: this.editForm.contact,
            avatar: this.editForm.avatar
          }
          
          // 4. 更新本地存储（包括头像）
          localStorage.setItem('userProfile', JSON.stringify({
            address: this.editForm.address,
            contact: this.editForm.contact,
            avatar: this.editForm.avatar
          }))
          
          ElMessage.success('修改成功')
          this.showDialog = false
        } catch (error) {
          console.error('保存失败:', error)
          
          let errorMessage = '保存失败，请重试'
          if (error.response) {
            if (error.response.status === 401) {
              errorMessage = '身份验证失败，请重新登录'
              this.redirectToLogin()
            } else if (error.response.status === 400) {
              errorMessage = error.response.data.message || '请求参数错误'
            } else if (error.response.status === 403) {
              errorMessage = '旧密码不正确'
            } else if (error.response.data && error.response.data.message) {
              errorMessage = error.response.data.message
            }
          }
          
          ElMessage.error(errorMessage)
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.user-profile {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.info-card {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.username-display {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.username-display label {
  font-weight: bold;
  color: #606266;
  margin-bottom: 5px;
}

.info-item label {
  font-weight: 500;
  color: #606266;
  width: 100px;
  flex-shrink: 0;
}

.info-content {
  flex: 1;
  word-break: break-word;
  color: #303133;
}

.avatar-preview {
  margin-top: 10px;
  position: relative;
}

.avatar-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.edit-button {
  margin-top: 20px;
  width: 200px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.avatar-upload {
  display: flex;
  align-items: center;
  gap: 20px;
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

@media (max-width: 768px) {
  .info-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .edit-button {
    width: 100%;
  }
  
  .avatar-upload {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>