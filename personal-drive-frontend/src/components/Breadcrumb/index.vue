<template>
  <div class="breadcrumb-container">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item @click="handleNavigate('root')">
        <el-icon><HomeFilled /></el-icon>
        <span style="margin-left: 5px">全部文件</span>
      </el-breadcrumb-item>
      
      <el-breadcrumb-item
        v-for="item in pathItems"
        :key="item.id"
        @click="handleNavigate(item.id)"
      >
        {{ item.name }}
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { HomeFilled } from '@element-plus/icons-vue'

interface PathItem {
  id: string
  name: string
}

interface Props {
  path: PathItem[]
}

const props = defineProps<Props>()
const emit = defineEmits<{
  navigate: [folderId: string]
}>()

const pathItems = computed(() => props.path)

function handleNavigate(folderId: string) {
  emit('navigate', folderId)
}
</script>

<style scoped>
.breadcrumb-container {
  padding: 16px 0;
}

:deep(.el-breadcrumb__item) {
  cursor: pointer;
}

:deep(.el-breadcrumb__inner) {
  display: inline-flex;
  align-items: center;
  color: #606266;
  font-weight: normal;
  transition: color 0.3s;
}

:deep(.el-breadcrumb__inner:hover) {
  color: #409eff;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #303133;
  font-weight: 500;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner:hover) {
  color: #303133;
  cursor: default;
}
</style>

