import axios from 'axios'

const apiClient = axios.create({
  baseURL: 'http://ajax-api.itheima.net', // 替换为你的实际API地址
  withCredentials: false,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json'
  }
})

export default {
  //login(user) {
  //  return apiClient.post('/login', user)
  //},
  login(user) {
    return apiClient.post('/login', user)
      .then(response => {
        // 添加角色字段
        return {
          ...response,
          data: {
            ...response.data,
            role: 'buyer' // 临时测试值
          }
        };
      });
  },
  register(user) {
    return apiClient.post('/register', {
      ...user,
      // 确保注册时提交的role字段与后端匹配
      role: user.role === 'business' ? 1 : 
           user.role === 'buyer' ? 2 :
           user.role === 'admin' ? 3 : 0
    })
  }
}