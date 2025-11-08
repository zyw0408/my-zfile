<template>
  <div class="storage-container">
    <el-card>
      <template #header>
        <span>存储管理</span>
      </template>

      <div v-loading="loading">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-statistic title="已用空间" :value="formatFileSize(stats?.totalUsed || 0)" />
          </el-col>
          <el-col :span="12">
            <el-statistic title="总空间" :value="formatFileSize(stats?.totalQuota || 0)" />
          </el-col>
        </el-row>

        <el-divider />

        <el-row :gutter="20">
          <el-col :span="12">
            <el-statistic title="文件数量" :value="stats?.fileCount || 0" />
          </el-col>
          <el-col :span="12">
            <el-statistic title="文件夹数量" :value="stats?.folderCount || 0" />
          </el-col>
        </el-row>

        <el-divider />

        <h3>文件类型统计</h3>
        <el-table :data="stats?.typeStats || []" style="width: 100%">
          <el-table-column prop="type" label="类型" />
          <el-table-column prop="count" label="数量" />
          <el-table-column prop="size" label="占用空间">
            <template #default="{ row }">
              {{ formatFileSize(row.size) }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getStorageStats } from '@/api/storage'
import { formatFileSize } from '@/utils/file'
import type { StorageStats } from '@/api/types'

const loading = ref(false)
const stats = ref<StorageStats>()

onMounted(() => {
  loadStats()
})

async function loadStats() {
  loading.value = true
  try {
    const res = await getStorageStats()
    stats.value = res.data
  } catch (error) {
    console.error('加载存储统计失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.storage-container {
  padding: 20px;
}
</style>

