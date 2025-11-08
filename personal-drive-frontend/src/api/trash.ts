// 回收站相关 API

import { get, post, del } from './request'
import type { ApiResponse, PageResponse, FileItem } from './types'

/**
 * 获取回收站列表
 */
export function getTrashList(params?: { page?: number; pageSize?: number }): Promise<ApiResponse<PageResponse<FileItem>>> {
  return get('/api/v1/trash', params)
}

/**
 * 恢复文件
 */
export function restoreFile(id: string): Promise<ApiResponse<null>> {
  return post(`/api/v1/trash/${id}/restore`)
}

/**
 * 永久删除文件
 */
export function permanentlyDelete(id: string): Promise<ApiResponse<null>> {
  return del(`/api/v1/trash/${id}`)
}

/**
 * 清空回收站
 */
export function clearTrash(): Promise<ApiResponse<null>> {
  return del('/api/v1/trash/clear')
}

