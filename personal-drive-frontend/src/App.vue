<template>
  <router-view />
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

onMounted(() => {
  // 如果已登录，获取用户信息
  if (authStore.isLoggedIn && !authStore.user) {
    authStore.fetchUserInfo()
  }
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body,
#app {
  width: 100%;
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

#app {
  color: #333;
  background-color: #f5f7fa;
}

::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-thumb {
  background-color: #ddd;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background-color: #ccc;
}

::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}
</style>

