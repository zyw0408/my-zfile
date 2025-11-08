<template>
  <div class="home-container">
    <!-- 面包屑导航 -->
    <Breadcrumb :path="fileStore.currentPath" @navigate="handleBreadcrumbNavigate" />
    
    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <template v-if="selectedFiles.length > 0">
          <el-tag type="info" style="margin-right: 10px">
            已选择 {{ selectedFiles.length }} 项
          </el-tag>
          <el-button type="danger" @click="handleBatchDelete">
            批量删除
          </el-button>
          <el-button @click="clearSelection">
            取消选择
          </el-button>
        </template>
        <template v-else>
          <el-button type="primary" :icon="Upload" @click="showUploadDialog = true">
            上传文件
          </el-button>
          <el-button :icon="FolderAdd" @click="showCreateFolderDialog = true">
            新建文件夹
          </el-button>
          <el-button
            :icon="Refresh"
            @click="fileStore.refresh()"
          >
            刷新
          </el-button>
        </template>
      </div>

      <div class="toolbar-right">
        <el-button-group>
          <el-button
            :type="fileStore.viewMode === 'list' ? 'primary' : ''"
            :icon="List"
            @click="fileStore.viewMode = 'list'"
          />
          <el-button
            :type="fileStore.viewMode === 'grid' ? 'primary' : ''"
            :icon="Grid"
            @click="fileStore.viewMode = 'grid'"
          />
        </el-button-group>
      </div>
    </div>

    <!-- 文件列表 -->
    <div 
      class="file-content"
      @drop.prevent="handleDrop"
      @dragover.prevent="handleDragOver"
      @dragleave.prevent="handleDragLeave"
      :class="{ 'drag-over': isDragging }"
    >
      <el-table
        v-if="fileStore.viewMode === 'list'"
        v-loading="fileStore.loading"
        :data="fileStore.files"
        style="width: 100%"
        @row-contextmenu="handleRowContextMenu"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        
        <el-table-column prop="name" label="文件名" min-width="300">
          <template #default="{ row }">
            <div class="file-name" @click.prevent.stop="handleRowClick(row)" style="cursor: pointer;">
              <el-icon :size="20" :color="row.type === 'folder' ? '#409eff' : '#909399'">
                <component :is="getFileIconComponent(row)" />
              </el-icon>
              <span>{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="size" label="大小" width="120">
          <template #default="{ row }">
            {{ row.type === 'folder' ? '-' : formatFileSize(row.size || 0) }}
          </template>
        </el-table-column>

        <el-table-column prop="updatedAt" label="修改时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.updatedAt) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click.stop="handleDownload(row)">
              下载
            </el-button>
            <el-button link type="primary" size="small" @click.stop="handleShare(row)">
              分享
            </el-button>
            <el-button link type="primary" size="small" @click.stop="handleRename(row)">
              重命名
            </el-button>
            <el-button link type="danger" size="small" @click.stop="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 网格视图 -->
      <div v-else class="grid-view">
        <div
          v-for="item in fileStore.files"
          :key="item.id"
          class="grid-item"
          :class="{ selected: isSelected(item.id) }"
          @click.prevent="handleGridItemClick(item, $event)"
          @contextmenu.prevent="handleGridContextMenu(item, $event)"
        >
          <div class="grid-item-icon">
            <el-icon :size="48" :color="item.type === 'folder' ? '#409eff' : '#909399'">
              <component :is="getFileIconComponent(item)" />
            </el-icon>
          </div>
          <div class="grid-item-name">{{ item.name }}</div>
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty v-if="!fileStore.loading && fileStore.files.length === 0" description="暂无文件" />
    </div>

    <!-- 分页 -->
    <div v-if="fileStore.total > 0" class="pagination">
      <el-pagination
        v-model:current-page="fileStore.page"
        v-model:page-size="fileStore.pageSize"
        :total="fileStore.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="fileStore.handlePageChange"
        @size-change="fileStore.handlePageSizeChange"
      />
    </div>

    <!-- 上传对话框 -->
    <el-dialog v-model="showUploadDialog" title="上传文件" width="500px">
      <el-upload
        drag
        multiple
        :auto-upload="false"
        :on-change="handleFileChange"
        :file-list="uploadFileList"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          将文件拖到此处，或<em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            支持单个或批量上传，单文件最大10GB
          </div>
        </template>
      </el-upload>

      <template #footer>
        <el-button @click="showUploadDialog = false">取消</el-button>
        <el-button type="primary" :loading="uploading" @click="handleUploadConfirm">
          {{ uploading ? '上传中...' : '开始上传' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- 文件预览 -->
    <FilePreview v-model="showPreview" :file="previewFile" />

    <!-- 分享对话框 -->
    <ShareDialog v-model="showShareDialog" :file="shareFile" @success="handleShareSuccess" />

    <!-- 右键菜单 -->
    <ContextMenu v-model="showContextMenu" :x="contextMenuX" :y="contextMenuY" :items="contextMenuItems" />

    <!-- 新建文件夹对话框 -->
    <el-dialog v-model="showCreateFolderDialog" title="新建文件夹" width="400px">
      <el-form @submit.prevent="handleCreateFolder">
        <el-form-item label="文件夹名称">
          <el-input
            v-model="newFolderName"
            placeholder="请输入文件夹名称"
            clearable
            @keyup.enter="handleCreateFolder"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showCreateFolderDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreateFolder">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useFileStore } from '@/stores/file'
import { useUpload } from '@/composables/useUpload'
import { formatFileSize } from '@/utils/file'
import { formatDateTime } from '@/utils/format'
import { downloadFile } from '@/api/file'
import { ElMessage, ElMessageBox, type UploadUserFile } from 'element-plus'
import { Upload, FolderAdd, Refresh, List, Grid, Folder, Document, UploadFilled, Download, Share, Edit, Delete } from '@element-plus/icons-vue'
import type { FileItem } from '@/api/types'
import FilePreview from '@/components/FilePreview/index.vue'
import Breadcrumb from '@/components/Breadcrumb/index.vue'
import ShareDialog from '@/components/ShareDialog/index.vue'
import ContextMenu from '@/components/ContextMenu/index.vue'
import type { MenuItem } from '@/components/ContextMenu/index.vue'

const router = useRouter()
const fileStore = useFileStore()
const { smartUpload, batchUpload } = useUpload()

// 批量选择
const selectedFiles = ref<FileItem[]>([])

// 右键菜单
const showContextMenu = ref(false)
const contextMenuX = ref(0)
const contextMenuY = ref(0)
const contextMenuTarget = ref<FileItem | null>(null)
const contextMenuItems = ref<MenuItem[]>([])

const showUploadDialog = ref(false)
const showCreateFolderDialog = ref(false)
const newFolderName = ref('')
const uploadFileList = ref<UploadUserFile[]>([])

// 拖拽上传
const isDragging = ref(false)

// 拖拽上传相关
function handleDragOver(event: DragEvent) {
  isDragging.value = true
}

function handleDragLeave(event: DragEvent) {
  isDragging.value = false
}

async function handleDrop(event: DragEvent) {
  isDragging.value = false
  
  const files = event.dataTransfer?.files
  if (!files || files.length === 0) return

  // 转换为 File 数组
  const fileList = Array.from(files)
  
  // 过滤掉文件夹（通过类型判断）
  const validFiles = fileList.filter(file => file.size > 0 || file.type !== '')
  
  if (validFiles.length === 0) {
    ElMessage.warning('不支持拖拽上传文件夹')
    return
  }

  ElMessage.success(`准备上传 ${validFiles.length} 个文件`)

  // 批量上传
  try {
    for (const file of validFiles) {
      await smartUpload(file, fileStore.currentFolder)
    }
    ElMessage.success('上传完成')
    fileStore.refresh()
  } catch (error) {
    console.error('拖拽上传失败:', error)
    ElMessage.error('部分文件上传失败')
  }
}

const uploading = ref(false)

// 加载文件列表
onMounted(() => {
  fileStore.loadFiles()
})

// 获取文件图标组件
function getFileIconComponent(file: FileItem) {
  return file.type === 'folder' ? Folder : Document
}

// 预览相关
const showPreview = ref(false)
const previewFile = ref<FileItem | null>(null)

// 行点击（进入文件夹或预览文件）
function handleRowClick(row: FileItem) {
  if (row.type === 'folder') {
    fileStore.loadFiles(row.id, row.name)
  } else {
    // 打开文件预览
    previewFile.value = row
    showPreview.value = true
  }
}

// 面包屑导航
function handleBreadcrumbNavigate(folderId: string) {
  fileStore.navigateToFolder(folderId)
  clearSelection() // 切换文件夹时清空选择
}

// 批量选择相关
function handleSelectionChange(selection: FileItem[]) {
  selectedFiles.value = selection
}

function isSelected(fileId: string) {
  return selectedFiles.value.some(f => f.id === fileId)
}

function handleGridItemClick(item: FileItem, event: MouseEvent) {
  // Ctrl/Cmd 键多选
  if (event.ctrlKey || event.metaKey) {
    event.preventDefault()
    const index = selectedFiles.value.findIndex(f => f.id === item.id)
    if (index >= 0) {
      selectedFiles.value.splice(index, 1)
    } else {
      selectedFiles.value.push(item)
    }
  } else {
    // 普通点击，进入文件夹或预览文件
    handleRowClick(item)
  }
}

function clearSelection() {
  selectedFiles.value = []
}

// 右键菜单相关
function handleRowContextMenu(row: FileItem, column: any, event: MouseEvent) {
  event.preventDefault()
  showContextMenuForItem(row, event.clientX, event.clientY)
}

function handleGridContextMenu(item: FileItem, event: MouseEvent) {
  showContextMenuForItem(item, event.clientX, event.clientY)
}

function showContextMenuForItem(item: FileItem, x: number, y: number) {
  contextMenuTarget.value = item
  contextMenuX.value = x
  contextMenuY.value = y

  contextMenuItems.value = [
    {
      label: item.type === 'folder' ? '打开' : '预览',
      icon: Document,
      action: () => handleRowClick(item)
    },
    {
      label: '下载',
      icon: Download,
      action: () => handleDownload(item),
      disabled: item.type === 'folder'
    },
    {
      label: '分享',
      icon: Share,
      action: () => handleShare(item)
    },
    {
      label: '重命名',
      icon: Edit,
      action: () => handleRename(item)
    },
    {
      label: '删除',
      icon: Delete,
      action: () => handleDelete(item),
      danger: true
    }
  ]

  showContextMenu.value = true
}

// 批量删除
async function handleBatchDelete() {
  if (selectedFiles.value.length === 0) {
    ElMessage.warning('请先选择要删除的文件')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedFiles.value.length} 个项目吗？`,
      '批量删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    // 逐个删除
    let successCount = 0
    for (const item of selectedFiles.value) {
      const result = await fileStore.deleteItem(item)
      if (result) successCount++
    }

    if (successCount === selectedFiles.value.length) {
      ElMessage.success(`成功删除 ${successCount} 个项目`)
    } else {
      ElMessage.warning(`成功删除 ${successCount} 个项目，失败 ${selectedFiles.value.length - successCount} 个`)
    }

    clearSelection()
    fileStore.refresh()
  } catch (error) {
    // 用户取消
  }
}

// 下载文件
function handleDownload(row: FileItem) {
  if (row.type === 'folder') {
    ElMessage.warning('暂不支持文件夹下载')
    return
  }

  const url = downloadFile(row.id)
  // Mock环境下，如果有base64内容，downloadFile会直接触发下载，不返回URL
  if (url) {
    window.open(url, '_blank')
  }
}

// 分享相关
const showShareDialog = ref(false)
const shareFile = ref<FileItem | null>(null)

// 分享文件
function handleShare(row: FileItem) {
  shareFile.value = row
  showShareDialog.value = true
}

// 分享成功
function handleShareSuccess() {
  ElMessage.success('分享创建成功')
  // 可以选择刷新分享列表等操作
}

// 重命名
async function handleRename(row: FileItem) {
  ElMessageBox.prompt('请输入新名称', '重命名', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputValue: row.name
  }).then(async ({ value }) => {
    if (value && value !== row.name) {
      await fileStore.renameItem(row, value)
    }
  })
}

// 删除
function handleDelete(row: FileItem) {
  ElMessageBox.confirm(
    `确定要删除 "${row.name}" 吗？删除后将移入回收站。`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    fileStore.deleteItem(row)
  })
}

// 文件选择变化
function handleFileChange(file: UploadUserFile, fileList: UploadUserFile[]) {
  uploadFileList.value = fileList
}

// 确认上传
async function handleUploadConfirm() {
  if (uploadFileList.value.length === 0) {
    ElMessage.warning('请先选择文件')
    return
  }

  uploading.value = true

  try {
    const files = uploadFileList.value.map(item => item.raw!).filter(Boolean)
    await batchUpload(files, fileStore.currentFolder)

    ElMessage.success('上传完成')
    showUploadDialog.value = false
    uploadFileList.value = []
    fileStore.refresh()
  } catch (error) {
    console.error('上传失败:', error)
  } finally {
    uploading.value = false
  }
}

// 创建文件夹
async function handleCreateFolder() {
  if (!newFolderName.value.trim()) {
    ElMessage.warning('请输入文件夹名称')
    return
  }

  const success = await fileStore.createNewFolder(newFolderName.value)
  if (success) {
    showCreateFolderDialog.value = false
    newFolderName.value = ''
  }
}
</script>

<style scoped>
.home-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  padding: 16px;
  background-color: #fff;
  border-radius: 4px;
  margin-bottom: 16px;
}

.toolbar-left,
.toolbar-right {
  display: flex;
  gap: 10px;
}

.file-content {
  flex: 1;
  background-color: #fff;
  border-radius: 4px;
  padding: 16px;
  overflow-y: auto;
  position: relative;
  transition: all 0.3s;
}

.file-content.drag-over {
  background-color: #ecf5ff;
  border: 2px dashed #409eff;
}

.file-content.drag-over::before {
  content: '释放以上传文件';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 24px;
  color: #409eff;
  font-weight: bold;
  z-index: 1000;
  pointer-events: none;
}

.file-name {
  display: flex;
  align-items: center;
  gap: 10px;
}

.grid-view {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 16px;
}

.grid-item {
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.grid-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.grid-item.selected {
  border-color: #409eff;
  background-color: #ecf5ff;
  box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 0.3);
}

.grid-item-icon {
  margin-bottom: 10px;
}

.grid-item-name {
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}
</style>

