// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus' // 添加这一行
import 'element-plus/dist/index.css' // 添加样式

// 创建Vue应用实例
const app = createApp(App)

// 创建Pinia实例
const pinia = createPinia()

// 按顺序安装插件
app.use(pinia)
app.use(router)
app.use(ElementPlus) // 注册Element Plusnop
// 最后挂载应用
app.mount('#app')