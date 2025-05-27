<template>
  <div class="info-container">
    <h2>商家信息</h2>
    <el-card>
      <el-form label-width="100px">
        <el-form-item label="店铺名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="form.contact" />
        </el-form-item>
        <el-form-item label="店铺地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-button type="primary">保存修改</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const form = ref({
  name: '我的店铺',
  contact: '13800138000',
  address: '北京市朝阳区'
})
</script>