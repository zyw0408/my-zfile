// Axios 实例配置 - 统一的HTTP请求封装

import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse, type InternalAxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { MOCK_CONFIG, mockLogin, mockRegister, mockUser, mockFiles, saveFilesToStorage } from '@/utils/mock'

// ==================== 文件读取工具函数 ====================

/**
 * 将File对象读取为base64字符串
 */
function readFileAsBase64(file: File): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = () => {
      const result = reader.result as string
      // 移除 data:xxx;base64, 前缀，只保留base64数据
      const base64 = result.split(',')[1] || result
      resolve(base64)
    }
    reader.onerror = () => reject(reader.error)
    reader.readAsDataURL(file)
  })
}

/**
 * 将base64字符串转换为Blob对象
 */
function base64ToBlob(base64: string, mimeType: string = 'application/octet-stream'): Blob {
  try {
    // 移除可能存在的 data:xxx;base64, 前缀
    const base64Data = base64.includes(',') ? base64.split(',')[1] : base64
    
    // 解码base64
    const byteCharacters = atob(base64Data)
    const byteNumbers = new Array(byteCharacters.length)
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i)
    }
    const byteArray = new Uint8Array(byteNumbers)
    return new Blob([byteArray], { type: mimeType })
  } catch (error) {
    console.error('[Mock] base64解码失败:', error)
    // 返回空Blob
    return new Blob([], { type: mimeType })
  }
}

/**
 * 从base64创建可访问的URL
 */
export function createBlobUrl(base64: string, mimeType: string): string {
  const blob = base64ToBlob(base64, mimeType)
  return URL.createObjectURL(blob)
}

/**
 * 下载base64文件
 */
export function downloadBase64File(base64: string, fileName: string, mimeType: string) {
  const blob = base64ToBlob(base64, mimeType)
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = fileName
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)
}

// 创建 axios 实例
const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 30000, // 30秒超时
  headers: {
    'Content-Type': 'application/json'
  }
})

// Mock 拦截器
if (MOCK_CONFIG.enabled) {
  request.interceptors.request.use(
    async (config: InternalAxiosRequestConfig) => {
      // 模拟网络延迟
      await new Promise(resolve => setTimeout(resolve, MOCK_CONFIG.delay))

      const url = config.url || ''
      const method = config.method?.toUpperCase()

      // 登录 Mock
      if (url.includes('/auth/login') && method === 'POST') {
        const { email, password } = config.data
        const result = mockLogin(email, password)
        
        if (result.success) {
          config.adapter = () => Promise.resolve({
            data: { code: 200, message: 'success', data: result.data },
            status: 200,
            statusText: 'OK',
            headers: {},
            config
          } as AxiosResponse)
        } else {
          config.adapter = () => Promise.reject({
            response: {
              data: { code: 10001, message: result.message },
              status: 401
            }
          })
        }
        return config
      }

      // 注册 Mock
      if (url.includes('/auth/register') && method === 'POST') {
        const { email, username, password } = config.data
        const result = mockRegister(email, username, password)
        
        if (result.success) {
          config.adapter = () => Promise.resolve({
            data: { code: 200, message: 'success', data: result.data },
            status: 200,
            statusText: 'OK',
            headers: {},
            config
          } as AxiosResponse)
        }
        return config
      }

      // 获取当前用户信息 Mock
      if (url.includes('/auth/me') && method === 'GET') {
        config.adapter = () => Promise.resolve({
          data: { code: 200, message: 'success', data: mockUser },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 获取文件列表 Mock
      if (url.includes('/files') && method === 'GET' && !url.includes('/files/')) {
        // 获取 folderId 参数 - 兼容相对URL和完整URL
        let folderId = 'root'
        
        // 调试：打印URL和params
        console.log('[Mock] 获取文件列表 - URL:', url)
        console.log('[Mock] config.params:', config.params)
        
        // 优先从 config.params 获取（这是axios传参的正确方式）
        if (config.params && config.params.folderId) {
          folderId = config.params.folderId
        } else {
          // fallback: 从URL解析
          try {
            const fullUrl = url.startsWith('http') ? url : `${config.baseURL}${url}`
            const urlObj = new URL(fullUrl)
            folderId = urlObj.searchParams.get('folderId') || 'root'
          } catch (e) {
            // 如果URL解析失败，尝试正则匹配
            const match = url.match(/folderId=([^&]+)/)
            if (match) folderId = match[1]
          }
        }
        
        console.log('[Mock] 使用 folderId:', folderId)
        
        // 根据 parentId 过滤文件，并排除已删除的
        const filteredFiles = mockFiles.filter(file => 
          file.parentId === folderId && !file.isDeleted
        )
        
        console.log('[Mock] 过滤后文件数:', filteredFiles.length, '文件:', filteredFiles.map(f => f.name))
        
        config.adapter = () => Promise.resolve({
          data: {
            code: 200,
            message: 'success',
            data: {
              items: filteredFiles,
              total: filteredFiles.length,
              page: 1,
              pageSize: 20
            }
          },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 创建文件夹 Mock
      if (url.includes('/folders') && method === 'POST') {
        const { name, parentId = 'root' } = config.data
        
        // 构建完整路径
        let fullPath = `/root/${name}`
        if (parentId !== 'root') {
          const parentFolder = mockFiles.find(f => f.id === parentId)
          if (parentFolder) {
            fullPath = `${parentFolder.path}/${name}`
          }
        }
        
        const newFolder = {
          id: 'folder-' + Date.now(),
          name,
          type: 'folder' as const,
          path: fullPath,
          parentId, // ✨ 使用传入的parentId
          isStarred: false,
          isDeleted: false,
          fileCount: 0,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        }
        mockFiles.push(newFolder)
        saveFilesToStorage(mockFiles) // 保存到 localStorage
        
        console.log('[Mock] 创建文件夹:', name, '父文件夹ID:', parentId)
        
        config.adapter = () => Promise.resolve({
          data: { code: 200, message: 'success', data: newFolder },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 重命名文件/文件夹 Mock
      if (url.match(/\/files\/[\w-]+$/) && method === 'PUT') {
        const fileId = url.split('/files/')[1].split('?')[0]
        const { name: newName } = config.data
        const file = mockFiles.find(f => f.id === fileId)
        
        if (file && newName) {
          const oldName = file.name
          file.name = newName
          file.updatedAt = new Date().toISOString()
          
          // 更新路径
          if (file.path) {
            const pathParts = file.path.split('/')
            pathParts[pathParts.length - 1] = newName
            file.path = pathParts.join('/')
          }
          
          saveFilesToStorage(mockFiles) // 保存到 localStorage
          
          console.log('[Mock] 重命名文件:', oldName, '→', newName)
          
          config.adapter = () => Promise.resolve({
            data: { code: 200, message: 'success', data: file },
            status: 200,
            statusText: 'OK',
            headers: {},
            config
          } as AxiosResponse)
        } else {
          config.adapter = () => Promise.reject({
            response: {
              data: { code: 10001, message: '文件不存在或名称无效' },
              status: 404
            }
          })
        }
        return config
      }

      // 删除文件 Mock - 标记为已删除而不是真删除
      if (url.match(/\/files\/[\w-]+$/) && method === 'DELETE') {
        const fileId = url.split('/files/')[1].split('?')[0]
        const file = mockFiles.find(f => f.id === fileId)
        if (file) {
          file.isDeleted = true
          file.deletedAt = new Date().toISOString()
          saveFilesToStorage(mockFiles) // 保存到 localStorage
        }
        
        config.adapter = () => Promise.resolve({
          data: { code: 200, message: 'success', data: null },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 回收站列表 Mock - 只返回已删除的文件
      if (url.includes('/trash') && method === 'GET') {
        const deletedFiles = mockFiles.filter(file => file.isDeleted === true)
        
        config.adapter = () => Promise.resolve({
          data: {
            code: 200,
            message: 'success',
            data: { 
              items: deletedFiles, 
              total: deletedFiles.length, 
              page: 1, 
              pageSize: 20 
            }
          },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 恢复文件 Mock
      if (url.includes('/trash/') && url.includes('/restore') && method === 'POST') {
        const fileId = url.split('/trash/')[1].split('/restore')[0]
        const file = mockFiles.find(f => f.id === fileId)
        if (file) {
          file.isDeleted = false
          delete file.deletedAt
          saveFilesToStorage(mockFiles) // 保存到 localStorage
        }
        
        config.adapter = () => Promise.resolve({
          data: { code: 200, message: 'success', data: null },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 永久删除 Mock
      if (url.includes('/trash/') && method === 'DELETE') {
        const fileId = url.split('/trash/')[1].split('?')[0]
        const fileIndex = mockFiles.findIndex(f => f.id === fileId)
        if (fileIndex >= 0) {
          mockFiles.splice(fileIndex, 1)
          saveFilesToStorage(mockFiles) // 保存到 localStorage
        }
        
        config.adapter = () => Promise.resolve({
          data: { code: 200, message: 'success', data: null },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 清空回收站 Mock
      if (url.includes('/trash/clear') && method === 'POST') {
        const deletedIndices = []
        for (let i = mockFiles.length - 1; i >= 0; i--) {
          if (mockFiles[i].isDeleted) {
            mockFiles.splice(i, 1)
          }
        }
        saveFilesToStorage(mockFiles) // 保存到 localStorage

        config.adapter = () => Promise.resolve({
          data: { code: 200, message: 'success', data: null },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 分享列表 Mock
      if (url.includes('/shares') && method === 'GET') {
        config.adapter = () => Promise.resolve({
          data: {
            code: 200,
            message: 'success',
            data: { items: [], total: 0, page: 1, pageSize: 20 }
          },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 分片上传 Mock
      if (url.includes('/chunk-upload') && method === 'POST') {
        const formData = config.data as FormData
        const chunkIndex = formData.get('chunkIndex')
        const totalChunks = formData.get('totalChunks')
        const hash = formData.get('hash') as string
        
        console.log('[Mock] 分片上传:', chunkIndex, '/', totalChunks)
        
        config.adapter = () => Promise.resolve({
          data: {
            code: 200,
            message: 'success',
            data: {
              uploaded: true,
              chunkIndex: Number(chunkIndex),
              hash
            }
          },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 合并分片 Mock
      if (url.includes('/merge-chunks') && method === 'POST') {
        const { fileName, hash, totalChunks, folderId = 'root' } = config.data
        
        // 构建完整路径
        let fullPath = `/root/${fileName}`
        if (folderId !== 'root') {
          const parentFolder = mockFiles.find(f => f.id === folderId)
          if (parentFolder) {
            fullPath = `${parentFolder.path}/${fileName}`
          }
        }
        
        // 创建新文件对象
        const newFile = {
          id: 'file-' + Date.now(),
          name: fileName,
          type: 'file' as const,
          mimeType: 'application/octet-stream',
          size: Math.floor(Math.random() * 10000000) + 1000000, // 模拟大小
          path: fullPath,
          parentId: folderId,
          hash: hash,
          isStarred: false,
          isDeleted: false,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        }
        
        // 添加到mock数据
        mockFiles.push(newFile)
        saveFilesToStorage(mockFiles) // 保存到 localStorage
        
        console.log('[Mock] 分片合并成功:', fileName, '添加到文件夹:', folderId)
        
        config.adapter = () => Promise.resolve({
          data: {
            code: 200,
            message: 'success',
            data: newFile
          },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 上传文件 Mock - 支持读取文件内容
      if (url.includes('/files/upload') && method === 'POST') {
        console.log('[Mock] 上传文件请求')
        
        // 从FormData中提取文件信息
        const formData = config.data
        let fileName = 'uploaded-file.txt'
        let fileSize = 1024
        let mimeType = 'application/octet-stream'
        let actualFile: File | null = null
        
        // 尝试从FormData提取信息
        if (formData && formData instanceof FormData) {
          const file = formData.get('file')
          if (file && file instanceof File) {
            fileName = file.name
            fileSize = file.size
            mimeType = file.type
            actualFile = file
          }
        }
        
        // 获取目标文件夹ID - 优先从FormData获取
        let folderId = 'root'
        if (formData && formData instanceof FormData) {
          const folderIdFromForm = formData.get('folderId')
          if (folderIdFromForm) {
            folderId = folderIdFromForm.toString()
          }
        }
        
        // 异步读取文件内容
        config.adapter = async () => {
          let base64Content: string | undefined = undefined
          
          // 读取文件内容（转为base64）
          if (actualFile) {
            try {
              base64Content = await readFileAsBase64(actualFile)
              console.log('[Mock] 文件内容已读取，大小:', (base64Content.length / 1024).toFixed(2), 'KB')
            } catch (error) {
              console.warn('[Mock] 读取文件内容失败:', error)
            }
          }
          
          // 构建完整路径
          let fullPath = `/root/${fileName}`
          if (folderId !== 'root') {
            const parentFolder = mockFiles.find(f => f.id === folderId)
            if (parentFolder) {
              fullPath = `${parentFolder.path}/${fileName}`
            }
          }
          
          // 创建新文件对象
          const newFile = {
            id: 'file-' + Date.now(),
            name: fileName,
            type: 'file' as const,
            mimeType: mimeType,
            size: fileSize,
            path: fullPath,
            parentId: folderId,
            hash: 'sha256-' + Date.now(),
            isStarred: false,
            isDeleted: false,
            base64Content, // ✨ 保存文件内容
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString()
          }
          
          // 添加到mockFiles
          mockFiles.push(newFile)
          saveFilesToStorage(mockFiles) // 保存到 localStorage
          
          console.log('[Mock] 文件上传成功:', fileName, '添加到文件夹:', folderId)
          
          return Promise.resolve({
            data: {
              code: 200,
              message: 'success',
              data: {
                id: newFile.id,
                name: newFile.name,
                size: newFile.size,
                url: `/files/${newFile.id}`
              }
            },
            status: 200,
            statusText: 'OK',
            headers: {},
            config
          } as AxiosResponse)
        }
        return config
      }

      // 分片上传 Mock
      if (url.includes('/chunk') && method === 'POST') {
        console.log('[Mock] 分片上传')
        
        config.adapter = () => Promise.resolve({
          data: {
            code: 200,
            message: 'success',
            data: {
              chunkIndex: config.data?.chunkIndex || 0,
              uploaded: true
            }
          },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 合并分片 Mock
      if (url.includes('/merge') && method === 'POST') {
        console.log('[Mock] 合并分片')
        
        const { fileName, fileSize, totalChunks, hash, folderId } = config.data || {}
        
        const newFile = {
          id: 'file-' + Date.now(),
          name: fileName || 'merged-file.bin',
          type: 'file' as const,
          mimeType: 'application/octet-stream',
          size: fileSize || 1024000,
          path: `/root/${fileName}`,
          parentId: folderId || 'root',
          hash: hash || 'sha256-' + Date.now(),
          isStarred: false,
          isDeleted: false,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        }
        
        mockFiles.push(newFile)
        saveFilesToStorage(mockFiles) // 保存到 localStorage
        
        console.log('[Mock] 分片合并成功:', fileName)

        config.adapter = () => Promise.resolve({
          data: {
            code: 200,
            message: 'success',
            data: {
              id: newFile.id,
              name: newFile.name,
              size: newFile.size,
              url: `/files/${newFile.id}`
            }
          },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 存储统计 Mock
      if (url.includes('/storage/stats') && method === 'GET') {
        config.adapter = () => Promise.resolve({
          data: {
            code: 200,
            message: 'success',
            data: {
              totalUsed: 2147483648,
              totalQuota: 10737418240,
              fileCount: 3,
              folderCount: 2,
              typeStats: [
                { type: 'PDF', count: 1, size: 2048000 },
                { type: 'Image', count: 1, size: 1024000 },
                { type: 'Video', count: 1, size: 52428800 }
              ]
            }
          },
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        } as AxiosResponse)
        return config
      }

      // 其他请求继续正常发送
      return config
    },
    (error) => {
      return Promise.reject(error)
    }
  )
}

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 添加 Token
    const authStore = useAuthStore()
    if (authStore.token) {
      config.headers.Authorization = `Bearer ${authStore.token}`
    }
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data

    // 如果返回的状态码不是 200，则认为是错误
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')

      // 401: Token过期或未登录
      if (res.code === 401) {
        const authStore = useAuthStore()
        authStore.logout()
        window.location.href = '/login'
      }

      return Promise.reject(new Error(res.message || '请求失败'))
    }

    return res
  },
  (error) => {
    console.error('响应错误:', error)

    // 处理网络错误
    if (error.response) {
      const { status, data } = error.response

      switch (status) {
        case 400:
          ElMessage.error(data.message || '请求参数错误')
          break
        case 401:
          ElMessage.error('登录已过期，请重新登录')
          const authStore = useAuthStore()
          authStore.logout()
          window.location.href = '/login'
          break
        case 403:
          ElMessage.error('没有权限访问该资源')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 413:
          ElMessage.error('文件过大')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(data.message || '请求失败')
      }
    } else if (error.request) {
      ElMessage.error('网络连接失败，请检查网络')
    } else {
      ElMessage.error('请求配置错误')
    }

    return Promise.reject(error)
  }
)

// 封装 GET 请求
export function get<T = any>(url: string, params?: any, config?: AxiosRequestConfig): Promise<T> {
  return request.get(url, { params, ...config })
}

// 封装 POST 请求
export function post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
  return request.post(url, data, config)
}

// 封装 PUT 请求
export function put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
  return request.put(url, data, config)
}

// 封装 DELETE 请求
export function del<T = any>(url: string, params?: any, config?: AxiosRequestConfig): Promise<T> {
  return request.delete(url, { params, ...config })
}

// 导出原始实例（用于特殊场景）
export default request

