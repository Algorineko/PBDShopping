<template>
  <div class="product-section">
    <h2><i class="el-icon-goods"></i> 商品管理</h2>
    <div class="table-header">
      <el-input 
        v-model="productFilter.keyword" 
        placeholder="输入商品名称搜索" 
        style="width: 300px"
        clearable
        prefix-icon="el-icon-search"
      />
      <el-button type="primary" icon="el-icon-plus" @click="showAddDialog">新增商品</el-button>
    </div>
    
    <el-table :data="filteredProducts" border style="width: 100%" class="data-table">
      <el-table-column label="商品图片" width="120">
        <template #default="scope">
          <el-image 
            v-if="scope.row.images && scope.row.images.length > 0"
            :src="scope.row.images[0]" 
            fit="cover" 
            style="width: 80px; height: 80px; border-radius: 4px;"
          >
            <template #error>
              <div class="image-slot">
                <i class="el-icon-picture-outline"></i>
              </div>
            </template>
          </el-image>
          <div v-else class="no-image">无图片</div>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" width="180" />
      <el-table-column prop="id" label="商品ID" width="120" />
      <el-table-column prop="categoryId" label="分类ID" width="120" />
      <el-table-column label="价格" width="120" align="right">
        <template #default="scope">
          ¥{{ (scope?.row?.price ?? 0).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="120" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <div v-if="scope?.row">
            <el-button 
              size="small" 
              icon="el-icon-edit"
              @click="editProduct(scope.row)"
            >编辑</el-button>
            <el-button 
              size="small" 
              type="danger" 
              icon="el-icon-delete"
              @click="deleteProduct(scope.row.id)"
            >删除</el-button>
          </div>
          <span v-else class="error-text">数据异常</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑商品对话框 -->
    <el-dialog v-model="productDialogVisible" :title="dialogTitle" width="800px">
      <el-form :model="currentProduct" label-width="100px">
        <el-form-item label="商品ID">
          <el-input v-model="currentProduct.id" placeholder="请输入商品ID" :disabled="isEditing" />
        </el-form-item>
        <el-form-item label="分类ID">
          <el-input v-model="currentProduct.categoryId" placeholder="请输入分类ID" />
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="currentProduct.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number 
            v-model="currentProduct.price" 
            :min="0" 
            :precision="2" 
            :controls="false"
            style="width: 200px"
          />
          <span class="input-unit">元</span>
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number 
            v-model="currentProduct.stock" 
            :min="0" 
            :controls="false"
            style="width: 200px"
          />
          <span class="input-unit">件</span>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="currentProduct.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="商品图片">
          <div class="image-upload-container">
            <div class="image-preview-list">
              <div v-for="(image, index) in currentProduct.images" :key="index" class="image-preview-item">
                <el-image 
                  :src="image" 
                  fit="cover" 
                  class="preview-image"
                />
                <div class="image-actions">
                  <el-button 
                    type="danger" 
                    icon="el-icon-delete" 
                    size="small"
                    circle
                    @click="removeImage(index)"
                  />
                </div>
              </div>
            </div>
            <el-upload
              action="#"
              :multiple="true"
              :show-file-list="false"
              :auto-upload="false"
              :on-change="handleImageChange"
              class="image-upload-btn"
            >
              <el-button type="primary" icon="el-icon-plus">添加图片</el-button>
              <div class="upload-tip">支持多图上传</div>
            </el-upload>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="productDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveProduct">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const productFilter = ref({ keyword: '' })
const products = ref([])
const productDialogVisible = ref(false)
const currentProduct = ref({
  id: '',
  categoryId: '',
  name: '',
  price: 0,
  stock: 0,
  description: '',
  images: []
})
const isEditing = ref(false)

const filteredProducts = computed(() => {
  return (products.value || [])
    .filter(p => 
      (p?.name?.toLowerCase() || '').includes(
        (productFilter.value.keyword?.toLowerCase() || '')
      )
    )
    .map(p => ({
      ...p,
      price: Number(p.price) || 0,
      stock: Math.max(Number(p.stock) || 0, 0)
    }))
})

const dialogTitle = computed(() => isEditing.value ? '编辑商品' : '新增商品')

const showAddDialog = () => {
  currentProduct.value = {
    id: '',
    categoryId: '',
    name: '',
    price: 0,
    stock: 0,
    description: '',
    images: []
  }
  isEditing.value = false
  productDialogVisible.value = true
}

const editProduct = (product) => {
  if (!product?.id) {
    ElMessage.warning('无效的商品数据')
    return
  }
  currentProduct.value = { ...product }
  isEditing.value = true
  productDialogVisible.value = true
}

const handleImageChange = (file) => {
  if (!file) return
  const rawFile = file.raw
  if (rawFile) {
    const reader = new FileReader()
    reader.onload = (e) => {
      currentProduct.value.images.push(e.target.result)
    }
    reader.readAsDataURL(rawFile)
  }
}

const removeImage = (index) => {
  currentProduct.value.images.splice(index, 1)
}

const saveProduct = () => {
  if (!currentProduct.value.id) {
    ElMessage.error('商品ID不能为空')
    return
  }
  if (!currentProduct.value.name?.trim()) {
    ElMessage.error('商品名称不能为空')
    return
  }
  if (currentProduct.value.price < 0) {
    ElMessage.error('价格不能为负数')
    return
  }

  try {
    if (isEditing.value) {
      const index = products.value.findIndex(p => p?.id === currentProduct.value?.id)
      if (index !== -1) {
        products.value.splice(index, 1, {
          ...currentProduct.value,
          price: Number(currentProduct.value.price) || 0,
          stock: Math.max(Number(currentProduct.value.stock), 0)
        })
      }
    } else {
      if (products.value.some(p => p.id === currentProduct.value.id)) {
        ElMessage.error('商品ID已存在，请使用不同的ID')
        return
      }
      
      products.value.push({
        ...currentProduct.value,
        name: currentProduct.value.name || '新商品',
        price: Number(currentProduct.value.price) || 0,
        stock: Math.max(Number(currentProduct.value.stock), 0)
      })
    }
    
    saveProductToLocalStorage()
    productDialogVisible.value = false
    ElMessage.success('商品保存成功')
  } catch (error) {
    ElMessage.error(`保存失败: ${error.message}`)
  }
}

const deleteProduct = (id) => {
  if (!id) {
    ElMessage.warning('无效的商品ID')
    return
  }
  const index = products.value.findIndex(p => p?.id === id)
  if (index !== -1) {
    products.value.splice(index, 1)
    saveProductToLocalStorage()
    ElMessage.success('商品删除成功')
  }
}

const saveProductToLocalStorage = () => {
  const businessId = localStorage.getItem('userId') || 'default'
  const storageKey = `businessProducts_${businessId}`
  localStorage.setItem(storageKey, JSON.stringify(products.value))
}

const loadProductsFromLocalStorage = () => {
  const businessId = localStorage.getItem('userId') || 'default'
  const storageKey = `businessProducts_${businessId}`
  const savedProducts = localStorage.getItem(storageKey)
  if (savedProducts) {
    products.value = JSON.parse(savedProducts)
  }
}

onMounted(() => {
  loadProductsFromLocalStorage()
})
</script>

<style scoped>
.product-section h2 {
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

.product-section h2 i {
  margin-right: 12px;
  font-size: 24px;
  color: #409eff;
}

.table-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 25px;
}

.data-table {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.error-text {
  color: #f56c6c;
  font-size: 12px;
  padding: 4px 8px;
}

.input-unit {
  margin-left: 10px;
  color: #909399;
}

.image-upload-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.image-preview-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.image-preview-item {
  position: relative;
  width: 120px;
  height: 120px;
  border: 1px dashed #dcdfe6;
  border-radius: 6px;
  overflow: hidden;
  transition: all 0.3s;
}

.image-preview-item:hover {
  border-color: #409eff;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-actions {
  position: absolute;
  top: 5px;
  right: 5px;
}

.image-upload-btn {
  width: 120px;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.no-image {
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
  color: #909399;
  font-size: 12px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 30px;
}
</style>