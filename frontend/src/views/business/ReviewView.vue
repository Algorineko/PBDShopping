<template>
  <div class="review-container">
    <h2>评价管理</h2>
    
    <el-table :data="reviews" style="width: 100%">
      <el-table-column prop="productName" label="商品名称" />
      <el-table-column label="评分" width="120">
        <template #default="{ row }">
          <el-rate 
            v-model="row.rating" 
            disabled 
            show-score 
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']" 
          />
        </template>
      </el-table-column>
      <el-table-column prop="content" label="评价内容" />
      <el-table-column prop="createTime" label="评价时间" width="180" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button 
            v-if="!row.replied"
            type="primary" 
            size="small"
            @click="showReplyDialog(row)"
          >
            回复
          </el-button>
          <span v-else style="color:#67C23A">已回复</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 回复对话框 -->
    <el-dialog v-model="replyDialogVisible" title="回复评价">
      <el-input
        v-model="replyContent"
        type="textarea"
        :rows="4"
        placeholder="请输入回复内容"
      />
      <template #footer>
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReply">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const reviews = ref([
  {
    id: 1,
    productName: '商品A',
    rating: 4.5,
    content: '商品质量不错，物流很快',
    createTime: '2023-10-01 10:00:00',
    replied: false
  },
  {
    id: 2,
    productName: '商品B',
    rating: 5,
    content: '非常满意的购物体验',
    createTime: '2023-10-01 11:00:00',
    replied: true
  }
])

const replyDialogVisible = ref(false)
const replyContent = ref('')
let currentReview = null

const showReplyDialog = (review) => {
  currentReview = review
  replyDialogVisible.value = true
}

const submitReply = () => {
  if (currentReview) {
    currentReview.replied = true
    console.log('回复内容:', replyContent.value)
    replyDialogVisible.value = false
    replyContent.value = ''
  }
}
</script>

<style scoped>
.review-container {
  padding: 20px;
}

.el-rate {
  display: flex;
  align-items: center;
}
</style>