<template>
  <div class="books-view" ref="booksViewRef">
    <div class="books-container">
      <!-- 左侧分类导航 -->
      <aside class="left-sidebar">
        <div class="category-section">
          <button class="reset-btn" @click="resetCategories">重置筛选</button>

          <!-- 难度单选筛选区域 -->
          <div class="filter-dropdown-container">
            <div class="filter-title" @mouseenter="handleDifficultyMouseEnter" @mouseleave="handleDifficultyMouseLeave"
              :class="{ active: isDifficultyVisible }">
              <span>难度</span>
              <span class="filter-arrow">{{ isDifficultyVisible ? '▼' : '▶' }}</span>
            </div>
            <div v-if="isDifficultyVisible" class="filter-dropdown" @mouseenter="handleDifficultyDropdownMouseEnter"
              @mouseleave="handleDifficultyDropdownMouseLeave">
              <div v-for="option in difficultyOptions" :key="option.value" class="filter-option"
                :class="{ active: selectedDifficulty === option.value }" @click="selectedDifficulty = option.value">
                <span class="option-name">{{ option.label }}</span>
                <span v-if="selectedDifficulty === option.value" class="option-check">✓</span>
              </div>
            </div>
          </div>

          <!-- 评分单选筛选区域 -->
          <div class="filter-dropdown-container">
            <div class="filter-title" @mouseenter="handleRatingMouseEnter" @mouseleave="handleRatingMouseLeave"
              :class="{ active: isRatingVisible }">
              <span>评分</span>
              <span class="filter-arrow">{{ isRatingVisible ? '▼' : '▶' }}</span>
            </div>
            <div v-if="isRatingVisible" class="filter-dropdown filter-dropdown2"
              @mouseenter="handleRatingDropdownMouseEnter" @mouseleave="handleRatingDropdownMouseLeave">
              <div v-for="option in ratingOptions" :key="option.value" class="filter-option"
                :class="{ active: selectedRating === option.value }" @click="selectedRating = option.value">
                <span class="option-name">{{ option.label }}</span>
                <span v-if="selectedRating === option.value" class="option-check">✓</span>
              </div>
            </div>
          </div>

          <!-- 一级分类（从后端获取） -->
          <div v-for="primary in primaryCategories" :key="primary.id">
            <div class="category-item primary" :class="{ active: hoveredPrimaryId === primary.id }"
              @mouseenter="handlePrimaryMouseEnter(primary.id)" @mouseleave="handlePrimaryMouseLeave">
              <span class="category-name">{{ primary.name }}</span>
              <span class="selected-secondary-name" v-if="hoveredPrimaryId === primary.id && selectedSecondaryId">
                - {{ getSelectedSecondaryName(primary.id, selectedSecondaryId) }}
              </span>
              <span class="category-arrow" :class="{ expanded: hoveredPrimaryId === primary.id }">▶</span>
            </div>
            <div v-if="hoveredPrimaryId === primary.id" class="categories-dropdown"
              @mouseenter="handleDropdownMouseEnter" @mouseleave="handleDropdownMouseLeave">
              <div v-for="secondary in primary.secondaryCategories" :key="secondary.id" class="category-wrapper">
                <div class="category-item secondary">
                  <span class="category-name">{{ secondary.name }}</span>
                </div>
                <div class="third-categories-grid">
                  <div v-for="third in getSelectedThirdCategories(primary.id, secondary.id)" :key="third.id"
                    class="category-item third" :class="{ active: selectedThirdCategories.includes(third.name) }"
                    @click="toggleThirdCategory(third.name)">
                    <span class="category-name">{{ third.name }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </aside>

      <!-- 中间书籍展示区域 -->
      <main class="books-main">
        <div class="sort-controls">
          <div class="sort-options">
            <button v-for="option in sortOptions" :key="option.value" class="sort-btn"
              :class="{
                active: option.value === 'price' ?
                  (sortOption === 'price-asc' || sortOption === 'price-desc') :
                  sortOption === option.value
              }" @click="handleSortClick(option.value)">
              {{ option.label }}
              <span v-if="option.value === 'price' && (sortOption === 'price-asc' || sortOption === 'price-desc')"
                class="sort-arrow">
                {{ sortOption === 'price-asc' ? '↑' : '↓' }}
              </span>
            </button>
          </div>
        </div>

        <div class="books-grid">
          <RouterLink v-for="book in books" :key="book.id" :to="`/books/${book.id}`" class="book-card"
            :title="book.title">
            <div class="book-cover">
              <img :src="book.coverUrl" :alt="book.title" />
              <div class="book-badge" v-if="book.isNew">新品</div>
              <div class="book-badge hot" v-if="book.isHot">热销</div>
            </div>
            <div class="book-info">
              <div class="book-header">
                <h3 class="book-title">{{
                  book.title && book.title.length > 6 ? book.title.substring(0, 6) + '...' : (book.title || '')
                }}</h3>
                <span class="book-author">{{ book.author || '' }}</span>
              </div>
              <div class="book-rating">
                <span class="stars">{{ '★'.repeat(Math.floor(book.ratingScore)) }}{{
                  '☆'.repeat(5 - Math.floor(book.ratingScore))
                }}</span>
                <span class="rating-score">{{ book.ratingScore?.toFixed(1) }}</span>
              </div>
              <div class="book-footer">
                <div class="book-price">¥{{ book.price.toFixed(2) }}</div>
                <div class="book-actions">
                  <button class="btn btn-icon add-to-cart" title="加入购物车">
                    <svg viewBox="0 0 24 24" width="18" height="18">
                      <path fill="currentColor"
                        d="M7 18c-1.1 0-1.99.9-1.99 2S5.9 22 7 22s2-.9 2-2-.9-2-2-2zM1 2v2h2l3.6 7.59-1.35 2.45c-.16.28-.25.61-.25.96 0 1.1.9 2 2 2h12v-2H7.42c-.14 0-.25-.11-.25-.25l.03-.12.9-1.63h7.45c.75 0 1.41-.41 1.75-1.03l3.58-6.49c.08-.14.12-.31.12-.48 0-.55-.45-1-1-1H5.21l-.94-2H1zm16 16c-1.1 0-1.99.9-1.99 2s.89 2 1.99 2 2-.9 2-2-.9-2-2-2z" />
                    </svg>
                  </button>
                  <button class="btn btn-icon read-online" title="在线阅读">
                    <svg viewBox="0 0 24 24" width="18" height="18">
                      <path fill="currentColor"
                        d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V5h14v14zM7 10h2v7H7zm4-3h2v10h-2zm4 6h2v4h-2z" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </RouterLink>
        </div>

        <div class="pagination">
          <button class="btn btn-outline" :disabled="currentPage === 1" @click="currentPage--">上一页</button>
          <span class="page-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
          <button class="btn btn-outline" :disabled="currentPage === totalPages" @click="currentPage++">下一页</button>
        </div>
      </main>

      <!-- 右侧模块区域 -->
      <aside class="right-sidebar">
        <div class="search-module">
          <div class="search-input-container">
            <svg class="search-icon" viewBox="0 0 24 24" width="16" height="16">
              <path fill="currentColor"
                d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0 0 16 9.5 6.5 6.5 0 1 0 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" />
            </svg>
            <input v-model="searchQuery" type="text" placeholder="搜索内容..." class="search-input"
              @keyup.enter="handleSearch" />
            <button class="search-btn" @click="handleSearch">搜索</button>
          </div>
        </div>

        <div class="sidebar-module">
          <h3>热销书籍</h3>
          <div class="hot-books-list">
            <RouterLink v-for="book in hotBooks" :key="book.id" :to="`/books/${book.id}`" class="hot-book-item">
              <img :src="book.coverUrl" :alt="book.title" class="hot-book-cover" />
              <div class="hot-book-info">
                <h4>{{ book.title }}</h4>
                <p>{{ book.author }}</p>
                <div class="hot-book-price">¥{{ book.price.toFixed(2) }}</div>
              </div>
            </RouterLink>
          </div>
        </div>

        <div class="sidebar-module">
          <h3>阅读热榜</h3>
          <div class="ranking-list">
            <RouterLink v-for="(book, index) in readingRank" :key="book.id" :to="`/books/${book.id}`"
              class="ranking-item">
              <span class="ranking-number" :class="{ 'top3': index < 3 }">{{ index + 1 }}</span>
              <div class="ranking-info">
                <h4>{{ book.title }}</h4>
                <p>{{ book.author }}</p>
              </div>
            </RouterLink>
          </div>
        </div>

        <div class="sidebar-module">
          <h3>推荐书籍</h3>
          <div class="recommended-books">
            <RouterLink v-for="book in recommendedBooks" :key="book.id" :to="`/books/${book.id}`"
              class="recommended-book">
              <img :src="book.coverUrl" :alt="book.title" />
              <h4>{{ book.title }}</h4>
              <div class="book-price">¥{{ book.price.toFixed(2) }}</div>
            </RouterLink>
          </div>
        </div>

        <div class="sidebar-module challenge-module">
          <h3>烹饪挑战赛</h3>
          <a href="#" class="challenge-info">
            <h4>意大利面创意烹饪</h4>
            <p>展示你的意大利面烹饪技巧</p>
            <div class="challenge-date">12月15日截止</div>
          </a>
          <button class="btn btn-primary join-btn">立即参与</button>
        </div>
      </aside>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { RouterLink } from 'vue-router'
import {
  getBookList,
  getHotBooks,
  getReadingRank,
  getRecommendBooks,
  getBookCategories
} from '@/api/book'

// ==================== 筛选状态 ====================
const selectedThirdCategories = ref<string[]>([])
const priceRange = ref<[number, number]>([0, 500])
const searchQuery = ref('')
const sortOption = ref('recommended')
const currentPage = ref(1)
const pageSize = 21
const priceSortDirection = ref<'asc' | 'desc'>(Math.random() > 0.5 ? 'asc' : 'desc')

const sortOptions = ref([
  { value: 'recommended', label: '推荐排序' },
  { value: 'newest', label: '最新上架' },
  { value: 'price', label: '价格' }
])

const selectedDifficulty = ref<string>('all')
const difficultyOptions = ref([
  { value: 'all', label: '全部' },
  { value: 'easy', label: '简单' },
  { value: 'medium', label: '中等' },
  { value: 'hard', label: '困难' }
])

const selectedRating = ref<string>('all')
const ratingOptions = ref([
  { value: 'all', label: '全部' },
  { value: '3+', label: '3分以上' },
  { value: '4+', label: '4分以上' },
  { value: '4.5+', label: '4.5分以上' }
])

// ==================== UI 交互控制 ====================
const isDifficultyVisible = ref(false)
const isRatingVisible = ref(false)
let difficultyLeaveTimer: number | null = null
let ratingLeaveTimer: number | null = null
let mouseLeaveTimer: number | null = null

// 分类相关
const primaryCategories = ref<any[]>([])
const selectedPrimaryId = ref<string | null>(null)
const selectedSecondaryId = ref<string | null>(null)
const hoveredPrimaryId = ref<string | null>(null)

// 侧边栏数据
const hotBooks = ref<any[]>([])
const readingRank = ref<any[]>([])
const recommendedBooks = ref<any[]>([])

// 书籍列表数据
const books = ref<any[]>([])
const total = ref(0)

const booksViewRef = ref<HTMLElement | null>(null)

// ==================== 辅助函数 ====================
const buildQueryParams = () => {
  let sortBy = sortOption.value
  if (sortOption.value === 'price-asc') sortBy = 'price_asc'
  else if (sortOption.value === 'price-desc') sortBy = 'price_desc'
  else if (sortOption.value === 'newest') sortBy = 'newest'
  else sortBy = 'recommended'

  let difficulty: number | null = null
  if (selectedDifficulty.value === 'easy') difficulty = 1
  else if (selectedDifficulty.value === 'medium') difficulty = 2
  else if (selectedDifficulty.value === 'hard') difficulty = 3

  let minRating: number | null = null
  if (selectedRating.value === '3+') minRating = 3
  else if (selectedRating.value === '4+') minRating = 4
  else if (selectedRating.value === '4.5+') minRating = 4.5

  return {
    pageNum: currentPage.value,
    pageSize,
    sortBy,
    title: searchQuery.value,
    difficulty,
    minRating,
    minPrice: priceRange.value[0],
    maxPrice: priceRange.value[1]
  }
}

const loadBooks = async () => {
  const params = buildQueryParams()
  const res = await getBookList(params)
  books.value = res.rows || []
  total.value = res.total || 0
}

const loadSidebarData = async () => {
  const [hotRes, rankRes, recommendRes, categoriesRes] = await Promise.all([
    getHotBooks({ pageSize: 5 }),
    getReadingRank({ pageSize: 10 }),
    getRecommendBooks({ pageSize: 4 }),
    getBookCategories()
  ])
  hotBooks.value = hotRes.rows || []
  readingRank.value = rankRes.rows || []
  recommendedBooks.value = recommendRes.rows || []
  primaryCategories.value = categoriesRes.rows || []
}

// ==================== 事件处理 ====================
const handleSortClick = (optionValue: string) => {
  if (optionValue === 'price') {
    priceSortDirection.value = priceSortDirection.value === 'asc' ? 'desc' : 'asc'
    sortOption.value = `price-${priceSortDirection.value}`
  } else {
    sortOption.value = optionValue
  }
}

const handleDifficultyMouseEnter = () => {
  if (difficultyLeaveTimer) clearTimeout(difficultyLeaveTimer)
  isDifficultyVisible.value = true
}
const handleDifficultyMouseLeave = () => {
  difficultyLeaveTimer = setTimeout(() => {
    isDifficultyVisible.value = false
  }, 300)
}
const handleDifficultyDropdownMouseEnter = () => {
  if (difficultyLeaveTimer) clearTimeout(difficultyLeaveTimer)
}
const handleDifficultyDropdownMouseLeave = () => {
  difficultyLeaveTimer = setTimeout(() => {
    isDifficultyVisible.value = false
  }, 300)
}

const handleRatingMouseEnter = () => {
  if (ratingLeaveTimer) clearTimeout(ratingLeaveTimer)
  isRatingVisible.value = true
}
const handleRatingMouseLeave = () => {
  ratingLeaveTimer = setTimeout(() => {
    isRatingVisible.value = false
  }, 300)
}
const handleRatingDropdownMouseEnter = () => {
  if (ratingLeaveTimer) clearTimeout(ratingLeaveTimer)
}
const handleRatingDropdownMouseLeave = () => {
  ratingLeaveTimer = setTimeout(() => {
    isRatingVisible.value = false
  }, 300)
}

const handlePrimaryMouseEnter = (primaryId: string) => {
  if (mouseLeaveTimer) clearTimeout(mouseLeaveTimer)
  hoveredPrimaryId.value = primaryId
}
const handlePrimaryMouseLeave = () => {
  mouseLeaveTimer = window.setTimeout(() => {
    hoveredPrimaryId.value = null
  }, 200)
}
const handleDropdownMouseEnter = () => {
  if (mouseLeaveTimer) clearTimeout(mouseLeaveTimer)
}
const handleDropdownMouseLeave = () => {
  handlePrimaryMouseLeave()
}

const handleScroll = () => {
  if (booksViewRef.value) {
    const scrollY = window.scrollY
    const newPadding = scrollY > 5 ? 5 : 20
    booksViewRef.value.style.paddingTop = `${newPadding}px`
  }
}

const resetCategories = () => {
  selectedPrimaryId.value = null
  selectedSecondaryId.value = null
  selectedThirdCategories.value = []
  priceRange.value = [0, 500]
  selectedDifficulty.value = 'all'
  selectedRating.value = 'all'
  searchQuery.value = ''
  sortOption.value = 'recommended'
  currentPage.value = 1
}

const getSelectedSecondaryName = (primaryId: string, secondaryId: string) => {
  const primary = primaryCategories.value.find(p => p.id === primaryId)
  if (!primary) return ''
  const secondary = primary.secondaryCategories?.find((s: any) => s.id === secondaryId)
  return secondary ? secondary.name : ''
}

const toggleThirdCategory = (thirdCategory: string) => {
  const index = selectedThirdCategories.value.indexOf(thirdCategory)
  if (index > -1) selectedThirdCategories.value.splice(index, 1)
  else selectedThirdCategories.value.push(thirdCategory)
  currentPage.value = 1
}

const getSelectedThirdCategories = (primaryId: string, secondaryId: string) => {
  const primary = primaryCategories.value.find(p => p.id === primaryId)
  if (!primary) return []
  const secondary = primary.secondaryCategories?.find((s: any) => s.id === secondaryId)
  return secondary?.thirdCategories || []
}

const handleSearch = () => {
  currentPage.value = 1
}

// ==================== 计算属性 ====================
const filteredBooks = computed(() => books.value)
const totalPages = computed(() => Math.ceil(total.value / pageSize))

// ==================== 监听筛选变化 ====================
watch(
  [searchQuery, selectedDifficulty, selectedRating, priceRange, sortOption, currentPage],
  () => {
    loadBooks()
  },
  { deep: true }
)

// ==================== 生命周期 ====================
onMounted(async () => {
  await loadSidebarData()
  await loadBooks()
  window.addEventListener('scroll', handleScroll)
  handleScroll()
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
@import '@/styles/components/BooksView.css';
</style>