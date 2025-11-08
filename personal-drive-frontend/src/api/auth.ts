// 认证相关 API

import { post, get } from './request'
import type {
  ApiResponse,
  LoginRequest,
  RegisterRequest,
  LoginResponse,
  User
} from './types'

/**
 * 用户注册
 */
export function register(data: RegisterRequest): Promise<ApiResponse<LoginResponse>> {
  return post('/api/v1/auth/register', data)
}

/**
 * 用户登录
 */
export function login(data: LoginRequest): Promise<ApiResponse<LoginResponse>> {
  return post('/api/v1/auth/login', data)
}

/**
 * 用户登出
 */
export function logout(): Promise<ApiResponse<null>> {
  return post('/api/v1/auth/logout')
}

/**
 * 刷新 Token
 */
export function refreshToken(refreshToken: string): Promise<ApiResponse<{ accessToken: string }>> {
  return post('/api/v1/auth/refresh-token', { refreshToken })
}

/**
 * 获取当前用户信息
 */
export function getCurrentUser(): Promise<ApiResponse<User>> {
  return get('/api/v1/auth/me')
}

