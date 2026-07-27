<template>
  <div class="recipe-detail-container" v-loading="loading">
    <div v-if="detail" class="detail-card">
      <!-- 标题区 -->
      <h1 class="title">{{ detail.title }}</h1>

      <!-- 作者信息 -->
      <div class="author-info">
        <el-avatar :size="40" :src="detail.userAvatar" />
        <div class="meta">
          <span class="name">{{ detail.userName }}</span>
          <span class="time">{{ formatDate(detail.createdAt) }}</span>
        </div>
      </div>

      <!-- 媒体展示（图片/视频） -->
      <div class="media">
        <ImageDisplay
          v-if="detail.type === 'image' && detail.imageUrl"
          :imgurl="detail.imageUrl"
          class="main-image"
        />
        <video
          v-else-if="detail.type === 'video' && detail.resourceId"
          :src="detail.resourceId"
          controls
          class="video-player"
        />
        <div v-else class="no-media">暂无图片</div>
      </div>

      <!-- 正文内容 -->
      <div class="content-text" v-html="formatContent(detail.content)"></div>

      <!-- 标签与难度 -->
      <div class="tags" v-if="detail.tags">
        <el-tag v-for="tag in parseTags(detail.tags)" :key="tag" size="small">{{ tag }}</el-tag>
      </div>
      <div class="difficulty" v-if="detail.difficulty">
        难度：{{ difficultyMap[detail.difficulty] }}
      </div>

      <!-- 互动栏 -->
      <div class="actions">
        <el-button
          :type="detail.liked ? 'primary' : 'default'"
          :icon="Star"
          @click="toggleLike"
        >
          {{ detail.likeCount }}
        </el-button>
        <el-button
          :type="detail.favorited ? 'primary' : 'default'"
          :icon="Star"
          @click="toggleFavorite"
        >
          收藏 {{ detail.favoriteCount }}
        </el-button>
        <el-button
          v-if="isOwner"
          type="danger"
          :icon="Delete"
          @click="handleDelete"
        >
          删除
        </el-button>
      </div>

      <!-- 评论组件 -->
      <CommentSection
        resource-type="forum_share"
        :resource-id="detail.id"
        :current-user-id="currentUserId"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Star, Delete } from '@element-plus/icons-vue'
import ImageDisplay from '@/components/ImageDisplay.vue'
import CommentSection from '@/components/CommentSection.vue'
import { getShareDetail, toggleShareLike, toggleShareFavorite, deleteShare } from '@/api/forum'

const route = useRoute()
const router = useRouter()
const id = route.params.id as string

const loading = ref(false)
const detail = ref<any>(null)
const currentUserId = 1 // 临时，实际应从登录状态获取

// 难度映射
const difficultyMap: Record<string, string> = {
  easy: '简单',
  medium: '中等',
  hard: '困难'
}

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${d.getMonth() + 1}-${d.getDate()} ${d.getHours()}:${d.getMinutes()}`
}

// 将换行转为 <br>
const formatContent = (content: string) => content.replace(/\n/g, '<br>')

// 解析 tags JSON 字符串
const parseTags = (tags: string) => {
  try {
    return JSON.parse(tags)
  } catch {
    return tags ? tags.split(',') : []
  }
}

// 是否为当前用户发布
const isOwner = computed(() => detail.value && detail.value.userId === currentUserId)

// 加载详情
const loadDetail = async () => {
  loading.value = true
  try {
    const res = await getShareDetail(id)
    console.log('分享详情', res)
    detail.value = res
  } catch (error) {
    ElMessage.error('加载分享详情失败')
  } finally {
    loading.value = false
  }
}

// 点赞
const toggleLike = async () => {
  try {
    await toggleShareLike(id)
    detail.value.liked = !detail.value.liked
    detail.value.likeCount += detail.value.liked ? 1 : -1
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 收藏
const toggleFavorite = async () => {
  try {
    await toggleShareFavorite(id)
    detail.value.favorited = !detail.value.favorited
    detail.value.favoriteCount += detail.value.favorited ? 1 : -1
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 删除
const handleDelete = async () => {
  try {
    await ElMessageBox.confirm('确定删除该分享吗？', '提示', { type: 'warning' })
    await deleteShare(id)
    ElMessage.success('删除成功')
    router.push('/forum/share')
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped>
.recipe-detail-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}
.detail-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
}
.title {
  font-size: 28px;
  margin: 0 0 20px;
}
.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}
.meta {
  display: flex;
  flex-direction: column;
}
.name {
  font-weight: 500;
}
.time {
  font-size: 12px;
  color: #999;
}
.media {
  margin: 20px 0;
}
.main-image {
  width: 100%;
  max-height: 500px;
  object-fit: contain;
  border-radius: 8px;
}
.video-player {
  width: 100%;
  max-height: 500px;
  border-radius: 8px;
}
.no-media {
  text-align: center;
  color: #999;
  padding: 40px;
  background: #f5f5f5;
  border-radius: 8px;
}
.content-text {
  font-size: 16px;
  line-height: 1.6;
  margin: 24px 0;
}
.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}
.difficulty {
  margin-bottom: 24px;
  color: #666;
}
.actions {
  display: flex;
  gap: 16px;
  margin: 24px 0;
  border-top: 1px solid #eee;
  padding-top: 24px;
}
</style>