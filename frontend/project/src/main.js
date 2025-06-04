import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios' // 导入 axios

// 创建Vue应用实例
const app = createApp(App)

// 创建Pinia实例
const pinia = createPinia()

// ================== Axios 全局配置 ==================
// 1. 设置基础URL（可选）
axios.defaults.baseURL = 'http://algorineko.top:8080/api'

// 2. 设置默认请求头
axios.defaults.headers.common['Content-Type'] = 'application/json'
axios.defaults.headers.common['Accept'] = 'application/json'

// 3. 允许跨域请求携带凭据
axios.defaults.withCredentials = true

// 4. 设置请求拦截器 - 自动添加 Token
axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 5. 设置响应拦截器 - 处理401未授权错误
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      // 处理未授权错误（例如跳转到登录页）
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

// 将 axios 添加为全局属性（可选）
app.config.globalProperties.$axios = axios

// ================== 按顺序安装插件 ==================
app.use(pinia)
app.use(router)
app.use(ElementPlus)

// 最后挂载应用
app.mount('#app')