// 文件管理相关 API

import { get, post, put, del, downloadBase64File } from './request'
import request from './request'
import { mockFiles, MOCK_CONFIG } from '@/utils/mock'
import type {
  ApiResponse,
  PageResponse,
  FileItem,
  FileUploadResponse,
  ChunkUploadResponse,
  MergeChunksRequest,
  SearchRequest
} from './types'

/**
 * 获取文件列表
 */
export function getFileList(params: {
  folderId?: string
  page?: number
  pageSize?: number
  sortBy?: string
  order?: 'asc' | 'desc'
}): Promise<ApiResponse<PageResponse<FileItem>>> {
  return get('/api/v1/files', params)
}

/**
 * 获取文件详情
 */
export function getFileDetail(id: string): Promise<ApiResponse<FileItem>> {
  return get(`/api/v1/files/${id}`)
}

/**
 * 上传文件（普通上传）
 */
export function uploadFile(formData: FormData, onProgress?: (progress: number) => void): Promise<ApiResponse<FileUploadResponse>> {
  return request.post('/api/v1/files/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    onUploadProgress: (progressEvent) => {
      if (progressEvent.total && onProgress) {
        const percent = Math.round((progressEvent.loaded * 100) / progressEvent.total)
        onProgress(percent)
      }
    }
  })
}

/**
 * 分片上传
 */
export function uploadChunk(formData: FormData): Promise<ApiResponse<ChunkUploadResponse>> {
  return request.post('/api/v1/files/chunk-upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 合并分片
 */
export function mergeChunks(data: MergeChunksRequest): Promise<ApiResponse<FileItem>> {
  return post('/api/v1/files/merge-chunks', data)
}

/**
 * 下载文件
 * Mock环境：如果文件有base64Content，直接触发下载
 * 真实环境：返回下载URL
 */
export function downloadFile(id: string): string | void {
  // Mock环境下，检查文件是否有base64内容
  if (MOCK_CONFIG.enabled) {
    const file = mockFiles.find(f => f.id === id)
    if (file && file.base64Content) {
      // 直接下载base64文件
      downloadBase64File(file.base64Content, file.name, file.mimeType || 'application/octet-stream')
      console.log('[Mock] 下载文件:', file.name)
      return // 不返回URL
    } else if (file) {
      console.warn('[Mock] 文件没有base64内容，无法下载:', file.name)
      return `#` // 返回占位符
    }
  }
  
  // 真实环境：返回下载URL
  const token = localStorage.getItem('access_token')
  const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
  return `${baseURL}/api/v1/files/${id}/download?token=${token}`
}

/**
 * 重命名文件
 */
export function renameFile(id: string, newName: string): Promise<ApiResponse<FileItem>> {
  return put(`/api/v1/files/${id}`, { name: newName })
}

/**
 * 移动文件
 */
export function moveFile(id: string, targetFolderId: string): Promise<ApiResponse<null>> {
  return post(`/api/v1/files/${id}/move`, { targetFolderId })
}

/**
 * 复制文件
 */
export function copyFile(id: string, targetFolderId: string): Promise<ApiResponse<FileItem>> {
  return post(`/api/v1/files/${id}/copy`, { targetFolderId })
}

/**
 * 删除文件（移入回收站）
 */
export function deleteFile(id: string): Promise<ApiResponse<null>> {
  return del(`/api/v1/files/${id}`)
}

/**
 * 创建分享链接
 */
export function createShare(fileId: string, password?: string, expireAt?: string): Promise<ApiResponse<{ shareCode: string }>> {
  return post(`/api/v1/files/${fileId}/share`, { password, expireAt })
}

/**
 * 搜索文件
 */
export function searchFiles(params: SearchRequest): Promise<ApiResponse<PageResponse<FileItem>>> {
  return get('/api/v1/files/search', params)
}

/**
 * 收藏/取消收藏文件
 */
export function toggleStar(id: string, isStarred: boolean): Promise<ApiResponse<null>> {
  return put(`/api/v1/files/${id}`, { isStarred })
}

