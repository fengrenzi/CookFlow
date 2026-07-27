<template>
  <div class="activity-detail-container">
    <div v-loading="loading" class="detail-card">
      <!-- 头部 banner -->
      <div class="hero">
        <ImageDisplay :src="activity.bannerUrl" class="banner" />
        <div class="overlay">
          <h1>{{ activity.title }}</h1>
        </div>
      </div>

      <!-- 基本信息 -->
      <div class="info">
        <div class="meta">
          <span><el-icon><Timer /></el-icon> {{ formatDate(activity.startTime) }} - {{ formatDate(activity.endTime) }}</span>
          <span><el-icon><User /></el-icon> {{ activity.userName }}</span>
          <span><el-icon><User /></el-icon> {{ activity.participantCount }} 人参与</span>
        </div>
        <div class="status-area">
          <div v-if="status === 'upcoming'" class="countdown">
            <el-icon><Timer /></el-icon> 距离开始: {{ countdown }}
          </div>
          <div v-else-if="status === 'ongoing'" class="countdown">
            <el-icon><Timer /></el-icon> 距离结束: {{ countdown }}
          </div>
          <el-button
            v-if="status === 'upcoming'"
            type="primary"
            @click="toggleJoin"
            :class="{ 'cancel': isJoined }"
          >
            {{ isJoined ? '取消报名' : '立即报名' }}
          </el-button>
          <el-button v-else-if="status === 'ongoing'" type="primary" disabled>
            {{ isJoined ? '已报名' : '活动已开始' }}
          </el-button>
          <el-button v-else type="primary" disabled>活动已结束</el-button>
        </div>
      </div>

      <!-- 活动详情 -->
      <div class="description">
        <h2>活动详情</h2>
        <p>{{ activity.summary }}</p>
      </div>

      <!-- 互动区域（活动进行中时显示分享和点赞榜） -->
      <div v-if="status !== 'ended'" class="interaction">
        <div class="shares-section">
          <div class="section-header">
            <h2>参与者分享</h2>
            <el-button type="primary" size="small" @click="openShareDialog">发布分享</el-button>
            <div class="sort">
              <el-radio-group v-model="shareSort" size="small">
                <el-radio-button label="time">时间排序</el-radio-button>
                <el-radio-button label="likes">点赞排序</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div class="shares-list">
            <div v-for="share in sortedShares" :key="share.id" class="share-item">
              <div class="share-author">
                <el-avatar :size="32" :src="share.userAvatar" />
                <div>
                  <div class="name">{{ share.userName }}</div>
                  <div class="time">{{ formatDate(share.createdAt) }}</div>
                </div>
              </div>
              <div class="share-content">{{ share.content }}</div>
              <div class="share-actions">
                <el-button
                  size="small"
                  :type="share.liked ? 'primary' : 'default'"
                  @click="toggleShareLike(share.id)"
                >
                  <el-icon><Star /></el-icon> {{ share.likeCount }}
                </el-button>
              </div>
            </div>
          </div>
        </div>
        <div v-if="status === 'ongoing'" class="ranking-section">
          <h2>点赞榜</h2>
          <div class="ranking-list">
            <div v-for="(share, idx) in top5Shares" :key="share.id" class="ranking-item">
              <div class="rank" :class="['rank-' + (idx+1)]">{{ idx+1 }}</div>
              <div class="user">{{ share.userName }}</div>
              <div class="score">{{ share.likeCount }} 赞</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 活动未开始时显示建议评论区 -->
      <div v-if="status === 'upcoming'" class="comments-section">
        <div class="section-header">
          <h2>活动建议</h2>
          <el-button type="primary" size="small" @click="openCommentDialog">提交建议</el-button>
        </div>
        <div class="comments-list">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <div class="comment-header">
              <el-avatar :size="24" :src="comment.userAvatar" />
              <span class="name">{{ comment.userName }}</span>
              <span class="time">{{ formatDate(comment.createdAt) }}</span>
            </div>
            <div class="comment-content">{{ comment.content }}</div>
            <div class="comment-actions">
              <el-button size="small" text @click="toggleCommentLike(comment.id)">
                <el-icon><Star /></el-icon> {{ comment.likeCount }}
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 发布分享弹窗 -->
      <el-dialog v-model="shareDialogVisible" title="发布分享" width="500px">
        <el-input v-model="newShare.content" type="textarea" rows="4" placeholder="请输入分享内容..." />
        <el-input v-model="newShare.imageUrl" placeholder="图片URL（可选）" class="mt-2" />
        <template #footer>
          <el-button @click="shareDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitShare">发布</el-button>
        </template>
      </el-dialog>

      <!-- 提交建议弹窗 -->
      <el-dialog v-model="commentDialogVisible" title="提交建议" width="500px">
        <el-input v-model="newComment" type="textarea" rows="4" placeholder="请输入您的建议..." />
        <template #footer>
          <el-button @click="commentDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitComment">提交</el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Timer, User, Star } from '@element-plus/icons-vue'
import ImageDisplay from '@/components/ImageDisplay.vue'
import {
  getActivityDetail,
  getActivityComments,
  getActivityShares,
  submitActivityComment,
  submitActivityShare,
  joinActivity,
  cancelJoinActivity,
  likeComment
} from '@/api/forum'

const route = useRoute()
const id = route.params.id as string

const loading = ref(false)
const activity = ref<any>({})
const comments = ref<any[]>([])
const shares = ref<any[]>([])
const isJoined = ref(false)
const status = ref<'upcoming' | 'ongoing' | 'ended'>('upcoming')
const countdown = ref('')
let timer: any = null

const shareSort = ref('time')
const sortedShares = computed(() => {
  const list = [...shares.value]
  if (shareSort.value === 'likes') {
    return list.sort((a, b) => b.likeCount - a.likeCount)
  }
  return list.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
})
const top5Shares = computed(() => sortedShares.value.slice(0, 5))

const shareDialogVisible = ref(false)
const newShare = ref({ content: '', imageUrl: '' })
const commentDialogVisible = ref(false)
const newComment = ref('')

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getMonth()+1}/${d.getDate()} ${d.getHours()}:${d.getMinutes()}`
}

const updateCountdown = () => {
  const now = Date.now()
  const start = new Date(activity.value.startTime).getTime()
  const end = new Date(activity.value.endTime).getTime()
  if (now < start) {
    status.value = 'upcoming'
    const diff = start - now
    const days = Math.floor(diff / (1000 * 60 * 60 * 24))
    const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
    const seconds = Math.floor((diff % (1000 * 60)) / 1000)
    countdown.value = `${days}天 ${hours}时 ${minutes}分 ${seconds}秒`
  } else if (now >= start && now <= end) {
    status.value = 'ongoing'
    const diff = end - now
    const days = Math.floor(diff / (1000 * 60 * 60 * 24))
    const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
    const seconds = Math.floor((diff % (1000 * 60)) / 1000)
    countdown.value = `${days}天 ${hours}时 ${minutes}分 ${seconds}秒`
  } else {
    status.value = 'ended'
    countdown.value = '活动已结束'
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const detailRes = await getActivityDetail(id)
    activity.value = detailRes
    const commentsRes = await getActivityComments(id)
    comments.value = commentsRes || []
    const sharesRes = await getActivityShares(id)
    shares.value = sharesRes || []
    console.log('活动详情', activity.value)
    console.log('活动建议', comments.value)
    console.log('参与者分享', shares.value)
    isJoined.value = false
    updateCountdown()
    if (timer) clearInterval(timer)
    timer = setInterval(updateCountdown, 1000)
  } catch (error) {
    ElMessage.error('加载活动失败')
  } finally {
    loading.value = false
  }
}

const toggleJoin = async () => {
  try {
    if (isJoined.value) {
      await cancelJoinActivity(id)
      isJoined.value = false
      activity.value.participantCount--
    } else {
      await joinActivity(id)
      isJoined.value = true
      activity.value.participantCount++
    }
    ElMessage.success(isJoined.value ? '报名成功' : '已取消报名')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const toggleShareLike = async (shareId: string) => {
  try {
    await likeComment(shareId)
    const share = shares.value.find(s => s.id === shareId)
    if (share) {
      share.liked = !share.liked
      share.likeCount += share.liked ? 1 : -1
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const toggleCommentLike = async (commentId: string) => {
  try {
    await likeComment(commentId)
    const comment = comments.value.find(c => c.id === commentId)
    if (comment) {
      comment.liked = !comment.liked
      comment.likeCount += comment.liked ? 1 : -1
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const openShareDialog = () => {
  newShare.value = { content: '', imageUrl: '' }
  shareDialogVisible.value = true
}
const submitShare = async () => {
  if (!newShare.value.content.trim()) {
    ElMessage.warning('请输入分享内容')
    return
  }
  try {
    const res = await submitActivityShare(id, newShare.value)
    shares.value.unshift(res.data)
    shareDialogVisible.value = false
    ElMessage.success('分享发布成功')
  } catch (error) {
    ElMessage.error('发布失败')
  }
}

const openCommentDialog = () => {
  newComment.value = ''
  commentDialogVisible.value = true
}
const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入建议内容')
    return
  }
  try {
    const res = await submitActivityComment(id, { content: newComment.value })
    comments.value.unshift(res.data)
    commentDialogVisible.value = false
    ElMessage.success('建议提交成功')
  } catch (error) {
    ElMessage.error('提交失败')
  }
}

onMounted(() => {
  loadData()
})
onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.activity-detail-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}
.detail-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
}
.hero {
  position: relative;
  height: 300px;
}
.banner {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0,0,0,0.7));
  color: white;
  padding: 20px;
}
.info {
  padding: 20px;
}
.meta {
  display: flex;
  gap: 24px;
  margin-bottom: 20px;
  color: #666;
}
.status-area {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.countdown {
  color: #f56c6c;
  font-weight: 500;
}
.description {
  padding: 20px;
  border-top: 1px solid #eee;
}
.interaction {
  display: flex;
  gap: 24px;
  padding: 20px;
  border-top: 1px solid #eee;
}
.shares-section {
  flex: 2;
}
.ranking-section {
  flex: 1;
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.share-item, .comment-item {
  border-bottom: 1px solid #f0f0f0;
  padding: 12px 0;
}
.share-author, .comment-header {
  display: flex;
  gap: 12px;
  margin-bottom: 8px;
}
.share-content, .comment-content {
  margin-left: 44px;
  margin-bottom: 8px;
}
.share-actions, .comment-actions {
  margin-left: 44px;
}
.ranking-list .ranking-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 0;
}
.rank {
  width: 28px;
  height: 28px;
  line-height: 28px;
  text-align: center;
  border-radius: 50%;
  font-weight: bold;
}
.rank-1 { background: #ffd700; color: #fff; }
.rank-2 { background: #c0c0c0; color: #fff; }
.rank-3 { background: #cd7f32; color: #fff; }
.mt-2 { margin-top: 8px; }
</style>