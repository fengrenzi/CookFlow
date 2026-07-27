<template>
  <div class="book-detail-container">
    <div class="page-loader" v-if="loading">
      <div class="loader-spinner"></div>
      <p class="loading-text">正在加载书籍详情...</p>
    </div>
    <template v-else-if="bookData.id">
      <div class="book-info-section">
        <div class="book-main-info">
          <div class="book-image">
            <img v-if="bookData.coverUrl" :src="bookData.coverUrl" :alt="bookData.title" class="book-cover" />
            <div v-else class="image-placeholder">
              <i class="icon-book"></i>
              <span>{{ bookData.title }}</span>
            </div>
          </div>
          <div class="book-details">
            <div class="book-meta">
              <h1 class="book-title">{{ bookData.title }}</h1>
              <div class="book-basic-info">
                <div class="info-item"><span class="info-label">作者：</span><span class="info-value">{{ bookData.author }}</span></div>
                <div class="info-item"><span class="info-label">出版社：</span><span class="info-value">{{ bookData.publisher }}</span></div>
                <div class="info-item"><span class="info-label">ISBN：</span><span class="info-value">{{ bookData.isbn }}</span></div>
                <div class="info-item"><span class="info-label">价格：</span><span class="info-value price">¥{{ bookData.price }}</span></div>
              </div>
              <div class="book-stats">
                <div class="stat-item"><span class="stat-label">菜谱数</span><span class="stat-value">{{ bookData.recipeCount }}</span></div>
                <div class="stat-item"><span class="stat-label">阅读人数</span><span class="stat-value">{{ bookData.readCount }}</span></div>
                <div class="stat-item"><span class="stat-label">推荐人数</span><span class="stat-value">{{ bookData.recommendCount }}</span></div>
                <div class="stat-item"><span class="stat-label">在读人数</span><span class="stat-value">{{ bookData.readingCount }}</span></div>
              </div>
              <div class="book-description">
                <h3>内容简介</h3>
                <p>{{ bookData.description }}</p>
              </div>
            </div>
            <div class="book-actions">
              <button class="btn btn-outline favorite-btn" :class="{ active: isFavorite }" @click="toggleFavorite">
                <StarFilled v-if="isFavorite" size="20" /><Star v-else size="20" />
              </button>
              <button class="btn btn-primary add-to-cart" :class="{ 'cart-added': isAddedToCart }" @click="addToCart">
                {{ isAddedToCart ? '已加入购物车' : '加入购物车' }}
              </button>
              <button class="btn btn-secondary start-reading" @click="startReading">开始阅读</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 评论区域 -->
      <div class="book-reviews-section">
        <CommentSection resource-type="book" :resource-id="bookId" :show-rating="true" />
      </div>
    </template>
    <div v-else class="error-message">
      <p>书籍不存在或加载失败</p>
      <button @click="$router.back()">返回上一页</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star, StarFilled } from '@element-plus/icons-vue'
import { getBookDetail, toggleBookFavorite } from '@/api/book'
import { useCartStore } from '@/store/modules/cart'
import CommentSection from '@/components/CommentSection.vue'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

const bookId = route.params.id as string
const bookData = ref<any>({})
const loading = ref(true)
const isFavorite = ref(false)
const isAddedToCart = ref(false)

const fetchBookDetail = async () => {
  try {
    const data = await getBookDetail(bookId)
    bookData.value = data
    // 检查购物车状态
    const cartItem = cartStore.allItems.find(item => item.type === 'book' && item.bookId === bookId)
    isAddedToCart.value = !!cartItem
    // 检查收藏状态（需要后端实现 /books/{id}/favorite-status）
    // 这里暂时设为 false，实际应调用接口获取
    isFavorite.value = false
  } catch (error) {
    console.error('加载书籍详情失败', error)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const toggleFavorite = async () => {
  try {
    await toggleBookFavorite(bookId)
    isFavorite.value = !isFavorite.value
    ElMessage.success(isFavorite.value ? '已加入收藏' : '已取消收藏')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const addToCart = () => {
  if (isAddedToCart.value) {
    const cartItem = cartStore.allItems.find(item => item.type === 'book' && item.bookId === bookId)
    if (cartItem) cartStore.removeFromCart(cartItem.id)
    isAddedToCart.value = false
    ElMessage.success('已取消加入购物车')
  } else {
    cartStore.addBookRecipeToCart(
      bookData.value.id,
      bookData.value.title,
      bookData.value.coverImageId,
      1,
      '书中精选菜谱',
      '',
      [],
      1
    )
    isAddedToCart.value = true
    ElMessage.success('已成功加入购物车')
  }
}

const startReading = () => {
  router.push(`/books/${bookId}/read`)
}

onMounted(() => {
  fetchBookDetail()
})
</script>

<style scoped>
@import '@/styles/components/BookDetailView.css';
</style>