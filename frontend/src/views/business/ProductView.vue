<template>
  <div class="product-container">
    <div class="header">
      <h2>商品管理</h2>
      <el-button type="primary">新增商品</el-button>
    </div>
    
    <el-table :data="products">
      <el-table-column prop="name" label="商品名称" />
      <el-table-column prop="price" label="价格" width="120" />
      <el-table-column prop="stock" label="库存" width="120" />
      <el-table-column label="操作" width="180">
        <template #default>
          <el-button size="small">编辑</el-button>
          <el-button size="small" type="danger">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref } from 'vue'  // 必须导入ref
const products = ref([
  { id: 1, name: '商品1', price: 99, stock: 100 },
  { id: 2, name: '商品2', price: 199, stock: 50 }
])
</script>