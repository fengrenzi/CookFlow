<template>
  <div v-if="loading" class="loading-container">
    <el-icon class="loading-icon"><Loading /></el-icon>
    <span>加载中...</span>
  </div>
  <div v-else class="recipe-detail-container">
    <!-- 上中和左右布局容器 -->
    <div class="upper-section">
      <!-- 详情卡片（左侧） -->
      <div class="detail-card">
        <div class="recipe-image-section">
          <el-carousel v-if="recipe.imgUrls && recipe.imgUrls.length" :interval="3000" height="250px" arrow="always"
            indicator-position="outside">
            <el-carousel-item v-for="(image, index) in recipe.imgUrls" :key="index">
              <div class="carousel-item-container">
                <ImageDisplay :imgurl="image" alt="菜谱封面" fit="cover" class="recipe-img" />
              </div>
            </el-carousel-item>
          </el-carousel>
          <div v-else class="no-image-placeholder">
            <el-icon><Picture /></el-icon>
            <span>暂无图片</span>
          </div>
        </div>

        <div class="recipe-info-section">
          <h1 class="recipe-title">{{ recipe.title }}</h1>

          <div class="author-info">
            <el-avatar size="small" :src="recipe.authorAvatar || undefined" :icon="User"></el-avatar>
            <span class="author-name">{{ recipe.authorName }}</span>
            <span class="publish-time">{{ recipe.publishTime }}</span>
          </div>

          <div class="main-ingredients">
            <span class="ingredients-label">主要食材：</span>
            <span class="ingredients-value">{{ recipe.mainIngredients }}</span>
          </div>

          <div class="recipe-meta">
            <div class="meta-item">
              <el-icon><Timer /></el-icon>
              <span class="meta-label">备菜：</span>
              <span class="meta-value">{{ recipe.prepTime }}分钟</span>
            </div>
            <div class="meta-separator">|</div>
            <div class="meta-item">
              <el-icon><Food /></el-icon>
              <span class="meta-label">烹饪：</span>
              <span class="meta-value">{{ recipe.cookTime }}分钟</span>
            </div>
            <div class="meta-separator">|</div>
            <div class="meta-item">
              <el-icon><Food /></el-icon>
              <span class="meta-label">难度：</span>
              <span class="meta-value">{{ recipe.difficulty }}</span>
            </div>
          </div>

          <div class="categories">
            <el-tag v-for="(category, index) in recipe.categoryNames" :key="index" effect="plain" size="small"
              class="category-tag">
              {{ category }}
            </el-tag>
          </div>

          <div class="interaction-buttons">
            <el-button :type="isFavorite ? 'primary' : 'default'" @click="toggleFavorite" class="favorite-btn"
              :icon="isFavorite ? StarFilled : Star">
              {{ isFavorite ? '已收藏' : '收藏' }}
            </el-button>
            <el-button :type="isAddedToCart ? 'success' : 'warning'" @click="addToCart" class="cart-btn"
              :class="{ 'cart-added': isAddedToCart }" :icon="ShoppingCart">
              {{ isAddedToCart ? '已加入购物车' : '加入购物车' }}
            </el-button>
            <el-button type="success" @click="handleStartCooking" class="cook-btn" :icon="CaretRight">
              开始烹饪
            </el-button>
          </div>
        </div>
      </div>

      <!-- 营养价值（右侧） -->
      <div class="nutrition-section">
        <div class="section-header">
          <h2>营养价值</h2>
          <span class="nutrition-unit">每1人份</span>
        </div>
        <div class="nutrition-grid">
          <div class="nutrition-item">
            <span class="nutrition-label">钠</span>
            <span class="nutrition-value">{{ recipe.nutrition?.sodium || '--' }}</span>
          </div>
          <div class="nutrition-item">
            <span class="nutrition-label">蛋白质</span>
            <span class="nutrition-value">{{ recipe.nutrition?.protein || '--' }}</span>
          </div>
          <div class="nutrition-item">
            <span class="nutrition-label">热量</span>
            <span class="nutrition-value">{{ recipe.nutrition?.calories || '--' }}</span>
          </div>
          <div class="nutrition-item">
            <span class="nutrition-label">脂肪</span>
            <span class="nutrition-value">{{ recipe.nutrition?.fat || '--' }}</span>
          </div>
          <div class="nutrition-item">
            <span class="nutrition-label">膳食纤维</span>
            <span class="nutrition-value">{{ recipe.nutrition?.fiber || '--' }}</span>
          </div>
          <div class="nutrition-item">
            <span class="nutrition-label">饱和脂肪</span>
            <span class="nutrition-value">{{ recipe.nutrition?.saturatedFat || '--' }}</span>
          </div>
          <div class="nutrition-item">
            <span class="nutrition-label">碳水化合物</span>
            <span class="nutrition-value">{{ recipe.nutrition?.carbs || '--' }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 评价展示 - 使用可复用的评论区组件 -->
    <CommentSection v-if="comments.length" :comments="comments" @like-comment="likeComment"
      @publish-comment="handlePublishComment" @sort-change="handleSortChange" />

    <!-- 开始烹饪弹出层 -->
    <el-dialog v-model="showCookingModal" title="烹饪模式" width="60%" style="margin: 20px auto;"
      :before-close="handleCloseCookingModal">
      <div class="cooking-modal-content">
        <!-- 烹饪进度条 -->
        <div class="cooking-progress-section">
          <h3>烹饪进度</h3>
          <div class="progress-bar-container">
            <div class="progress-bar" :style="{ width: cookingProgress + '%' }"></div>
          </div>
          <span class="progress-percentage">{{ cookingProgress }}%</span>
        </div>

        <!-- 当前步骤 -->
        <div class="current-step-section">
          <div class="step-header">
            <el-tag type="success" effect="dark">{{ currentStep + 1 }}</el-tag>
            <span class="step-label">步骤 {{ currentStep + 1 }}/{{ totalSteps }}</span>
          </div>
          <div class="step-content">
            <p class="step-description">{{ currentStepData?.description || '' }}</p>
          </div>
        </div>

        <!-- 计时器 -->
        <div class="timer-section">
          <div class="timer-display">{{ timerDisplay }}</div>
          <div class="timer-info">预计时间: {{ currentStepData?.expectedTime || '' }}</div>
          <div class="timer-controls">
            <el-button :type="isTimerRunning ? 'info' : 'primary'" @click="toggleTimer"
              :icon="isTimerRunning ? VideoPause : VideoPlay">
              {{ isTimerRunning ? '暂停计时' : '开始计时' }}
            </el-button>
            <el-button type="warning" @click="resetTimer" :icon="RefreshRight">重置计时</el-button>
          </div>
        </div>

        <!-- 步骤导航 -->
        <div class="step-navigation">
          <el-button :disabled="currentStep === 0" @click="prevStep" :icon="ArrowLeft">上一步</el-button>
          <el-button type="primary" @click="nextStep" :icon="ArrowRight">
            {{ currentStep === totalSteps - 1 ? '完成烹饪' : '完成，下一步' }}
          </el-button>
        </div>

        <!-- 所有步骤列表 -->
        <div class="all-steps-section">
          <h4>所有步骤</h4>
          <div class="steps-list">
            <div v-for="(step, index) in recipe.steps" :key="index" class="step-item"
              :class="{
                'completed-step': index < currentStep,
                'current-step-item': index === currentStep,
                'upcoming-step': index > currentStep
              }">
              <div class="step-number">
                <el-tag v-if="index < currentStep" type="success" effect="dark" size="small"><Check /></el-tag>
                <el-tag v-else-if="index === currentStep" type="primary" effect="dark" size="small">{{ index + 1
                }}</el-tag>
                <el-tag v-else size="small">{{ index + 1 }}</el-tag>
              </div>
              <div class="step-details">
                <p class="step-text">{{ step.description }}</p>
                <span class="step-time">{{ step.expectedTime }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { Star, StarFilled, Timer, Food, CaretRight, User, ShoppingCart, VideoPlay, VideoPause, RefreshRight, ArrowLeft, ArrowRight, Check, Loading, Picture } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import CommentSection from '@/components/CommentSection.vue'
import ImageDisplay from '@/components/ImageDisplay.vue'
import { useCartStore } from '@/store/modules/cart'
import { getRecipeDetail } from '@/api/recipe'

const route = useRoute()
const cartStore = useCartStore()

const loading = ref(true)
const showCookingModal = ref(false)
const isFavorite = ref(false)
const isAddedToCart = ref(false)

// 菜谱数据（初始化时所有数组字段都设为空数组）
const recipe = ref({
  id: '',
  title: '',
  imgUrls: [] as string[],
  authorName: '',
  authorAvatar: '',
  publishTime: '',
  prepTime: 0,
  cookTime: 0,
  difficulty: '',
  mainIngredients: '',
  categoryNames: [] as string[],
  steps: [] as { description: string; expectedTime: string }[],
  nutrition: {
    sodium: '',
    protein: '',
    calories: '',
    fat: '',
    fiber: '',
    saturatedFat: '',
    carbs: ''
  },
  comments: [] as any[]
})

// 生成随机评论数据
const generateRandomComments = () => {
  // 用于演示的评论图片 URL（可换成您实际的后端图片地址）
  const demoImageUrl = 'http://localhost:9999/image/61b6f69c-2770-11f1-aefa-3043d7ef8d98.jpg'
  const avatars = [demoImageUrl]
  const names = ['美食家小张', '吃货小王', '厨房小白', '大厨阿杰', '健康饮食者', '甜点控', '无辣不欢', '清淡至上', '养生达人', '家常菜高手']
  const contents = [
    '太赞了，跟着做了一次，家人都说好吃！',
    '步骤很详细，适合新手，收藏了。',
    '味道不错，就是盐放多了点，下次注意。',
    '这个菜谱简单实用，已经推荐给朋友了。',
    '食材容易买，做出来很成功，感谢分享。',
    '改良了一下，加了点辣椒，更符合我的口味。',
    '第一次做就成功了，很有成就感！',
    '图片看起来太诱人了，忍不住想尝试。',
    '期待楼主出更多菜谱，已关注。',
    '非常棒的菜谱，每一步都讲得很清楚。'
  ]

  return Array.from({ length: 10 }, (_, i) => {
    // 随机决定是否带图片（带图片概率 50%）
    const hasImages = Math.random() > 0.5
    // 如果有图片，可以生成1-3张图片（这里示例使用1张或2张）
    let imageIds: string[] = []
    if (hasImages) {
      const imageCount = Math.floor(Math.random() * 2) + 1 // 1 或 2 张
      imageIds = Array(imageCount).fill(demoImageUrl)
    }
    
    return {
      id: `comment_${i}`,
      username: names[i % names.length],
      avatar: avatars[i % avatars.length],
      content: contents[i % contents.length] + (hasImages ? '（附图为证）' : ''),
      likes: Math.floor(Math.random() * 100),
      time: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toLocaleDateString(),
      imageIds: imageIds   // 评论图片 URL 列表
    }
  })
}

// 评论数据（用于 CommentSection）
const comments = computed(() => recipe.value.comments || [])

// 烹饪相关
const currentStep = ref(0)
const totalSteps = computed(() => recipe.value.steps.length)
const cookingProgress = computed(() => totalSteps.value === 0 ? 0 : Math.floor(((currentStep.value + 1) / totalSteps.value) * 100))
const currentStepData = computed(() => recipe.value.steps[currentStep.value])

// 计时器
const isTimerRunning = ref(false)
const timerSeconds = ref(0)
let timerInterval: number | null = null
const timerDisplay = computed(() => {
  const minutes = Math.floor(timerSeconds.value / 60)
  const seconds = timerSeconds.value % 60
  return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`
})

// 获取菜谱详情
const fetchRecipeDetail = async () => {
  const id = route.params.id as string
  try {
    const data = await getRecipeDetail(id)
    // 合并数据，确保数组字段不为 undefined
    recipe.value = {
      ...recipe.value,
      ...data,
    }
    // 显式设置数组字段
    recipe.value.comments = data.comments && data.comments.length ? data.comments : generateRandomComments()
    recipe.value.imgUrls = data.imgUrls || []
    recipe.value.steps = data.steps || []
    recipe.value.categoryNames = data.categoryNames || []
    recipe.value.nutrition = data.nutrition || recipe.value.nutrition
    // ... 其他字段
  } catch (error) {
    // 错误时也填充随机评论
    recipe.value.comments = generateRandomComments()
  } finally {
    loading.value = false
  }
}

// 点赞评论
const likeComment = (commentId: string) => {
  console.log('点赞评论', commentId)
}

// 处理发布评论
const handlePublishComment = (comment: any) => {
  recipe.value.comments.unshift(comment)
  ElMessage.success('评论发布成功')
}

// 排序切换
const handleSortChange = (sortType: 'time' | 'hot') => {
  console.log('排序方式', sortType)
}

// 收藏切换
const toggleFavorite = async () => {
  try {
    isFavorite.value = !isFavorite.value
    ElMessage.success(isFavorite.value ? '收藏成功' : '取消收藏')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 购物车
let isProcessing = false
const addToCart = () => {
  if (isProcessing) return
  isProcessing = true

  if (isAddedToCart.value) {
    const cartItem = cartStore.allItems.find(item => item.type === 'recipe' && item.recipeId === recipe.value.id)
    if (cartItem) cartStore.removeFromCart(cartItem.id)
    isAddedToCart.value = false
    ElMessage.success('已取消加入购物车')
  } else {
    // 构建食材列表（需从后端获取，暂时留空）
    const detailedIngredients: any[] = [] // 实际应从 recipe.ingredients 获取
    cartStore.addRecipeToCart(recipe.value.id, recipe.value.title, recipe.value.imgUrls[0] || '', detailedIngredients)
    isAddedToCart.value = true
    ElMessage.success('已添加到购物车')
  }

  setTimeout(() => {
    isProcessing = false
  }, 500)
}

// 开始烹饪
const handleStartCooking = () => {
  if (recipe.value.steps.length === 0) {
    ElMessage.warning('暂无烹饪步骤')
    return
  }
  currentStep.value = 0
  resetTimer()
  showCookingModal.value = true
}

// 计时器函数
const startTimer = () => {
  if (!timerInterval) {
    timerInterval = window.setInterval(() => { timerSeconds.value++ }, 1000)
    isTimerRunning.value = true
  }
}
const pauseTimer = () => {
  if (timerInterval) {
    clearInterval(timerInterval)
    timerInterval = null
    isTimerRunning.value = false
  }
}
const stopTimer = () => {
  pauseTimer()
  timerSeconds.value = 0
}
const resetTimer = () => { stopTimer() }
const toggleTimer = () => {
  if (isTimerRunning.value) pauseTimer()
  else startTimer()
}

const prevStep = () => {
  if (currentStep.value > 0) {
    stopTimer()
    currentStep.value--
  }
}
const nextStep = () => {
  stopTimer()
  if (currentStep.value < totalSteps.value - 1) {
    currentStep.value++
  } else {
    showCookingModal.value = false
    ElMessage.success('恭喜您完成了所有烹饪步骤！')
  }
}
const handleCloseCookingModal = (done: () => void) => {
  stopTimer()
  done()
}

onMounted(() => {
  fetchRecipeDetail()
  window.scrollTo({ top: 0, behavior: 'smooth' })
})
</script>

<style scoped>
@import '@/styles/components/RecipeDetailView.css';

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
  color: #999;
}

.loading-icon {
  font-size: 32px;
  margin-bottom: 16px;
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }
}

.no-image-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 250px;
  background-color: #f5f7fa;
  color: #c0c4cc;
}

.cooking-modal-content {
  padding: 20px 0;
}

.cooking-progress-section {
  margin-bottom: 20px;
}

.progress-bar-container {
  position: relative;
  height: 8px;
  background-color: #e5e7eb;
  border-radius: 4px;
  margin: 10px 0;
}

.progress-bar {
  height: 100%;
  background-color: #22c55e;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-percentage {
  float: right;
  font-weight: 600;
  color: #22c55e;
}

.current-step-section {
  background-color: #f8fafc;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
  border-left: 4px solid #2563eb;
}

.step-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}

.step-description {
  font-size: 16px;
  line-height: 1.6;
  margin: 0;
}

.timer-section {
  text-align: center;
  margin-bottom: 20px;
}

.timer-display {
  font-size: 48px;
  font-weight: bold;
  margin: 10px 0;
  color: #2563eb;
}

.timer-info {
  font-size: 14px;
  color: #64748b;
  margin-bottom: 15px;
}

.timer-controls {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.step-navigation {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.all-steps-section {
  margin-top: 20px;
}

.steps-list {
  max-height: 300px;
  overflow-y: auto;
}

.step-item {
  display: flex;
  gap: 12px;
  padding: 10px;
  border-radius: 6px;
  margin-bottom: 8px;
  transition: background-color 0.2s;
}

.step-item:hover {
  background-color: #f1f5f9;
}

.completed-step {
  background-color: #f0fdf4;
}

.current-step-item {
  background-color: #eff6ff;
  border-left: 3px solid #2563eb;
}

.step-number {
  flex-shrink: 0;
}

.step-details {
  flex-grow: 1;
}

.step-text {
  margin: 0 0 4px 0;
  line-height: 1.5;
}

.step-time {
  font-size: 12px;
  color: #64748b;
}

.steps-list::-webkit-scrollbar {
  width: 6px;
}

.steps-list::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 3px;
}

.steps-list::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.steps-list::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
</style>