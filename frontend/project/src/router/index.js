import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/RegisterView.vue')
  },
  {
    path: '/business/:username',
    name: 'Business',
    component: () => import('../views/business/BusinessView.vue'),
    meta: { requiresAuth: true, role: 'merchant' },
    props: true,
    children: [
      {
        path: 'info',
        name: 'BusinessInfo',
        component: () => import('../views/business/BusinessInfo.vue')
      },
      {
        path: 'products',
        name: 'BusinessProducts',
        component: () => import('../views/business/ProductManagement.vue')
      },
      {
        path: 'orders',
        name: 'BusinessOrders',
        component: () => import('../views/business/OrderManagement.vue')
      },
      {
        path: '',
        redirect: { name: 'BusinessInfo' }
      }
    ]
  },
  {
    path: '/buyer/:username',
    name: 'Buyer',
    component: () => import('../views/Buyer/BuyerView.vue'),
    meta: { requiresAuth: true, role: 'customer' },
    props: true,
    children: [
      {
        path: '',
        redirect: to => ({ path: `/buyer/${to.params.username}/products` })
      },
      {
        path: 'products',
        component: () => import('../views/Buyer/ProductList.vue')
      },
      {
        path: 'product/:id',
        component: () => import('../views/Buyer/ProductDetail.vue'),
        props: true
      },
      {
        path: 'user',
        component: () => import('../views/Buyer/UserCenter.vue'),
        children: [
          {
            path: '',
            redirect: to => ({ path: `/buyer/${to.params.username}/user/profile` })
          },
          {
            path: 'profile',
            component: () => import('../components/UserProfile.vue')
          },
          {
            path: 'cart',
            component: () => import('../components/ShoppingCart.vue')
          },
          {
            path: 'orders',
            component: () => import('../components/UserOrders.vue')
          },
          {
            path: 'reviews',
            component: () => import('../components/UserReviews.vue')
          }
        ]
      }
    ]
  },
  {
    path: '/admin',
    name: 'Admin',
    redirect: () => {
      // 直接重定向到Swagger UI
      window.location.href = 'http://algorineko.top:8080/swagger-ui/index.html';
      return { path: '/' }; // 防止Vue Router报错
    },
    meta: { requiresAuth: true, role: 'admin' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token')
  const userRole = localStorage.getItem('role')
  const username = localStorage.getItem('username')

  if (to.meta.requiresAuth) {
    if (!isAuthenticated) {
      next('/login')
    } else if (to.meta.role !== userRole) {
      next(from.path)
      alert('无权访问该页面')
    } else {
      // 检查路由参数中是否有username
      if (to.params.username && to.params.username !== username) {
        // 重定向到当前用户的路由
        const newPath = to.path.replace(/\/\w+(\/|$)/, `/${username}$1`)
        next(newPath)
      } else {
        next()
      }
    }
  } else {
    next()
  }
})

export default router