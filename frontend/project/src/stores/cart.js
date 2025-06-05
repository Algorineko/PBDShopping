// stores/cart.js
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'

// 解析JWT token的辅助函数
const parseJwt = (token) => {
  try {
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    return JSON.parse(jsonPayload)
  } catch (e) {
    console.error('Token解析失败:', e)
    return null
  }
}

// 格式化图片URL
const formatImageUrl = (url) => {
  if (!url || url.trim() === '') return '';
  
  if (url.startsWith('http') || url.startsWith('data:')) {
    return url;
  }
  
  // 使用固定基础URL
  const baseUrl = 'http://algorineko.top:8081';
  
  if (url.startsWith('/')) {
    return `${baseUrl}${url}`;
  }
  
  return `${baseUrl}/${url}`;
}

export const useCartStore = defineStore('cart', () => {
  const items = ref([])
  const selectedItems = ref(new Set())
  const loading = ref(false)
  const error = ref(null)
  
  // 获取商品详情
  const fetchProductDetail = async (productId) => {
    try {
      const response = await axios.get(
        `http://algorineko.top:8080/api/merchant/product/detail/${productId}`
      )
      return response.data
    } catch (err) {
      console.error(`获取商品详情失败 (ID: ${productId}):`, err)
      return null
    }
  }

  // 添加商品（本地方法）
  const addItem = (item) => {
    const existing = items.value.find(i => i.id === item.id)
    if (existing) {
      existing.quantity += Number(item.quantity) || 1
    } else {
      items.value.push({
        id: item.id,
        name: item.name || '未知商品',
        price: Number(item.price) || 0,
        quantity: Math.max(1, Number(item.quantity) || 1),
        image: formatImageUrl(item.image) || '/placeholder-product.jpg',
        cartItemId: item.cartItemId
      })
    }
  }

  // 新增：API方式添加商品到购物车
  const addItemToCart = async (item) => {
    const token = localStorage.getItem('token')
    if (!token) {
      throw new Error('请先登录后再添加商品到购物车')
    }
    
    // 从token解析用户信息
    const payload = parseJwt(token)
    if (!payload || !payload.customerId) {
      throw new Error('用户信息不完整，请重新登录')
    }
    
    // 构造请求数据
    const requestData = {
      customerId: Number(payload.customerId),
      productId: Number(item.id),
      quantity: Number(item.quantity),
      selectedOptions: ""
    }
    
    try {
      // 调用后端加入购物车API
      const response = await axios.post(
        'http://algorineko.top:8080/api/customer/cart/add', 
        requestData,
        {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }
      )
      
      // 更新本地购物车状态
      addItem({
        id: item.id,
        name: item.name,
        price: item.price,
        quantity: item.quantity,
        image: item.image
      })
      
      return response.data
    } catch (error) {
      let errorMsg = '加入购物车失败'
      
      if (error.response) {
        if (error.response.status === 400) {
          errorMsg = error.response.data?.message || '请求参数错误'
        } else if (error.response.status === 401) {
          errorMsg = '身份验证失败，请重新登录'
        } else if (error.response.status === 500) {
          errorMsg = error.response.data?.message || '服务器内部错误'
        } else {
          errorMsg = error.response.data?.message || '未知错误'
        }
      } else {
        errorMsg = error.message || '网络错误，请检查连接'
      }
      
      throw new Error(errorMsg)
    }
  }
  
  // 从后端获取购物车数据
  const fetchCart = async () => {
    loading.value = true
    error.value = null
    try {
      const token = localStorage.getItem('token')
      if (!token) throw new Error('用户未登录')
    
      const response = await axios.get(
        'http://algorineko.top:8080/api/customer/cart/get',
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      )
      
      // 创建临时数组存储购物车项
      const tempItems = []
      
      // 遍历购物车项并获取商品详情
      for (const cartItem of response.data) {
        const productDetail = await fetchProductDetail(cartItem.productId)
        
        // 处理商品图片
        let formattedImage = '/placeholder-product.jpg';
        if (productDetail && productDetail.images) {
          // 过滤空字符串并处理图片URL
          const images = (productDetail.images || [])
            .filter(img => img && img.trim() !== '')
            .map(img => formatImageUrl(img));
          
          if (images.length > 0) {
            formattedImage = images[0];
          }
        } else if (cartItem.image) {
          formattedImage = formatImageUrl(cartItem.image);
        }
        
        // 合并购物车项和商品详情数据
        tempItems.push({
          id: cartItem.productId,
          cartItemId: cartItem.cartItemId,
          name: productDetail?.productName || cartItem.productName || '未知商品',
          price: Number(productDetail?.price) || Number(cartItem.price) || 0,
          quantity: Math.max(1, Number(cartItem.quantity) || 1),
          image: formattedImage
        })
      }
      
      // 更新购物车项
      items.value = tempItems
    } catch (err) {
      error.value = err.response?.data?.message || err.message
      console.error('获取购物车失败:', err)
      // 错误时保持现有数据
      if (!items.value.length) {
        items.value = []
      }
    } finally {
      loading.value = false
    }
  }
  
  // 更新购物车项数量
  const updateQuantity = async (cartItemId, quantity) => {
    loading.value = true
    try {
      const token = localStorage.getItem('token')
      if (!token) throw new Error('用户未登录')
      
      // 从token解析用户信息
      const payload = parseJwt(token)
      if (!payload || !payload.customerId) {
        throw new Error('用户信息不完整，请重新登录')
      }
      
      // 查找商品信息
      const item = items.value.find(i => i.cartItemId === cartItemId)
      if (!item) {
        throw new Error('购物车项不存在')
      }
      
      // 构建符合后端要求的请求体
      const requestData = {
        cartItemId: Number(cartItemId),
        customerId: Number(payload.customerId),
        productId: Number(item.id),
        quantity: Number(quantity),
        selectedOptions: ""
      }
      
      // 发送PUT请求
      await axios.put(
        `http://algorineko.top:8080/api/customer/cart/${cartItemId}`,
        requestData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      )
      
      // 更新本地状态
      if (item) {
        item.quantity = quantity
      }
    } catch (err) {
      error.value = err.response?.data?.message || err.message
      console.error('更新购物车项失败:', err)
      // 重新获取最新数据
      await fetchCart()
    } finally {
      loading.value = false
    }
  }
  
  // 移除商品 - 修正URL格式
  const removeItem = async (cartItemId) => {
    loading.value = true
    try {
      const token = localStorage.getItem('token')
      if (!token) throw new Error('用户未登录')
      
      // 使用正确的URL格式
      await axios.delete(
        `http://algorineko.top:8080/api/customer/cart/${cartItemId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      )
      
      // 更新本地状态
      items.value = items.value.filter(i => i.cartItemId !== cartItemId)
      selectedItems.value.delete(cartItemId)
    } catch (err) {
      error.value = err.response?.data?.message || err.message
      console.error('删除购物车项失败:', err)
    } finally {
      loading.value = false
    }
  }
  
  // 批量移除商品 - 修正URL格式
  const removeItems = async (cartItemIds) => {
    loading.value = true
    try {
      const token = localStorage.getItem('token')
      if (!token) throw new Error('用户未登录')
      
      // 并行删除所有选中项
      await Promise.all(
        cartItemIds.map(id => 
          axios.delete(
            `http://algorineko.top:8080/api/customer/cart/${id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            }
          )
        )
      )
      
      // 更新本地状态
      items.value = items.value.filter(i => !cartItemIds.includes(i.cartItemId))
      cartItemIds.forEach(id => selectedItems.value.delete(id))
    } catch (err) {
      error.value = err.response?.data?.message || err.message
      console.error('批量删除购物车项失败:', err)
    } finally {
      loading.value = false
    }
  }
  
  // 总价计算
  const totalPrice = computed(() => 
    items.value.reduce((sum, item) => {
      const price = Number(item.price) || 0
      const quantity = Number(item.quantity) || 1
      return sum + (price * quantity)
    }, 0)
  )
  
  // 选中商品总价
  const selectedTotalPrice = computed(() => 
    items.value.reduce((sum, item) => {
      if (selectedItems.value.has(item.cartItemId)) {
        const price = Number(item.price) || 0
        const quantity = Number(item.quantity) || 1
        return sum + (price * quantity)
      }
      return sum
    }, 0)
  )
  
  // 选中的商品列表
  const selectedItemsList = computed(() => 
    items.value.filter(item => selectedItems.value.has(item.cartItemId))
  )

  const toggleSelection = (cartItemId) => {
    if (selectedItems.value.has(cartItemId)) {
      selectedItems.value.delete(cartItemId)
    } else {
      selectedItems.value.add(cartItemId)
    }
  }
  
  // 全选/取消全选
  const toggleSelectAll = () => {
    if (selectedItems.value.size === items.value.length) {
      selectedItems.value.clear()
    } else {
      items.value.forEach(item => selectedItems.value.add(item.cartItemId))
    }
  }

  const clearSelected = () => selectedItems.value.clear()

  return {
    items: computed(() => items.value),
    totalPrice,
    selectedTotalPrice,
    selectedItems: computed(() => selectedItems.value),
    selectedItemsList,
    loading: computed(() => loading.value),
    error: computed(() => error.value),
    fetchCart,
    updateQuantity,
    addItemToCart,
    removeItem,
    removeItems,
    toggleSelection,
    toggleSelectAll,
    clearSelected
  }
})