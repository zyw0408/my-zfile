// 存储统计相关 API

import { get } from './request'
import type { ApiResponse, StorageStats, TypeStats } from './types'

/**
 * 获取存储统计信息
 */
export function getStorageStats(): Promise<ApiResponse<StorageStats>> {
  return get('/api/v1/storage/stats')
}

/**
 * 按文件类型统计
 */
export function getTypeStats(): Promise<ApiResponse<TypeStats[]>> {
  return get('/api/v1/storage/types')
}

