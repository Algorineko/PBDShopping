<template>
  <div class="user-profile">
    <h2>我的信息</h2>
    
    <div class="info-card">
      <div class="info-item">
        <label>用户ID：</label>
        <span class="info-content">{{ userData.id || '未设置' }}</span>
      </div>
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
            :src="userData.avatar || '/default-avatar.jpg'" 
          />
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
        <el-form-item label="用户ID">
          <el-input v-model="editForm.id" disabled />
        </el-form-item>
        
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="editForm.username"
            placeholder="请输入2-20位用户名"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="登录密码">
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
              :src="editForm.avatar || '/default-avatar.jpg'" 
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
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showDialog = false">取消</el-button>
          <el-button 
            type="primary"
            @click="submitForm"
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
      userData: {
        id: '',
        username: '',
        address: '',
        contact: '',
        password: '',
        avatar: ''
      },
      showDialog: false,
      editForm: {
        id: '',
        username: '',
        address: '',
        contact: '',
        password: '',
        avatar: ''
      },
      formRules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
          { min: 2, max: 20, message: '长度需在2-20个字符之间', trigger: 'blur' }
        ],
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
    this.loadLocalData()
  },
  methods: {
    loadLocalData() {
      try {
        const userId = localStorage.getItem('userId')
        const userName = localStorage.getItem('userName')
        
        // 设置默认用户ID和名称
        this.userData.id = userId || 'BUYER2023'
        this.userData.username = userName || '买家用户'
        
        // 尝试从本地存储获取完整用户信息
        const savedData = localStorage.getItem('userProfile')
        if (savedData) {
          const profileData = JSON.parse(savedData)
          this.userData = {
            ...this.userData,
            address: profileData.address || '',
            contact: profileData.contact || '',
            avatar: profileData.avatar || ''
          }
        }
      } catch (error) {
        ElMessage.error('本地数据加载失败')
        console.error('数据解析错误:', error)
      }
    },

    handleEditClick() {
      this.showDialog = true
      this.editForm = { ...this.userData, password: '' }
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
      
      const rawFile = file.raw
      if (rawFile) {
        const reader = new FileReader()
        reader.onload = (e) => {
          this.editForm.avatar = e.target.result
        }
        reader.readAsDataURL(rawFile)
      }
    },

    submitForm() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          // 更新用户信息
          this.userData = {
            ...this.userData,
            username: this.editForm.username,
            address: this.editForm.address,
            contact: this.editForm.contact,
            avatar: this.editForm.avatar
          }
          
          // 如果输入了新密码，则更新密码
          if (this.editForm.password) {
            this.userData.password = this.editForm.password
          }
          
          // 保存到本地存储
          localStorage.setItem('userProfile', JSON.stringify({
            address: this.editForm.address,
            contact: this.editForm.contact,
            avatar: this.editForm.avatar
          }))
          
          // 更新本地存储中的用户名
          localStorage.setItem('userName', this.editForm.username)
          
          ElMessage.success('修改成功')
          this.showDialog = false
        } else {
          ElMessage.warning('请正确填写表单')
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