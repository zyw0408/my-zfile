<template>
  <el-dialog
    v-model="visible"
    title="创建分享"
    width="500px"
    @close="handleClose"
  >
    <el-form :model="form" label-width="100px">
      <el-form-item label="分享文件">
        <div class="file-info">
          <el-icon :size="24">
            <component :is="getFileIcon()" />
          </el-icon>
          <span style="margin-left: 10px">{{ file?.name }}</span>
        </div>
      </el-form-item>

      <el-form-item label="有效期">
        <el-select v-model="form.expireDays" placeholder="请选择有效期">
          <el-option label="1天" :value="1" />
          <el-option label="7天" :value="7" />
          <el-option label="30天" :value="30" />
          <el-option label="永久有效" :value="0" />
        </el-select>
      </el-form-item>

      <el-form-item label="提取码">
        <el-switch
          v-model="form.needPassword"
          active-text="需要"
          inactive-text="不需要"
        />
      </el-form-item>

      <el-form-item v-if="form.needPassword" label="提取码">
        <el-input
          v-model="form.password"
          placeholder="请输入4位提取码"
          maxlength="4"
          show-word-limit
        >
          <template #append>
            <el-button @click="generatePassword">随机生成</el-button>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item v-if="shareLink" label="分享链接">
        <el-input v-model="shareLink" readonly>
          <template #append>
            <el-button @click="copyLink">
              <el-icon><DocumentCopy /></el-icon>
              复制
            </el-button>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item v-if="shareLink && form.needPassword" label="提取码">
        <el-input :value="form.password" readonly>
          <template #append>
            <el-button @click="copyPassword">
              <el-icon><DocumentCopy /></el-icon>
              复制
            </el-button>
          </template>
        </el-input>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button v-if="!shareLink" type="primary" @click="handleCreateShare" :loading="creating">
          创建分享
        </el-button>
        <el-button v-else type="success" @click="copyAll">
          <el-icon><DocumentCopy /></el-icon>
          复制链接和提取码
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { DocumentCopy, Document, Folder } from '@element-plus/icons-vue'
import { createShare } from '@/api/share'
import type { FileItem } from '@/api/types'

interface Props {
  modelValue: boolean
  file: FileItem | null
}

interface ShareForm {
  expireDays: number
  needPassword: boolean
  password: string
}

const props = defineProps<Props>()
const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  success: []
}>()

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const form = ref<ShareForm>({
  expireDays: 7,
  needPassword: true,
  password: ''
})

const creating = ref(false)
const shareLink = ref('')
const shareCode = ref('')

// 获取文件图标
function getFileIcon() {
  return props.file?.type === 'folder' ? Folder : Document
}

// 生成随机提取码
function generatePassword() {
  const chars = '0123456789abcdefghijklmnopqrstuvwxyz'
  let password = ''
  for (let i = 0; i < 4; i++) {
    password += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  form.value.password = password
}

// 创建分享
async function handleCreateShare() {
  if (!props.file) return

  if (form.value.needPassword && !form.value.password) {
    ElMessage.warning('请输入提取码')
    return
  }

  if (form.value.needPassword && form.value.password.length !== 4) {
    ElMessage.warning('提取码必须是4位')
    return
  }

  try {
    creating.value = true
    const res = await createShare({
      fileId: props.file.id,
      expireDays: form.value.expireDays,
      password: form.value.needPassword ? form.value.password : undefined
    })

    shareCode.value = res.data.code
    shareLink.value = `${window.location.origin}/s/${res.data.code}`
    
    ElMessage.success('分享创建成功')
    emit('success')
  } catch (error) {
    console.error('创建分享失败:', error)
  } finally {
    creating.value = false
  }
}

// 复制链接
function copyLink() {
  navigator.clipboard.writeText(shareLink.value)
  ElMessage.success('链接已复制到剪贴板')
}

// 复制提取码
function copyPassword() {
  navigator.clipboard.writeText(form.value.password)
  ElMessage.success('提取码已复制到剪贴板')
}

// 复制全部
function copyAll() {
  const text = form.value.needPassword
    ? `分享链接：${shareLink.value}\n提取码：${form.value.password}`
    : `分享链接：${shareLink.value}`
  
  navigator.clipboard.writeText(text)
  ElMessage.success('已复制到剪贴板')
}

// 关闭对话框
function handleClose() {
  visible.value = false
  // 延迟重置，避免动画问题
  setTimeout(() => {
    shareLink.value = ''
    shareCode.value = ''
    form.value = {
      expireDays: 7,
      needPassword: true,
      password: ''
    }
  }, 300)
}

// 监听文件变化，自动生成提取码
watch(() => props.file, (newFile) => {
  if (newFile && form.value.needPassword && !form.value.password) {
    generatePassword()
  }
})
</script>

<style scoped>
.file-info {
  display: flex;
  align-items: center;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>

