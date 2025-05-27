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
        <el-form-item label="用户名：" prop="username">
          <el-input
            v-model="editForm.username"
            placeholder="请输入2-20位用户名"
            clearable
          />
        </el-form-item>

        <el-form-item label="收货地址：" prop="address">
          <el-input
            v-model="editForm.address"
            type="textarea"
            :rows="3"
            placeholder="请输入详细收货地址"
            maxlength="100"
            show-word-limit
          />
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
    return {
      userData: {
        username: '',
        address: ''
      },
      showDialog: false,
      editForm: {
        username: '',
        address: ''
      },
      formRules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
          { min: 2, max: 20, message: '长度需在2-20个字符之间', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '收货地址不能为空', trigger: 'blur' },
          { max: 100, message: '最多可输入100个字符', trigger: 'blur' }
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
        const savedData = localStorage.getItem('userProfile')
        if (savedData) {
          this.userData = JSON.parse(savedData)
        }
      } catch (error) {
        ElMessage.error('本地数据加载失败')
        console.error('数据解析错误:', error)
      }
    },

    handleEditClick() {
      this.showDialog = true
      this.editForm = { ...this.userData }
    },

    handleDialogOpen() {
      this.$nextTick(() => {
        if (this.$refs.formRef) {
          this.$refs.formRef.clearValidate()
        }
      })
    },

    submitForm() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.userData = { ...this.editForm }
          localStorage.setItem('userProfile', JSON.stringify(this.userData))
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
  align-items: baseline;
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

.edit-button {
  margin-top: 20px;
  width: 200px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

@media (max-width: 768px) {
  .info-item {
    flex-direction: column;
    gap: 8px;
  }
  
  .edit-button {
    width: 100%;
  }
}
</style>