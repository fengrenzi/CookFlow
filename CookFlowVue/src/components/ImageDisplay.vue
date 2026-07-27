<template>
  <div class="image-display-container">
    <template v-if="imgurl">
      <img
        :src="imgurl"
        :alt="altText"
        class="display-media"
        :style="{ objectFit: fit }"
        @error="handleError"
        @load="handleLoad"
      />
      <div v-if="isLoading" class="loading-overlay">
        <el-icon class="loading-icon"><Loading /></el-icon>
      </div>
      <div v-if="hasError" class="error-overlay">
        <el-icon><Picture /></el-icon>
        <span>加载失败</span>
      </div>
    </template>
    <div v-else class="empty-overlay">
      <el-icon><Picture /></el-icon>
      <span>暂无图片</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, defineProps } from 'vue'
import { Loading, Picture } from '@element-plus/icons-vue'

const props = defineProps<{
  imgurl?: string
  alt?: string
  fit?: 'fill' | 'contain' | 'cover' | 'none' | 'scale-down'
}>()

const altText = computed(() => props.alt || '图片')
const isLoading = ref(false)
const hasError = ref(false)

const handleLoad = () => {
  isLoading.value = false
  hasError.value = false
}

const handleError = () => {
  isLoading.value = false
  hasError.value = true
}

watch(() => props.imgurl, (newUrl) => {
  if (newUrl) {
    isLoading.value = true
    hasError.value = false
  } else {
    isLoading.value = false
    hasError.value = false
  }
}, { immediate: true })
</script>

<style scoped>
.image-display-container {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.display-media {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 默认值 */
}
.loading-overlay,
.error-overlay,
.empty-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.1);
  color: #999;
}
.loading-icon {
  animation: rotate 1s linear infinite;
}
@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>