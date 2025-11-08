// Mock 数据 - 用于前端独立测试

import type { User, FileItem } from '@/api/types'

// Mock 配置
export const MOCK_CONFIG = {
  enabled: true, // 是否启用 Mock
  delay: 300, // Mock 延迟时间 (ms)
  usePersistence: true, // 是否使用 localStorage 持久化
}

// ==================== localStorage 持久化管理 ====================

const STORAGE_KEY = 'MOCK_FILES_DATA'

/**
 * 从 localStorage 加载文件数据
 */
function loadFilesFromStorage(): FileItem[] {
  if (!MOCK_CONFIG.usePersistence) return []
  
  try {
    const data = localStorage.getItem(STORAGE_KEY)
    if (data) {
      const parsed = JSON.parse(data)
      console.log('[Mock Storage] 从 localStorage 加载了', parsed.length, '个文件')
      return parsed
    }
  } catch (error) {
    console.error('[Mock Storage] 加载失败:', error)
  }
  return []
}

/**
 * 保存文件数据到 localStorage
 */
export function saveFilesToStorage(files: FileItem[]) {
  if (!MOCK_CONFIG.usePersistence) return
  
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(files))
    console.log('[Mock Storage] 已保存', files.length, '个文件到 localStorage')
  } catch (error) {
    console.error('[Mock Storage] 保存失败:', error)
  }
}

/**
 * 清空 localStorage 中的文件数据
 */
export function clearFilesStorage() {
  localStorage.removeItem(STORAGE_KEY)
  console.log('[Mock Storage] 已清空 localStorage')
}

// 测试用户
export const mockUser: User = {
  id: 'user-test-001',
  email: '123@qq.com',
  username: '测试用户',
  avatar: '',
  role: 'user',
  storageUsed: 2147483648, // 2GB
  storageQuota: 10737418240, // 10GB
  isActive: true,
  createdAt: '2025-01-01T00:00:00Z',
  updatedAt: '2025-01-01T00:00:00Z'
}

// 测试Token
export const mockTokens = {
  accessToken: 'mock-access-token-123456',
  refreshToken: 'mock-refresh-token-123456'
}

/**
 * 文件类型分类配置
 */
export const FILE_CATEGORIES = {
  office: {
    name: 'Office文档',
    folderId: 'folder-office',
    extensions: ['docx', 'xlsx', 'pptx', 'doc', 'xls', 'ppt'],
    mimeTypes: {
      docx: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
      xlsx: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      pptx: 'application/vnd.openxmlformats-officedocument.presentationml.presentation',
      doc: 'application/msword',
      xls: 'application/vnd.ms-excel',
      ppt: 'application/vnd.ms-powerpoint'
    }
  },
  image: {
    name: '图片',
    folderId: 'folder-image',
    extensions: ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg'],
    mimeTypes: {
      jpg: 'image/jpeg',
      jpeg: 'image/jpeg',
      png: 'image/png',
      gif: 'image/gif',
      bmp: 'image/bmp',
      webp: 'image/webp',
      svg: 'image/svg+xml'
    }
  },
  text: {
    name: '文本文件',
    folderId: 'folder-text',
    extensions: ['txt', 'md', 'json', 'xml', 'csv', 'log'],
    mimeTypes: {
      txt: 'text/plain',
      md: 'text/markdown',
      json: 'application/json',
      xml: 'text/xml',
      csv: 'text/csv',
      log: 'text/plain'
    }
  },
  video: {
    name: '视频',
    folderId: 'folder-video',
    extensions: ['mp4', 'avi', 'mkv', 'mov', 'wmv', 'flv', 'webm'],
    mimeTypes: {
      mp4: 'video/mp4',
      avi: 'video/x-msvideo',
      mkv: 'video/x-matroska',
      mov: 'video/quicktime',
      wmv: 'video/x-ms-wmv',
      flv: 'video/x-flv',
      webm: 'video/webm'
    }
  },
  audio: {
    name: '音频',
    folderId: 'folder-audio',
    extensions: ['mp3', 'wav', 'flac', 'aac', 'ogg', 'm4a'],
    mimeTypes: {
      mp3: 'audio/mpeg',
      wav: 'audio/wav',
      flac: 'audio/flac',
      aac: 'audio/aac',
      ogg: 'audio/ogg',
      m4a: 'audio/mp4'
    }
  },
  pdf: {
    name: 'PDF文档',
    folderId: 'folder-pdf',
    extensions: ['pdf'],
    mimeTypes: {
      pdf: 'application/pdf'
    }
  }
}

// 初始测试文件列表（默认数据）
const initialMockFiles: FileItem[] = [
  // ==================== 根目录分类文件夹 ====================
  
  // Office文档 文件夹
  {
    id: 'folder-office',
    name: 'Office文档',
    type: 'folder',
    path: '/root/Office文档',
    parentId: 'root',
    isStarred: false,
    isDeleted: false,
    fileCount: 3,
    createdAt: '2025-01-01T10:00:00Z',
    updatedAt: '2025-01-01T10:00:00Z'
  },
  
  // 图片 文件夹
  {
    id: 'folder-image',
    name: '图片',
    type: 'folder',
    path: '/root/图片',
    parentId: 'root',
    isStarred: false,
    isDeleted: false,
    fileCount: 4,
    createdAt: '2025-01-01T10:01:00Z',
    updatedAt: '2025-01-01T10:01:00Z'
  },
  
  // 文本文件 文件夹
  {
    id: 'folder-text',
    name: '文本文件',
    type: 'folder',
    path: '/root/文本文件',
    parentId: 'root',
    isStarred: false,
    isDeleted: false,
    fileCount: 3,
    createdAt: '2025-01-01T10:02:00Z',
    updatedAt: '2025-01-01T10:02:00Z'
  },
  
  // 视频 文件夹
  {
    id: 'folder-video',
    name: '视频',
    type: 'folder',
    path: '/root/视频',
    parentId: 'root',
    isStarred: false,
    isDeleted: false,
    fileCount: 2,
    createdAt: '2025-01-01T10:03:00Z',
    updatedAt: '2025-01-01T10:03:00Z'
  },
  
  // 音频 文件夹
  {
    id: 'folder-audio',
    name: '音频',
    type: 'folder',
    path: '/root/音频',
    parentId: 'root',
    isStarred: false,
    isDeleted: false,
    fileCount: 2,
    createdAt: '2025-01-01T10:04:00Z',
    updatedAt: '2025-01-01T10:04:00Z'
  },
  
  // PDF文档 文件夹
  {
    id: 'folder-pdf',
    name: 'PDF文档',
    type: 'folder',
    path: '/root/PDF文档',
    parentId: 'root',
    isStarred: false,
    isDeleted: false,
    fileCount: 2,
    createdAt: '2025-01-01T10:05:00Z',
    updatedAt: '2025-01-01T10:05:00Z'
  },
  
  // ==================== Office文档 文件夹内的文件 ====================
  
  {
    id: 'file-office-001',
    name: '工作报告.docx',
    type: 'file',
    mimeType: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    size: 524288, // 512KB
    path: '/root/Office文档/工作报告.docx',
    parentId: 'folder-office',
    hash: 'sha256-office001',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-02T10:00:00Z',
    updatedAt: '2025-01-02T10:00:00Z'
  },
  {
    id: 'file-office-002',
    name: '销售数据.xlsx',
    type: 'file',
    mimeType: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    size: 1048576, // 1MB
    path: '/root/Office文档/销售数据.xlsx',
    parentId: 'folder-office',
    hash: 'sha256-office002',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-02T10:10:00Z',
    updatedAt: '2025-01-02T10:10:00Z'
  },
  {
    id: 'file-office-003',
    name: '项目介绍.pptx',
    type: 'file',
    mimeType: 'application/vnd.openxmlformats-officedocument.presentationml.presentation',
    size: 2097152, // 2MB
    path: '/root/Office文档/项目介绍.pptx',
    parentId: 'folder-office',
    hash: 'sha256-office003',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-02T10:20:00Z',
    updatedAt: '2025-01-02T10:20:00Z'
  },
  
  // ==================== 图片 文件夹内的文件 ====================
  
  {
    id: 'file-image-001',
    name: '风景照.jpg',
    type: 'file',
    mimeType: 'image/jpeg',
    size: 2048000, // 2MB
    path: '/root/图片/风景照.jpg',
    parentId: 'folder-image',
    hash: 'sha256-image001',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-03T10:00:00Z',
    updatedAt: '2025-01-03T10:00:00Z'
  },
  {
    id: 'file-image-002',
    name: '产品图.png',
    type: 'file',
    mimeType: 'image/png',
    size: 1536000, // 1.5MB
    path: '/root/图片/产品图.png',
    parentId: 'folder-image',
    hash: 'sha256-image002',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-03T10:10:00Z',
    updatedAt: '2025-01-03T10:10:00Z'
  },
  {
    id: 'file-image-003',
    name: '动图示例.gif',
    type: 'file',
    mimeType: 'image/gif',
    size: 512000, // 500KB
    path: '/root/图片/动图示例.gif',
    parentId: 'folder-image',
    hash: 'sha256-image003',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-03T10:20:00Z',
    updatedAt: '2025-01-03T10:20:00Z'
  },
  {
    id: 'file-image-004',
    name: 'Logo设计.webp',
    type: 'file',
    mimeType: 'image/webp',
    size: 256000, // 250KB
    path: '/root/图片/Logo设计.webp',
    parentId: 'folder-image',
    hash: 'sha256-image004',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-03T10:30:00Z',
    updatedAt: '2025-01-03T10:30:00Z'
  },
  
  // ==================== 文本文件 文件夹内的文件 ====================
  
  {
    id: 'file-text-001',
    name: '说明文档.txt',
    type: 'file',
    mimeType: 'text/plain',
    size: 10240, // 10KB
    path: '/root/文本文件/说明文档.txt',
    parentId: 'folder-text',
    hash: 'sha256-text001',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-04T10:00:00Z',
    updatedAt: '2025-01-04T10:00:00Z'
  },
  {
    id: 'file-text-002',
    name: 'README.md',
    type: 'file',
    mimeType: 'text/markdown',
    size: 15360, // 15KB
    path: '/root/文本文件/README.md',
    parentId: 'folder-text',
    hash: 'sha256-text002',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-04T10:10:00Z',
    updatedAt: '2025-01-04T10:10:00Z'
  },
  {
    id: 'file-text-003',
    name: '配置文件.json',
    type: 'file',
    mimeType: 'application/json',
    size: 8192, // 8KB
    path: '/root/文本文件/配置文件.json',
    parentId: 'folder-text',
    hash: 'sha256-text003',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-04T10:20:00Z',
    updatedAt: '2025-01-04T10:20:00Z'
  },
  
  // ==================== 视频 文件夹内的文件 ====================
  
  {
    id: 'file-video-001',
    name: '教学视频.mp4',
    type: 'file',
    mimeType: 'video/mp4',
    size: 52428800, // 50MB
    path: '/root/视频/教学视频.mp4',
    parentId: 'folder-video',
    hash: 'sha256-video001',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-05T10:00:00Z',
    updatedAt: '2025-01-05T10:00:00Z'
  },
  {
    id: 'file-video-002',
    name: '产品演示.webm',
    type: 'file',
    mimeType: 'video/webm',
    size: 31457280, // 30MB
    path: '/root/视频/产品演示.webm',
    parentId: 'folder-video',
    hash: 'sha256-video002',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-05T10:10:00Z',
    updatedAt: '2025-01-05T10:10:00Z'
  },
  
  // ==================== 音频 文件夹内的文件 ====================
  
  {
    id: 'file-audio-001',
    name: '背景音乐.mp3',
    type: 'file',
    mimeType: 'audio/mpeg',
    size: 5242880, // 5MB
    path: '/root/音频/背景音乐.mp3',
    parentId: 'folder-audio',
    hash: 'sha256-audio001',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-06T10:00:00Z',
    updatedAt: '2025-01-06T10:00:00Z'
  },
  {
    id: 'file-audio-002',
    name: '录音记录.wav',
    type: 'file',
    mimeType: 'audio/wav',
    size: 10485760, // 10MB
    path: '/root/音频/录音记录.wav',
    parentId: 'folder-audio',
    hash: 'sha256-audio002',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-06T10:10:00Z',
    updatedAt: '2025-01-06T10:10:00Z'
  },
  
  // ==================== PDF文档 文件夹内的文件 ====================
  
  {
    id: 'file-pdf-001',
    name: '技术规范.pdf',
    type: 'file',
    mimeType: 'application/pdf',
    size: 3145728, // 3MB
    path: '/root/PDF文档/技术规范.pdf',
    parentId: 'folder-pdf',
    hash: 'sha256-pdf001',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-07T10:00:00Z',
    updatedAt: '2025-01-07T10:00:00Z'
  },
  {
    id: 'file-pdf-002',
    name: '用户手册.pdf',
    type: 'file',
    mimeType: 'application/pdf',
    size: 2097152, // 2MB
    path: '/root/PDF文档/用户手册.pdf',
    parentId: 'folder-pdf',
    hash: 'sha256-pdf002',
    isStarred: false,
    isDeleted: false,
    createdAt: '2025-01-07T10:10:00Z',
    updatedAt: '2025-01-07T10:10:00Z'
  }
]

// Mock 文件数据（优先从 localStorage 加载）
const loadedFiles = loadFilesFromStorage()
export const mockFiles: FileItem[] = loadedFiles.length > 0 ? loadedFiles : initialMockFiles

// 如果是首次加载（使用初始数据），保存到 localStorage
if (loadedFiles.length === 0 && MOCK_CONFIG.usePersistence) {
  saveFilesToStorage(initialMockFiles)
  console.log('[Mock] 首次加载，已保存初始数据到 localStorage')
}

// ==================== Mock 认证函数 ====================

/**
 * Mock 登录
 */
export function mockLogin(email: string, password: string) {
  // 简单验证
  if (email === mockUser.email && password === '123456') {
    return {
      success: true,
      data: {
        user: mockUser,
        accessToken: mockTokens.accessToken,
        refreshToken: mockTokens.refreshToken
      }
    }
  }
  return {
    success: false,
    message: '邮箱或密码错误'
  }
}

/**
 * Mock 注册
 */
export function mockRegister(email: string, username: string, password: string) {
  // 简单验证：检查邮箱是否已存在
  if (email === mockUser.email) {
    return {
      success: false,
      message: '该邮箱已被注册'
    }
  }
  
  // 注册成功，返回新用户信息
  return {
    success: true,
    data: {
      user: {
        ...mockUser,
        id: 'user-' + Date.now(),
        email,
        username
      },
      accessToken: mockTokens.accessToken,
      refreshToken: mockTokens.refreshToken
    }
  }
}
