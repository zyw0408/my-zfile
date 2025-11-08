// 文件管理状态

import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getFileList, deleteFile, renameFile, moveFile } from '@/api/file'
import { createFolder, deleteFolder, renameFolder } from '@/api/folder'
import type { FileItem, PageResponse } from '@/api/types'
import { ElMessage } from 'element-plus'

interface PathItem {
  id: string
  name: string
}

export const useFileStore = defineStore('file', () => {
  // 状态
  const files = ref<FileItem[]>([])
  const currentFolder = ref<string>('root')
  const currentPath = ref<PathItem[]>([]) // 当前路径
  const total = ref<number>(0)
  const page = ref<number>(1)
  const pageSize = ref<number>(20)
  const loading = ref<boolean>(false)
  const viewMode = ref<'list' | 'grid'>('list') // 视图模式
  const sortBy = ref<string>('name')
  const sortOrder = ref<'asc' | 'desc'>('asc')

  /**
   * 加载文件列表
   */
  async function loadFiles(folderId: string = 'root', folderName?: string) {
    try {
      loading.value = true
      const res = await getFileList({
        folderId,
        page: page.value,
        pageSize: pageSize.value,
        sortBy: sortBy.value,
        order: sortOrder.value
      })

      files.value = res.data.items
      total.value = res.data.total
      currentFolder.value = folderId

      // 更新路径
      updatePath(folderId, folderName)
    } catch (error) {
      console.error('加载文件列表失败:', error)
      ElMessage.error('加载文件列表失败')
    } finally {
      loading.value = false
    }
  }

  /**
   * 更新当前路径
   */
  function updatePath(folderId: string, folderName?: string) {
    if (folderId === 'root') {
      currentPath.value = []
    } else {
      // 查找是否已经在路径中
      const existingIndex = currentPath.value.findIndex(p => p.id === folderId)
      
      if (existingIndex >= 0) {
        // 如果已存在，截断到该位置（实现返回上级）
        currentPath.value = currentPath.value.slice(0, existingIndex + 1)
      } else {
        // 添加新路径
        currentPath.value.push({
          id: folderId,
          name: folderName || '未命名文件夹'
        })
      }
    }
  }

  /**
   * 导航到指定文件夹
   */
  function navigateToFolder(folderId: string) {
    if (folderId === 'root') {
      currentPath.value = []
      loadFiles('root')
    } else {
      // 查找路径中的位置
      const pathIndex = currentPath.value.findIndex(p => p.id === folderId)
      if (pathIndex >= 0) {
        // 截断路径并加载
        currentPath.value = currentPath.value.slice(0, pathIndex + 1)
        loadFiles(folderId)
      }
    }
  }

  /**
   * 刷新当前文件夹
   */
  function refresh() {
    loadFiles(currentFolder.value)
  }

  /**
   * 创建新文件夹
   */
  async function createNewFolder(name: string) {
    try {
      await createFolder(name, currentFolder.value)
      ElMessage.success('创建文件夹成功')
      refresh()
      return true
    } catch (error) {
      console.error('创建文件夹失败:', error)
      return false
    }
  }

  /**
   * 删除文件或文件夹
   */
  async function deleteItem(item: FileItem) {
    try {
      if (item.type === 'file') {
        await deleteFile(item.id)
      } else {
        await deleteFolder(item.id)
      }
      ElMessage.success('已移入回收站')
      refresh()
      return true
    } catch (error) {
      console.error('删除失败:', error)
      return false
    }
  }

  /**
   * 重命名文件或文件夹
   */
  async function renameItem(item: FileItem, newName: string) {
    try {
      if (item.type === 'file') {
        await renameFile(item.id, newName)
      } else {
        await renameFolder(item.id, newName)
      }
      ElMessage.success('重命名成功')
      refresh()
      return true
    } catch (error) {
      console.error('重命名失败:', error)
      return false
    }
  }

  /**
   * 移动文件
   */
  async function moveItem(itemId: string, targetFolderId: string) {
    try {
      await moveFile(itemId, targetFolderId)
      ElMessage.success('移动成功')
      refresh()
      return true
    } catch (error) {
      console.error('移动失败:', error)
      return false
    }
  }

  /**
   * 切换视图模式
   */
  function toggleViewMode() {
    viewMode.value = viewMode.value === 'list' ? 'grid' : 'list'
  }

  /**
   * 设置排序
   */
  function setSort(field: string, order: 'asc' | 'desc') {
    sortBy.value = field
    sortOrder.value = order
    refresh()
  }

  /**
   * 分页变化
   */
  function handlePageChange(newPage: number) {
    page.value = newPage
    loadFiles(currentFolder.value)
  }

  /**
   * 每页数量变化
   */
  function handlePageSizeChange(newSize: number) {
    pageSize.value = newSize
    page.value = 1
    loadFiles(currentFolder.value)
  }

  return {
    files,
    currentFolder,
    currentPath,
    total,
    page,
    pageSize,
    loading,
    viewMode,
    sortBy,
    sortOrder,
    loadFiles,
    refresh,
    createNewFolder,
    deleteItem,
    renameItem,
    moveItem,
    toggleViewMode,
    setSort,
    handlePageChange,
    handlePageSizeChange,
    navigateToFolder
  }
})

