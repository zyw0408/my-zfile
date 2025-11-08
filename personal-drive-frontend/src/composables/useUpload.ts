// 文件上传组合式函数

import { ref } from 'vue'
import { uploadFile, uploadChunk, mergeChunks } from '@/api/file'
import { UPLOAD_CONFIG } from '@/utils/constants'
import { ElMessage } from 'element-plus'

export interface UploadProgress {
  file: File
  percent: number
  status: 'pending' | 'uploading' | 'success' | 'error'
  error?: string
}

export function useUpload() {
  const uploadList = ref<UploadProgress[]>([])
  const uploading = ref(false)

  /**
   * 普通上传（小文件）
   */
  async function normalUpload(file: File, folderId: string = 'root') {
    const progress: UploadProgress = {
      file,
      percent: 0,
      status: 'uploading'
    }
    uploadList.value.push(progress)

    try {
      const formData = new FormData()
      formData.append('file', file)
      formData.append('folderId', folderId)

      await uploadFile(formData, (percent) => {
        progress.percent = percent
      })

      progress.status = 'success'
      progress.percent = 100
      ElMessage.success(`${file.name} 上传成功`)
      return true
    } catch (error) {
      progress.status = 'error'
      progress.error = error instanceof Error ? error.message : '上传失败'
      ElMessage.error(`${file.name} 上传失败`)
      return false
    }
  }

  /**
   * 分片上传（大文件）
   */
  async function chunkUpload(file: File, folderId: string = 'root') {
    const progress: UploadProgress = {
      file,
      percent: 0,
      status: 'uploading'
    }
    uploadList.value.push(progress)

    try {
      const totalChunks = Math.ceil(file.size / UPLOAD_CONFIG.CHUNK_SIZE)
      const sessionId = crypto.randomUUID()
      let uploadedChunks = 0

      // 上传所有分片
      for (let i = 0; i < totalChunks; i++) {
        const start = i * UPLOAD_CONFIG.CHUNK_SIZE
        const end = Math.min(start + UPLOAD_CONFIG.CHUNK_SIZE, file.size)
        const chunk = file.slice(start, end)

        const formData = new FormData()
        formData.append('chunk', chunk)
        formData.append('sessionId', sessionId)
        formData.append('chunkIndex', i.toString())
        formData.append('totalChunks', totalChunks.toString())

        await uploadChunk(formData)

        uploadedChunks++
        progress.percent = Math.round((uploadedChunks / totalChunks) * 95) // 留5%给合并
      }

      // 合并分片
      await mergeChunks({
        sessionId,
        fileName: file.name,
        folderId
      })

      progress.status = 'success'
      progress.percent = 100
      ElMessage.success(`${file.name} 上传成功`)
      return true
    } catch (error) {
      progress.status = 'error'
      progress.error = error instanceof Error ? error.message : '上传失败'
      ElMessage.error(`${file.name} 上传失败`)
      return false
    }
  }

  /**
   * 智能上传（自动选择普通或分片）
   */
  async function smartUpload(file: File, folderId: string = 'root') {
    uploading.value = true

    try {
      // 检查文件大小
      if (file.size > UPLOAD_CONFIG.MAX_SIZE) {
        ElMessage.error(`文件 ${file.name} 超过最大限制 10GB`)
        return false
      }

      // 大文件使用分片上传
      if (file.size > UPLOAD_CONFIG.LARGE_FILE_THRESHOLD) {
        return await chunkUpload(file, folderId)
      } else {
        return await normalUpload(file, folderId)
      }
    } finally {
      uploading.value = false
    }
  }

  /**
   * 批量上传
   */
  async function batchUpload(files: File[], folderId: string = 'root') {
    uploading.value = true

    try {
      const results = []
      for (const file of files) {
        const result = await smartUpload(file, folderId)
        results.push(result)
      }
      return results
    } finally {
      uploading.value = false
    }
  }

  /**
   * 清除上传列表
   */
  function clearUploadList() {
    uploadList.value = uploadList.value.filter(item => item.status === 'uploading')
  }

  return {
    uploadList,
    uploading,
    smartUpload,
    batchUpload,
    clearUploadList
  }
}

