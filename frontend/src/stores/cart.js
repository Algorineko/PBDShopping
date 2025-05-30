// stores/cart.js
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCartStore = defineStore('cart', () => {
  
  // 增强localStorage读取健壮性
  const initialCart = () => {
    try {
      return JSON.parse(localStorage.getItem('cart')) || []
    } catch (error) {
      console.error('购物车数据解析失败:', error)
      return []
    }
  }

  const items = ref(initialCart())
  const selectedItems = ref(new Set())
  
  // 添加商品
  const addItem = (item) => {
    const existing = items.value.find(i => i.id === item.id)
    if (existing) {
      existing.quantity += Number(item.quantity) || 1
    } else {
      items.value.push({
        id: item.id,
        name: item.name || '未知商品',
        price: Number(item.price) || 0, // 强制转换为数字
        quantity: Math.max(1, Number(item.quantity) || 1),
        image: item.image || '/placeholder-product.jpg'
      })
    }
    persistCart()
  }

  // 移除商品
  const removeItem = (id) => {
    items.value = items.value.filter(i => i.id !== id)
    selectedItems.value.delete(id) // 同时从选中项中移除
    persistCart()
  }
  
  // 批量移除商品
  const removeItems = (ids) => {
    items.value = items.value.filter(i => !ids.includes(i.id))
    ids.forEach(id => selectedItems.value.delete(id))
    persistCart()
  }

  // 持久化存储
  const persistCart = () => {
    localStorage.setItem('cart', JSON.stringify(items.value))
  }

  // 总价计算（增强数值安全）
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
      if (selectedItems.value.has(item.id)) {
        const price = Number(item.price) || 0
        const quantity = Number(item.quantity) || 1
        return sum + (price * quantity)
      }
      return sum
    }, 0)
  )
  
  // 选中的商品列表
  const selectedItemsList = computed(() => 
    items.value.filter(item => selectedItems.value.has(item.id))
  )

  const toggleSelection = (id) => {
    if (selectedItems.value.has(id)) {
      selectedItems.value.delete(id)
    } else {
      selectedItems.value.add(id)
    }
    persistCart()
  }
  
  // 全选/取消全选
  const toggleSelectAll = () => {
    if (selectedItems.value.size === items.value.length) {
      selectedItems.value.clear()
    } else {
      items.value.forEach(item => selectedItems.value.add(item.id))
    }
    persistCart()
  }

  const clearSelected = () => selectedItems.value.clear()

  return {
    items: computed(() => items.value),
    totalPrice,
    selectedTotalPrice,
    selectedItems: computed(() => selectedItems.value),
    selectedItemsList,
    addItem,
    removeItem,
    removeItems,
    toggleSelection,
    toggleSelectAll,
    clearSelected
  }
})