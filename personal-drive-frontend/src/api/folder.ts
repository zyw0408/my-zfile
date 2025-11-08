// 文件夹管理相关 API

import { get, post, put, del } from './request'
import type { ApiResponse, FileItem } from './types'

/**
 * 创建文件夹
 */
export function createFolder(name: string, parentId: string = 'root'): Promise<ApiResponse<FileItem>> {
  return post('/api/v1/folders', { name, parentId })
}

/**
 * 获取文件夹详情
 */
export function getFolderDetail(id: string): Promise<ApiResponse<FileItem>> {
  return get(`/api/v1/folders/${id}`)
}

/**
 * 重命名文件夹
 */
export function renameFolder(id: string, newName: string): Promise<ApiResponse<FileItem>> {
  return put(`/api/v1/folders/${id}`, { name: newName })
}

/**
 * 移动文件夹
 */
export function moveFolder(id: string, targetFolderId: string): Promise<ApiResponse<null>> {
  return post(`/api/v1/folders/${id}/move`, { targetFolderId })
}

/**
 * 删除文件夹
 */
export function deleteFolder(id: string): Promise<ApiResponse<null>> {
  return del(`/api/v1/folders/${id}`)
}

