import axios from 'axios'

const apiClient = axios.create({
  baseURL: 'http://algorineko.top:8080/api',
  headers: {
    Authorization: `Bearer ${localStorage.getItem('token')}`
  }
})

export default {
  // 获取商家商品列表
  getMerchantProducts(merchantId) {
    return apiClient.get(`/merchant/product/list?merchantId=${merchantId}`)
  },
  
  // 添加商品
  addProduct(product) {
    return apiClient.post('/merchant/product/add', product)
  },
  
  // 更新商品
  updateProduct(product) {
    return apiClient.put('/merchant/product/update', product)
  },
  
  // 删除商品
  deleteProduct(productId) {
    return apiClient.delete(`/merchant/product/delete?productId=${productId}`)
  },
  
  // 获取所有商品（买家使用）
  getAllProducts() {
    return apiClient.get('/products/all')
  }
}