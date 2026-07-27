<template>
  <div class="user-profile-container">
    <div class="profile-layout">
      <!-- 左侧边栏 -->
      <aside class="sidebar">
        <div class="user-avatar-section">
          <el-avatar :size="80" :src="userInfo.avatar" class="user-avatar">
            {{ usernameInitial }}
          </el-avatar>
          <h3 class="sidebar-username">{{ userInfo.userName || userInfo.username }}</h3>
        </div>

        <nav class="sidebar-nav">
          <el-menu :default-active="activeSidebarItem" class="el-menu-vertical-demo" @select="handleSidebarSelect">
            <el-menu-item index="profile">
              <el-icon><User /></el-icon>
              <span>个人资料</span>
            </el-menu-item>
            <el-menu-item index="replies">
              <el-icon><ChatDotRound /></el-icon>
              <span>回复</span>
              <el-badge :value="unreadReplies" class="message-badge" />
            </el-menu-item>
            <el-menu-item index="recipes">
              <el-icon><Crop /></el-icon>
              <span>菜谱</span>
            </el-menu-item>
            <el-menu-item index="books">
              <el-icon><Collection /></el-icon>
              <span>书籍</span>
            </el-menu-item>
            <el-menu-item index="qa">
              <el-icon><HelpFilled /></el-icon>
              <span>问答</span>
            </el-menu-item>
            <el-menu-item index="activities">
              <el-icon><Calendar /></el-icon>
              <span>活动</span>
            </el-menu-item>
            <el-menu-item index="suggestions">
              <el-icon><WarningFilled /></el-icon>
              <span>建议</span>
            </el-menu-item>
            <el-menu-item index="settings">
              <el-icon><Setting /></el-icon>
              <span>设置</span>
            </el-menu-item>
          </el-menu>
        </nav>
      </aside>

      <!-- 右侧内容区 -->
      <main class="content-area">
        <!-- 个人资料 -->
        <div v-if="activeSidebarItem === 'profile'" class="profile-content">
          <div class="content-header">
            <h2>个人资料</h2>
            <el-button type="primary" @click="editProfile">编辑资料</el-button>
          </div>
          <div class="profile-info">
            <el-form :model="tempUserInfo" label-width="120px" :disabled="!isEditing">
              <el-form-item label="用户名">
                <el-input v-model="tempUserInfo.userName" placeholder="请输入用户名"></el-input>
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="tempUserInfo.email" type="email" placeholder="请输入邮箱"></el-input>
              </el-form-item>
              <el-form-item label="手机号">
                <el-input v-model="tempUserInfo.phonenumber" placeholder="请输入手机号"></el-input>
              </el-form-item>
              <el-form-item label="真实姓名">
                <el-input v-model="tempUserInfo.realName" placeholder="请输入真实姓名"></el-input>
              </el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="tempUserInfo.gender">
                  <el-radio :label="0">未知</el-radio>
                  <el-radio :label="1">男</el-radio>
                  <el-radio :label="2">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="生日">
                <el-date-picker v-model="tempUserInfo.birthday" type="date" placeholder="选择日期" value-format="YYYY-MM-DD"></el-date-picker>
              </el-form-item>
              <el-form-item label="所在地">
                <el-input v-model="tempUserInfo.location" placeholder="请输入所在地"></el-input>
              </el-form-item>
              <el-form-item label="个人简介">
                <el-input v-model="tempUserInfo.bio" type="textarea" :rows="3" placeholder="请输入个人简介"></el-input>
              </el-form-item>
              <el-form-item label="注册时间">
                <el-input v-model="userInfo.registerDate" disabled></el-input>
              </el-form-item>
              <el-form-item v-if="isEditing">
                <el-button type="primary" @click="saveProfile">保存</el-button>
                <el-button @click="cancelEdit">取消</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <!-- 回复列表 -->
        <div v-else-if="activeSidebarItem === 'replies'" class="replies-content">
          <div class="content-header">
            <h2>我的回复</h2>
          </div>
          <div v-loading="repliesLoading" class="reply-list">
            <div v-for="reply in repliesList" :key="reply.id" class="reply-item" :class="{ unread: !reply.isRead }" @click="markReplyRead(reply.id)">
              <div class="reply-content">{{ reply.content }}</div>
              <div class="reply-meta">来自：{{ reply.sourceTitle }} · {{ formatDate(reply.createTime) }}</div>
            </div>
            <el-empty v-if="!repliesLoading && repliesList.length === 0" description="暂无回复" />
            <el-pagination v-if="repliesTotal > repliesPageSize"
              :current-page="repliesPageNum"
              :page-size="repliesPageSize"
              :total="repliesTotal"
              layout="prev, pager, next"
              @current-change="handleReplyPageChange"
              class="reply-pagination"
            />
          </div>
        </div>

        <!-- 菜谱区域（简化，可按需扩展） -->
        <div v-else-if="activeSidebarItem === 'recipes'" class="recipes-content">
          <div class="content-header">
            <h2>我的菜谱</h2>
            <el-button type="primary">发布新菜谱</el-button>
          </div>
          <el-tabs v-model="activeRecipeTab">
            <el-tab-pane label="发布" name="published">
              <div class="recipe-grid">
                <div v-for="recipe in publishedRecipes" :key="recipe.id" class="recipe-card">
                  <img :src="recipe.image" class="recipe-image">
                  <div class="recipe-info">{{ recipe.title }}</div>
                </div>
                <el-empty v-if="publishedRecipes.length === 0" description="暂无发布的菜谱" />
              </div>
            </el-tab-pane>
            <el-tab-pane label="收藏" name="collected">
              <div class="recipe-grid">
                <div v-for="recipe in collectedRecipes" :key="recipe.id" class="recipe-card">
                  <img :src="recipe.image" class="recipe-image">
                  <div class="recipe-info">{{ recipe.title }}</div>
                </div>
                <el-empty v-if="collectedRecipes.length === 0" description="暂无收藏的菜谱" />
              </div>
            </el-tab-pane>
            <el-tab-pane label="点赞" name="liked">
              <div class="recipe-grid">
                <div v-for="recipe in likedRecipes" :key="recipe.id" class="recipe-card">
                  <img :src="recipe.image" class="recipe-image">
                  <div class="recipe-info">{{ recipe.title }}</div>
                </div>
                <el-empty v-if="likedRecipes.length === 0" description="暂无点赞的菜谱" />
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 书籍区域（简化） -->
        <div v-else-if="activeSidebarItem === 'books'" class="books-content">
          <div class="content-header">
            <h2>我的书籍</h2>
            <el-button type="primary">添加书籍</el-button>
          </div>
          <el-tabs v-model="activeBookTab">
            <el-tab-pane label="发布" name="published">
              <el-table :data="publishedBooks" style="width: 100%">
                <el-table-column prop="title" label="书名" />
                <el-table-column prop="author" label="作者" />
              </el-table>
              <el-empty v-if="publishedBooks.length === 0" description="暂无发布的书籍" />
            </el-tab-pane>
            <el-tab-pane label="收藏" name="collected">
              <el-table :data="collectedBooks" style="width: 100%">
                <el-table-column prop="title" label="书名" />
                <el-table-column prop="author" label="作者" />
              </el-table>
              <el-empty v-if="collectedBooks.length === 0" description="暂无收藏的书籍" />
            </el-tab-pane>
            <el-tab-pane label="阅读历史" name="history">
              <el-table :data="readingHistory" style="width: 100%">
                <el-table-column prop="bookTitle" label="书名" />
                <el-table-column prop="lastReadTime" label="最后阅读时间" />
                <el-table-column prop="progress" label="进度">
                  <template #default="scope">
                    <el-progress :percentage="scope.row.progress" :format="() => ''" />
                  </template>
                </el-table-column>
              </el-table>
              <el-empty v-if="readingHistory.length === 0" description="暂无阅读历史" />
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 问答区域（简化） -->
        <div v-else-if="activeSidebarItem === 'qa'" class="qa-content">
          <div class="content-header">
            <h2>我的问答</h2>
            <el-button type="primary">提问</el-button>
          </div>
          <el-tabs v-model="activeQATab">
            <el-tab-pane label="提问" name="questions">
              <div v-for="q in myQuestions" :key="q.id" class="qa-item">
                <h4>{{ q.title }}</h4>
                <p>{{ q.content }}</p>
                <div class="qa-meta">{{ q.answers }}回答 · {{ q.views }}浏览</div>
              </div>
              <el-empty v-if="myQuestions.length === 0" description="暂无提问" />
            </el-tab-pane>
            <el-tab-pane label="回答" name="answers">
              <div v-for="a in myAnswers" :key="a.id" class="qa-item">
                <h4>回答了：{{ a.questionTitle }}</h4>
                <p>{{ a.content }}</p>
                <div class="qa-meta">{{ a.likes }}赞同 · {{ a.comments }}评论</div>
              </div>
              <el-empty v-if="myAnswers.length === 0" description="暂无回答" />
            </el-tab-pane>
            <el-tab-pane label="关注" name="following">
              <div v-for="q in followedQuestions" :key="q.id" class="qa-item">
                <h4>{{ q.title }}</h4>
                <div class="qa-meta">{{ q.answers }}回答 · {{ q.views }}浏览</div>
              </div>
              <el-empty v-if="followedQuestions.length === 0" description="暂无关注的问题" />
            </el-tab-pane>
            <el-tab-pane label="收藏" name="collected">
              <div v-for="q in collectedQuestions" :key="q.id" class="qa-item">
                <h4>{{ q.title }}</h4>
                <div class="qa-meta">{{ q.answers }}回答 · 作者：{{ q.author }}</div>
              </div>
              <el-empty v-if="collectedQuestions.length === 0" description="暂无收藏的问题" />
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 活动区域 -->
        <div v-else-if="activeSidebarItem === 'activities'" class="activities-content">
          <div class="content-header">
            <h2>我的活动</h2>
            <el-button type="primary">创建活动</el-button>
          </div>
          <el-tabs v-model="activeActivityTab">
            <el-tab-pane label="我组织" name="organized">
              <div v-for="act in organizedActivities" :key="act.id" class="activity-item">
                <h4>{{ act.title }}</h4>
                <p>{{ act.date }} · {{ act.location }}</p>
                <div>{{ act.participants }}人报名 · {{ act.status }}</div>
              </div>
              <el-empty v-if="organizedActivities.length === 0" description="暂无组织的活动" />
            </el-tab-pane>
            <el-tab-pane label="已报名" name="joined">
              <div v-for="act in joinedActivities" :key="act.id" class="activity-item">
                <h4>{{ act.title }}</h4>
                <p>{{ act.date }} · {{ act.location }}</p>
                <div>组织者：{{ act.organizer }}</div>
              </div>
              <el-empty v-if="joinedActivities.length === 0" description="暂无报名的活动" />
            </el-tab-pane>
            <el-tab-pane label="已建议" name="suggested">
              <div v-for="act in suggestedActivities" :key="act.id" class="activity-item">
                <h4>{{ act.title }}</h4>
                <p>{{ act.suggestion }}</p>
                <div>{{ act.status }} · {{ act.date }}</div>
              </div>
              <el-empty v-if="suggestedActivities.length === 0" description="暂无建议的活动" />
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 建议区域 -->
        <div v-else-if="activeSidebarItem === 'suggestions'" class="suggestions-content">
          <div class="content-header">
            <h2>我的建议</h2>
            <el-button type="primary" @click="openSuggestionDialog">提交建议</el-button>
          </div>
          <div v-for="sug in userSuggestions" :key="sug.id" class="suggestion-item">
            <h4>{{ sug.title }}</h4>
            <p>{{ sug.content }}</p>
            <div class="status">状态：{{ suggestionStatusMap[sug.status] }}</div>
          </div>
          <el-empty v-if="userSuggestions.length === 0" description="暂无建议" />
        </div>

        <!-- 设置占位 -->
        <div v-else class="placeholder-content">
          <el-empty description="功能开发中" />
        </div>
      </main>
    </div>

    <!-- 提交建议弹窗 -->
    <el-dialog v-model="suggestionDialogVisible" title="提交建议" width="500px">
      <el-input v-model="newSuggestion.title" placeholder="建议标题" />
      <el-input v-model="newSuggestion.content" type="textarea" rows="4" placeholder="建议内容" />
      <template #footer>
        <el-button @click="suggestionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitSuggestion">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User, ChatDotRound, Crop, Collection, HelpFilled, Calendar, WarningFilled, Setting } from '@element-plus/icons-vue'
import { getUserProfile, updateUserProfile } from '@/api/user'
import { getUnreadReplyCount, getReplyList, markRepliesAsRead } from '@/api/commentReply'
import { getUserPublishedRecipes, getUserCollectedRecipes, getUserLikedRecipes } from '@/api/recipe'
import { getUserPublishedBooks, getUserCollectedBooks } from '@/api/book'
import { getReadHistoryList } from '@/api/bookReadHistory'
import { getUserQuestions, getUserAnswers, getUserFollowedQuestions, getUserCollectedQuestions } from '@/api/forum'
import { getOrganizedActivities, getJoinedActivities, getSuggestedActivities, submitActivitySuggestion } from '@/api/activityUser'

// ========== 用户信息 ==========
const userInfo = ref<any>({})
const tempUserInfo = ref<any>({})
const isEditing = ref(false)
const activeSidebarItem = ref('profile')

// ========== 回复相关 ==========
const unreadReplies = ref(0)
const repliesList = ref<any[]>([])
const repliesLoading = ref(false)
const repliesPageNum = ref(1)
const repliesPageSize = ref(10)
const repliesTotal = ref(0)

// ========== 菜谱相关 ==========
const publishedRecipes = ref([])
const collectedRecipes = ref([])
const likedRecipes = ref([])
const activeRecipeTab = ref('published')

// ========== 书籍相关 ==========
const publishedBooks = ref([])
const collectedBooks = ref([])
const readingHistory = ref([])
const activeBookTab = ref('published')

// ========== 问答相关 ==========
const myQuestions = ref([])
const myAnswers = ref([])
const followedQuestions = ref([])
const collectedQuestions = ref([])
const activeQATab = ref('questions')

// ========== 活动相关 ==========
const organizedActivities = ref([])
const joinedActivities = ref([])
const suggestedActivities = ref([])
const activeActivityTab = ref('organized')

// ========== 建议相关 ==========
const userSuggestions = ref([])
const suggestionDialogVisible = ref(false)
const newSuggestion = ref({ title: '', content: '' })
const suggestionStatusMap: Record<number, string> = { 0: '待审核', 1: '已采纳', 2: '已拒绝' }

// 辅助函数
const usernameInitial = computed(() => userInfo.value.userName?.charAt(0).toUpperCase() || '')
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${d.getMonth() + 1}-${d.getDate()} ${d.getHours()}:${d.getMinutes()}`
}

// ========== 加载个人资料 ==========
const loadProfile = async () => {
  try {
    const res = await getUserProfile()
    userInfo.value = res
    tempUserInfo.value = { ...res }
  } catch (error) {
    ElMessage.error('获取用户资料失败')
  }
}

// ========== 加载未读回复数（修正角标） ==========
const loadUnreadCount = async () => {
  try {
    const res = await getUnreadReplyCount()
    // 如果响应拦截器已经返回 data，则 res 就是数字；否则需要取 res.data
    unreadReplies.value = typeof res === 'number' ? res : (res?.data ?? 0)
  } catch (error) {
    console.error('获取未读回复数失败', error)
    unreadReplies.value = 0
  }
}

// ========== 加载回复列表 ==========
const loadReplies = async (resetPage = true) => {
  if (resetPage) repliesPageNum.value = 1
  repliesLoading.value = true
  try {
    const res = await getReplyList({ pageNum: repliesPageNum.value, pageSize: repliesPageSize.value })
    // 假设返回的数据结构是 { rows: [], total: number }
    const data = res as any
    if (resetPage) repliesList.value = data.rows || []
    else repliesList.value.push(...(data.rows || []))
    repliesTotal.value = data.total || 0
  } catch (error) {
    ElMessage.error('加载回复列表失败')
  } finally {
    repliesLoading.value = false
  }
}

// 标记回复已读
const markReplyRead = async (replyId: string) => {
  try {
    await markRepliesAsRead([replyId])
    const reply = repliesList.value.find(r => r.id === replyId)
    if (reply && !reply.isRead) {
      reply.isRead = true
      unreadReplies.value = Math.max(0, unreadReplies.value - 1)
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleReplyPageChange = (page: number) => {
  repliesPageNum.value = page
  loadReplies(false)
}

// ========== 加载菜谱数据 ==========
const loadRecipesData = async () => {
  try {
    const [published, collected, liked] = await Promise.all([
      getUserPublishedRecipes(),
      getUserCollectedRecipes(),
      getUserLikedRecipes()
    ])
    publishedRecipes.value = published || []
    collectedRecipes.value = collected || []
    likedRecipes.value = liked || []
  } catch (error) {
    console.error('加载菜谱数据失败', error)
  }
}

// ========== 加载书籍数据 ==========
const loadBooksData = async () => {
  try {
    const [published, collected, history] = await Promise.all([
      getUserPublishedBooks(),
      getUserCollectedBooks(),
      getReadHistoryList()
    ])
    publishedBooks.value = published || []
    collectedBooks.value = collected || []
    readingHistory.value = history || []
  } catch (error) {
    console.error('加载书籍数据失败', error)
  }
}

// ========== 加载问答数据 ==========
const loadQAData = async () => {
  try {
    const [questions, answers, followed, collected] = await Promise.all([
      getUserQuestions(),
      getUserAnswers(),
      getUserFollowedQuestions(),
      getUserCollectedQuestions()
    ])
    myQuestions.value = questions || []
    myAnswers.value = answers || []
    followedQuestions.value = followed || []
    collectedQuestions.value = collected || []
  } catch (error) {
    console.error('加载问答数据失败', error)
  }
}

// ========== 加载活动数据 ==========
const loadActivitiesData = async () => {
  try {
    const [organized, joined, suggested] = await Promise.all([
      getOrganizedActivities(),
      getJoinedActivities(),
      getSuggestedActivities()
    ])
    organizedActivities.value = organized || []
    joinedActivities.value = joined || []
    suggestedActivities.value = suggested || []
    userSuggestions.value = suggested || []
  } catch (error) {
    console.error('加载活动数据失败', error)
  }
}

// ========== 提交建议 ==========
const submitSuggestion = async () => {
  if (!newSuggestion.value.title.trim()) {
    ElMessage.warning('请填写标题')
    return
  }
  try {
    await submitActivitySuggestion(newSuggestion.value)
    ElMessage.success('建议提交成功')
    suggestionDialogVisible.value = false
    newSuggestion.value = { title: '', content: '' }
    await loadActivitiesData() // 刷新建议列表
  } catch (error) {
    ElMessage.error('提交失败')
  }
}

const openSuggestionDialog = () => {
  newSuggestion.value = { title: '', content: '' }
  suggestionDialogVisible.value = true
}

// ========== 保存个人资料 ==========
const saveProfile = async () => {
  try {
    await updateUserProfile(tempUserInfo.value)
    Object.assign(userInfo.value, tempUserInfo.value)
    isEditing.value = false
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  }
}
const editProfile = () => {
  tempUserInfo.value = { ...userInfo.value }
  isEditing.value = true
}
const cancelEdit = () => {
  tempUserInfo.value = { ...userInfo.value }
  isEditing.value = false
}

// ========== 侧边栏切换，按需加载 ==========
const handleSidebarSelect = async (key: string) => {
  activeSidebarItem.value = key
  if (key === 'replies' && repliesList.value.length === 0) {
    await loadReplies()
  } else if (key === 'recipes' && publishedRecipes.value.length === 0) {
    await loadRecipesData()
  } else if (key === 'books' && publishedBooks.value.length === 0) {
    await loadBooksData()
  } else if (key === 'qa' && myQuestions.value.length === 0) {
    await loadQAData()
  } else if (key === 'activities' && organizedActivities.value.length === 0) {
    await loadActivitiesData()
  } else if (key === 'suggestions' && userSuggestions.value.length === 0) {
    await loadActivitiesData()
  }
}

// ========== 初始化 ==========
onMounted(async () => {
  await loadProfile()
  await loadUnreadCount()
  // 其他数据按需加载，不预加载所有
})
</script>

<style scoped>
@import '@/styles/components/UserProfileView.css';

.reply-item {
  padding: 12px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}
.reply-item.unread {
  background-color: #fef0e6;
}
.reply-content {
  font-size: 14px;
  margin-bottom: 6px;
}
.reply-meta {
  font-size: 12px;
  color: #999;
}
.reply-pagination {
  margin-top: 16px;
  text-align: center;
}
.recipe-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 16px;
}
.recipe-card {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
}
.recipe-image {
  width: 100%;
  height: 120px;
  object-fit: cover;
}
.recipe-info {
  padding: 8px;
  font-size: 14px;
  text-align: center;
}
.activity-item, .qa-item, .suggestion-item {
  border-bottom: 1px solid #eee;
  padding: 12px 0;
}

.reply-item {
  padding: 12px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}
.reply-item.unread {
  background-color: #fef0e6;
}
.reply-content {
  font-size: 14px;
  margin-bottom: 6px;
}
.reply-meta {
  font-size: 12px;
  color: #999;
}
.reply-pagination {
  margin-top: 16px;
  text-align: center;
}
.recipe-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 16px;
}
.recipe-card {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
}
.recipe-image {
  width: 100%;
  height: 120px;
  object-fit: cover;
}
.recipe-info {
  padding: 8px;
  font-size: 14px;
  text-align: center;
}
.activity-item, .qa-item, .suggestion-item {
  border-bottom: 1px solid #eee;
  padding: 12px 0;
}
</style>