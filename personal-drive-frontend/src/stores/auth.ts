// 认证状态管理

import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, register as registerApi, getCurrentUser, logout as logoutApi } from '@/api/auth'
import type { User, LoginRequest, RegisterRequest } from '@/api/types'
import { ElMessage } from 'element-plus'

export const useAuthStore = defineStore('auth', () => {
  // 状态
  const token = ref<string>(localStorage.getItem('access_token') || '')
  const refreshToken = ref<string>(localStorage.getItem('refresh_token') || '')
  const user = ref<User | null>(null)
  const loading = ref(false)

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'admin')

  /**
   * 登录
   */
  async function login(loginData: LoginRequest) {
    try {
      loading.value = true
      const res = await loginApi(loginData)
      
      // 保存Token
      token.value = res.data.accessToken
      refreshToken.value = res.data.refreshToken
      localStorage.setItem('access_token', res.data.accessToken)
      localStorage.setItem('refresh_token', res.data.refreshToken)
      
      // 保存用户信息
      user.value = res.data.user
      
      ElMessage.success('登录成功')
      return true
    } catch (error) {
      console.error('登录失败:', error)
      return false
    } finally {
      loading.value = false
    }
  }

  /**
   * 注册
   */
  async function register(registerData: RegisterRequest) {
    try {
      loading.value = true
      const res = await registerApi(registerData)
      
      // 注册成功后自动登录
      token.value = res.data.accessToken
      refreshToken.value = res.data.refreshToken
      localStorage.setItem('access_token', res.data.accessToken)
      localStorage.setItem('refresh_token', res.data.refreshToken)
      
      user.value = res.data.user
      
      ElMessage.success('注册成功')
      return true
    } catch (error) {
      console.error('注册失败:', error)
      return false
    } finally {
      loading.value = false
    }
  }

  /**
   * 登出
   */
  async function logout() {
    try {
      await logoutApi()
    } catch (error) {
      console.error('登出失败:', error)
    } finally {
      // 清除本地数据
      token.value = ''
      refreshToken.value = ''
      user.value = null
      localStorage.removeItem('access_token')
      localStorage.removeItem('refresh_token')
      
      ElMessage.success('已退出登录')
    }
  }

  /**
   * 获取用户信息
   */
  async function fetchUserInfo() {
    if (!token.value) return

    try {
      const res = await getCurrentUser()
      user.value = res.data
    } catch (error) {
      console.error('获取用户信息失败:', error)
      // Token失效，清除登录状态
      logout()
    }
  }

  /**
   * 更新用户信息
   */
  function updateUser(newUser: Partial<User>) {
    if (user.value) {
      user.value = { ...user.value, ...newUser }
    }
  }

  return {
    token,
    refreshToken,
    user,
    loading,
    isLoggedIn,
    isAdmin,
    login,
    register,
    logout,
    fetchUserInfo,
    updateUser
  }
})

