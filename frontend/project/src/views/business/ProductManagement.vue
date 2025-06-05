<template>
  <div class="product-section">
    <div class="page-header">
      <div class="brand-section">
        <div class="brand-icon">
          🛒
        </div>
        <div class="brand-info">
          <h1>拼宝东商城</h1>
          <p class="page-subtitle">商品管理系统</p>
        </div>
      </div>
      <div class="user-info">
        <div class="user-avatar">👤</div>
        <span class="username">{{ merchantName }}</span>
      </div>
    </div>

    <div class="section-card">
      <div class="section-header">
        <div class="header-title">
          <h2>📦 商品管理</h2>
          <div class="summary-info">
            <span class="stat-item">
              📋 共 {{ products.length }} 个商品
            </span>
            <span class="stat-item">
              🏷️ {{ categoryOptions.length }} 个分类
            </span>
          </div>
        </div>
        <div class="actions-bar">
          <div class="search-container">
            <input 
              v-model="productFilter.keyword" 
              placeholder="输入商品名称搜索" 
              class="search-input"
              @keyup.enter="doSearch"
            />
            <span class="search-icon">🔍</span>
          </div>
          
          <button 
            class="action-btn primary"
            @click="showAddDialog"
          >
            ➕ 新增商品
          </button>
          <button 
            class="action-btn success"
            @click="loadProducts"
          >
            🔄 刷新数据
          </button>
        </div>
      </div>
      
      <div class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th width="130">商品图片</th>
              <th width="200">商品名称</th>
              <th width="140">分类</th>
              <th width="140">价格</th>
              <th width="180">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(product, index) in filteredProducts" :key="index">
              <td>
                <div class="image-container">
                  <img 
                    v-if="product.images && product.images.length > 0"
                    :src="product.images[0]" 
                    class="product-image"
                  />
                  <div v-else class="no-image">🖼️</div>
                </div>
              </td>
              <td>
                <div class="product-name">
                  <strong>{{ product.productName }}</strong>
                  <div class="product-id">ID: {{ product.productId }}</div>
                </div>
              </td>
              <td>
                <div class="category-badge">
                  {{ getCategoryName(product.categoryId) }}
                </div>
              </td>
              <td align="right">
                <div class="product-price">
                  <span class="price-tag">¥</span>
                  <span class="price-value">{{ (product?.price ?? 0).toFixed(2) }}</span>
                </div>
              </td>
              <td>
                <div v-if="product" class="action-buttons">
                  <button 
                    class="edit-btn"
                    @click="editProduct(product)"
                  >
                    编辑
                  </button>
                  <button 
                    class="delete-btn"
                    @click="deleteProduct(product.productId)"
                  >
                    删除
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- 商品卡片视图（在响应式布局时显示） -->
        <div class="product-grid">
          <div v-for="(product, index) in filteredProducts" :key="index" class="product-card">
            <div class="card-header">
              <div class="product-image-container">
                <img 
                  v-if="product.images && product.images.length > 0"
                  :src="product.images[0]" 
                  class="card-image"
                />
                <div v-else class="no-image">🖼️</div>
              </div>
              <div class="product-meta">
                <h3>{{ product.productName }}</h3>
                <div class="product-id">ID: {{ product.productId }}</div>
                <div class="category-badge">{{ getCategoryName(product.categoryId) }}</div>
              </div>
            </div>
            <div class="card-body">
              <div class="product-price">
                <span class="price-tag">¥</span>
                <span class="price-value">{{ (product?.price ?? 0).toFixed(2) }}</span>
              </div>
            </div>
            <div class="card-footer">
              <button 
                class="view-btn"
                @click="viewDetails(product)"
              >
                查看
              </button>
              <button 
                class="edit-btn"
                @click="editProduct(product)"
              >
                编辑
              </button>
              <button 
                class="delete-btn"
                @click="deleteProduct(product.productId)"
              >
                删除
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 添加/编辑商品对话框 -->
      <div v-if="productDialogVisible" class="dialog-overlay">
        <div class="product-dialog">
          <div class="dialog-header">
            <h2>{{ dialogTitle }}</h2>
            <button class="close-btn" @click="productDialogVisible = false">✖</button>
          </div>
          <div class="dialog-body">
            <div class="form-group">
              <label>📛 商品名称 <span class="required">*</span></label>
              <input 
                v-model="currentProduct.productName" 
                placeholder="请输入商品名称"
              />
            </div>
            
            <div class="form-group">
              <label>🏷️ 商品分类 <span class="required">*</span></label>
              <select 
                v-model="currentProduct.categoryId" 
                class="category-selector"
              >
                <option value="" disabled selected>请选择分类</option>
                <option v-for="category in categoryOptions" :key="category.id" :value="category.id">
                  {{ category.name }} (ID: {{ category.id }})
                </option>
              </select>
            </div>
            
            <div class="form-group">
              <label>💰 销售价格 <span class="required">*</span></label>
              <div class="price-input-container">
                <span class="price-prefix">¥</span>
                <input 
                  type="number" 
                  v-model="currentProduct.price" 
                  min="0" 
                  step="0.01"
                  class="price-input"
                />
              </div>
            </div>
            
            <div class="form-group">
              <label>📝 商品描述</label>
              <textarea 
                v-model="currentProduct.description" 
                rows="4"
                placeholder="请输入商品描述..."
                class="description-input"
              ></textarea>
            </div>
            
            <div class="form-group">
              <label>🖼️ 商品图片</label>
              <div class="image-upload-container">
                <div class="upload-title">商品主图展示（最多5张）</div>
                <div class="image-preview-list">
                  <div v-for="(image, index) in currentProduct.images" :key="index" class="image-preview-item">
                    <img :src="image" class="preview-image" />
                    <div class="image-actions">
                      <button 
                        class="remove-btn"
                        @click="removeImage(index)"
                        :disabled="isEditing"
                      >
                        删除
                      </button>
                    </div>
                  </div>
                  <div 
                    v-if="currentProduct.images.length < 5"
                    class="image-upload-card"
                    @click="triggerFileInput"
                  >
                    <div class="upload-placeholder">
                      <span>➕</span>
                      <div class="upload-text">添加图片</div>
                    </div>
                  </div>
                  <input 
                    type="file" 
                    ref="fileInput" 
                    style="display: none" 
                    @change="handleImageChange"
                    multiple
                    :disabled="isEditing"
                  />
                </div>
                <div class="upload-tips">
                  <div class="upload-tip">建议尺寸：800×800像素</div>
                  <div class="upload-tip">支持格式：JPG、PNG，单张不超过2MB</div>
                </div>
                <div v-if="isEditing" class="info-tip">编辑模式下不可修改图片</div>
              </div>
            </div>
          </div>
          <div class="dialog-footer">
            <button class="cancel-btn" @click="productDialogVisible = false">取消</button>
            <button 
              class="save-btn"
              @click="saveProduct"
            >
              保存商品
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const productFilter = ref({ keyword: '' })
const products = ref([])
const productDialogVisible = ref(false)
const currentProduct = ref({
  productName: '',
  categoryId: '',
  price: 0,
  description: '',
  images: []
})
const isEditing = ref(false)
const merchantId = ref(Number(localStorage.getItem('userId')))
const merchantName = ref(localStorage.getItem('username') || '商家')
const fileInput = ref(null)

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

const dialogTitle = computed(() => isEditing.value ? '编辑商品' : '添加新商品')

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
    alert('无效的商品数据')
    return
  }
  currentProduct.value = { 
    ...product,
    productId: product.productId
  }
  isEditing.value = true
  productDialogVisible.value = true
}

const viewDetails = (product) => {
  alert(`查看商品 ${product.productName} 的详细信息`);
}

const triggerFileInput = () => {
  if (!isEditing.value) {
    fileInput.value.click()
  }
}

const handleImageChange = (event) => {
  const files = event.target.files;
  if (!files || files.length === 0) return

  for (let i = 0; i < files.length; i++) {
    const file = files[i];
    // 检查文件大小，限制为2MB
    const maxSize = 2 * 1024 * 1024 // 2MB
    if (file.size > maxSize) {
      alert('图片大小不能超过2MB')
      continue
    }
    // 检查图片数量，最多5张
    if (currentProduct.value.images.length >= 5) {
      alert('最多只能上传5张图片')
      break
    }
    
    const reader = new FileReader()
    reader.onload = (e) => {
      currentProduct.value.images.push(e.target.result)
    }
    reader.readAsDataURL(file)
  }
  
  // 重置文件输入，允许再次选择相同的文件
  event.target.value = null;
}

const removeImage = (index) => {
  currentProduct.value.images.splice(index, 1)
}

const saveProduct = async () => {
  if (!currentProduct.value.productName?.trim()) {
    alert('商品名称不能为空')
    return
  }
  if (!currentProduct.value.categoryId) {
    alert('请选择商品分类')
    return
  }
  if (currentProduct.value.price < 0) {
    alert('价格不能为负数')
    return
  }

  try {
    const loadingMessage = document.createElement('div');
    loadingMessage.textContent = '保存中...';
    loadingMessage.style.position = 'fixed';
    loadingMessage.style.top = '0';
    loadingMessage.style.left = '0';
    loadingMessage.style.width = '100%';
    loadingMessage.style.padding = '10px';
    loadingMessage.style.background = 'rgba(0, 0, 0, 0.7)';
    loadingMessage.style.color = 'white';
    loadingMessage.style.textAlign = 'center';
    loadingMessage.style.zIndex = '1000';
    document.body.appendChild(loadingMessage);
    
    if (isEditing.value) {
      // 编辑商品逻辑保持不变
      const response = await axios.put('http://algorineko.top:8080/api/merchant/product/update', {
        productId: currentProduct.value.productId,
        merchantId: merchantId.value,
        productName: currentProduct.value.productName,
        description: currentProduct.value.description || '',
        price: currentProduct.value.price,
        categoryId: currentProduct.value.categoryId
      })
      
      if (response.data?.message?.includes('成功')) {
        alert('商品更新成功');
        productDialogVisible.value = false;
        await loadProducts();
      } else {
        throw new Error(response.data?.message || '商品更新失败');
      }
    } else {
      // 新增商品 - 使用FormData上传
      const formData = new FormData();
      formData.append('productName', currentProduct.value.productName);
      formData.append('price', currentProduct.value.price);
      formData.append('categoryId', currentProduct.value.categoryId);
      formData.append('merchantId', merchantId.value);
      formData.append('description', currentProduct.value.description || '');
      
      // 添加图片文件
      currentProduct.value.images.forEach((base64Image, index) => {
        const blob = dataURLtoBlob(base64Image);
        formData.append('images', blob, `image_${index}.jpg`);
      });
      
      const response = await axios.post('http://algorineko.top:8080/api/merchant/product/add', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      
      if (response.data?.message?.includes('成功')) {
        alert('商品添加成功');
        productDialogVisible.value = false;
        await loadProducts();
      } else {
        throw new Error(response.data?.message || '商品添加失败');
      }
    }
  } catch (error) {
    const errorMsg = error.response?.data?.message || error.message || '操作失败'
    alert(`操作失败: ${errorMsg}`)
  } finally {
    const loadingElements = document.querySelectorAll('div');
    for (const el of loadingElements) {
      if (el.textContent === '保存中...' && el.style.position === 'fixed') {
        document.body.removeChild(el);
        break;
      }
    }
  }
}

// 将base64转换为Blob对象
function dataURLtoBlob(dataurl) {
  const arr = dataurl.split(',');
  const mime = arr[0].match(/:(.*?);/)[1];
  const bstr = atob(arr[1]);
  let n = bstr.length;
  const u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  return new Blob([u8arr], { type: mime });
}

// 删除商品方法
const deleteProduct = async (productId) => {
  if (!productId) {
    alert('无效的商品ID')
    return
  }
  
  if (!confirm('确定要删除这个商品吗？此操作不可恢复。')) {
    return;
  }

  try {
    const loadingMessage = document.createElement('div');
    loadingMessage.textContent = '删除中...';
    loadingMessage.style.position = 'fixed';
    loadingMessage.style.top = '0';
    loadingMessage.style.left = '0';
    loadingMessage.style.width = '100%';
    loadingMessage.style.padding = '10px';
    loadingMessage.style.background = 'rgba(0, 0, 0, 0.7)';
    loadingMessage.style.color = 'white';
    loadingMessage.style.textAlign = 'center';
    loadingMessage.style.zIndex = '1000';
    document.body.appendChild(loadingMessage);
    
    const response = await axios.delete(`http://algorineko.top:8080/api/merchant/product/delete?productId=${productId}`)
    
    if (typeof response.data === 'string' && response.data.includes('成功')) {
      const index = products.value.findIndex(p => p.productId === productId)
      if (index !== -1) {
        products.value.splice(index, 1)
      }
      alert('商品删除成功')
    } else {
      throw new Error(response.data || '删除失败')
    }
  } catch (error) {
    const errorMsg = error.response?.data?.message || error.message || '删除失败'
    alert(`删除失败: ${errorMsg}`)
  } finally {
    const loadingElements = document.querySelectorAll('div');
    for (const el of loadingElements) {
      if (el.textContent === '删除中...' && el.style.position === 'fixed') {
        document.body.removeChild(el);
        break;
      }
    }
  }
}

// 执行搜索
const doSearch = () => {
  console.log(`正在搜索商品: ${productFilter.value.keyword}`);
}

const loadProducts = async () => {
  try {
    const loadingMessage = document.createElement('div');
    loadingMessage.textContent = '加载商品数据...';
    loadingMessage.style.position = 'fixed';
    loadingMessage.style.top = '0';
    loadingMessage.style.left = '0';
    loadingMessage.style.width = '100%';
    loadingMessage.style.padding = '10px';
    loadingMessage.style.background = 'rgba(0, 0, 0, 0.7)';
    loadingMessage.style.color = 'white';
    loadingMessage.style.textAlign = 'center';
    loadingMessage.style.zIndex = '1000';
    document.body.appendChild(loadingMessage);
    
    const response = await axios.get(`http://algorineko.top:8080/api/merchant/product/list?merchantId=${merchantId.value}`)
    console.log('商品数据:', response.data);
    // 处理API响应数据
    if (Array.isArray(response.data)) {
      products.value = response.data.map(item => {
        // 处理图片URL - 确保是完整URL
        const images = (item.images || [])
          .filter(img => img && img.trim() !== '')  // 过滤空字符串
          .map(img => {
            // 如果图片URL是相对路径，则转换为绝对路径
            if (img && !img.startsWith('http') && !img.startsWith('data:')) {
              // 处理以斜杠开头的情况
              if (img.startsWith('/')) {
                return `http://algorineko.top:8081${img}`;
              } else {
                return `http://algorineko.top:8081/${img}`;
              }
            }
            return img;
          });
        
        return {
          productId: item.productId,
          productName: item.productName,
          categoryId: String(item.categoryId || ''),
          price: item.price,
          description: item.description,
          images: images
        }
      });
    } else {
      throw new Error('返回的数据格式不正确')
    }
  } catch (error) {
    const errorMsg = error.response?.data?.message || error.message || '加载商品失败'
    alert(`加载商品失败: ${errorMsg}`)
    // 示例数据使用字符串格式的分类ID
    products.value = [
      {
        productId: 1,
        productName: '优质棉质T恤',
        categoryId: '1010000',
        price: 89.9,
        description: '100%纯棉，舒适透气',
        images: []
      },
      {
        productId: 2,
        productName: '高端智能手机',
        categoryId: '1043000',
        price: 3999,
        description: '最新款旗舰手机',
        images: []
      },
      {
        productId: 3,
        productName: '有机全脂牛奶',
        categoryId: '1005002',
        price: 12.5,
        description: '有机认证，富含营养',
        images: []
      }
    ]
  } finally {
    const loadingElements = document.querySelectorAll('div');
    for (const el of loadingElements) {
      if (el.textContent === '加载商品数据...' && el.style.position === 'fixed') {
        document.body.removeChild(el);
        break;
      }
    }
  }
}

onMounted(() => {
  loadProducts()
})
</script>

<style scoped>
/* =========== 基础布局 =========== */
.product-section {
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background-color: #f5f7fa;
  min-height: 100vh;
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding: 0 15px;
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

.section-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  padding: 30px;
}

.section-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 20px;
}

.header-title {
  flex: 1;
}

.header-title h2 {
  font-size: 20px;
  color: #303133;
  font-weight: 600;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.summary-info {
  display: flex;
  gap: 25px;
  margin-top: 12px;
}

.stat-item {
  font-size: 14px;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 6px;
}

.actions-bar {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.search-container {
  position: relative;
}

.search-input {
  width: 300px;
  padding: 12px 15px 12px 40px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
}

.search-input:focus {
  outline: none;
  border-color: #e53935;
  box-shadow: 0 0 0 2px rgba(229, 57, 53, 0.2);
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 12px;
  font-size: 16px;
  color: #909399;
}

.action-btn {
  border: none;
  border-radius: 8px;
  font-weight: 500;
  padding: 12px 18px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 8px;
}

.primary {
  background: #e53935;
  color: white;
}

.primary:hover {
  background: #c62b28;
}

.success {
  background: #28a745;
  color: white;
}

.success:hover {
  background: #218838;
}

/* =========== 表格样式 =========== */
.table-container {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th {
  background-color: #f9fafc;
  font-weight: 600;
  color: #606266;
  height: 54px;
  text-align: left;
  padding: 15px;
  border-bottom: 1px solid #ebeef5;
}

.data-table td {
  height: 64px;
  padding: 15px;
  transition: all 0.2s;
  border-bottom: 1px solid #ebeef5;
}

.data-table tbody tr:hover td {
  background-color: #f9fbfd !important;
}

.image-container {
  display: flex;
  justify-content: center;
}

.product-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  transition: all 0.3s;
}

.product-image:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.12);
}

.no-image {
  width: 80px;
  height: 80px;
  background: #f5f7fa;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #c0c4cc;
  font-size: 20px;
  border: 1px dashed #dcdfe6;
}

.product-name {
  line-height: 1.5;
}

.product-id {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.category-badge {
  display: inline-block;
  padding: 5px 12px;
  background-color: #ecf5ff;
  color: #409eff;
  border-radius: 50px;
  font-size: 13px;
  border: 1px solid #d9ecff;
}

.product-price {
  display: flex;
  align-items: baseline;
}

.price-tag {
  font-size: 14px;
  color: #e53935;
  font-weight: 500;
  margin-right: 3px;
}

.price-value {
  font-size: 16px;
  font-weight: 600;
  color: #e53935;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.view-btn, .edit-btn, .delete-btn {
  padding: 7px 12px;
  border-radius: 8px;
  transition: all 0.2s;
  border: 1px solid #dcdfe6;
  background: white;
  font-size: 14px;
  cursor: pointer;
}

.view-btn:hover {
  background-color: #ecf5ff;
  color: #409eff;
  border-color: #d9ecff;
}

.edit-btn:hover {
  background-color: #f0f9eb;
  color: #67c23a;
  border-color: #e1f3d8;
}

.delete-btn:hover {
  background-color: #fef0f0;
  color: #f56c6c;
  border-color: #fde2e2;
}

/* =========== 商品卡片视图 =========== */
.product-grid {
  display: none;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-top: 25px;
}

.product-card {
  border: 1px solid #ebeef5;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
  transition: all 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.card-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
}

.product-meta h3 {
  font-size: 16px;
  margin: 0 0 5px 0;
  font-weight: 600;
  color: #303133;
}

.card-body {
  padding: 8px 0;
}

.card-footer {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  padding-top: 15px;
  border-top: 1px solid #f1f3f7;
}

/* =========== 弹窗样式 =========== */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.product-dialog {
  width: 800px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0,0,0,0.2);
  overflow: hidden;
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  background: linear-gradient(to right, #f9fafc, #f0f4f8);
  border-bottom: 1px solid #ebeef5;
}

.dialog-header h2 {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 20px;
  color: #606266;
  cursor: pointer;
  padding: 5px;
}

.close-btn:hover {
  color: #e53935;
}

.dialog-body {
  padding: 20px;
  max-height: 70vh;
  overflow-y: auto;
}

.form-group {
  margin-bottom: 24px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 8px;
}

.required {
  color: #e53935;
  font-size: 16px;
}

input, select, textarea {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
}

input:focus, select:focus, textarea:focus {
  outline: none;
  border-color: #e53935;
  box-shadow: 0 0 0 2px rgba(229, 57, 53, 0.2);
}

.category-selector {
  padding: 12px;
}

.price-input-container {
  display: flex;
  width: 200px;
}

.price-prefix {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  background: #f5f7fa;
  border: 1px solid #dcdfe6;
  border-right: none;
  border-radius: 4px 0 0 4px;
  color: #606266;
  font-weight: 600;
}

.price-input {
  border-left: none;
  border-radius: 0 4px 4px 0;
  padding: 12px;
}

.description-input {
  resize: vertical;
}

.image-upload-container {
  margin-top: 15px;
}

.upload-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
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
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: all 0.3s;
}

.image-preview-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.15);
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 8px;
  background: rgba(0,0,0,0.6);
  display: flex;
  justify-content: center;
}

.remove-btn {
  background: #f56c6c;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 4px 8px;
  font-size: 12px;
  cursor: pointer;
}

.image-upload-card {
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9fafc;
  border: 1px dashed #dcdfe6;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.image-upload-card:hover {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.upload-placeholder {
  text-align: center;
  color: #909399;
}

.upload-placeholder span {
  font-size: 28px;
}

.upload-text {
  font-size: 14px;
  margin-top: 8px;
}

.upload-tips {
  margin-top: 15px;
  font-size: 13px;
  color: #909399;
  line-height: 1.6;
}

.info-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

.dialog-footer {
  padding: 20px 24px;
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  border-top: 1px solid #ebeef5;
}

.cancel-btn, .save-btn {
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.cancel-btn {
  background: #f5f7fa;
  color: #606266;
  border: 1px solid #dcdfe6;
}

.cancel-btn:hover {
  background: #e4e7eb;
}

.save-btn {
  background: linear-gradient(to right, #e53935, #e35d5b);
  color: white;
  border: none;
}

.save-btn:hover {
  background: linear-gradient(to right, #d63031, #e05553);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(229, 57, 53, 0.3);
}

/* =========== 响应式设计 =========== */
@media (max-width: 992px) {
  .actions-bar {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .search-input {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .data-table {
    display: none;
  }
  
  .product-grid {
    display: grid;
  }
  
  .dialog-overlay {
    padding: 20px;
  }
  
  .product-dialog {
    width: 100%;
  }
  
  .price-input-container {
    width: 100%;
  }
}
</style>