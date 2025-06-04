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
      <el-table-column prop="productName" label="商品名称" width="180" />
      <el-table-column prop="productId" label="商品ID" width="120" />
      <!-- 显示分类名称 -->
      <el-table-column label="分类" width="120">
        <template #default="scope">
          {{ getCategoryName(scope.row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column label="价格" width="120" align="right">
        <template #default="scope">
          ¥{{ (scope?.row?.price ?? 0).toFixed(2) }}
        </template>
      </el-table-column>
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
              @click="deleteProduct(scope.row.productId)"
            >删除</el-button>
          </div>
          <span v-else class="error-text">数据异常</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑商品对话框 -->
    <el-dialog v-model="productDialogVisible" :title="dialogTitle" width="800px">
      <el-form :model="currentProduct" label-width="100px">
        <el-form-item label="商品名称" required>
          <el-input v-model="currentProduct.productName" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="分类" required>
          <el-select 
            v-model="currentProduct.categoryId" 
            placeholder="请选择分类"
            style="width: 100%"
          >
            <el-option
              v-for="category in categoryOptions"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" required>
          <el-input-number 
            v-model="currentProduct.price" 
            :min="0" 
            :precision="2" 
            :controls="false"
            style="width: 200px"
          />
          <span class="input-unit">元</span>
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
                    :disabled="isEditing"
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
              :disabled="isEditing"
              class="image-upload-btn"
            >
              <el-button type="primary" icon="el-icon-plus" :disabled="isEditing">添加图片</el-button>
              <div class="upload-tip">支持多图上传（最多5张，单张不超过2MB）</div>
            </el-upload>
          </div>
          <div v-if="isEditing" class="info-tip">编辑模式下不可修改图片</div>
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
import { ElMessage, ElLoading } from 'element-plus'
import axios from 'axios'

const productFilter = ref({ keyword: '' })
const products = ref([])
const productDialogVisible = ref(false)
const currentProduct = ref({
  productName: '',
  categoryId: '',
  price: 0,
  description: '',
  images: [""]
})
const isEditing = ref(false)
const merchantId = ref(Number(localStorage.getItem('userId')))

// 分类选项
const categoryOptions = ref([
  { id: "1005000", name: "居家" },
  { id: "1005002", name: "美食" },
  { id: "1010000", name: "服饰" },
  { id: "1011000", name: "母婴" },
  { id: "1013001", name: "个护" },
  { id: "1019000", name: "严选" },
  { id: "1043000", name: "数码" },
  { id: "109243029", name: "运动" },
  { id: "19999999", name: "杂项" }
])

const filteredProducts = computed(() => {
  return (products.value || [])
    .filter(p => 
      (p?.productName?.toLowerCase() || '').includes(
        (productFilter.value.keyword?.toLowerCase() || '')
      )
    )
    .map(p => ({
      ...p,
      price: Number(p.price) || 0
    }))
})

const dialogTitle = computed(() => isEditing.value ? '编辑商品' : '新增商品')

// 根据分类ID获取分类名称
const getCategoryName = (categoryId) => {
  const category = categoryOptions.value.find(cat => cat.id === categoryId)
  return category ? category.name : categoryId || '未知分类'
}

const showAddDialog = () => {
  currentProduct.value = {
    productName: '',
    categoryId: '',
    price: 0,
    description: '',
    images: []
  }
  isEditing.value = false
  productDialogVisible.value = true
}

const editProduct = (product) => {
  if (!product?.productId) {
    ElMessage.warning('无效的商品数据')
    return
  }
  currentProduct.value = { 
    ...product,
    productId: product.productId
  }
  isEditing.value = true
  productDialogVisible.value = true
}

const handleImageChange = (file) => {
  if (!file) return
  // 检查文件大小，限制为2MB
  const maxSize = 2 * 1024 * 1024 // 2MB
  if (file.size > maxSize) {
    ElMessage.error('图片大小不能超过2MB')
    return
  }
  // 检查图片数量，最多5张
  if (currentProduct.value.images.length >= 5) {
    ElMessage.error('最多只能上传5张图片')
    return
  }
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

const saveProduct = async () => {
  if (!currentProduct.value.productName?.trim()) {
    ElMessage.error('商品名称不能为空')
    return
  }
  if (!currentProduct.value.categoryId) {
    ElMessage.error('请选择商品分类')
    return
  }
  if (currentProduct.value.price < 0) {
    ElMessage.error('价格不能为负数')
    return
  }

  let loadingInstance = null
  try {
    loadingInstance = ElLoading.service({
      lock: true,
      text: '保存中...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    
    if (isEditing.value) {
      // 编辑商品
      const response = await axios.put('http://algorineko.top:8080/api/merchant/product/update', {
        productId: currentProduct.value.productId,
        merchantId: merchantId.value,
        productName: currentProduct.value.productName,
        description: currentProduct.value.description || '',
        price: currentProduct.value.price,
        categoryId: currentProduct.value.categoryId
      })
      
      if (response.data.includes('成功')) {
        // 更新成功后重新加载商品列表
        ElMessage.success('商品更新成功')
        productDialogVisible.value = false
        await loadProducts()
      } else {
        throw new Error(response.data || '商品更新失败')
      }
    } else {
      // 新增商品
      const response = await axios.post('http://algorineko.top:8080/api/merchant/product/add', {
        merchantId: merchantId.value,
        productName: currentProduct.value.productName,
        description: currentProduct.value.description || '',
        price: currentProduct.value.price,
        categoryId: currentProduct.value.categoryId,
        images: currentProduct.value.images.length > 0 
                ? currentProduct.value.images 
                : [""]
      })
      if (response.data.includes('成功')) {
        // 新增成功后重新加载商品列表
        ElMessage.success('商品添加成功')
        productDialogVisible.value = false
        await loadProducts()
      } else {
        throw new Error(response.data || '商品添加失败')
      }
    }
  } catch (error) {
    const errorMsg = error.response?.data?.message || error.message || '操作失败'
    ElMessage.error(`操作失败: ${errorMsg}`)
  } finally {
    if (loadingInstance) {
      loadingInstance.close()
    }
  }
}

// 删除商品方法
const deleteProduct = async (productId) => {
  if (!productId) {
    ElMessage.warning('无效的商品ID')
    return
  }
  
  let loadingInstance = null
  try {
    loadingInstance = ElLoading.service({
      lock: true,
      text: '删除中...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    
    const response = await axios.delete(`http://algorineko.top:8080/api/merchant/product/delete?productId=${productId}`)
    
    if (typeof response.data === 'string' && response.data.includes('成功')) {
      const index = products.value.findIndex(p => p.productId === productId)
      if (index !== -1) {
        products.value.splice(index, 1)
      }
      ElMessage.success('商品删除成功')
    } else {
      throw new Error(response.data || '删除失败')
    }
  } catch (error) {
    const errorMsg = error.response?.data?.message || error.message || '删除失败'
    ElMessage.error(`删除失败: ${errorMsg}`)
  } finally {
    if (loadingInstance) {
      loadingInstance.close()
    }
  }
}

// 加载商品方法
const loadProducts = async () => {
  let loadingInstance = null
  try {
    loadingInstance = ElLoading.service({
      lock: true,
      text: '加载商品数据...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    
    const response = await axios.get(`http://algorineko.top:8080/api/merchant/product/list?merchantId=${merchantId.value}`)
    
    if (Array.isArray(response.data)) {
      products.value = response.data.map(item => ({
        productId: item.productId,
        productName: item.productName,
        // 确保分类ID为字符串格式，以便匹配分类选项
        categoryId: String(item.categoryId || ''),
        price: item.price,
        description: item.description,
        images: item.images || []
      }))
    } else {
      throw new Error('返回的数据格式不正确')
    }
  } catch (error) {
    const errorMsg = error.response?.data?.message || error.message || '加载商品失败'
    ElMessage.error(`加载商品失败: ${errorMsg}`)
    // 示例数据使用字符串格式的分类ID
    products.value = [
      {
        productId: 1,
        productName: '示例商品1',
        categoryId: '1005000',
        price: 19.9,
        description: '这是一个示例商品',
        images: []
      }
    ]
  } finally {
    if (loadingInstance) {
      loadingInstance.close()
    }
  }
}

onMounted(() => {
  loadProducts()
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

.info-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
</style>