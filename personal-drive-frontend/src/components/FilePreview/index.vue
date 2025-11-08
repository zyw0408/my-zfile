<template>
  <el-dialog
    v-model="visible"
    :title="file?.name || '文件预览'"
    width="80%"
    :fullscreen="isFullscreen"
    @close="handleClose"
  >
    <div class="preview-container">
      <!-- 图片预览 -->
      <div v-if="isImage" class="image-preview">
        <el-image
          :src="previewUrl"
          fit="contain"
          :preview-src-list="[previewUrl]"
          :initial-index="0"
          style="width: 100%; height: 600px"
        />
      </div>

      <!-- 视频预览 -->
      <div v-else-if="isVideo" class="video-preview">
        <video
          :src="previewUrl"
          controls
          controlsList="nodownload"
          style="width: 100%; max-height: 600px"
        >
          您的浏览器不支持视频播放
        </video>
      </div>

      <!-- 音频预览 -->
      <div v-else-if="isAudio" class="audio-preview">
        <div class="audio-info">
          <el-icon :size="80"><Headset /></el-icon>
          <h3>{{ file?.name }}</h3>
          <p>{{ formatFileSize(file?.size || 0) }}</p>
        </div>
        <audio
          :src="previewUrl"
          controls
          controlsList="nodownload"
          style="width: 100%; margin-top: 20px"
        >
          您的浏览器不支持音频播放
        </audio>
      </div>

      <!-- PDF 预览 -->
      <div v-else-if="isPdf" class="pdf-preview">
        <div class="pdf-toolbar">
          <el-button-group>
            <el-button :disabled="currentPage <= 1" @click="currentPage--">
              上一页
            </el-button>
            <el-button disabled>
              {{ currentPage }} / {{ totalPages || '?' }}
            </el-button>
            <el-button :disabled="currentPage >= totalPages" @click="currentPage++">
              下一页
            </el-button>
          </el-button-group>
          <el-button-group style="margin-left: 10px">
            <el-button @click="scale -= 0.1" :disabled="scale <= 0.5">
              <el-icon><ZoomOut /></el-icon>
            </el-button>
            <el-button disabled>{{ Math.round(scale * 100) }}%</el-button>
            <el-button @click="scale += 0.1" :disabled="scale >= 3">
              <el-icon><ZoomIn /></el-icon>
            </el-button>
          </el-button-group>
        </div>
        <div class="pdf-content">
          <VuePdfEmbed
            :source="previewUrl"
            :page="currentPage"
            :scale="scale"
            @rendered="handlePdfRendered"
          />
        </div>
      </div>

      <!-- 文本预览 -->
      <div v-else-if="isText" class="text-preview">
        <el-input
          v-model="textContent"
          type="textarea"
          :rows="20"
          readonly
        />
      </div>

      <!-- Office文档预览 -->
      <div v-else-if="isOffice" class="office-preview">
        <div v-if="officePreviewMode === 'online' && officeOnlineUrl" class="office-online-preview">
          <iframe 
            :src="officeOnlineUrl" 
            style="width: 100%; height: 600px; border: none;"
            frameborder="0"
          />
        </div>
        <el-result
          v-else
          icon="info"
          title="Office文档在线预览"
          :sub-title="`${file?.name}`"
        >
          <template #extra>
            <el-alert
              title="Office文档预览选项"
              type="info"
              :closable="false"
              style="margin-bottom: 20px; text-align: left;"
            >
              <p><strong>📄 支持的格式：</strong></p>
              <p>• Word文档（.docx, .doc）</p>
              <p>• Excel表格（.xlsx, .xls）</p>
              <p>• PowerPoint演示（.pptx, .ppt）</p>
              <br>
              <p><strong>💡 预览方式：</strong></p>
              <p>• 在线预览 - 使用微软 Office Online 服务（需文件有效URL）</p>
              <p>• 下载查看 - 下载到本地用 Office 软件打开</p>
              <br>
              <p v-if="isMockWithoutContent" style="color: #E6A23C;">
                ⚠️ 当前文件是初始Mock数据，没有实际内容，无法使用在线预览
              </p>
              <p v-else-if="!canUseOfficeOnline" style="color: #E6A23C;">
                ⚠️ Mock环境下，Office Online需要公网可访问的文件URL
              </p>
            </el-alert>
            <el-space>
              <el-button 
                v-if="canUseOfficeOnline"
                type="primary" 
                @click="handleOfficeOnlinePreview"
              >
                <el-icon><View /></el-icon>
                在线预览
              </el-button>
              <el-button type="success" @click="handleDownload">
                <el-icon><Download /></el-icon>
                下载文件
              </el-button>
            </el-space>
          </template>
        </el-result>
      </div>

      <!-- Mock环境没有文件内容提示 -->
      <div v-else-if="isMockWithoutContent" class="mock-no-content">
        <el-result
          icon="info"
          title="Mock环境限制"
          :sub-title="`${file?.name} 是初始Mock数据，没有文件内容`"
        >
          <template #extra>
            <el-alert
              title="如何预览文件？"
              type="info"
              :closable="false"
              style="margin-bottom: 20px; text-align: left;"
            >
              <p>✅ 上传一个真实文件（如图片、PDF、文本）</p>
              <p>✅ 然后双击上传的文件即可预览</p>
              <p>⚠️ 初始Mock文件（项目文档.pdf、照片.jpg等）没有文件内容，无法预览</p>
            </el-alert>
          </template>
        </el-result>
      </div>

      <!-- 不支持的格式 -->
      <div v-else class="unsupported-preview">
        <el-result
          icon="warning"
          title="不支持预览"
          :sub-title="`${file?.name} 暂不支持在线预览`"
        >
          <template #extra>
            <el-button type="primary" @click="handleDownload">
              <el-icon><Download /></el-icon>
              下载文件
            </el-button>
          </template>
        </el-result>
      </div>
    </div>

    <template #footer>
      <div class="preview-footer">
        <div class="file-info">
          <span>文件名：{{ file?.name }}</span>
          <span style="margin-left: 20px">大小：{{ formatFileSize(file?.size || 0) }}</span>
          <span style="margin-left: 20px">修改时间：{{ formatDateTime(file?.updatedAt || '') }}</span>
        </div>
        <div class="actions">
          <el-button @click="isFullscreen = !isFullscreen">
            <el-icon><FullScreen /></el-icon>
            {{ isFullscreen ? '退出全屏' : '全屏' }}
          </el-button>
          <el-button type="primary" @click="handleDownload">
            <el-icon><Download /></el-icon>
            下载
          </el-button>
          <el-button @click="handleClose">关闭</el-button>
        </div>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { Download, FullScreen, ZoomIn, ZoomOut, Headset, View } from '@element-plus/icons-vue'
import VuePdfEmbed from 'vue-pdf-embed'
import { isImage as checkIsImage, isVideo as checkIsVideo, isAudio as checkIsAudio, getFileExtension } from '@/utils/file'
import { formatFileSize } from '@/utils/file'
import { formatDateTime } from '@/utils/format'
import { downloadFile } from '@/api/file'
import { createBlobUrl } from '@/api/request'
import { mockFiles, MOCK_CONFIG } from '@/utils/mock'
import type { FileItem } from '@/api/types'

interface Props {
  modelValue: boolean
  file: FileItem | null
}

const props = defineProps<Props>()
const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const isFullscreen = ref(false)
const currentPage = ref(1)
const totalPages = ref(0)
const scale = ref(1)
const textContent = ref('')

// Office 在线预览相关
const officePreviewMode = ref<'none' | 'online'>('none')
const officeOnlineUrl = ref('')

// 判断文件类型
const isImage = computed(() => props.file ? checkIsImage(props.file.name, props.file.mimeType) : false)
const isVideo = computed(() => props.file ? checkIsVideo(props.file.name, props.file.mimeType) : false)
const isAudio = computed(() => props.file ? checkIsAudio(props.file.name, props.file.mimeType) : false)
const isPdf = computed(() => {
  if (!props.file) return false
  const ext = getFileExtension(props.file.name)
  return ext === 'pdf' || props.file.mimeType === 'application/pdf'
})
const isText = computed(() => {
  if (!props.file) return false
  const ext = getFileExtension(props.file.name)
  return ['txt', 'md', 'json', 'js', 'ts', 'vue', 'html', 'css', 'xml', 'csv', 'log'].includes(ext)
})

// 判断是否是Office文档
const isOffice = computed(() => {
  if (!props.file) return false
  const ext = getFileExtension(props.file.name)
  return ['docx', 'xlsx', 'pptx', 'doc', 'xls', 'ppt'].includes(ext)
})

// 判断是否是Mock环境且没有文件内容
const isMockWithoutContent = computed(() => {
  if (!props.file) return false
  if (!MOCK_CONFIG.enabled) return false
  
  const mockFile = mockFiles.find(f => f.id === props.file!.id)
  // 如果是Mock环境，但文件没有base64Content，说明是初始Mock数据
  return !mockFile || !mockFile.base64Content
})

// 判断是否可以使用Office Online预览
const canUseOfficeOnline = computed(() => {
  if (!props.file || !isOffice.value) return false
  
  // Mock环境下，需要有base64Content的文件（已上传的文件）
  if (MOCK_CONFIG.enabled) {
    const mockFile = mockFiles.find(f => f.id === props.file!.id)
    // 暂时禁用Mock环境下的Office Online（因为需要公网URL）
    return false // mockFile && mockFile.base64Content
  }
  
  // 真实环境，可以使用（后端会提供公网URL）
  return true
})

// 预览URL
const previewUrl = computed(() => {
  if (!props.file) return ''
  
  // Mock环境：尝试从base64创建URL
  if (MOCK_CONFIG.enabled) {
    const mockFile = mockFiles.find(f => f.id === props.file!.id)
    if (mockFile && mockFile.base64Content) {
      // 从base64创建Blob URL
      const blobUrl = createBlobUrl(mockFile.base64Content, mockFile.mimeType || 'application/octet-stream')
      console.log('[Mock] 创建预览URL:', mockFile.name, 'base64长度:', mockFile.base64Content.length)
      return blobUrl
    } else {
      // 没有base64内容，返回空字符串（会显示isMockWithoutContent提示）
      console.warn('[Mock] 文件没有base64内容:', props.file.name)
      return ''
    }
  }
  
  // 真实环境：返回后端预览URL
  const token = localStorage.getItem('access_token')
  const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
  return `${baseURL}/api/v1/files/${props.file.id}/preview?token=${token}`
})

// PDF渲染完成
function handlePdfRendered(event: any) {
  totalPages.value = event.pages || 0
}

// 下载文件
function handleDownload() {
  if (!props.file) return
  const url = downloadFile(props.file.id)
  window.open(url, '_blank')
}

// Office Online 在线预览
function handleOfficeOnlinePreview() {
  if (!props.file) return
  
  // 构建文件URL（真实环境）
  const token = localStorage.getItem('access_token')
  const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
  const fileUrl = `${baseURL}/api/v1/files/${props.file.id}/download?token=${token}`
  
  // 使用微软 Office Online Viewer
  // 参考：https://docs.microsoft.com/en-us/office/dev/add-ins/concepts/browsers-used-by-office-web-add-ins
  const encodedUrl = encodeURIComponent(fileUrl)
  officeOnlineUrl.value = `https://view.officeapps.live.com/op/view.aspx?src=${encodedUrl}`
  officePreviewMode.value = 'online'
  
  console.log('[Office Online] 预览URL:', officeOnlineUrl.value)
}

// 关闭预览
function handleClose() {
  visible.value = false
  // 重置状态
  currentPage.value = 1
  scale.value = 1
  isFullscreen.value = false
  textContent.value = ''
  officePreviewMode.value = 'none'
  officeOnlineUrl.value = ''
}

// 监听文件变化，加载文本内容
watch(() => props.file, async (newFile) => {
  if (newFile && isText.value) {
    // Mock环境：从base64解码文本
    if (MOCK_CONFIG.enabled) {
      const mockFile = mockFiles.find(f => f.id === newFile.id)
      if (mockFile && mockFile.base64Content) {
        try {
          // 将base64解码为文本
          const decodedText = atob(mockFile.base64Content)
          textContent.value = decodedText
          console.log('[Mock] 加载文本内容:', mockFile.name)
        } catch (error) {
          console.warn('[Mock] 解码文本失败:', error)
          textContent.value = '// 无法解码文本内容'
        }
      } else {
        textContent.value = '// Mock环境：文件没有base64内容\n// 请上传真实文件以查看内容'
      }
    } else {
      // 真实环境：调用后端API
      // TODO: 实现真实API调用
      textContent.value = '// 实际环境下会加载真实文件内容'
    }
  }
})
</script>

<style scoped>
.preview-container {
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-preview,
.video-preview,
.audio-preview,
.pdf-preview,
.text-preview {
  width: 100%;
}

.audio-preview {
  text-align: center;
  padding: 40px 0;
}

.audio-info {
  margin-bottom: 20px;
}

.audio-info h3 {
  margin: 20px 0 10px;
  font-size: 18px;
  color: #333;
}

.audio-info p {
  color: #666;
  font-size: 14px;
}

.pdf-toolbar {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.pdf-content {
  max-height: 600px;
  overflow-y: auto;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 20px;
  background-color: #fff;
}

.unsupported-preview {
  padding: 40px 0;
}

.preview-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.file-info {
  font-size: 14px;
  color: #666;
}

.actions {
  display: flex;
  gap: 10px;
}
</style>

