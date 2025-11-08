<template>
  <teleport to="body">
    <div
      v-if="visible"
      class="context-menu"
      :style="{ left: x + 'px', top: y + 'px' }"
      @click.stop
      @contextmenu.prevent
    >
      <div
        v-for="item in menuItems"
        :key="item.label"
        class="menu-item"
        :class="{ disabled: item.disabled, danger: item.danger }"
        @click="handleMenuClick(item)"
      >
        <el-icon v-if="item.icon">
          <component :is="item.icon" />
        </el-icon>
        <span>{{ item.label }}</span>
      </div>
    </div>
  </teleport>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import type { Component } from 'vue'

export interface MenuItem {
  label: string
  icon?: Component
  action: () => void
  disabled?: boolean
  danger?: boolean
}

interface Props {
  modelValue: boolean
  x: number
  y: number
  items: MenuItem[]
}

const props = defineProps<Props>()
const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const menuItems = computed(() => props.items)

function handleMenuClick(item: MenuItem) {
  if (item.disabled) return
  item.action()
  visible.value = false
}

function handleClickOutside() {
  visible.value = false
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  document.addEventListener('contextmenu', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  document.removeEventListener('contextmenu', handleClickOutside)
})
</script>

<style scoped>
.context-menu {
  position: fixed;
  z-index: 9999;
  background-color: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 4px 0;
  min-width: 160px;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 8px 16px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 14px;
  color: #606266;
}

.menu-item:hover:not(.disabled) {
  background-color: #f5f7fa;
}

.menu-item.disabled {
  color: #c0c4cc;
  cursor: not-allowed;
}

.menu-item.danger {
  color: #f56c6c;
}

.menu-item.danger:hover:not(.disabled) {
  background-color: #fef0f0;
}

.menu-item .el-icon {
  margin-right: 8px;
  font-size: 16px;
}
</style>

