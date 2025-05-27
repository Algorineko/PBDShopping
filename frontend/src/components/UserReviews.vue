<template>
  <div class="user-reviews">
    <h2>评价管理</h2>
    
    <el-table :data="reviews" border style="width: 100%">
      <el-table-column prop="product" label="商品名称" width="200">
        <template #default="{ row }">
          <router-link :to="`/buyer/product/${row.productId}`">
            {{ row.productName }}
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="评分" width="120">
        <template #default="scope">
          <el-rate 
            v-model="scope.row.rating"
            disabled
            show-score
            text-color="#ff9900"
          />
        </template>
      </el-table-column>
      <el-table-column prop="content" label="评价内容" />
      <el-table-column prop="date" label="评价时间" width="180" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="editReview(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="deleteReview(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty 
      v-if="reviews.length === 0" 
      description="暂无评价记录"
      class="empty-tip"
    />

    <!-- 编辑对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑评价">
      <el-form :model="editingReview">
        <el-form-item label="评分">
          <el-rate v-model="editingReview.rating" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input
            v-model="editingReview.content"
            type="textarea"
            :rows="4"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const reviews = ref([])
const editDialogVisible = ref(false)
const editingReview = ref({})

const loadReviews = () => {
  const savedReviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
  reviews.value = savedReviews
}

const editReview = (review) => {
  editingReview.value = { ...review }
  editDialogVisible.value = true
}

const deleteReview = (review) => {
  const newReviews = JSON.parse(localStorage.getItem('productReviews') || [])
    .filter(r => !(r.orderId === review.orderId && r.productId === review.productId))
  localStorage.setItem('productReviews', JSON.stringify(newReviews))
  loadReviews()
  ElMessage.success('删除成功')
}

const saveEdit = () => {
  const newReviews = JSON.parse(localStorage.getItem('productReviews') || [])
  const index = newReviews.findIndex(r => 
    r.orderId === editingReview.value.orderId && 
    r.productId === editingReview.value.productId
  )
  
  if (index > -1) {
    newReviews[index] = {
      ...editingReview.value,
      date: new Date().toLocaleString()
    }
    localStorage.setItem('productReviews', JSON.stringify(newReviews))
    ElMessage.success('修改成功')
    editDialogVisible.value = false
    loadReviews()
  }
}

onMounted(loadReviews)
</script>

<style scoped>
.user-reviews {
  padding: 20px;
  background: #fff;
}

.empty-tip {
  margin-top: 50px;
}
</style>