/*

import axios from 'axios'

const apiClient = axios.create({
  baseURL: 'http://ajax-api.itheima.net',
  withCredentials: false,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json'
  }
})
*/
/*
export default {
  login(user) {
    return new Promise((resolve, reject) => {
      const roles = ['business', 'buyer', 'admin'];
      let foundUser = null;
      
      for (const role of roles) {
        const key = `${role}Users`;
        const users = JSON.parse(localStorage.getItem(key) || '[]')
        
        // 确保正确处理空数组情况
        if (Array.isArray(users)) {
          foundUser = users.find(u => 
            u.userId === user.userId && u.password === user.password
          );
        }

        if (foundUser) {
          return resolve({
            data: {
              token: 'simulated-token',
              userId: foundUser.userId,
              userName: foundUser.userName,
              role: foundUser.role
            }
          });
        }
      }
      
      reject(new Error('用户ID或密码错误'));
    });
  },

  register(user) {
    return new Promise((resolve, reject) => {
      const key = `${user.role}Users`;
      const users = JSON.parse(localStorage.getItem(key) || '[]');

      // 检查用户ID是否已存在
      if (users.some(u => u.userId === user.userId)) {
        reject(new Error('用户ID已存在'));
        return;
      }

      // 创建新用户对象
      const newUser = {
        userId: user.userId,
        userName: user.userName,
        password: user.password,
        role: user.role
      };

      // 更新存储
      users.push(newUser);
      localStorage.setItem(key, JSON.stringify(users));
      
      resolve({ 
        data: {
          message: '注册成功',
          user: newUser
        }
      });
    });
  }
}
*/
import axios from 'axios'

const apiClient = axios.create({
  baseURL: 'http://algorineko.top:8080/api',
  withCredentials: false,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json'
  }
})

// 添加请求拦截器 - 自动携带token
apiClient.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
}, error => {
  return Promise.reject(error)
})

// 添加响应拦截器
apiClient.interceptors.response.use(response => {
  return response
}, error => {
  console.error('API error:', error)
  return Promise.reject(error)
})

export default {
  login(user) {
    return apiClient.post('/user/login', {
      username: user.username,
      password: user.password,
      userType: this.mapRoleToUserType(user.role)
    })
  },

  register(user) {
    return apiClient.post('/user/register', {
      username: user.username,
      password: user.password,
      phone: user.phone,
      address: user.address,
      userType: this.mapRoleToUserType(user.role)
    })
  },

  mapRoleToUserType(role) {
    const mapping = {
      'merchant': 'merchant',
      'customer': 'customer',
      'admin': 'admin'
    }
    return mapping[role] || 'customer'
  },
  
  // 新增：根据用户名获取用户信息
  getUserInfo(username) {
    return apiClient.get(`/user/${username}`)
  },
  
  // 新增：商家相关API
  getBusinessProducts(username) {
    return apiClient.get(`/merchant/${username}/products`)
  },
  
  // 新增：买家相关API
  getBuyerCart(username) {
    return apiClient.get(`/customer/${username}/cart`)
  }
}