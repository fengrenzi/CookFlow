<template>
  <div class="comments-section">
    <div class="section-header">
      <h2>评论 ({{ total }})</h2>
      <div class="comment-actions">
        <el-button type="primary" size="small" @click="showCommentForm = true" :icon="Edit">
          发布分享
        </el-button>
        <el-radio-group v-model="sortType" size="small" @change="changeSort">
          <el-radio-button label="hot">最热</el-radio-button>
          <el-radio-button label="time">最新</el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <!-- 评论列表 -->
    <div v-loading="loading" class="comments-list">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <div class="comment-header">
          <div class="comment-user">
            <el-avatar size="small" :src="comment.avatarUrl" :icon="User" />
            <span class="username">{{ comment.user.userName }}</span>
            <span class="comment-time">{{ formatTime(comment.createdAt) }}</span>
          </div>
          <div v-if="comment.rating" class="comment-rating">
            <el-rate v-model="comment.rating" disabled size="small" show-score text-color="#ff9900" score-template="{value}星" />
          </div>
        </div>
        <div class="comment-content">
          <TruncatedText :text="comment.content" :max-lines="3" />
        </div>

        <!-- 评论图片 -->
        <div v-if="comment.imageUrls && comment.imageUrls.length" class="comment-images">
          <ImageDisplay
            v-for="(url, idx) in comment.imageUrls.slice(0, 5)"
            :key="idx"
            :imgurl="url"
            class="comment-image"
            alt="评论图片"
          />
        </div>

        <!-- 回复列表（简单展示） -->
        <div v-if="comment.replies && comment.replies.length" class="replies">
          <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
            <div class="reply-header">
              <span class="reply-user">{{ reply.user.userName }}</span>
              <span class="reply-time">{{ formatTime(reply.createdAt) }}</span>
            </div>
            <div class="reply-content">{{ reply.content }}</div>
            <div class="reply-actions">
              <button @click="showReplyInput(comment.id)">回复</button>
            </div>
          </div>
          <div v-if="comment.replyCount > comment.replies.length" class="more-replies">
            <a href="javascript:;" @click="loadMoreReplies(comment.id)">查看全部 {{ comment.replyCount }} 条回复</a>
          </div>
        </div>

        <!-- 回复输入框 -->
        <div v-if="replyTarget === comment.id" class="reply-input">
          <textarea v-model="replyContent" placeholder="回复 {{ comment.user.userName }}..." rows="2"></textarea>
          <div class="reply-actions">
            <button @click="submitReply(comment.id)">发送</button>
            <button @click="cancelReply">取消</button>
          </div>
        </div>

        <div class="comment-footer">
          <button class="like-button" @click="handleLike(comment)" :class="{ liked: comment.isLiked }">
            <el-icon><Star :class="{ filled: comment.isLiked }" /></el-icon>
            <span>{{ comment.likes }}</span>
          </button>
          <button class="reply-button" @click="showReplyInput(comment.id)">回复</button>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination" v-if="total > pageSize">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="page"
        @current-change="handlePageChange"
      />
    </div>

    <!-- 发布评论表单 -->
    <el-dialog v-model="showCommentForm" title="发布你的评价" width="600px">
      <el-form label-width="80px">
        <el-form-item label="评分" v-if="showRating">
          <el-rate v-model="newComment.rating" size="large"></el-rate>
        </el-form-item>
        <el-form-item label="评论内容">
          <el-input v-model="newComment.content" type="textarea" rows="4" placeholder="分享你的体验..."></el-input>
        </el-form-item>
        <el-form-item label="上传图片">
          <div class="image-upload-container">
            <el-upload
              action="#"
              :http-request="handleImageUpload"
              :before-upload="beforeUpload"
              :file-list="imageFileList"
              list-type="picture-card"
              :limit="5"
              :on-exceed="handleExceed"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
            <div class="image-upload-tip">最多上传5张图片，支持jpg、png格式，大小不超过5MB</div>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCommentForm = false">取消</el-button>
        <el-button type="primary" :loading="publishing" @click="publishComment">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit, User, Star, Plus } from '@element-plus/icons-vue'
import TruncatedText from './TruncatedText.vue'
import ImageDisplay from './ImageDisplay.vue'
import { getComments, addComment, likeComment } from '@/api/comment'
import { uploadImage } from '@/api/image'   // 假设存在图片上传API

const props = defineProps<{
  resourceType: string   // 'book', 'recipe', 'activity', 'forum'
  resourceId: string
  showRating?: boolean   // 是否显示评分
}>()

// 评论数据
const comments = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const sortType = ref<'hot' | 'time'>('hot')
const publishing = ref(false)

// 回复相关
const replyTarget = ref<string | null>(null)
const replyContent = ref('')

// 发布评论表单
const showCommentForm = ref(false)
const newComment = ref({
  rating: 5,
  content: '',
  imageIds: [] as string[]
})
const imageFileList = ref<any[]>([])

// 加载评论列表
const loadComments = async () => {
  loading.value = true
  try {
    const params = {
      resourceType: props.resourceType,
      resourceId: props.resourceId,
      pageNum: page.value,
      pageSize: pageSize.value,
      sort: sortType.value === 'hot' ? 'likes_desc' : 'created_desc'
    }
    const res = await getComments(params)
    comments.value = res.rows || []
    total.value = res.total || 0
  } catch (error) {
    console.error('加载评论失败', error)
    ElMessage.error('加载评论失败')
  } finally {
    loading.value = false
  }
}

// 切换排序
const changeSort = () => {
  page.value = 1
  loadComments()
}

// 分页
const handlePageChange = (val: number) => {
  page.value = val
  loadComments()
}

// 点赞
const handleLike = async (comment: any) => {
  try {
    const res = await likeComment(comment.id)
    comment.likes = res.likes
    comment.isLiked = res.isLiked
  } catch (error) {
    ElMessage.error('点赞失败')
  }
}

// 显示回复输入框
const showReplyInput = (commentId: string) => {
  replyTarget.value = commentId
  replyContent.value = ''
}

// 取消回复
const cancelReply = () => {
  replyTarget.value = null
  replyContent.value = ''
}

// 提交回复
const submitReply = async (parentId: string) => {
  if (!replyContent.value.trim()) return
  try {
    await addComment({
      resourceType: props.resourceType,
      resourceId: props.resourceId,
      parentId,
      content: replyContent.value
    })
    cancelReply()
    loadComments() // 重新加载
    ElMessage.success('回复成功')
  } catch (error) {
    ElMessage.error('回复失败')
  }
}

// 图片上传
const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB')
    return false
  }
  return true
}

const handleImageUpload = async (options: any) => {
  try {
    const res = await uploadImage(options.file)
    newComment.value.imageIds.push(res.id)
    imageFileList.value.push({
      name: res.originalName,
      url: res.url,
      uid: res.id
    })
    ElMessage.success('上传成功')
  } catch (error) {
    ElMessage.error('上传失败')
  }
}

const handleExceed = () => {
  ElMessage.warning('最多只能上传5张图片')
}

// 发布评论
const publishComment = async () => {
  if (!newComment.value.content.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  publishing.value = true
  try {
    await addComment({
      resourceType: props.resourceType,
      resourceId: props.resourceId,
      content: newComment.value.content,
      rating: props.showRating ? newComment.value.rating : undefined,
      imageIds: newComment.value.imageIds
    })
    showCommentForm.value = false
    newComment.value = { rating: 5, content: '', imageIds: [] }
    imageFileList.value = []
    loadComments()
    ElMessage.success('评论发布成功')
  } catch (error) {
    ElMessage.error('发布失败')
  } finally {
    publishing.value = false
  }
}

// 格式化时间
const formatTime = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  if (diff < 60 * 1000) return '刚刚'
  if (diff < 60 * 60 * 1000) return `${Math.floor(diff / (60 * 1000))}分钟前`
  if (diff < 24 * 60 * 60 * 1000) return `${Math.floor(diff / (60 * 60 * 1000))}小时前`
  if (diff < 30 * 24 * 60 * 60 * 1000) return `${Math.floor(diff / (24 * 60 * 60 * 1000))}天前`
  return date.toLocaleDateString()
}

// 监听资源变化，重新加载
watch(() => [props.resourceType, props.resourceId], () => {
  page.value = 1
  loadComments()
}, { immediate: true })
</script>

<style scoped>
.comments-section {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 2rem;
  margin-top: 20px;
  width: 100%;
}
.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1.5rem;
}
.comment-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
}
.comments-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}
.comment-item {
  padding: 1.5rem;
  background: #f9f9f9;
  border-radius: 8px;
  transition: all 0.3s ease;
}
.comment-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}
.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}
.comment-user {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
.username {
  font-weight: 500;
  color: #333;
}
.comment-time {
  color: #909399;
  font-size: 0.875rem;
}
.comment-content {
  color: #333;
  line-height: 1.6;
  margin-bottom: 1rem;
  white-space: pre-wrap;
}
.comment-images {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
  flex-wrap: wrap;
}
.comment-image {
  width: 200px;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.comment-footer {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}
.like-button,
.reply-button {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.25rem 0.75rem;
  background: none;
  border: 1px solid #dcdfe6;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #606266;
}
.like-button:hover,
.reply-button:hover {
  border-color: #ff9900;
  color: #ff9900;
}
.like-button.liked {
  border-color: #ff9900;
  background-color: #fff7e6;
  color: #ff9900;
}
.reply-input {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
}
.reply-input textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  font-family: inherit;
}
.reply-actions {
  margin-top: 0.5rem;
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}
.replies {
  margin-top: 1rem;
  padding-left: 2rem;
  border-left: 2px solid #eee;
}
.reply-item {
  margin-bottom: 1rem;
  padding: 0.5rem;
  background: #f5f5f5;
  border-radius: 4px;
}
.reply-header {
  margin-bottom: 0.25rem;
}
.reply-user {
  font-weight: 500;
  font-size: 0.875rem;
  margin-right: 0.5rem;
}
.reply-time {
  color: #999;
  font-size: 0.75rem;
}
.reply-content {
  font-size: 0.9rem;
  line-height: 1.4;
  margin-bottom: 0.25rem;
}
.more-replies {
  margin-top: 0.5rem;
  text-align: right;
  font-size: 0.85rem;
}
.pagination {
  margin-top: 1.5rem;
  display: flex;
  justify-content: center;
}
.image-upload-tip {
  color: #909399;
  font-size: 0.9rem;
  margin-top: 0.5rem;
}
.image-upload-container {
  margin-top: 8px;
}
</style>