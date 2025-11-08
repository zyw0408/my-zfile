<template>
  <div class="trash-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>回收站</span>
          <el-button type="danger" :icon="Delete" @click="handleClearAll">
            清空回收站
          </el-button>
        </div>
      </template>

      <el-table v-loading="loading" :data="trashList" style="width: 100%">
        <el-table-column prop="name" label="文件名" min-width="300" />
        <el-table-column prop="size" label="大小" width="120">
          <template #default="{ row }">
            {{ formatFileSize(row.size || 0) }}
          </template>
        </el-table-column>
        <el-table-column prop="deletedAt" label="删除时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleRestore(row)">
              恢复
            </el-button>
            <el-button link type="danger" @click="handleDelete(row)">
              永久删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && trashList.length === 0" description="回收站为空" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getTrashList, restoreFile, permanentlyDelete, clearTrash } from '@/api/trash'
import { formatFileSize } from '@/utils/file'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import type { FileItem } from '@/api/types'

const loading = ref(false)
const trashList = ref<FileItem[]>([])

onMounted(() => {
  loadTrash()
})

async function loadTrash() {
  loading.value = true
  try {
    const res = await getTrashList()
    trashList.value = res.data.items
  } catch (error) {
    console.error('加载回收站失败:', error)
  } finally {
    loading.value = false
  }
}

async function handleRestore(row: FileItem) {
  try {
    await restoreFile(row.id)
    ElMessage.success('恢复成功')
    loadTrash()
  } catch (error) {
    console.error('恢复失败:', error)
  }
}

function handleDelete(row: FileItem) {
  ElMessageBox.confirm('确定要永久删除吗？此操作不可恢复！', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await permanentlyDelete(row.id)
      ElMessage.success('删除成功')
      loadTrash()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

function handleClearAll() {
  ElMessageBox.confirm('确定要清空回收站吗？此操作不可恢复！', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await clearTrash()
      ElMessage.success('清空成功')
      loadTrash()
    } catch (error) {
      console.error('清空失败:', error)
    }
  })
}
</script>

<style scoped>
.trash-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

