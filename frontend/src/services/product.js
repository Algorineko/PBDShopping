import axios from 'axios'

const apiClient = axios.create({
  baseURL: 'http://your-api-domain.com/api/products',
  headers: {
    Authorization: `Bearer ${localStorage.getItem('token')}`
  }
})

export default {
  getBusinessProducts() {
    return apiClient.get('/')
  },
  createProduct(product) {
    return apiClient.post('/', product)
  },
  updateProduct(id, product) {
    return apiClient.put(`/${id}`, product)
  },
  deleteProduct(id) {
    return apiClient.delete(`/${id}`)
  },
  getAllProducts() {
    return axios.get('http://your-api-domain.com/api/products/all')
  }
}