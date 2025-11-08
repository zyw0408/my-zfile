// API 类型定义文件 - 遵循开发文档

/**
 * 统一响应格式
 */
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

/**
 * 分页响应
 */
export interface PageResponse<T> {
  items: T[]
  total: number
  page: number
  pageSize: number
}

/**
 * 用户信息
 */
export interface User {
  id: string
  email: string
  username: string
  avatar?: string
  role: 'user' | 'admin'
  storageUsed: number
  storageQuota: number
  isActive: boolean
  createdAt: string
  updatedAt: string
}

/**
 * 登录请求
 */
export interface LoginRequest {
  email: string
  password: string
}

/**
 * 注册请求
 */
export interface RegisterRequest {
  email: string
  username: string
  password: string
}

/**
 * 登录响应
 */
export interface LoginResponse {
  user: User
  accessToken: string
  refreshToken: string
}

/**
 * 文件/文件夹项
 */
export interface FileItem {
  id: string
  name: string
  type: 'file' | 'folder'
  mimeType?: string
  size?: number
  path: string
  parentId: string
  hash?: string
  isStarred: boolean
  isDeleted: boolean
  createdAt: string
  updatedAt: string
  fileCount?: number // 文件夹专用：包含的文件数
  deletedAt?: string // 删除时间
  base64Content?: string // ✨ 新增：文件的base64内容（用于Mock环境）
}

/**
 * 文件上传响应
 */
export interface FileUploadResponse {
  file: FileItem
}

/**
 * 分片上传响应
 */
export interface ChunkUploadResponse {
  sessionId: string
  uploadedChunks: number
  totalChunks: number
  isComplete: boolean
}

/**
 * 合并分片请求
 */
export interface MergeChunksRequest {
  sessionId: string
  fileName: string
  folderId: string
}

/**
 * 分享信息
 */
export interface Share {
  id: string
  fileId: string
  userId: string
  shareCode: string
  password?: string
  expireAt?: string
  viewCount: number
  downloadCount: number
  createdAt: string
}

/**
 * 创建分享请求
 */
export interface CreateShareRequest {
  fileId: string
  password?: string
  expireAt?: string
}

/**
 * 访问分享请求
 */
export interface AccessShareRequest {
  code: string
  password?: string
}

/**
 * 存储统计信息
 */
export interface StorageStats {
  totalUsed: number
  totalQuota: number
  fileCount: number
  folderCount: number
  typeStats: TypeStats[]
}

/**
 * 按类型统计
 */
export interface TypeStats {
  type: string
  count: number
  size: number
}

/**
 * 文件搜索请求
 */
export interface SearchRequest {
  keyword: string
  type?: 'file' | 'folder'
  mimeType?: string
  startDate?: string
  endDate?: string
  page?: number
  pageSize?: number
}

/**
 * 文件操作请求
 */
export interface FileOperationRequest {
  targetId: string
  newName?: string
  targetFolderId?: string
}

