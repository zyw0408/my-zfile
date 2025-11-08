# 个人网盘系统 - 前端

基于 Vue 3 + TypeScript + Vite + Element Plus 构建的个人网盘前端项目。

## 技术栈

- **框架**: Vue 3 (Composition API)
- **语言**: TypeScript
- **构建工具**: Vite
- **UI 组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP 客户端**: Axios
- **日期处理**: Day.js

## 项目结构

```
src/
├── api/                    # API 请求封装
│   ├── request.ts         # Axios 实例配置
│   ├── auth.ts            # 认证相关 API
│   ├── file.ts            # 文件管理 API
│   ├── folder.ts          # 文件夹管理 API
│   ├── share.ts           # 分享管理 API
│   ├── trash.ts           # 回收站 API
│   ├── storage.ts         # 存储统计 API
│   └── types.ts           # API 类型定义
├── components/            # 公共组件
│   └── Layout/            # 主布局组件
├── views/                 # 页面组件
│   ├── Login/             # 登录页
│   ├── Register/          # 注册页
│   ├── Home/              # 主页（文件列表）
│   ├── Trash/             # 回收站
│   ├── Share/             # 我的分享
│   ├── Storage/           # 存储管理
│   ├── Settings/          # 个人设置
│   └── Admin/             # 系统管理
├── stores/                # 状态管理 (Pinia)
│   ├── auth.ts            # 认证状态
│   └── file.ts            # 文件状态
├── composables/           # 组合式函数
│   └── useUpload.ts       # 文件上传逻辑
├── utils/                 # 工具函数
│   ├── file.ts            # 文件处理工具
│   ├── format.ts          # 格式化工具
│   └── constants.ts       # 常量定义
├── router/                # 路由配置
│   └── index.ts
├── App.vue                # 根组件
└── main.ts                # 入口文件
```

## 功能特性

### 已实现

- ✅ 用户注册与登录
- ✅ 文件列表展示（表格/网格视图）
- ✅ 文件上传（支持普通上传和分片上传）
- ✅ 文件下载
- ✅ 文件/文件夹操作（重命名、删除、移动）
- ✅ 新建文件夹
- ✅ 回收站管理
- ✅ 分享管理
- ✅ 存储空间统计
- ✅ 个人设置
- ✅ 响应式布局

### 待完善

- ⏳ 文件预览（图片、视频、PDF）
- ⏳ 高级搜索功能
- ⏳ 分享链接生成与访问
- ⏳ 批量操作
- ⏳ 文件拖拽排序
- ⏳ 系统管理功能（管理员）

## 环境要求

- Node.js >= 18.0.0
- npm >= 9.0.0

## 快速开始

### 1. 安装依赖

```bash
npm install
```

### 2. 配置环境变量

创建 `.env.development` 文件：

```bash
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_TITLE=个人网盘系统
```

### 3. 启动开发服务器

```bash
npm run dev
```

项目将运行在 http://localhost:5173

### 4. 构建生产版本

```bash
npm run build
```

### 5. 预览生产构建

```bash
npm run preview
```

## API 对接说明

前端默认连接后端地址：`http://localhost:8080`

确保后端服务已启动，并且 CORS 配置正确。

### API 端点

所有 API 请求都会自动添加 `/api/v1` 前缀，例如：

- `POST /api/v1/auth/login` - 登录
- `GET /api/v1/files` - 获取文件列表
- `POST /api/v1/files/upload` - 上传文件

详细 API 文档请参考后端项目的 Swagger 文档。

## 开发规范

### 代码风格

- 使用 TypeScript 类型定义
- 遵循 Vue 3 Composition API 风格
- 使用 ESLint + Prettier 格式化代码

### 组件命名

- 页面组件：`views/功能名/index.vue`
- 公共组件：`components/组件名/index.vue`

### 状态管理

- 使用 Pinia 管理全局状态
- 每个模块对应一个 store 文件

### API 调用

- 所有 API 调用统一通过 `src/api` 目录
- 使用 TypeScript 类型定义请求和响应

## 常见问题

### 1. 跨域问题

前端已配置代理：

```typescript
// vite.config.ts
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true
  }
}
```

### 2. Token 失效

Token 失效会自动跳转到登录页，刷新页面即可。

### 3. 大文件上传

超过 100MB 的文件会自动使用分片上传，单个文件最大支持 10GB。

## License

MIT

## 作者

Personal Drive Team

