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
 /*   // 新增角色路由
    {
      path: '/business',
      component: () => import('@/views/business/BusinessView.vue'),
      meta: { requiresAuth: true, role: 'business' },
      children: [
        {
          path: 'info',
          component: () => import('@/views/business/InfoView.vue')
        },
        {
          path: 'products',
          component: () => import('@/views/business/ProductView.vue')
        },
        {
          path: 'orders',
          component: () => import('@/views/business/OrderView.vue')
        },
        {
          path: 'reviews',
          component: () => import('@/views/business/ReviewView.vue')
        },
        {
          path: '',
          redirect: '/business/info'
        }
      ]
    },
  */
  {
    path: '/business',
    name: 'Business',
    component: () => import('../views/BusinessView.vue'),
    meta: { requiresAuth: true, role: 'business' },

  },
/*
  {
      path: '/buyer',
      name: 'Buyer',
      component: () => import('../views/BuyerView.vue'),
      meta: { requiresAuth: true, role: 'buyer' }
  },
*/
{
  path: '/buyer',
  component: () => import('../views/Buyer/BuyerView.vue'),
  children: [
    {
      path: '',
      redirect: '/buyer/products'
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
          redirect: '/buyer/user/profile'
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
      component: () => import('../views/AdminView.vue'),
      meta: { requiresAuth: true, role: 'admin' }
  }

]

const router = createRouter({
  history: createWebHistory(),
  routes
})
// 添加导航守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token')
  const userRole = localStorage.getItem('role')

  if (to.meta.requiresAuth) {
    if (!isAuthenticated) {
      next('/login')
    } else if (to.meta.role !== userRole) {
      next(from.path) // 停留在当前页面
      alert('无权访问该页面')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router