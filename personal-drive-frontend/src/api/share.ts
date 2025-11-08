// 分享管理相关 API

import { get, post, del } from './request'
import type { ApiResponse, PageResponse, Share, CreateShareRequest, AccessShareRequest, FileItem } from './types'

/**
 * 创建分享
 */
export function createShare(data: CreateShareRequest): Promise<ApiResponse<Share>> {
  return post('/api/v1/shares', data)
}

/**
 * 获取我的分享列表
 */
export function getMyShares(params?: { page?: number; pageSize?: number }): Promise<ApiResponse<PageResponse<Share>>> {
  return get('/api/v1/shares', params)
}

/**
 * 通过分享码访问（公开）
 */
export function accessShare(data: AccessShareRequest): Promise<ApiResponse<FileItem>> {
  return post(`/api/v1/shares/${data.code}`, { password: data.password })
}

/**
 * 取消分享
 */
export function cancelShare(id: string): Promise<ApiResponse<null>> {
  return del(`/api/v1/shares/${id}`)
}

