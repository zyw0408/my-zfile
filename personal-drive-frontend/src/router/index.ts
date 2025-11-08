// 路由配置

import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login/index.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register/index.vue'),
    meta: { title: '注册', requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/components/Layout/index.vue'),
    meta: { requiresAuth: true },
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/Home/index.vue'),
        meta: { title: '我的文件', icon: 'Folder' }
      },
      {
        path: 'trash',
        name: 'Trash',
        component: () => import('@/views/Trash/index.vue'),
        meta: { title: '回收站', icon: 'Delete' }
      },
      {
        path: 'share',
        name: 'Share',
        component: () => import('@/views/Share/index.vue'),
        meta: { title: '我的分享', icon: 'Share' }
      },
      {
        path: 'storage',
        name: 'Storage',
        component: () => import('@/views/Storage/index.vue'),
        meta: { title: '存储管理', icon: 'PieChart' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/Settings/index.vue'),
        meta: { title: '个人设置', icon: 'Setting' }
      },
      {
        path: 'admin',
        name: 'Admin',
        component: () => import('@/views/Admin/index.vue'),
        meta: { title: '系统管理', icon: 'Tools', requiresAdmin: true }
      }
    ]
  },
  {
    path: '/s/:code',
    name: 'ShareAccess',
    component: () => import('@/views/ShareAccess/index.vue'),
    meta: { title: '访问分享', requiresAuth: false }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound/index.vue'),
    meta: { title: '404' }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()

  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - ${import.meta.env.VITE_APP_TITLE || '个人网盘系统'}`
  }

  // 检查是否需要登录
  if (to.meta.requiresAuth) {
    if (!authStore.isLoggedIn) {
      next({ name: 'Login', query: { redirect: to.fullPath } })
      return
    }

    // 获取用户信息（如果还没有）
    if (!authStore.user) {
      await authStore.fetchUserInfo()
    }

    // 检查管理员权限
    if (to.meta.requiresAdmin && !authStore.isAdmin) {
      next({ name: 'Home' })
      return
    }
  }

  // 已登录用户访问登录/注册页，跳转到首页
  if ((to.name === 'Login' || to.name === 'Register') && authStore.isLoggedIn) {
    next({ name: 'Home' })
    return
  }

  next()
})

export default router

