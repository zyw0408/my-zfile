# 云盘系统开发文档 - API接口文档

## 1. 接口规范

### 1.1 请求规范
- **协议**: HTTPS
- **请求方式**: GET、POST、PUT、DELETE
- **Content-Type**: `application/json`
- **认证方式**: JWT Token (Header: `Authorization: Bearer {token}`)

### 1.2 响应规范

#### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1699000000000
}
```

#### 失败响应
```json
{
  "code": 400,
  "message": "错误信息",
  "data": null,
  "timestamp": 1699000000000
}
```

### 1.3 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未认证 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 2. 认证授权接口

### 2.1 用户注册

**接口地址**: `POST /api/auth/register`

**请求参数**:
```json
{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com",
  "verificationCode": "123456"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": null
}
```

### 2.2 用户登录

**接口地址**: `POST /api/auth/login`

**请求参数**:
```json
{
  "username": "testuser",
  "password": "password123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 7200,
    "tokenType": "Bearer",
    "userInfo": {
      "id": 1,
      "username": "testuser",
      "nickname": "测试用户",
      "avatar": "https://example.com/avatar.jpg",
      "email": "test@example.com",
      "userType": 0
    }
  }
}
```

### 2.3 退出登录

**接口地址**: `POST /api/auth/logout`

**请求头**: `Authorization: Bearer {token}`

**响应示例**:
```json
{
  "code": 200,
  "message": "退出成功"
}
```

### 2.4 刷新Token

**接口地址**: `POST /api/auth/refresh`

**请求参数**:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 7200
  }
}
```

### 2.5 发送验证码

**接口地址**: `POST /api/auth/send-code`

**请求参数**:
```json
{
  "email": "test@example.com",
  "type": "register"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "验证码已发送"
}
```

## 3. 文件管理接口

### 3.1 获取文件列表

**接口地址**: `GET /api/files`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| parentId | Long | 否 | 父文件夹ID，默认0（根目录） |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认20 |
| sortBy | String | 否 | 排序字段：name, size, time |
| sortOrder | String | 否 | 排序方式：asc, desc |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "pageNum": 1,
    "pageSize": 20,
    "totalPages": 5,
    "records": [
      {
        "id": 1,
        "fileName": "文档.pdf",
        "fileSize": 1024000,
        "fileSizeFormat": "1.00 MB",
        "fileType": "pdf",
        "isFolder": false,
        "parentId": 0,
        "thumbnailUrl": "https://example.com/thumb.jpg",
        "createTime": "2025-01-01 10:00:00",
        "updateTime": "2025-01-01 10:00:00"
      }
    ]
  }
}
```

### 3.2 获取文件详情

**接口地址**: `GET /api/files/{fileId}`

**路径参数**:
- `fileId`: 文件ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "fileName": "文档.pdf",
    "filePath": "/documents/文档.pdf",
    "fileSize": 1024000,
    "fileSizeFormat": "1.00 MB",
    "fileType": "pdf",
    "contentType": "application/pdf",
    "fileMd5": "d41d8cd98f00b204e9800998ecf8427e",
    "storageType": "local",
    "isFolder": false,
    "parentId": 0,
    "thumbnailUrl": "https://example.com/thumb.jpg",
    "downloadCount": 10,
    "isPublic": false,
    "createTime": "2025-01-01 10:00:00",
    "updateTime": "2025-01-01 10:00:00"
  }
}
```

### 3.3 上传文件

**接口地址**: `POST /api/files/upload`

**请求方式**: `multipart/form-data`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| file | File | 是 | 文件对象 |
| parentId | Long | 否 | 父文件夹ID，默认0 |

**响应示例**:
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "id": 1,
    "fileName": "文档.pdf",
    "fileSize": 1024000,
    "fileType": "pdf",
    "fileUrl": "https://example.com/files/xxx.pdf"
  }
}
```

### 3.4 分片上传初始化

**接口地址**: `POST /api/files/chunk/init`

**请求参数**:
```json
{
  "fileName": "大文件.mp4",
  "fileSize": 104857600,
  "fileMd5": "d41d8cd98f00b204e9800998ecf8427e",
  "chunkSize": 5242880,
  "parentId": 0
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "uploadId": "upload_123456",
    "chunkCount": 20,
    "needUpload": true,
    "uploadedChunks": []
  }
}
```

### 3.5 上传分片

**接口地址**: `POST /api/files/chunk/upload`

**请求方式**: `multipart/form-data`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| uploadId | String | 是 | 上传ID |
| chunkIndex | Integer | 是 | 分片索引（从0开始） |
| chunk | File | 是 | 分片文件 |

**响应示例**:
```json
{
  "code": 200,
  "message": "分片上传成功"
}
```

### 3.6 合并分片

**接口地址**: `POST /api/files/chunk/merge`

**请求参数**:
```json
{
  "uploadId": "upload_123456",
  "fileName": "大文件.mp4",
  "fileMd5": "d41d8cd98f00b204e9800998ecf8427e"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "文件上传成功",
  "data": {
    "id": 1,
    "fileName": "大文件.mp4",
    "fileSize": 104857600,
    "fileUrl": "https://example.com/files/xxx.mp4"
  }
}
```

### 3.7 秒传检查

**接口地址**: `POST /api/files/sec-upload`

**请求参数**:
```json
{
  "fileMd5": "d41d8cd98f00b204e9800998ecf8427e",
  "fileSize": 1024000,
  "fileName": "文档.pdf",
  "parentId": 0
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "canSecUpload": true,
    "fileInfo": {
      "id": 1,
      "fileName": "文档.pdf",
      "fileSize": 1024000
    }
  }
}
```

### 3.8 下载文件

**接口地址**: `GET /api/files/download/{fileId}`

**路径参数**:
- `fileId`: 文件ID

**响应**: 文件流

### 3.9 批量下载

**接口地址**: `POST /api/files/batch-download`

**请求参数**:
```json
{
  "fileIds": [1, 2, 3]
}
```

**响应**: ZIP文件流

### 3.10 创建文件夹

**接口地址**: `POST /api/files/folder`

**请求参数**:
```json
{
  "folderName": "新建文件夹",
  "parentId": 0
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 10,
    "fileName": "新建文件夹",
    "isFolder": true
  }
}
```

### 3.11 重命名文件

**接口地址**: `PUT /api/files/{fileId}/rename`

**路径参数**:
- `fileId`: 文件ID

**请求参数**:
```json
{
  "newName": "新文件名.pdf"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "重命名成功"
}
```

### 3.12 移动文件

**接口地址**: `PUT /api/files/{fileId}/move`

**请求参数**:
```json
{
  "targetFolderId": 5
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "移动成功"
}
```

### 3.13 复制文件

**接口地址**: `POST /api/files/{fileId}/copy`

**请求参数**:
```json
{
  "targetFolderId": 5
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "复制成功"
}
```

### 3.14 删除文件

**接口地址**: `DELETE /api/files/{fileId}`

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功"
}
```

### 3.15 批量删除

**接口地址**: `POST /api/files/batch-delete`

**请求参数**:
```json
{
  "fileIds": [1, 2, 3]
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "批量删除成功"
}
```

### 3.16 搜索文件

**接口地址**: `GET /api/files/search`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| keyword | String | 是 | 搜索关键词 |
| pageNum | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页数量 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 10,
    "records": [
      {
        "id": 1,
        "fileName": "文档.pdf",
        "filePath": "/documents/文档.pdf",
        "fileSize": 1024000
      }
    ]
  }
}
```

### 3.17 获取文件预览URL

**接口地址**: `GET /api/files/{fileId}/preview`

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "previewUrl": "https://example.com/preview/xxx.pdf",
    "fileType": "pdf",
    "supportPreview": true
  }
}
```

## 4. 分享管理接口

### 4.1 创建分享

**接口地址**: `POST /api/share`

**请求参数**:
```json
{
  "fileId": 1,
  "shareType": 1,
  "sharePassword": "1234",
  "expireDays": 7,
  "downloadLimit": 10
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "shareCode": "abcd1234",
    "shareUrl": "https://example.com/s/abcd1234",
    "sharePassword": "1234",
    "qrCodeUrl": "https://example.com/qrcode/abcd1234.png",
    "expireTime": "2025-01-08 10:00:00"
  }
}
```

### 4.2 获取分享信息

**接口地址**: `GET /api/share/{shareCode}`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| password | String | 否 | 分享密码（需要密码时必填） |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "shareCode": "abcd1234",
    "fileName": "文档.pdf",
    "fileSize": 1024000,
    "fileType": "pdf",
    "createTime": "2025-01-01 10:00:00",
    "expireTime": "2025-01-08 10:00:00",
    "viewCount": 10
  }
}
```

### 4.3 访问分享文件（下载）

**接口地址**: `GET /api/share/{shareCode}/download`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| password | String | 否 | 分享密码 |

**响应**: 文件流

### 4.4 取消分享

**接口地址**: `DELETE /api/share/{shareId}`

**响应示例**:
```json
{
  "code": 200,
  "message": "取消成功"
}
```

### 4.5 我的分享列表

**接口地址**: `GET /api/share/my`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页数量 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 10,
    "records": [
      {
        "id": 1,
        "shareCode": "abcd1234",
        "fileName": "文档.pdf",
        "shareUrl": "https://example.com/s/abcd1234",
        "expireTime": "2025-01-08 10:00:00",
        "viewCount": 10,
        "downloadCount": 5,
        "status": 1,
        "createTime": "2025-01-01 10:00:00"
      }
    ]
  }
}
```

## 5. 用户管理接口

### 5.1 获取用户信息

**接口地址**: `GET /api/user/profile`

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "testuser",
    "nickname": "测试用户",
    "email": "test@example.com",
    "phone": "13800138000",
    "avatar": "https://example.com/avatar.jpg",
    "storageQuota": 10737418240,
    "usedStorage": 1073741824,
    "storageQuotaFormat": "10.00 GB",
    "usedStorageFormat": "1.00 GB",
    "usagePercent": 10.0,
    "createTime": "2025-01-01 10:00:00"
  }
}
```

### 5.2 更新用户信息

**接口地址**: `PUT /api/user/profile`

**请求参数**:
```json
{
  "nickname": "新昵称",
  "email": "newemail@example.com",
  "phone": "13800138001"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功"
}
```

### 5.3 修改密码

**接口地址**: `PUT /api/user/password`

**请求参数**:
```json
{
  "oldPassword": "oldpass123",
  "newPassword": "newpass123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "密码修改成功"
}
```

### 5.4 上传头像

**接口地址**: `POST /api/user/avatar`

**请求方式**: `multipart/form-data`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| avatar | File | 是 | 头像文件 |

**响应示例**:
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "avatarUrl": "https://example.com/avatar/xxx.jpg"
  }
}
```

### 5.5 获取存储统计

**接口地址**: `GET /api/user/storage-stats`

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalFiles": 100,
    "totalFolders": 10,
    "totalSize": 1073741824,
    "imageCount": 50,
    "videoCount": 10,
    "documentCount": 30,
    "otherCount": 10,
    "recentUploadCount": 5
  }
}
```

## 6. 管理后台接口

### 6.1 用户管理

#### 6.1.1 获取用户列表

**接口地址**: `GET /api/admin/users`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| keyword | String | 否 | 搜索关键词 |
| status | Integer | 否 | 用户状态 |
| pageNum | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页数量 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "records": [
      {
        "id": 1,
        "username": "testuser",
        "nickname": "测试用户",
        "email": "test@example.com",
        "userType": 0,
        "status": 1,
        "storageQuota": 10737418240,
        "usedStorage": 1073741824,
        "lastLoginTime": "2025-01-01 10:00:00",
        "createTime": "2025-01-01 10:00:00"
      }
    ]
  }
}
```

#### 6.1.2 创建用户

**接口地址**: `POST /api/admin/users`

**请求参数**:
```json
{
  "username": "newuser",
  "password": "password123",
  "email": "newuser@example.com",
  "nickname": "新用户",
  "storageQuota": 10737418240
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 100
  }
}
```

#### 6.1.3 更新用户

**接口地址**: `PUT /api/admin/users/{userId}`

**请求参数**:
```json
{
  "nickname": "新昵称",
  "status": 1,
  "storageQuota": 21474836480
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功"
}
```

#### 6.1.4 删除用户

**接口地址**: `DELETE /api/admin/users/{userId}`

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功"
}
```

### 6.2 文件管理

#### 6.2.1 获取所有文件

**接口地址**: `GET /api/admin/files`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| keyword | String | 否 | 搜索关键词 |
| userId | Long | 否 | 用户ID |
| fileType | String | 否 | 文件类型 |
| startDate | String | 否 | 开始日期 |
| endDate | String | 否 | 结束日期 |
| pageNum | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页数量 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 1000,
    "records": [
      {
        "id": 1,
        "fileName": "文档.pdf",
        "fileSize": 1024000,
        "fileType": "pdf",
        "userId": 1,
        "username": "testuser",
        "downloadCount": 10,
        "createTime": "2025-01-01 10:00:00"
      }
    ]
  }
}
```

#### 6.2.2 删除文件

**接口地址**: `DELETE /api/admin/files/{fileId}`

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功"
}
```

### 6.3 存储源管理

#### 6.3.1 获取存储源列表

**接口地址**: `GET /api/admin/storage`

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "storageName": "本地存储",
      "storageType": "local",
      "isDefault": true,
      "status": 1,
      "basePath": "/data/upload",
      "createTime": "2025-01-01 10:00:00"
    }
  ]
}
```

#### 6.3.2 创建存储源

**接口地址**: `POST /api/admin/storage`

**请求参数**:
```json
{
  "storageName": "阿里云OSS",
  "storageType": "aliyun_oss",
  "isDefault": false,
  "endpoint": "oss-cn-hangzhou.aliyuncs.com",
  "bucketName": "my-bucket",
  "accessKey": "your-access-key",
  "secretKey": "your-secret-key",
  "basePath": "/upload",
  "domain": "https://cdn.example.com"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功"
}
```

#### 6.3.3 更新存储源

**接口地址**: `PUT /api/admin/storage/{storageId}`

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功"
}
```

#### 6.3.4 删除存储源

**接口地址**: `DELETE /api/admin/storage/{storageId}`

**响应示例**:
```json
{
  "code": 200,
  "message": "删除成功"
}
```

### 6.4 系统设置

#### 6.4.1 获取系统配置

**接口地址**: `GET /api/admin/system/config`

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "siteName": "云盘系统",
    "siteDescription": "安全、快速、易用",
    "siteLogo": "/logo.png",
    "uploadMaxSize": 104857600,
    "allowTypes": ["jpg", "png", "pdf"],
    "recycleExpireDays": 30
  }
}
```

#### 6.4.2 更新系统配置

**接口地址**: `PUT /api/admin/system/config`

**请求参数**:
```json
{
  "siteName": "我的云盘",
  "uploadMaxSize": 209715200
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "更新成功"
}
```

#### 6.4.3 获取系统统计

**接口地址**: `GET /api/admin/system/statistics`

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalUsers": 1000,
    "totalFiles": 50000,
    "totalStorage": 107374182400,
    "todayUpload": 100,
    "todayDownload": 500,
    "onlineUsers": 50
  }
}
```

#### 6.4.4 获取操作日志

**接口地址**: `GET /api/admin/system/logs`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 否 | 用户ID |
| operationType | String | 否 | 操作类型 |
| startDate | String | 否 | 开始日期 |
| endDate | String | 否 | 结束日期 |
| pageNum | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页数量 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 10000,
    "records": [
      {
        "id": 1,
        "userId": 1,
        "username": "testuser",
        "operationType": "upload",
        "operationDesc": "上传文件：文档.pdf",
        "ipAddress": "192.168.1.100",
        "status": 1,
        "createTime": "2025-01-01 10:00:00"
      }
    ]
  }
}
```

## 7. 错误码说明

| 错误码 | 说明 |
|--------|------|
| 1001 | 用户名已存在 |
| 1002 | 邮箱已被注册 |
| 1003 | 用户名或密码错误 |
| 1004 | Token已过期 |
| 1005 | Token无效 |
| 2001 | 文件不存在 |
| 2002 | 文件名重复 |
| 2003 | 存储空间不足 |
| 2004 | 文件类型不允许 |
| 2005 | 文件大小超限 |
| 2006 | 文件上传失败 |
| 3001 | 分享不存在 |
| 3002 | 分享已过期 |
| 3003 | 分享密码错误 |
| 3004 | 下载次数已用完 |
| 4001 | 无权限访问 |
| 4002 | 操作被禁止 |

## 8. Swagger文档访问

访问地址: `http://localhost:8080/api/doc.html`

在开发环境下，可以通过Knife4j提供的可视化界面测试所有API接口。

