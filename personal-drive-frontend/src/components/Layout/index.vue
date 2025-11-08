<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="200px" class="layout-aside">
      <div class="logo">
        <el-icon :size="32" color="#409eff">
          <Folder />
        </el-icon>
        <span>个人网盘</span>
      </div>

      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        router
        @select="handleMenuSelect"
      >
        <el-menu-item index="/home">
          <el-icon><Folder /></el-icon>
          <span>我的文件</span>
        </el-menu-item>

        <el-menu-item index="/trash">
          <el-icon><Delete /></el-icon>
          <span>回收站</span>
        </el-menu-item>

        <el-menu-item index="/share">
          <el-icon><Share /></el-icon>
          <span>我的分享</span>
        </el-menu-item>

        <el-menu-item index="/storage">
          <el-icon><PieChart /></el-icon>
          <span>存储管理</span>
        </el-menu-item>

        <el-menu-item index="/settings">
          <el-icon><Setting /></el-icon>
          <span>个人设置</span>
        </el-menu-item>

        <el-menu-item v-if="authStore.isAdmin" index="/admin">
          <el-icon><Tools /></el-icon>
          <span>系统管理</span>
        </el-menu-item>
      </el-menu>

      <!-- 存储空间显示 -->
      <div v-if="authStore.user" class="storage-info">
        <div class="storage-text">
          <span class="label">存储空间</span>
          <span class="usage">
            {{ formatFileSize(authStore.user.storageUsed) }} /
            {{ formatFileSize(authStore.user.storageQuota) }}
          </span>
        </div>
        <el-progress
          :percentage="storagePercentage"
          :color="storageColor"
          :show-text="false"
        />
      </div>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <!-- 顶部导航 -->
      <el-header class="layout-header">
        <!-- 面包屑 -->
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>{{ currentPageTitle }}</el-breadcrumb-item>
        </el-breadcrumb>

        <!-- 搜索框 -->
        <div class="header-search">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索文件"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>

        <!-- 用户信息 -->
        <div class="header-user">
          <el-dropdown @command="handleUserCommand">
            <div class="user-info">
              <el-avatar :size="32" :src="authStore.user?.avatar">
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="username">{{ authStore.user?.username }}</span>
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="settings">
                  <el-icon><Setting /></el-icon>
                  个人设置
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容 -->
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { formatFileSize } from '@/utils/file'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const searchKeyword = ref('')

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 当前页面标题
const currentPageTitle = computed(() => {
  return route.meta.title as string || '我的文件'
})

// 存储空间百分比
const storagePercentage = computed(() => {
  if (!authStore.user) return 0
  return Math.round((authStore.user.storageUsed / authStore.user.storageQuota) * 100)
})

// 存储空间颜色
const storageColor = computed(() => {
  const percentage = storagePercentage.value
  if (percentage >= 90) return '#f56c6c'
  if (percentage >= 70) return '#e6a23c'
  return '#409eff'
})

// 菜单选择
function handleMenuSelect(index: string) {
  router.push(index)
}

// 搜索
function handleSearch() {
  if (searchKeyword.value.trim()) {
    router.push({
      path: '/home',
      query: { search: searchKeyword.value }
    })
  }
}

// 用户下拉菜单
function handleUserCommand(command: string) {
  if (command === 'settings') {
    router.push('/settings')
  } else if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      authStore.logout()
      router.push('/login')
    })
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: #fff;
  border-right: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 60px;
  gap: 10px;
  font-size: 18px;
  font-weight: bold;
  color: #333;
  border-bottom: 1px solid #e4e7ed;
}

.sidebar-menu {
  flex: 1;
  border-right: none;
}

.storage-info {
  padding: 20px;
  border-top: 1px solid #e4e7ed;
}

.storage-text {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 12px;
}

.storage-text .label {
  color: #666;
}

.storage-text .usage {
  color: #409eff;
  font-weight: 500;
}

.layout-header {
  background-color: #fff;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-search {
  width: 300px;
}

.header-user {
  margin-left: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #333;
}

.layout-main {
  background-color: #f5f7fa;
  overflow-y: auto;
}
</style>

