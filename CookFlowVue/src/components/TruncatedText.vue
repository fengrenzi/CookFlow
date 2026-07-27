<template>
  <div class="truncated-text-container">
    <div class="truncated-text-wrapper" :class="{ expanded: isExpanded }">
      <p 
        class="truncated-text"
        :style="textContainerStyle"
      >
        {{ isExpanded ? text : displayText }}
        <span 
          v-if="hasMore || isExpanded" 
          :class="isExpanded ? 'collapse-trigger' : 'expand-trigger'"
          @click.stop="toggleExpand"
        >
          {{ isExpanded ? collapseText : expandText }}
        </span>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';

// 组件属性
interface Props {
  text: string;
  maxLines?: number;
  expandText?: string;
  collapseText?: string;
  hasImage?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  maxLines: 3,
  expandText: ' 展开',
  collapseText: ' 收起',
  hasImage: false
});

// 组件事件
const emit = defineEmits<{
  (e: 'expand'): void;
  (e: 'collapse'): void;
}>();

// 响应式数据
const isExpanded = ref(false);
const displayText = ref('');
const hasMore = ref(false);

// 计算文本容器样式
const textContainerStyle = computed(() => {
  const style: Record<string, string | number> = {
    lineHeight: '1.6',
    whiteSpace: 'normal',
    wordBreak: 'break-word',
  };
  
  // 折叠状态下应用限制样式
  if (!isExpanded.value) {
    style.maxHeight = `calc(${props.maxLines} * 1.6em)`;
    style.overflow = 'hidden';
    style.textOverflow = 'clip';
  }
  
  return style;
});

// 简化的文本截断逻辑
const truncateText = (text: string, maxLines: number, hasImage: boolean): { displayText: string; hasMore: boolean } => {
  // 根据是否有图片来调整每行字符数估算
  // 有图片时每行显示更少的字符
  const charsPerLine = hasImage ? 25 : 40;
  const totalChars = charsPerLine * maxLines;
  
  if (!text || text.length <= totalChars) {
    return { displayText: text || '', hasMore: false };
  }
  
  const truncatedText = text.slice(0, totalChars - 3) + '...';
  return { displayText: truncatedText, hasMore: true };
};

// 更新显示文本
const updateDisplayText = () => {
  if (isExpanded.value) {
    displayText.value = props.text;
    hasMore.value = false;
  } else {
    const result = truncateText(props.text, props.maxLines, props.hasImage);
    displayText.value = result.displayText;
    hasMore.value = result.hasMore;
  }
};

// 切换展开/收起状态，@click.stop防止事件冒泡导致页面跳转
const toggleExpand = () => {
  isExpanded.value = !isExpanded.value;
  
  if (isExpanded.value) {
    emit('expand');
  } else {
    emit('collapse');
  }
  
  updateDisplayText();
};

// 监听属性变化
watch(() => props.text, () => updateDisplayText());
watch(() => props.maxLines, () => updateDisplayText());
watch(() => props.hasImage, () => updateDisplayText());

// 生命周期钩子
onMounted(() => {
  updateDisplayText();
});
</script>

<style scoped>
.truncated-text-container {
  position: relative;
}

.truncated-text-wrapper {
  width: 100%;
}

.truncated-text {
  margin: 0;
  padding: 0;
  line-height: 1.6;
  white-space: normal;
  word-break: break-word;
}

/* 展开状态下移除所有限制 */
.truncated-text-wrapper.expanded .truncated-text {
  max-height: none;
  overflow: visible;
}

.expand-trigger,
.collapse-trigger {
  color: #1677ff;
  cursor: pointer;
  user-select: none;
  margin-left: 2px;
  display: inline;
}

.expand-trigger:hover,
.collapse-trigger:hover {
  text-decoration: underline;
}
</style>