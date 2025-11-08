// 文件处理工具函数

/**
 * 格式化文件大小
 */
export function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + ' ' + sizes[i]
}

/**
 * 获取文件扩展名
 */
export function getFileExtension(filename: string): string {
  const lastDot = filename.lastIndexOf('.')
  return lastDot > 0 ? filename.substring(lastDot + 1).toLowerCase() : ''
}

/**
 * 获取文件图标类型
 */
export function getFileIcon(filename: string, mimeType?: string): string {
  const ext = getFileExtension(filename)

  // 根据扩展名
  const iconMap: Record<string, string> = {
    // 图片
    jpg: 'Picture',
    jpeg: 'Picture',
    png: 'Picture',
    gif: 'Picture',
    bmp: 'Picture',
    svg: 'Picture',
    webp: 'Picture',
    // 视频
    mp4: 'VideoCamera',
    avi: 'VideoCamera',
    mkv: 'VideoCamera',
    mov: 'VideoCamera',
    wmv: 'VideoCamera',
    flv: 'VideoCamera',
    // 音频
    mp3: 'Headset',
    wav: 'Headset',
    flac: 'Headset',
    aac: 'Headset',
    // 文档
    pdf: 'Document',
    doc: 'Document',
    docx: 'Document',
    xls: 'Document',
    xlsx: 'Document',
    ppt: 'Document',
    pptx: 'Document',
    txt: 'Document',
    // 压缩包
    zip: 'FolderOpened',
    rar: 'FolderOpened',
    '7z': 'FolderOpened',
    tar: 'FolderOpened',
    gz: 'FolderOpened',
    // 代码
    js: 'Document',
    ts: 'Document',
    vue: 'Document',
    html: 'Document',
    css: 'Document',
    json: 'Document',
    md: 'Document'
  }

  return iconMap[ext] || 'Document'
}

/**
 * 判断是否为图片
 */
export function isImage(filename: string, mimeType?: string): boolean {
  if (mimeType) {
    return mimeType.startsWith('image/')
  }
  const ext = getFileExtension(filename)
  return ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'svg', 'webp'].includes(ext)
}

/**
 * 判断是否为视频
 */
export function isVideo(filename: string, mimeType?: string): boolean {
  if (mimeType) {
    return mimeType.startsWith('video/')
  }
  const ext = getFileExtension(filename)
  return ['mp4', 'avi', 'mkv', 'mov', 'wmv', 'flv', 'webm'].includes(ext)
}

/**
 * 判断是否为音频
 */
export function isAudio(filename: string, mimeType?: string): boolean {
  if (mimeType) {
    return mimeType.startsWith('audio/')
  }
  const ext = getFileExtension(filename)
  return ['mp3', 'wav', 'flac', 'aac', 'ogg'].includes(ext)
}

/**
 * 判断是否可预览
 */
export function isPreviewable(filename: string, mimeType?: string): boolean {
  return isImage(filename, mimeType) || isVideo(filename, mimeType) || isAudio(filename, mimeType) || getFileExtension(filename) === 'pdf'
}

/**
 * 获取文件类型分类
 */
export function getFileCategory(filename: string, mimeType?: string): string {
  if (isImage(filename, mimeType)) return 'image'
  if (isVideo(filename, mimeType)) return 'video'
  if (isAudio(filename, mimeType)) return 'audio'
  
  const ext = getFileExtension(filename)
  if (['pdf', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt'].includes(ext)) return 'document'
  if (['zip', 'rar', '7z', 'tar', 'gz'].includes(ext)) return 'archive'
  if (['js', 'ts', 'vue', 'html', 'css', 'json', 'md', 'java', 'py'].includes(ext)) return 'code'
  
  return 'other'
}

/**
 * 验证文件名
 */
export function validateFileName(filename: string): { valid: boolean; message?: string } {
  if (!filename || filename.trim() === '') {
    return { valid: false, message: '文件名不能为空' }
  }

  if (filename.length > 255) {
    return { valid: false, message: '文件名过长（最多255个字符）' }
  }

  // 不允许的字符
  const invalidChars = /[<>:"/\\|?*]/g
  if (invalidChars.test(filename)) {
    return { valid: false, message: '文件名不能包含以下字符: < > : " / \\ | ? *' }
  }

  // 不允许的文件名
  const reservedNames = ['CON', 'PRN', 'AUX', 'NUL', 'COM1', 'COM2', 'COM3', 'COM4', 'COM5', 'COM6', 'COM7', 'COM8', 'COM9', 'LPT1', 'LPT2', 'LPT3', 'LPT4', 'LPT5', 'LPT6', 'LPT7', 'LPT8', 'LPT9']
  const nameWithoutExt = filename.split('.')[0].toUpperCase()
  if (reservedNames.includes(nameWithoutExt)) {
    return { valid: false, message: '不允许使用系统保留名称' }
  }

  return { valid: true }
}

/**
 * 生成唯一文件名（避免重复）
 */
export function generateUniqueFileName(filename: string, existingNames: string[]): string {
  if (!existingNames.includes(filename)) {
    return filename
  }

  const ext = getFileExtension(filename)
  const nameWithoutExt = ext ? filename.slice(0, -(ext.length + 1)) : filename
  
  let counter = 1
  let newName = `${nameWithoutExt} (${counter})${ext ? '.' + ext : ''}`
  
  while (existingNames.includes(newName)) {
    counter++
    newName = `${nameWithoutExt} (${counter})${ext ? '.' + ext : ''}`
  }
  
  return newName
}

