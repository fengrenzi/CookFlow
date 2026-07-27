<template>
  <div class="question-detail-container">
    <div v-loading="loading" class="detail-card">
      <!-- 问题详情区域 -->
      <div class="question-section">
        <!-- 作者与统计 -->
        <div class="author-stats">
          <div class="author-info">
            <el-avatar :size="48" :src="question.userAvatar" />
            <div class="author-meta">
              <div class="name">{{ question.userName }}</div>
              <div class="time">{{ formatDate(question.createdAt) }}</div>
            </div>
          </div>
          <div class="stats">
            <div class="stat-item">
              <el-icon><View /></el-icon>
              <span>{{ question.viewCount || 0 }} 浏览</span>
            </div>
            <div class="stat-item">
              <el-icon><Star /></el-icon>
              <span>{{ question.followCount || 0 }} 关注者</span>
            </div>
          </div>
        </div>

        <!-- 标题 -->
        <h1 class="title">{{ question.title }}</h1>

        <!-- 正文 -->
        <div class="content">{{ question.content }}</div>

        <!-- 标签 -->
        <div class="tags" v-if="question.tags && question.tags.length">
          <el-tag v-for="tag in question.tags" :key="tag" size="small" effect="plain">
            {{ tag }}
          </el-tag>
        </div>

        <!-- 互动按钮 -->
        <div class="actions">
          <el-button
            :type="isFollowing ? 'primary' : 'default'"
            :plain="!isFollowing"
            @click="toggleFollow"
          >
            关注问题 {{ question.followCount }}
          </el-button>
          <el-button
            :type="isGood ? 'primary' : 'default'"
            plain
            @click="toggleGood"
          >
            好问题 {{ question.goodCount }}
          </el-button>
          <el-button type="primary" plain @click="showAnswerModal = true" :icon="Edit">
            写回答
          </el-button>
        </div>
      </div>

      <!-- 回答列表区域 -->
      <div class="answers-section">
        <div class="answers-header">
          <h3>{{ answers.length }} 个回答</h3>
          <div class="sort">
            <el-dropdown @command="handleSortChange">
              <span class="sort-text">{{ sortText }} <el-icon><ArrowDown /></el-icon></span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="time">按时间排序</el-dropdown-item>
                  <el-dropdown-item command="likes">按点赞排序</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>

        <div v-for="answer in sortedAnswers" :key="answer.id" class="answer-card">
          <div class="answer-author">
            <el-avatar :size="36" :src="answer.userAvatar" />
            <div class="answerer-info">
              <div class="name">{{ answer.userName }}</div>
              <div class="time">{{ formatDate(answer.createdAt) }}</div>
            </div>
          </div>
          <div class="answer-content">{{ answer.content }}</div>
          <div class="answer-actions">
            <el-button
              :type="answer.liked ? 'primary' : 'default'"
              size="small"
              @click="toggleAnswerLike(answer.id)"
            >
              <el-icon><Star /></el-icon> {{ answer.likeCount }}
            </el-button>
            <el-button size="small" @click="toggleComments(answer.id)">
              <el-icon><ChatDotRound /></el-icon> {{ answer.commentCount }} 评论
            </el-button>
            <el-button
              v-if="isOwner && !answer.isAccepted && !question.isResolved"
              size="small"
              type="success"
              @click="acceptAnswer(answer.id)"
            >
              采纳
            </el-button>
          </div>

          <!-- 评论区域 -->
          <div v-if="showComments[answer.id]" class="comments-section">
            <div v-for="comment in comments[answer.id] || []" :key="comment.id" class="comment">
              <div class="comment-header">
                <el-avatar :size="28" :src="comment.userAvatar" />
                <div class="comment-info">
                  <span class="name">{{ comment.userName }}</span>
                  <span class="time">{{ formatDate(comment.createdAt) }}</span>
                </div>
              </div>
              <div class="comment-content">{{ comment.content }}</div>
              <div class="comment-actions">
                <el-button size="small" text @click="toggleCommentLike(answer.id, comment.id)">
                  <el-icon><Star /></el-icon> {{ comment.likeCount }}
                </el-button>
              </div>
            </div>
            <div class="comment-input">
              <el-input
                v-model="commentInputs[answer.id]"
                type="textarea"
                :rows="2"
                placeholder="写下你的评论..."
                maxlength="200"
                resize="none"
              />
              <el-button type="primary" size="small" @click="submitComment(answer.id)">发表</el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 写回答弹窗 -->
      <el-dialog v-model="showAnswerModal" title="写回答" width="600px">
        <el-input
          v-model="newAnswer"
          type="textarea"
          :rows="8"
          placeholder="分享您的见解，帮助提问者解决问题..."
          maxlength="2000"
          show-word-limit
        />
        <template #footer>
          <el-button @click="showAnswerModal = false">取消</el-button>
          <el-button type="primary" @click="submitAnswer">提交回答</el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Star, View, Edit, ArrowDown, ChatDotRound } from '@element-plus/icons-vue'
import {
  getQuestionDetail,
  getAnswers,
  getComments,
  submitAnswer,
  submitComment,
  likeComment,
  toggleQuestionFollow,
  toggleQuestionFavorite as toggleGood,
  likeAnswer,
  acceptAnswer as apiAcceptAnswer
} from '@/api/forum'

const route = useRoute()
const router = useRouter()
const id = route.params.id as string

const loading = ref(false)
const question = ref<any>({})
const answers = ref<any[]>([])
const comments = ref<Record<string, any[]>>({})
const showComments = ref<Record<string, boolean>>({})
const isFollowing = ref(false)
const isGood = ref(false)
const currentUserId = 1 // 临时，实际从登录状态获取

const sortType = ref('time')
const sortText = computed(() => sortType.value === 'time' ? '按时间排序' : '按点赞排序')
const sortedAnswers = computed(() => {
  const list = [...answers.value]
  if (sortType.value === 'likes') {
    return list.sort((a, b) => b.likeCount - a.likeCount)
  }
  return list.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
})

const showAnswerModal = ref(false)
const newAnswer = ref('')
const commentInputs = ref<Record<string, string>>({})

const isOwner = computed(() => question.value.userId === currentUserId)

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${d.getMonth() + 1}-${d.getDate()} ${d.getHours()}:${d.getMinutes()}`
}

const loadDetail = async () => {
  loading.value = true
  try {
    const res = await getQuestionDetail(id)
    question.value = res
    const ansRes = await getAnswers(id)
    answers.value = ansRes || []
    // 后续可获取用户状态
  } catch (error) {
    ElMessage.error('加载问题失败')
  } finally {
    loading.value = false
  }
}

const toggleComments = async (answerId: string) => {
  const current = showComments.value[answerId]
  if (!current && !comments.value[answerId]) {
    try {
      const res = await getComments(answerId)
      comments.value[answerId] = res.data || []
    } catch (error) {
      ElMessage.error('加载评论失败')
      return
    }
  }
  showComments.value[answerId] = !current
}

const toggleFollow = async () => {
  try {
    await toggleQuestionFollow(id)
    isFollowing.value = !isFollowing.value
    question.value.followCount += isFollowing.value ? 1 : -1
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const toggleGood = async () => {
  try {
    await toggleGood(id)
    isGood.value = !isGood.value
    question.value.goodCount += isGood.value ? 1 : -1
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const toggleAnswerLike = async (answerId: string) => {
  try {
    await likeAnswer(answerId)
    const answer = answers.value.find(a => a.id === answerId)
    if (answer) {
      answer.liked = !answer.liked
      answer.likeCount += answer.liked ? 1 : -1
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const submitAnswer = async () => {
  if (!newAnswer.value.trim()) {
    ElMessage.warning('请输入回答内容')
    return
  }
  try {
    const res = await submitAnswer(id, { content: newAnswer.value })
    answers.value.unshift(res.data)
    showAnswerModal.value = false
    newAnswer.value = ''
    ElMessage.success('回答提交成功')
  } catch (error) {
    ElMessage.error('提交失败')
  }
}

const submitComment = async (answerId: string) => {
  const content = commentInputs.value[answerId]?.trim()
  if (!content) return
  try {
    const res = await submitComment(answerId, { content })
    if (!comments.value[answerId]) comments.value[answerId] = []
    comments.value[answerId].unshift(res.data)
    commentInputs.value[answerId] = ''
    const answer = answers.value.find(a => a.id === answerId)
    if (answer) answer.commentCount++
    ElMessage.success('评论成功')
  } catch (error) {
    ElMessage.error('评论失败')
  }
}

const toggleCommentLike = async (answerId: string, commentId: string) => {
  try {
    await likeComment(commentId)
    const comment = comments.value[answerId]?.find(c => c.id === commentId)
    if (comment) {
      comment.liked = !comment.liked
      comment.likeCount += comment.liked ? 1 : -1
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const acceptAnswer = async (answerId: string) => {
  try {
    await apiAcceptAnswer(answerId)
    ElMessage.success('已采纳该回答')
    await loadDetail() // 刷新数据
  } catch (error) {
    ElMessage.error('采纳失败')
  }
}

const handleSortChange = (cmd: string) => {
  sortType.value = cmd
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped>
.question-detail-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 24px;
  background-color: #f5f7fa;
}

.detail-card {
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

/* 问题区域 */
.question-section {
  padding: 28px 32px;
  border-bottom: 1px solid #eef2f6;
}

.author-stats {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 16px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-meta {
  display: flex;
  flex-direction: column;
}

.author-meta .name {
  font-weight: 600;
  color: #1f2f3d;
  font-size: 16px;
}

.author-meta .time {
  font-size: 13px;
  color: #8a99aa;
}

.stats {
  display: flex;
  gap: 20px;
  color: #5d6e85;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #1e2a3a;
  margin: 0 0 20px 0;
  line-height: 1.3;
}

.content {
  font-size: 16px;
  line-height: 1.6;
  color: #2c3e50;
  margin-bottom: 24px;
  white-space: pre-wrap;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 28px;
}

.actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding-top: 8px;
}

/* 回答区域 */
.answers-section {
  padding: 28px 32px;
}

.answers-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 12px;
}

.answers-header h3 {
  font-size: 20px;
  font-weight: 600;
  color: #1e2a3a;
  margin: 0;
}

.sort-text {
  font-size: 14px;
  color: #5d6e85;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

/* 回答卡片 */
.answer-card {
  background: #fafbfc;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  transition: all 0.2s;
  border: 1px solid #edf2f7;
}

.answer-card:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  border-color: #e2e8f0;
}

.answer-author {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.answerer-info .name {
  font-weight: 600;
  color: #1f2f3d;
  font-size: 15px;
}

.answerer-info .time {
  font-size: 12px;
  color: #8a99aa;
}

.answer-content {
  font-size: 15px;
  line-height: 1.6;
  color: #2c3e50;
  margin-bottom: 16px;
  white-space: pre-wrap;
}

.answer-actions {
  display: flex;
  gap: 16px;
}

/* 评论区域 */
.comments-section {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #e9ecef;
}

.comment {
  padding: 12px 0;
  border-bottom: 1px solid #f0f2f5;
}

.comment:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.comment-info {
  display: flex;
  flex-direction: column;
}

.comment-info .name {
  font-weight: 500;
  font-size: 13px;
  color: #1f2f3d;
}

.comment-info .time {
  font-size: 11px;
  color: #8a99aa;
}

.comment-content {
  font-size: 14px;
  line-height: 1.5;
  color: #2c3e50;
  margin-left: 38px;
  margin-bottom: 8px;
}

.comment-actions {
  margin-left: 38px;
}

.comment-input {
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.comment-input :deep(.el-textarea__inner) {
  border-radius: 8px;
  background-color: #ffffff;
}

/* 按钮微调 */
:deep(.el-button--small) {
  border-radius: 20px;
  padding: 6px 14px;
}

:deep(.el-button.is-plain) {
  background-color: #ffffff;
}
</style>