<template>
  <div class="info-section">
    <h2><i class="el-icon-user"></i> 商家信息管理</h2>
    <el-card class="info-card">
      <el-form label-width="120px">
        <el-form-item label="商家ID">
          <el-input v-model="shopInfo.id" disabled />
          <div class="info-tip">商家ID不可更改</div>
        </el-form-item>
        <el-form-item label="商家名称">
          <el-input v-model="shopInfo.name" />
        </el-form-item>
        <el-form-item label="登录密码">
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
        <el-form-item label="联系方式">
          <el-input v-model="shopInfo.contact" placeholder="请输入联系电话" />
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
                @click="shopInfo.avatar = ''"
              >
                移除
              </el-button>
            </div>
          </div>
        </el-form-item>
        <div class="form-actions">
          <el-button type="primary" @click="saveShopInfo">
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
import { ref, defineProps, defineEmits, watch } from 'vue'
import { ElMessage, ElNotification } from 'element-plus'

const props = defineProps({
  shopInfo: {
    type: Object,
    required: true
  }
})

const emits = defineEmits(['update-shop-info'])

const shopInfo = ref({ ...props.shopInfo })
const originalShopInfo = ref({ ...props.shopInfo })

watch(() => props.shopInfo, (newVal) => {
  shopInfo.value = { ...newVal }
  originalShopInfo.value = { ...newVal }
}, { deep: true })

const saveShopInfo = () => {
  const businessUsers = JSON.parse(localStorage.getItem('businessUsers') || '[]')
  const userId = localStorage.getItem('userId')
  
  if (!userId) {
    ElMessage.error('用户信息异常，请重新登录')
    return
  }
  
  const index = businessUsers.findIndex(u => u.userId === userId)
  
  if (index !== -1) {
    const updatedUser = {
      ...businessUsers[index],
      userName: shopInfo.value.name,
      address: shopInfo.value.address,
      contact: shopInfo.value.contact,
      avatar: shopInfo.value.avatar
    }
    
    if (shopInfo.value.password) {
      updatedUser.password = shopInfo.value.password
    }
    
    businessUsers[index] = updatedUser
    localStorage.setItem('businessUsers', JSON.stringify(businessUsers))
    localStorage.setItem('userName', shopInfo.value.name)
    
    originalShopInfo.value = { ...shopInfo.value }
    
    emits('update-shop-info', {
      name: shopInfo.value.name,
      address: shopInfo.value.address,
      contact: shopInfo.value.contact,
      avatar: shopInfo.value.avatar
    })
    
    ElNotification.success({
      title: '保存成功',
      message: '商家信息已更新',
      duration: 2000
    })
    
    shopInfo.value.password = ''
  } else {
    ElMessage.error('未找到商家信息，请重新登录')
  }
}

const resetForm = () => {
  shopInfo.value = { ...originalShopInfo.value }
  shopInfo.value.password = ''
  ElMessage.info('已重置修改内容')
}

const handleAvatarChange = (file) => {
  if (!file) return
  const rawFile = file.raw
  if (rawFile) {
    const reader = new FileReader()
    reader.onload = (e) => {
      shopInfo.value.avatar = e.target.result
    }
    reader.readAsDataURL(rawFile)
  }
}
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