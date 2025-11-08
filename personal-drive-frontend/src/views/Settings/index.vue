<template>
  <div class="settings-container">
    <el-card>
      <template #header>
        <span>个人设置</span>
      </template>

      <el-form :model="form" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="form.email" disabled />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSave">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Mock 数据管理 -->
    <el-card style="margin-top: 20px;">
      <template #header>
        <span>开发者选项</span>
      </template>

      <el-alert
        title="Mock数据已启用localStorage持久化"
        type="info"
        :closable="false"
        style="margin-bottom: 20px;"
      >
        <p>上传的文件会保存到浏览器本地存储，刷新页面不会丢失。</p>
      </el-alert>

      <el-space>
        <el-button type="danger" @click="handleResetMockData">
          重置Mock数据
        </el-button>
        <el-button @click="handleViewStorage">
          查看存储信息
        </el-button>
      </el-space>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { ElMessage, ElMessageBox } from 'element-plus'
import { clearFilesStorage } from '@/utils/mock'

const authStore = useAuthStore()

const form = reactive({
  username: authStore.user?.username || '',
  email: authStore.user?.email || ''
})

function handleSave() {
  ElMessage.success('功能开发中')
}

// 重置Mock数据
function handleResetMockData() {
  ElMessageBox.confirm(
    '确定要重置Mock数据吗？这将清空所有上传的文件，恢复到初始状态。',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    clearFilesStorage()
    ElMessage.success('Mock数据已重置，请刷新页面')
    setTimeout(() => {
      window.location.reload()
    }, 1000)
  }).catch(() => {
    // 取消操作
  })
}

// 查看存储信息
function handleViewStorage() {
  const data = localStorage.getItem('MOCK_FILES_DATA')
  if (data) {
    try {
      const parsed = JSON.parse(data)
      ElMessageBox.alert(
        `已存储 ${parsed.length} 个文件\n数据大小: ${(data.length / 1024).toFixed(2)} KB`,
        '存储信息',
        {
          confirmButtonText: '确定',
        }
      )
    } catch (e) {
      ElMessage.error('解析存储数据失败')
    }
  } else {
    ElMessage.info('当前使用初始Mock数据，未保存到localStorage')
  }
}
</script>

<style scoped>
.settings-container {
  padding: 20px;
}
</style>

