// 常量定义

/**
 * 文件上传相关常量
 */
export const UPLOAD_CONFIG = {
  MAX_SIZE: 10 * 1024 * 1024 * 1024, // 10GB
  CHUNK_SIZE: 5 * 1024 * 1024, // 5MB 分片大小
  MAX_CONCURRENT_CHUNKS: 3, // 最大并发分片数
  LARGE_FILE_THRESHOLD: 100 * 1024 * 1024 // 100MB 以上使用分片上传
}

/**
 * 分页配置
 */
export const PAGE_CONFIG = {
  DEFAULT_PAGE_SIZE: 20,
  PAGE_SIZE_OPTIONS: [10, 20, 50, 100]
}

/**
 * 错误码映射
 */
export const ERROR_CODES = {
  SUCCESS: 200,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  CONFLICT: 409,
  PAYLOAD_TOO_LARGE: 413,
  INTERNAL_SERVER_ERROR: 500,
  
  // 业务错误码
  INVALID_CREDENTIALS: 10001,
  USER_ALREADY_EXISTS: 10002,
  VERIFICATION_CODE_ERROR: 10003,
  TOKEN_EXPIRED: 10004,
  FILE_NOT_FOUND: 20001,
  STORAGE_QUOTA_EXCEEDED: 20002,
  DUPLICATE_FILE_NAME: 20003,
  UNSUPPORTED_FILE_TYPE: 20004,
  FILE_UPLOAD_FAILED: 20005,
  SHARE_EXPIRED: 30001,
  SHARE_PASSWORD_ERROR: 30002
}

/**
 * 文件类型 MIME 映射
 */
export const MIME_TYPES = {
  // 图片
  image: ['image/jpeg', 'image/png', 'image/gif', 'image/bmp', 'image/webp', 'image/svg+xml'],
  // 视频
  video: ['video/mp4', 'video/avi', 'video/mkv', 'video/mov', 'video/wmv', 'video/flv', 'video/webm'],
  // 音频
  audio: ['audio/mpeg', 'audio/wav', 'audio/flac', 'audio/aac', 'audio/ogg'],
  // 文档
  document: [
    'application/pdf',
    'application/msword',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    'application/vnd.ms-excel',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    'application/vnd.ms-powerpoint',
    'application/vnd.openxmlformats-officedocument.presentationml.presentation',
    'text/plain'
  ],
  // 压缩包
  archive: [
    'application/zip',
    'application/x-rar-compressed',
    'application/x-7z-compressed',
    'application/x-tar',
    'application/gzip'
  ]
}

/**
 * 本地存储 Key
 */
export const STORAGE_KEYS = {
  ACCESS_TOKEN: 'access_token',
  REFRESH_TOKEN: 'refresh_token',
  USER_INFO: 'user_info',
  VIEW_MODE: 'view_mode',
  SORT_BY: 'sort_by',
  SORT_ORDER: 'sort_order'
}

/**
 * 分享链接有效期选项
 */
export const SHARE_EXPIRE_OPTIONS = [
  { label: '1天', value: 1 },
  { label: '7天', value: 7 },
  { label: '30天', value: 30 },
  { label: '永久', value: 0 }
]

