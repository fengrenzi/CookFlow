<template>
  <div class="recipes-container">
    <!-- 搜索区域 -->
    <div class="search-section">
      <div class="search-input-container">
        <div class="search-icon">
          <svg viewBox="0 0 1024 1024" width="16" height="16" fill="currentColor">
            <path
              d="M909.6 854.5L649.9 594.8C690.2 542.7 712 479 712 412c0-80.2-31.3-155.4-87.9-212.1-56.6-56.7-132-87.9-212.1-87.9s-155.5 31.3-212.1 87.9C143.2 256.5 112 331.8 112 412c0 80.1 31.3 155.5 87.9 212.1C256.5 680.8 331.8 712 412 712c67 0 130.6-21.8 182.7-62l259.7 259.6a8.2 8.2 0 0 0 11.6 0l43.6-43.5a8.2 8.2 0 0 0 0-11.6zM570.4 412c0 73.1-59.5 132.6-132.6 132.6s-132.6-59.5-132.6-132.6c0-73.1 59.5-132.6 132.6-132.6s132.6 59.5 132.6 132.6z" />
          </svg>
        </div>
        <input v-model="searchQuery" placeholder="搜索菜谱名称、食材或作者..." @keyup.enter="handleSearch" class="search-input" type="text" />
        <button v-if="searchQuery" @click="clearSearch" class="clear-search-btn" title="清除搜索">&times;</button>
        <button @click="handleSearch" class="green-search-btn">搜索</button>
      </div>
    </div>

    <!-- 分类选择区域 -->
    <div class="filter-section">
      <div v-for="(category, index) in categories" :key="category.categoryName" class="category-row">
        <div class="category-title">
          <span class="category-icon">{{ category.icon }}</span>
          <span class="category-name">{{ category.categoryName }}</span>
        </div>
        <div class="category-content">
          <div class="category-options">
            <el-tag v-for="option in category.options.slice(0, defaultVisibleCount)" :key="option.id"
              :effect="selectedFilters[index] === option.id ? 'dark' : 'plain'"
              @click="selectFilter(index, option.id, option.name)" class="option-tag">
              {{ option.name }}
            </el-tag>
            <el-button v-if="category.options.length > defaultVisibleCount" type="text" @click="toggleExpand(index)"
              class="expand-btn">
              {{ expandedCategories[index] ? '收起' : `展开 ${category.options.length - defaultVisibleCount} 项` }}
              <el-icon>
                <ArrowDown v-if="!expandedCategories[index]" />
                <ArrowUp v-else />
              </el-icon>
            </el-button>
          </div>
          <div v-if="expandedCategories[index]" class="expanded-options">
            <el-tag v-for="option in category.options.slice(defaultVisibleCount)" :key="option.id"
              :effect="selectedFilters[index] === option.id ? 'dark' : 'plain'"
              @click="selectFilter(index, option.id, option.name)" class="option-tag">
              {{ option.name }}
            </el-tag>
          </div>
        </div>
      </div>

      <!-- 已选筛选条件 -->
      <div v-if="activeFilters.length > 0" class="active-filters">
        <div class="active-filters-title">已选条件：</div>
        <div class="active-filters-list">
          <el-tag v-for="(filter, idx) in activeFilters" :key="idx" closable @close="removeFilter(filter)"
            type="info">
            {{ filter.optionName }}
          </el-tag>
          <el-button type="text" size="small" @click="clearAllFilters" class="clear-all-btn">清空</el-button>
        </div>
      </div>
    </div>

    <!-- 菜谱展示区域 -->
    <div class="recipes-section">
      <!-- 排序和筛选控制 -->
      <div class="recipes-controls">
        <div class="sort-options">
          <el-radio-group v-model="sortBy" size="large" @change="handleSortChange">
            <el-radio-button label="newest" border>最新发布</el-radio-button>
            <el-radio-button label="popular" border>最受欢迎</el-radio-button>
            <el-radio-button label="difficulty" border>难度</el-radio-button>
          </el-radio-group>
        </div>
        <div class="view-options">
          <el-button-group size="small">
            <el-button :type="viewMode === 'grid' ? 'primary' : 'default'" @click="viewMode = 'grid'">
              <el-icon><Grid /></el-icon>
            </el-button>
            <el-button :type="viewMode === 'list' ? 'primary' : 'default'" @click="viewMode = 'list'">
              <el-icon><List /></el-icon>
            </el-button>
          </el-button-group>
        </div>
      </div>

      <!-- 菜谱卡片网格（一行3列） -->
      <div class="recipes-grid" :class="{ 'list-view': viewMode === 'list' }">
        <el-card v-for="recipe in recipes" :key="recipe.id" class="recipe-card"
          :body-style="{ padding: '0px', margin: '0px', height: '100%' }"
          style="margin-bottom: 12px; border-radius: 8px; border: 1px solid #f0f0f0; overflow: hidden;"
          :shadow="'hover'">
          <div class="card-content" @click="navigateToDetail(recipe.id)" style="cursor: pointer;">
            <div class="recipe-image">
              <ImageDisplay :imgurl="recipe.imgUrl" alt="菜谱封面" fit="cover" class="recipe-img" />
            </div>
            <div class="recipe-info">
              <div class="recipe-header">
                <h3 class="recipe-title">{{ recipe.title || '未命名菜谱' }}</h3>
                <span class="publish-time">{{ recipe.publishTime || '' }}</span>
              </div>

              <div class="recipe-ingredients" v-if="recipe.ingredients">
                <span class="ingredients-label">主要食材：</span>
                <span v-for="(ingredient, i) in recipe.ingredients" :key="i"
                  class="ingredient-tag">
                  {{ ingredient }}
                  <span v-if="i < (recipe.ingredients || []).slice(0, 3).length - 1">、</span>
                </span>
                <span v-if="(recipe.ingredients || []).length > 3" class="ingredients-more">
                  等{{ recipe.ingredients.length }}种
                </span>
              </div>

              <div class="recipe-time-info">
                <span class="time-item">备菜：{{ recipe.prepTime || '未知' }}</span>
                <span class="time-separator">|</span>
                <span class="time-item">烹饪：{{ recipe.cookTime || '未知' }}</span>
              </div>

              <div class="recipe-time-info">
                <span class="time-item">难度：{{ recipe.difficulty || '未知' }}</span>
              </div>

              <div class="recipe-categories">
                <el-tag v-for="(cat, i) in (recipe.categoryNames || [])" :key="i" size="small" effect="plain">
                  {{ cat }}
                </el-tag>
              </div>

              <div class="recipe-bottom-info">
                <div class="recipe-author">
                  <el-avatar size="small" :src="recipe.authorAvatar || undefined" :icon="User"></el-avatar>
                  <span class="author-name">{{ recipe.authorName || '未知作者' }}</span>
                </div>

                <div class="recipe-right-section">
                  <div class="recipe-interaction">
                    <span class="interaction-item">
                      <el-icon><Star /></el-icon>
                      {{ recipe.favoriteCount || 0 }}
                    </span>
                    <span class="interaction-item">
                      <el-icon><Message /></el-icon>
                      {{ recipe.commentCount || 0 }}
                    </span>
                  </div>

                  <div>
                    <el-button size="small"
                      :type="(favorites && recipe.id && favorites.has(recipe.id)) ? 'primary' : 'default'" plain
                      @click.stop="likeRecipe(recipe.id)" class="favorite-btn">
                      <el-icon><Star /></el-icon>
                      {{ favorites.has(recipe.id) ? '已收藏' : '收藏' }}
                    </el-button>

                    <el-button size="small" type="success" plain @click.stop="addToCart(recipe)" class="cart-btn">
                      <el-icon><ShoppingCart /></el-icon>
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 分页（每页显示30的倍数） -->
      <div class="pagination">
        <el-pagination
          :current-page="filterParams.pageNum"
          :page-size="filterParams.pageSize"
          :total="total"
          :page-sizes="[30, 60, 90, 120]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowUp, ArrowDown, Grid, List, Star, Message, User, ShoppingCart } from '@element-plus/icons-vue'
import { getRecipeList } from '@/api/recipe'
import { getCategoryTree } from '@/api/category'
import { addToCart as addToCartApi } from '@/api/shoppingCart'
import ImageDisplay from '@/components/ImageDisplay.vue'

const router = useRouter()
const route = useRoute()

// ---------- 分类相关 ----------
interface CategoryOption {
  id: number
  name: string
}

interface Category {
  categoryName: string
  icon: string
  options: CategoryOption[]
}

const categories = ref<Category[]>([])
const selectedFilters = ref<(number | null)[]>([])
const activeFilters = ref<{ categoryName: string; optionId: number; optionName: string }[]>([])
const expandedCategories = ref<boolean[]>([])
const defaultVisibleCount = 15

// 获取分类树
const loadCategories = async () => {
  try {
    const res = await getCategoryTree('recipes')
    const transformed = res.map((item: any) => ({
      categoryName: item.name,
      icon: getIconForCategory(item.name),
      options: item.children.map((op: any) => ({
        id: op.id,
        name: op.name
      }))
    }))
    categories.value = transformed
    selectedFilters.value = new Array(categories.value.length).fill(null)
    expandedCategories.value = new Array(categories.value.length).fill(false)
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

// 一级图标映射
const getIconForCategory = (name: string) => {
  const map: Record<string, string> = {
    '菜系': '🌍', '食材': '🥬', '工艺': '👨‍🍳', '标签': '🏷️',
    '口味': '😋', '耗时': '⏰', '难度': '📊', '场景': '🎯',
    '人群': '👥', '养生': '🌿', '时节': '📅'
  }
  return map[name] || '📌'
}

// ---------- 菜谱相关 ----------
const recipes = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
const searchQuery = ref('')
const sortBy = ref('newest')
const viewMode = ref('grid')

// 筛选参数（默认每页30条）
const filterParams = reactive({
  keyword: '',
  categoryId: null as number | null,
  sort: 'newest',
  pageNum: 1,
  pageSize: 30
})

// 收藏状态（前端维护）
const favorites = ref<Set<number>>(new Set())

// 加载菜谱列表
const loadRecipes = async () => {
  loading.value = true
  try {
    const params = {
      keyword: filterParams.keyword,
      categoryId: filterParams.categoryId,
      sort: filterParams.sort,
      pageNum: filterParams.pageNum,
      pageSize: filterParams.pageSize
    }
    const res = await getRecipeList(params)
    recipes.value = res.rows || []
    total.value = res.total || 0
  } catch (error) {
    ElMessage.error('加载菜谱失败')
  } finally {
    loading.value = false
  }
}

// 监听筛选参数变化
watch([
  () => filterParams.keyword,
  () => filterParams.categoryId,
  () => filterParams.sort,
  () => filterParams.pageNum,
  () => filterParams.pageSize
], () => {
  loadRecipes()
})

// 搜索
const handleSearch = () => {
  filterParams.keyword = searchQuery.value
  filterParams.pageNum = 1
}

const clearSearch = () => {
  searchQuery.value = ''
  filterParams.keyword = ''
  filterParams.pageNum = 1
}

// 排序切换
const handleSortChange = (value: string) => {
  sortBy.value = value
  filterParams.sort = value
  filterParams.pageNum = 1
}

// 分页处理
const handleSizeChange = (size: number) => {
  filterParams.pageSize = size
  filterParams.pageNum = 1
}
const handleCurrentChange = (page: number) => {
  filterParams.pageNum = page
}

// 收藏
const likeRecipe = async (id: number) => {
  try {
    if (favorites.value.has(id)) {
      favorites.value.delete(id)
      ElMessage.success('已取消收藏')
    } else {
      favorites.value.add(id)
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 添加购物车
const addToCart = async (recipe: any) => {
  try {
    await addToCartApi({
      itemType: 'recipe',
      itemId: recipe.id,
      quantity: 1
    })
    ElMessage.success('已加入购物车')
  } catch (error) {
    ElMessage.error('加入购物车失败')
  }
}

// 跳转详情
const navigateToDetail = (id: number) => {
  router.push(`/recipe/${id}`)
}

// ---------- 筛选器交互 ----------
const selectFilter = (categoryIndex: number, optionId: number, optionName: string) => {
  if (optionId === 0) {
    selectedFilters.value[categoryIndex] = null
  } else {
    selectedFilters.value[categoryIndex] = optionId
  }

  if (categories.value[categoryIndex]?.categoryName === '菜系') {
    filterParams.categoryId = selectedFilters.value[categoryIndex]
  } else {
    filterParams.categoryId = null
  }

  updateActiveFilters()
  filterParams.pageNum = 1
}

const updateActiveFilters = () => {
  activeFilters.value = []
  for (let i = 0; i < categories.value.length; i++) {
    const selectedId = selectedFilters.value[i]
      if (selectedId !== null && selectedId !== 0) {
        const option = categories.value[i].options.find(opt => opt.id === selectedId)
      if (option) {
        activeFilters.value.push({
          categoryName: categories.value[i].categoryName,
          optionId: selectedId,
          optionName: option.name
        })
      }
    }
  }
}

const removeFilter = (filter: { categoryName: string; optionId: number; optionName: string }) => {
  const index = categories.value.findIndex(cat => cat.categoryName === filter.categoryName)
  if (index !== -1) {
    selectedFilters.value[index] = null
    if (filter.categoryName === '菜系') {
      filterParams.categoryId = null
    }
  }
  updateActiveFilters()
  filterParams.pageNum = 1
}

const clearAllFilters = () => {
  selectedFilters.value = new Array(categories.value.length).fill(null)
  filterParams.categoryId = null
  filterParams.keyword = ''
  searchQuery.value = ''
  updateActiveFilters()
  filterParams.pageNum = 1
}

const toggleExpand = (index: number) => {
  expandedCategories.value[index] = !expandedCategories.value[index]
}

// 从 URL 初始化分类选中
const initCategoryFromUrl = () => {
  const categoryId = route.query.categoryId
  if (!categoryId) return

  const setCategory = () => {
    for (let i = 0; i < categories.value.length; i++) {
      const category = categories.value[i]
      if (category.categoryName === '菜系') {
        const matchedOption = category.options.find(opt => String(opt.id) === String(categoryId))
        if (matchedOption) {
          selectedFilters.value[i] = matchedOption.id
          filterParams.categoryId = matchedOption.id
          updateActiveFilters()
          filterParams.pageNum = 1
          break
        }
      }
    }
  }

  if (categories.value.length > 0) {
    setCategory()
  } else {
    const unwatch = watch(categories, (newVal) => {
      if (newVal.length > 0) {
        setCategory()
        unwatch()
      }
    })
  }
}

// ---------- 生命周期 ----------
onMounted(async () => {
  await loadCategories()
  initCategoryFromUrl()
  await loadRecipes()
})
</script>

<style scoped>
@import '@/styles/components/RecipesView.css';

.sort-options {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
.sort-options .el-radio-button__original-radio:checked + .el-radio-button__inner {
  background-color: #1890ff;
  border-color: #1890ff;
}

/* 一行3列网格布局 */
.recipes-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}
/* 列表视图覆盖为单列 */
.recipes-grid.list-view {
  display: block;
}
/* 响应式：屏幕小于 768px 时改为 2 列 */
@media (max-width: 768px) {
  .recipes-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
/* 屏幕小于 480px 时改为 1 列 */
@media (max-width: 480px) {
  .recipes-grid {
    grid-template-columns: 1fr;
  }
}
</style>