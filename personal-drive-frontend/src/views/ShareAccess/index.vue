<template>
  <div class="share-access-container">
    <el-card class="share-card">
      <template #header>
        <div class="header">
          <el-icon :size="24"><Share /></el-icon>
          <span style="margin-left: 10px">{{ pageTitle }}</span>
        </div>
      </template>

      <!-- 输入提取码 -->
      <div v-if="!verified" class="verify-section">
        <div class="share-info">
          <el-icon :size="60" color="#409eff"><FolderOpened /></el-icon>
          <h2 style="margin: 20px 0 10px">{{ shareInfo?.fileName || '加载中...' }}</h2>
          <p class="meta-info">分享者：{{ shareInfo?.ownerName || '-' }}</p>
          <p class="meta-info">分享时间：{{ formatDateTime(shareInfo?.createdAt || '') }}</p>
          <p v-if="shareInfo?.expireAt" class="meta-info">
            有效期至：{{ formatDateTime(shareInfo.expireAt) }}
          </p>
        </div>

        <el-form v-if="shareInfo?.needPassword" @submit.prevent="handleVerify" style="margin-top: 30px">
          <el-form-item label="提取码">
            <el-input
              v-model="password"
              placeholder="请输入4位提取码"
              maxlength="4"
              show-word-limit
              clearable
            >
              <template #append>
                <el-button type="primary" @click="handleVerify" :loading="verifying">
                  提取文件
                </el-button>
              </template>
            </el-input>
          </el-form-item>
        </el-form>

        <el-button v-else type="primary" size="large" @click="handleVerify" :loading="verifying" style="width: 100%">
          查看文件
        </el-button>
      </div>

      <!-- 文件详情 -->
      <div v-else class="file-detail">
        <div class="file-preview">
          <el-icon :size="80"><component :is="getFileIcon()" /></el-icon>
          <h2 style="margin: 20px 0">{{ fileDetail?.name }}</h2>
          <div class="file-meta">
            <span>大小：{{ formatFileSize(fileDetail?.size || 0) }}</span>
            <el-divider direction="vertical" />
            <span>修改时间：{{ formatDateTime(fileDetail?.updatedAt || '') }}</span>
          </div>
        </div>

        <div class="actions">
          <el-button type="primary" size="large" @click="handleDownload" :loading="downloading">
            <el-icon><Download /></el-icon>
            下载文件
          </el-button>
          <el-button size="large" @click="handlePreview" v-if="canPreview">
            <el-icon><View /></el-icon>
            在线预览
          </el-button>
        </div>
      </div>

      <!-- 错误提示 -->
      <el-result
        v-if="error"
        icon="error"
        :title="errorTitle"
        :sub-title="errorMessage"
      >
        <template #extra>
          <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
        </template>
      </el-result>
    </el-card>

    <!-- 文件预览 -->
    <FilePreview v-model="showPreview" :file="fileDetail" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Share, FolderOpened, Download, View, Document, Folder, Picture, VideoPlay, Headset } from '@element-plus/icons-vue'
import { accessShare } from '@/api/share'
import { downloadFile } from '@/api/file'
import { formatDateTime } from '@/utils/format'
import { formatFileSize } from '@/utils/file'
import type { FileItem } from '@/api/types'
import FilePreview from '@/components/FilePreview/index.vue'

const route = useRoute()
const router = useRouter()

const shareCode = ref<string>('')
const password = ref<string>('')
const verified = ref(false)
const verifying = ref(false)
const downloading = ref(false)
const error = ref(false)
const errorTitle = ref('')
const errorMessage = ref('')

interface ShareInfo {
  fileName: string
  ownerName: string
  createdAt: string
  expireAt?: string
  needPassword: boolean
}

const shareInfo = ref<ShareInfo | null>(null)
const fileDetail = ref<FileItem | null>(null)

const pageTitle = computed(() => {
  if (error.value) return '分享访问失败'
  if (verified.value) return '分享文件详情'
  return '访问分享文件'
})

// 预览相关
const showPreview = ref(false)

// 判断文件是否可以预览
const canPreview = computed(() => {
  if (!fileDetail.value) return false
  const ext = fileDetail.value.name.split('.').pop()?.toLowerCase()
  return ['jpg', 'jpeg', 'png', 'gif', 'webp', 'mp4', 'mp3', 'pdf', 'txt', 'md'].includes(ext || '')
})

// 获取文件图标
function getFileIcon() {
  if (!fileDetail.value) return Document
  
  const ext = fileDetail.value.name.split('.').pop()?.toLowerCase()
  if (['jpg', 'jpeg', 'png', 'gif', 'webp'].includes(ext || '')) return Picture
  if (['mp4', 'avi', 'mov'].includes(ext || '')) return VideoPlay
  if (['mp3', 'wav', 'flac'].includes(ext || '')) return Headset
  if (fileDetail.value.type === 'folder') return Folder
  
  return Document
}

// 加载分享信息
async function loadShareInfo() {
  try {
    // TODO: 实际环境下应调用 API 获取分享基本信息
    // const res = await getShareInfo(shareCode.value)
    
    // Mock 数据
    shareInfo.value = {
      fileName: '示例文件.pdf',
      ownerName: '演示用户',
      createdAt: new Date().toISOString(),
      expireAt: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString(),
      needPassword: true
    }
  } catch (err) {
    error.value = true
    errorTitle.value = '分享不存在或已过期'
    errorMessage.value = '该分享链接可能已被取消或已过期'
  }
}

// 验证提取码
async function handleVerify() {
  if (shareInfo.value?.needPassword && !password.value) {
    ElMessage.warning('请输入提取码')
    return
  }

  if (shareInfo.value?.needPassword && password.value.length !== 4) {
    ElMessage.warning('提取码必须是4位')
    return
  }

  try {
    verifying.value = true
    const res = await accessShare(shareCode.value, password.value || undefined)
    
    fileDetail.value = res.data
    verified.value = true
    ElMessage.success('验证成功')
  } catch (err: any) {
    if (err.response?.data?.code === 10003) {
      ElMessage.error('提取码错误')
    } else {
      ElMessage.error('验证失败，请重试')
    }
  } finally {
    verifying.value = false
  }
}

// 下载文件
function handleDownload() {
  if (!fileDetail.value) return
  
  downloading.value = true
  const url = downloadFile(fileDetail.value.id)
  window.open(url, '_blank')
  
  setTimeout(() => {
    downloading.value = false
  }, 1000)
}

// 预览文件
function handlePreview() {
  showPreview.value = true
}

onMounted(() => {
  shareCode.value = route.params.code as string
  if (!shareCode.value) {
    error.value = true
    errorTitle.value = '无效的分享链接'
    errorMessage.value = '分享链接格式不正确'
    return
  }
  
  loadShareInfo()
})
</script>

<style scoped>
.share-access-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.share-card {
  width: 100%;
  max-width: 600px;
  min-height: 400px;
}

.header {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 500;
}

.verify-section {
  padding: 20px 0;
}

.share-info {
  text-align: center;
  padding: 20px 0;
}

.meta-info {
  color: #666;
  font-size: 14px;
  margin: 5px 0;
}

.file-detail {
  padding: 20px 0;
}

.file-preview {
  text-align: center;
  padding: 40px 0;
}

.file-meta {
  display: flex;
  justify-content: center;
  align-items: center;
  color: #666;
  font-size: 14px;
  margin-top: 10px;
}

.actions {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 30px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}
</style>

