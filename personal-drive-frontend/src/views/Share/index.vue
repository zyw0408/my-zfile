<template>
  <div class="share-container">
    <el-card>
      <template #header>
        <span>我的分享</span>
      </template>

      <el-table v-loading="loading" :data="shareList" style="width: 100%">
        <el-table-column prop="shareCode" label="分享码" width="150" />
        <el-table-column prop="fileName" label="文件名" min-width="300" />
        <el-table-column prop="viewCount" label="查看次数" width="120" />
        <el-table-column prop="downloadCount" label="下载次数" width="120" />
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleCopy(row)">
              复制链接
            </el-button>
            <el-button link type="danger" @click="handleCancel(row)">
              取消分享
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && shareList.length === 0" description="暂无分享" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getMyShares, cancelShare } from '@/api/share'
import { ElMessage } from 'element-plus'
import type { Share } from '@/api/types'

const loading = ref(false)
const shareList = ref<Share[]>([])

onMounted(() => {
  loadShares()
})

async function loadShares() {
  loading.value = true
  try {
    const res = await getMyShares()
    shareList.value = res.data.items
  } catch (error) {
    console.error('加载分享列表失败:', error)
  } finally {
    loading.value = false
  }
}

function handleCopy(row: Share) {
  const url = `${window.location.origin}/s/${row.shareCode}`
  navigator.clipboard.writeText(url)
  ElMessage.success('链接已复制到剪贴板')
}

async function handleCancel(row: Share) {
  try {
    await cancelShare(row.id)
    ElMessage.success('已取消分享')
    loadShares()
  } catch (error) {
    console.error('取消分享失败:', error)
  }
}
</script>

<style scoped>
.share-container {
  padding: 20px;
}
</style>

