<template>
  <div class="forum-container">
    <div class="forum-layout">
      <!-- 左侧内容区 -->
      <div class="left-module">
        <!-- 分享区 -->
        <template v-if="currentType === 'share'">
          <div class="share-header">
            <div class="category-tabs">
              <el-radio-group v-model="shareCategory" @change="() => loadShares(true)">
                <el-radio-button value="">全部</el-radio-button>
                <el-radio-button value="cooking">烹饪技巧</el-radio-button>
                <el-radio-button value="recipe">菜谱分享</el-radio-button>
                <el-radio-button value="dessert">甜点烘焙</el-radio-button>
                <el-radio-button value="drink">饮品制作</el-radio-button>
                <el-radio-button value="restaurant">餐厅推荐</el-radio-button>
              </el-radio-group>
            </div>
            <div class="sort-dropdown">
              <el-select v-model="shareSortBy" @change="() => loadShares(true)" size="small">
                <el-option label="最新发布" value="latest" />
                <el-option label="最热" value="hot" />
              </el-select>
            </div>
          </div>

          <div class="waterfall-container" v-loading="shareLoading">
            <div class="waterfall-column" v-for="(col, idx) in shareColumns" :key="idx">
              <div
                v-for="item in col"
                :key="item.id"
                class="share-card"
                @click="goToShareDetail(item.id)"
              >
                <el-card :body-style="{ padding: 0 }" shadow="hover">
                  <div class="card-image">
                    <ImageDisplay
                      v-if="item.type === 'video' && item.imageUrl"
                      :cover-url="item.imageUrl"
                      :is-video="true"
                    />
                    <ImageDisplay
                      v-else-if="item.type === 'image' && item.imageUrl"
                      :imgurl="item.imageUrl"
                    />
                    <div v-else class="img-placeholder">
                      <el-icon><Picture /></el-icon>
                    </div>
                  </div>
                  <div class="card-info">
                    <h3>{{ item.title }}</h3>
                    <div class="meta">
                      <div class="author">
                        <el-avatar :size="24" :src="item.userAvatar" />
                        <span>{{ item.userName }}</span>
                      </div>
                      <div class="stats">
                        <el-icon><Star /></el-icon> {{ item.likeCount }}
                      </div>
                    </div>
                  </div>
                </el-card>
              </div>
            </div>
            <div v-if="shareHasMore" ref="shareLoadMoreTrigger" class="load-more-trigger"></div>
            <div v-if="shareLoadingMore" class="loading-more">加载更多...</div>
          </div>
        </template>

        <!-- 问答区 -->
        <template v-else-if="currentType === 'question'">
          <div class="question-list">
            <div
              v-for="item in questionItems"
              :key="item.id"
              class="question-card"
              @click="goToQuestionDetail(item.id)"
            >
              <div class="question-header">
                <h3>{{ item.title }}</h3>
                <span class="time">{{ formatDate(item.createdAt) }}</span>
              </div>
              <div class="question-body">
                <TruncatedText
                  :text="item.content"
                  :max-lines="3"
                  :has-image="false"
                  expand-text=" 展开"
                  collapse-text=" 收起"
                />
              </div>
              <div class="question-footer">
                <div class="footer-left">
                  <div class="author">
                    <el-avatar :size="24" :src="item.userAvatar" />
                    <span>{{ item.userName }}</span>
                  </div>
                  <span>{{ item.answerCount || 0 }} 回答</span>
                  <button class="action-btn" :class="{ active: item.favorited }" @click.stop="toggleQuestionFavorite(item.id)">
                    <el-icon><Star /></el-icon> 收藏
                  </button>
                  <button class="action-btn" :class="{ active: item.followed }" @click.stop="toggleQuestionFollow(item.id)">
                    <el-icon><Bell /></el-icon> 关注
                  </button>
                </div>
                <span class="views">{{ item.viewCount || 0 }} 浏览</span>
              </div>
            </div>
          </div>
          <div v-if="questionHasMore" ref="questionLoadMoreTrigger" class="load-more-trigger"></div>
          <div v-if="questionLoadingMore" class="loading-more">加载更多...</div>
        </template>

        <!-- 主题活动区 -->
        <template v-else-if="currentType === 'activity'">
          <div v-if="hotActivities.length" class="activity-carousel">
            <el-carousel :interval="5000" arrow="always" height="240px">
              <el-carousel-item v-for="act in hotActivities" :key="act.id">
                <div class="carousel-item" @click="goToActivityDetail(act.id)">
                  <ImageDisplay :imgurl="act.images?.[0]?.imageUrl" class="carousel-img" />
                  <div class="carousel-info">
                    <h3>{{ act.title }}</h3>
                    <p>{{ act.summary }}</p>
                    <span>{{ act.participantCount }} 人参与</span>
                  </div>
                </div>
              </el-carousel-item>
            </el-carousel>
          </div>

          <div class="activity-controls">
            <div class="category-tabs">
              <el-radio-group v-model="activityCategory" @change="() => loadActivities(true)">
                <el-radio-button value="">全部</el-radio-button>
                <el-radio-button value="线上比赛">线上比赛</el-radio-button>
                <el-radio-button value="线下活动">线下活动</el-radio-button>
                <el-radio-button value="打卡活动">打卡活动</el-radio-button>
              </el-radio-group>
            </div>
            <div class="sort-dropdown">
              <el-select v-model="activitySortBy" @change="() => loadActivities(true)" size="small">
                <el-option label="最新发布" value="latest" />
                <el-option label="即将开始" value="upcoming" />
                <el-option label="热门参与" value="hot" />
              </el-select>
            </div>
          </div>

          <div class="activity-list">
            <div
              v-for="act in activityItems"
              :key="act.id"
              class="activity-card"
              @click="goToActivityDetail(act.id)"
            >
              <ImageDisplay :imgurl="act.images?.[0]?.imageUrl" class="activity-banner" />
              <div class="activity-info">
                <h3>{{ act.title }}</h3>
                <p>{{ act.summary }}</p>
                <div class="time">{{ formatDate(act.startTime) }} 至 {{ formatDate(act.endTime) }}</div>
                <div class="meta">
                  <span>{{ act.participantCount }} 人参与</span>
                  <span class="tag">{{ act.tag }}</span>
                </div>
                <el-button
                  v-if="!act.participated"
                  size="small"
                  type="primary"
                  @click.stop="handleJoinActivity(act.id)"
                >参与活动</el-button>
                <el-button
                  v-else
                  size="small"
                  @click.stop="handleCancelJoinActivity(act.id)"
                >取消参与</el-button>
              </div>
            </div>
          </div>
          <div v-if="activityHasMore" ref="activityLoadMoreTrigger" class="load-more-trigger"></div>
          <div v-if="activityLoadingMore" class="loading-more">加载更多...</div>
        </template>
      </div>

      <!-- 右侧边栏 -->
      <div class="right-module">
        <div class="search-module">
          <el-input v-model="searchKeyword" placeholder="搜索内容..." prefix-icon="Search" @keyup.enter="handleSearch" />
        </div>
        <div class="action-module">
          <el-button type="primary" @click="openPublishDialog">{{ publishButtonText }}</el-button>
        </div>
        <div class="hot-tags">
          <div class="title">热门话题</div>
          <div class="tag-list">
            <el-tag v-for="tag in hotTags" :key="tag.id" size="small" @click="searchByTag(tag.name)">
              {{ tag.name }}
            </el-tag>
          </div>
        </div>
        <div class="active-users">
          <div class="title">活跃用户</div>
          <div class="user-list">
            <div v-for="u in activeUsers" :key="u.userId" class="user-item">
              <el-avatar :size="32" :src="u.avatar" />
              <span>{{ u.nickName || u.userName }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 发布分享弹窗 -->
    <ShareDialog v-model="showShareDialog" @success="() => loadShares(true)" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Picture, Star, Bell, Search } from '@element-plus/icons-vue'
import ImageDisplay from '@/components/ImageDisplay.vue'
import TruncatedText from '@/components/TruncatedText.vue'
import ShareDialog from '@/components/ShareDialog.vue'
import {
  getShareList,
  getQuestionList,
  getActivityList,
  toggleQuestionFavorite as apiToggleQuestionFavorite,
  toggleQuestionFollow as apiToggleQuestionFollow,
  joinActivity,
  cancelJoinActivity,
  getHotTags
} from '@/api/forum'
import { getActiveUsers } from '@/api/user'

const router = useRouter()
const route = useRoute()

const currentType = computed(() => {
  const path = route.path
  if (path.includes('/forum/share')) return 'share'
  if (path.includes('/forum/question')) return 'question'
  if (path.includes('/forum/activity')) return 'activity'
  return 'share'
})

// 分享模块
const shareItems = ref<any[]>([])
const shareLoading = ref(false)
const shareLoadingMore = ref(false)
const sharePage = ref(1)
const shareHasMore = ref(true)
const shareCategory = ref('')
const shareSortBy = ref('latest')
const pageSize = 12

const shareColumns = computed(() => {
  const colCount = 3
  const cols: any[][] = Array.from({ length: colCount }, () => [])
  shareItems.value.forEach((item, idx) => {
    cols[idx % colCount].push(item)
  })
  return cols
})

const loadShares = async (reset: boolean) => {
  if (reset) {
    sharePage.value = 1
    shareHasMore.value = true
    shareItems.value = []
    shareLoading.value = true
  }
  if (!shareHasMore.value) return
  shareLoadingMore.value = true
  try {
    const res = await getShareList({
      page: sharePage.value,
      size: pageSize,
      category: shareCategory.value,
      sortBy: shareSortBy.value
    })
    const newList = res.records || []
    if (reset) shareItems.value = newList
    else shareItems.value.push(...newList)
    shareHasMore.value = newList.length === pageSize
    sharePage.value++
  } catch (error) {
    ElMessage.error('加载分享失败')
  } finally {
    shareLoadingMore.value = false
    shareLoading.value = false
  }
}

// 问答模块
const questionItems = ref<any[]>([])
const questionLoadingMore = ref(false)
const questionPage = ref(1)
const questionHasMore = ref(true)

const loadQuestions = async (reset: boolean) => {
  if (reset) {
    questionPage.value = 1
    questionHasMore.value = true
    questionItems.value = []
  }
  if (!questionHasMore.value) return
  questionLoadingMore.value = true
  try {
    const res = await getQuestionList({ page: questionPage.value, size: pageSize })
    const newList = res.records || []
    if (reset) questionItems.value = newList
    else questionItems.value.push(...newList)
    questionHasMore.value = newList.length === pageSize
    questionPage.value++
  } catch (error) {
    ElMessage.error('加载问答失败')
  } finally {
    questionLoadingMore.value = false
  }
}

const toggleQuestionFavorite = async (id: string) => {
  try {
    await apiToggleQuestionFavorite(id)
    const item = questionItems.value.find(i => i.id === id)
    if (item) item.favorited = !item.favorited
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const toggleQuestionFollow = async (id: string) => {
  try {
    await apiToggleQuestionFollow(id)
    const item = questionItems.value.find(i => i.id === id)
    if (item) item.followed = !item.followed
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 活动模块
const activityItems = ref<any[]>([])
const activityLoadingMore = ref(false)
const activityPage = ref(1)
const activityHasMore = ref(true)
const activityCategory = ref('')
const activitySortBy = ref('latest')
const hotActivities = ref<any[]>([])

const loadActivities = async (reset: boolean) => {
  if (reset) {
    activityPage.value = 1
    activityHasMore.value = true
    activityItems.value = []
  }
  if (!activityHasMore.value) return
  activityLoadingMore.value = true
  try {
    const params: any = { page: activityPage.value, size: pageSize }
    if (activityCategory.value) params.category = activityCategory.value
    if (activitySortBy.value) params.sortBy = activitySortBy.value
    const res = await getActivityList(params)
    const newList = res.records || []
    if (reset) {
      activityItems.value = newList
      hotActivities.value = newList.slice(0, 3)
    } else {
      activityItems.value.push(...newList)
    }
    activityHasMore.value = newList.length === pageSize
    activityPage.value++
  } catch (error) {
    ElMessage.error('加载活动失败')
  } finally {
    activityLoadingMore.value = false
  }
}

const handleJoinActivity = async (id: string) => {
  try {
    await joinActivity(id)
    const item = activityItems.value.find(i => i.id === id)
    if (item) {
      item.participated = true
      item.participantCount += 1
    }
    ElMessage.success('参与成功')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleCancelJoinActivity = async (id: string) => {
  try {
    await cancelJoinActivity(id)
    const item = activityItems.value.find(i => i.id === id)
    if (item) {
      item.participated = false
      item.participantCount -= 1
    }
    ElMessage.success('已取消参与')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 公共数据
const hotTags = ref<any[]>([])
const activeUsers = ref<any[]>([])

const loadHotTags = async () => {
  try {
    const res = await getHotTags(currentType.value, 8)
    hotTags.value = res.data || []
  } catch (error) {
    console.error('加载标签失败', error)
  }
}

const loadActiveUsers = async () => {
  try {
    const res = await getActiveUsers(5)
    activeUsers.value = res || []
  } catch (error) {
    console.error('加载活跃用户失败', error)
    activeUsers.value = [
      { userId: 1, nickName: '美食达人', avatar: '' },
      { userId: 2, nickName: '烘焙爱好者', avatar: '' }
    ]
  }
}

// 其他功能
const searchKeyword = ref('')
const handleSearch = () => ElMessage.info('搜索功能开发中')
const publishButtonText = computed(() => {
  if (currentType.value === 'share') return '发布分享'
  if (currentType.value === 'question') return '发布问题'
  return '发布活动'
})
const showShareDialog = ref(false)
const openPublishDialog = () => {
  if (currentType.value === 'share') showShareDialog.value = true
  else ElMessage.info('发布功能开发中')
}
const searchByTag = (tag: string) => ElMessage.info(`搜索标签：${tag}`)

const goToShareDetail = (id: string) => router.push(`/forum/share/${id}`)
const goToQuestionDetail = (id: string) => router.push(`/forum/question/${id}`)
const goToActivityDetail = (id: string) => router.push(`/forum/activity/${id}`)

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getMonth() + 1}/${d.getDate()} ${d.getHours()}:${d.getMinutes()}`
}

// 无限滚动
const shareLoadMoreTrigger = ref<HTMLElement>()
const questionLoadMoreTrigger = ref<HTMLElement>()
const activityLoadMoreTrigger = ref<HTMLElement>()

let shareObserver: IntersectionObserver | null = null
let questionObserver: IntersectionObserver | null = null
let activityObserver: IntersectionObserver | null = null

watch(() => route.path, (newPath) => {
  if (newPath.includes('/forum/share')) {
    loadShares(true)
    loadHotTags()
  } else if (newPath.includes('/forum/question')) {
    loadQuestions(true)
    loadHotTags()
  } else if (newPath.includes('/forum/activity')) {
    loadActivities(true)
    loadHotTags()
  }
  loadActiveUsers()
})

onMounted(() => {
  if (currentType.value === 'share') loadShares(true)
  else if (currentType.value === 'question') loadQuestions(true)
  else if (currentType.value === 'activity') loadActivities(true)
  loadHotTags()
  loadActiveUsers()

  if (shareLoadMoreTrigger.value) {
    shareObserver = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && !shareLoadingMore.value && shareHasMore.value) loadShares(false)
      },
      { rootMargin: '100px' }
    )
    shareObserver.observe(shareLoadMoreTrigger.value)
  }
  if (questionLoadMoreTrigger.value) {
    questionObserver = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && !questionLoadingMore.value && questionHasMore.value) loadQuestions(false)
      },
      { rootMargin: '100px' }
    )
    questionObserver.observe(questionLoadMoreTrigger.value)
  }
  if (activityLoadMoreTrigger.value) {
    activityObserver = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && !activityLoadingMore.value && activityHasMore.value) loadActivities(false)
      },
      { rootMargin: '100px' }
    )
    activityObserver.observe(activityLoadMoreTrigger.value)
  }
})

onUnmounted(() => {
  shareObserver?.disconnect()
  questionObserver?.disconnect()
  activityObserver?.disconnect()
})
</script>

<style scoped>
.forum-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}
.forum-layout {
  display: flex;
  gap: 24px;
}
.left-module {
  flex: 3;
}
.right-module {
  flex: 1;
}
.share-header, .activity-controls {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}
.waterfall-container {
  display: flex;
  gap: 20px;
}
.waterfall-column {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.share-card {
  cursor: pointer;
  transition: transform 0.2s;
}
.share-card:hover {
  transform: translateY(-4px);
}
.card-image {
  width: 100%;
  aspect-ratio: 4 / 3;
  overflow: hidden;
  background: #f5f5f5;
}
.card-image img, .card-image video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.img-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ccc;
  font-size: 48px;
}
.card-info {
  padding: 12px;
}
.card-info h3 {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #999;
}
.author {
  display: flex;
  align-items: center;
  gap: 6px;
}
.question-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  cursor: pointer;
}
.question-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}
.question-header h3 {
  margin: 0;
  font-size: 18px;
}
.question-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 12px;
  font-size: 13px;
  color: #666;
}
.footer-left {
  display: flex;
  align-items: center;
  gap: 16px;
}
.action-btn {
  background: none;
  border: none;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #666;
}
.action-btn.active {
  color: #f56c6c;
}
.activity-card {
  display: flex;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 16px;
  cursor: pointer;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}
.activity-banner {
  width: 200px;
  height: 120px;
  object-fit: cover;
}
.activity-info {
  flex: 1;
  padding: 12px;
}
.carousel-item {
  position: relative;
  height: 100%;
  cursor: pointer;
}
.carousel-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.carousel-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0,0,0,0.7));
  color: white;
  padding: 20px;
}
.load-more-trigger {
  height: 20px;
}
.loading-more {
  text-align: center;
  padding: 20px;
  color: #999;
}
.right-module > div {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 20px;
}
.title {
  font-weight: 600;
  margin-bottom: 12px;
}
.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.user-list .user-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}
</style>