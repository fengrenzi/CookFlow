<template>
  <div class="recipes-container">
    <!-- 搜索区域 -->
    <div class="search-section">
      <div class="search-input-container">
        <div class="search-icon">
          <svg viewBox="0 0 1024 1024" width="16" height="16" fill="currentColor">
            <path d="M909.6 854.5L649.9 594.8C690.2 542.7 712 479 712 412c0-80.2-31.3-155.4-87.9-212.1-56.6-56.7-132-87.9-212.1-87.9s-155.5 31.3-212.1 87.9C143.2 256.5 112 331.8 112 412c0 80.1 31.3 155.5 87.9 212.1C256.5 680.8 331.8 712 412 712c67 0 130.6-21.8 182.7-62l259.7 259.6a8.2 8.2 0 0 0 11.6 0l43.6-43.5a8.2 8.2 0 0 0 0-11.6zM570.4 412c0 73.1-59.5 132.6-132.6 132.6s-132.6-59.5-132.6-132.6c0-73.1 59.5-132.6 132.6-132.6s132.6 59.5 132.6 132.6z" />
          </svg>
        </div>
        <input v-model="searchQuery" placeholder="搜索菜谱名称、食材或作者..." @keyup.enter="handleSearch" class="search-input" type="text" />
        <button v-if="searchQuery" @click="clearSearch" class="clear-search-btn" title="清除搜索">&times;</button>
        <button @click="handleSearch" class="green-search-btn">搜索</button>
      </div>
    </div>

    <!-- 分类选择区域 -->
    <div class="filter-section">
      <div v-for="(category, index) in categories" :key="category.分类名称" class="category-row">
        <div class="category-title">
          <span class="category-icon">{{ category.icon }}</span>
          <span class="category-name">{{ category.分类名称 }}</span>
        </div>
        <div class="category-content">
          <div class="category-options">
            <el-tag v-for="option in category.选项.slice(0, defaultVisibleCount)" :key="option"
              :effect="selectedFilters[index] === option ? 'dark' : 'plain'" @click="selectFilter(index, option)"
              class="option-tag">
              {{ option }}
            </el-tag>
            <el-button v-if="category.选项.length > defaultVisibleCount" type="text" @click="toggleExpand(index)"
              class="expand-btn">
              {{ expandedCategories[index] ? '收起' : `展开 ${category.选项.length - defaultVisibleCount} 项` }}
              <el-icon><ArrowDown v-if="!expandedCategories[index]" /><ArrowUp v-else /></el-icon>
            </el-button>
          </div>
          <div v-if="expandedCategories[index]" class="expanded-options">
            <el-tag v-for="option in category.选项.slice(defaultVisibleCount)" :key="option"
              :effect="selectedFilters[index] === option ? 'dark' : 'plain'" @click="selectFilter(index, option)"
              class="option-tag">
              {{ option }}
            </el-tag>
          </div>
        </div>
      </div>

      <!-- 已选筛选条件 -->
      <div v-if="activeFilters.length > 0" class="active-filters">
        <div class="active-filters-title">已选条件：</div>
        <div class="active-filters-list">
          <el-tag v-for="(filter, index) in activeFilters" :key="index" closable @close="removeFilter(filter)" type="info">
            {{ filter }}
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
          <el-radio-group v-model="sortBy" size="large">
            <el-radio-button label="newest" border @click="setSortBy('newest')">最新发布</el-radio-button>
            <el-radio-button label="popular" border @click="setSortBy('popular')">最受欢迎</el-radio-button>
            <el-radio-button label="difficulty" border @click="setSortBy('difficulty')">难度</el-radio-button>
          </el-radio-group>
        </div>
        <div class="view-options">
          <el-button-group size="small">
            <el-button :type="viewMode === 'grid' ? 'primary' : 'default'" @click="viewMode = 'grid'"><el-icon><Grid /></el-icon></el-button>
            <el-button :type="viewMode === 'list' ? 'primary' : 'default'" @click="viewMode = 'list'"><el-icon><List /></el-icon></el-button>
          </el-button-group>
        </div>
      </div>

      <!-- 菜谱卡片网格 -->
      <div class="recipes-grid" :class="{ 'list-view': viewMode === 'list' }">
        <el-card v-for="recipe in filteredAndSortedRecipes" :key="recipe.id" class="recipe-card"
          :body-style="{ padding: '0px', margin: '0px', height: '100%' }"
          style="margin-bottom: 12px; border-radius: 8px; border: 1px solid #f0f0f0; overflow: hidden;"
          :shadow="'hover'">
          <div class="card-content" @click="navigateToDetail(recipe.id)" style="cursor: pointer;">
            <div class="recipe-image">
              <ImageDisplay :image-id="recipe.imageId" fit="cover" />
            </div>
            <div class="recipe-info">
              <div class="recipe-header">
                <h3 class="recipe-title">{{ recipe.title || '未命名菜谱' }}</h3>
                <span class="publish-time">{{ recipe.publishTime || '' }}</span>
              </div>
              <div class="recipe-ingredients">
                <span class="ingredients-label">主要食材：</span>
                <span v-for="(ingredient, idx) in (recipe.ingredients || []).slice(0, 3)" :key="idx" class="ingredient-tag">
                  {{ ingredient }}<span v-if="idx < (recipe.ingredients || []).slice(0, 3).length - 1">、</span>
                </span>
                <span v-if="(recipe.ingredients || []).length > 3" class="ingredients-more">等{{ recipe.ingredients.length }}种</span>
              </div>
              <div class="recipe-time-info">
                <span class="time-item">备菜：{{ recipe.prepTime || '未知' }}</span>
                <span class="time-separator">|</span>
                <span class="time-item">烹饪：{{ recipe.cookTime || '未知' }}</span>
              </div>
              <div class="recipe-categories">
                <el-tag v-for="(cat, idx) in (recipe.categories || [])" :key="idx" size="small" effect="plain">{{ cat }}</el-tag>
              </div>
              <div class="recipe-bottom-info">
                <div class="recipe-author">
                  <el-avatar size="small" :src="recipe.avatarUrl || undefined" :icon="User"></el-avatar>
                  <span class="author-name">{{ recipe.author || '未知作者' }}</span>
                </div>
                <div class="recipe-right-section">
                  <div class="recipe-interaction">
                    <span class="interaction-item"><el-icon><Star /></el-icon>{{ recipe.favoriteCount || 0 }}</span>
                    <span class="interaction-item"><el-icon><Message /></el-icon>{{ recipe.commentCount || 0 }}</span>
                  </div>
                  <div>
                    <el-button size="small" :type="favorites.has(recipe.id) ? 'primary' : 'default'" plain
                      @click.stop="likeRecipe(recipe.id)" class="favorite-btn">
                      <el-icon><Star /></el-icon>{{ favorites.has(recipe.id) ? '已收藏' : '收藏' }}
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

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper" :total="totalRecipes" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ArrowUp, ArrowDown, Grid, List, Star, Message, User, ShoppingCart } from '@element-plus/icons-vue'
import { ElTag, ElRadioGroup, ElRadioButton, ElCard, ElPagination, ElAvatar, ElEmpty } from 'element-plus'
import { useRecipesViewLogic } from '@/scripts/recipesViewLogic'
import ImageDisplay from '@/components/ImageDisplay.vue'

const logic = useRecipesViewLogic()

const {
  categories,
  selectedFilters,
  activeFilters,
  sortBy,
  viewMode,
  currentPage,
  pageSize,
  totalRecipes,
  expandedCategories,
  defaultVisibleCount,
  searchQuery,
  filteredRecipes,
  favorites,
  selectFilter,
  removeFilter,
  clearAllFilters,
  toggleExpand,
  handleSizeChange,
  handleCurrentChange,
  likeRecipe,
  addToCart,
  navigateToDetail,
  handleSearch,
  clearSearch,
  filteredAndSortedRecipes,
  setSortBy
} = logic
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
</style>