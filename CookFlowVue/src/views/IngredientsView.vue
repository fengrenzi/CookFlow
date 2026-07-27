<template>
  <div class="ingredients-main-container">
    <div class="main-content">
      <!-- 左侧：食材列表（一级分类 + 二级食材网格） -->
      <div class="ingredients-container">
        <div v-if="loading" class="loading-state">
          <p>加载食材中...</p>
        </div>
        <div v-else-if="categories.length > 0" class="categories-list">
          <div
            v-for="category in categories"
            :key="category.id"
            class="category-section"
          >
            <!-- 一级分类标题（仅文字） -->
            <div class="category-title">{{ category.name }}</div>
            <!-- 二级食材网格（水平排列） -->
            <div class="ingredients-grid">
              <div
                v-for="ingredient in category.children"
                :key="ingredient.id"
                class="ingredient-card"
                @click="toggleIngredient(ingredient)"
              >
                <div class="ingredient-image">
                  <img :src="ingredient.imageUrl" class="ingredient-img" :alt="ingredient.name" />
                </div>
                <div class="ingredient-name-container">
                  <div class="ingredient-name" :class="{ selected: isSelected(ingredient.id) }">
                    {{ ingredient.name }}
                  </div>
                  <button class="selection-button" @click.stop="toggleIngredient(ingredient)">
                    <span v-if="isSelected(ingredient.id)">−</span>
                    <span v-else>+</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="empty-state">
          <p>暂无食材分类</p>
        </div>
      </div>

      <!-- 右侧：推荐菜谱 -->
      <div class="recommended-recipes-container">
        <div class="section-title-container section-title">
          <p>推荐菜谱</p>
          <button v-if="selectedIngredients.length > 0" class="clear-all-btn" @click="clearAll">
            全部清空
          </button>
        </div>
        <div class="recipes-section">
          <div v-if="loadingRecipes" class="loading-recipes">
            <p>加载推荐中...</p>
          </div>
          <div v-else-if="recommendedRecipes.length > 0" class="recipes-list">
            <div
              v-for="recipe in recommendedRecipes"
              :key="recipe.id"
              class="recipe-card"
              @click="goToRecipe(recipe.id)"
            >
              <div class="recipe-image">
                <img :src="recipe.imgUrl" class="recipe-img" :alt="recipe.name" />
              </div>
              <div class="recipe-info">
                <h4 class="recipe-name">{{ recipe.title }}</h4>
                <p class="recipe-description">{{ recipe.description }}</p>
              </div>
            </div>
          </div>
          <div v-else class="empty-recipes">
            <p>选择食材后查看相关推荐菜谱</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getIngredientTree, getRecommendedRecipesByIngredients } from '@/api/ingredient'

// 类型定义（对应后端 CategoryTreeDto）
interface CategoryNode {
  id: string
  name: string
  imageUrl?: string
  children: CategoryNode[]
}

interface Recipe {
  id: string
  name: string
  description: string
  imageUrl: string
}

// 响应式数据
const categories = ref<CategoryNode[]>([])          // 一级分类列表
const selectedIngredients = ref<CategoryNode[]>([]) // 选中的食材对象列表（二级）
const recommendedRecipes = ref<Recipe[]>([])

const loading = ref(false)
const loadingRecipes = ref(false)

const router = useRouter()

// 获取食材分类树
const fetchIngredientTree = async () => {
  loading.value = true
  try {
    const data = await getIngredientTree()
    categories.value = data
  } catch (error) {
    ElMessage.error('获取食材列表失败')
  } finally {
    loading.value = false
  }
}

// 切换食材选中状态
const toggleIngredient = (ingredient: CategoryNode) => {
  const index = selectedIngredients.value.findIndex(i => i.id === ingredient.id)
  if (index === -1) {
    selectedIngredients.value.push(ingredient)
  } else {
    selectedIngredients.value.splice(index, 1)
  }
}

// 判断食材是否被选中
const isSelected = (id: string): boolean => {
  return selectedIngredients.value.some(i => i.id === id)
}

// 清空所有选中食材
const clearAll = () => {
  selectedIngredients.value = []
}

// 监听选中食材变化，请求推荐菜谱
watch(
  selectedIngredients,
  async (newVal: CategoryNode[]) => {
    if (newVal.length === 0) {
      recommendedRecipes.value = []
      return
    }
    loadingRecipes.value = true
    try {
      const ingredientIds = newVal.map(i => i.id)
      const res = await getRecommendedRecipesByIngredients(ingredientIds)
      recommendedRecipes.value = res
    } catch (error) {
      ElMessage.error('获取推荐菜谱失败')
      recommendedRecipes.value = []
    } finally {
      loadingRecipes.value = false
    }
  },
  { deep: true }
)

// 跳转菜谱详情
const goToRecipe = (id: string) => {
  router.push(`/recipe/${id}`)
}

// 初始化
onMounted(() => {
  fetchIngredientTree()
})
</script>

<style scoped>
@import '@/styles/components/IngredientsView.css';

/* 新增样式：一级分类竖向排列 */
.categories-list {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.category-section {
  width: 100%;
}

.category-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--secondary-color, #2c3e50);
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid var(--secondary-color, #2c3e50);
}

/* 覆盖原有 .ingredients-grid 样式，保持水平网格布局 */
.ingredients-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 1rem;
}

/* 确保食材卡片样式与之前一致 */
.ingredient-card {
  cursor: pointer;
  transition: transform 0.3s ease;
  padding: 0.5rem;
}

.ingredient-card:hover {
  transform: translateY(-4px);
}

.ingredient-image {
  width: 100%;
  height: 120px;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f0f0;
}

.ingredient-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.ingredient-card:hover .ingredient-img {
  transform: scale(1.1);
}

.ingredient-name-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.ingredient-name {
  font-size: 1rem;
  color: var(--text-color, #333);
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
  text-align: center;
}

.ingredient-name.selected {
  color: var(--primary-color, #ff9900);
  font-weight: 600;
}

.selection-button {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.9);
  border: 1px solid var(--primary-color, #ff9900);
  color: var(--primary-color, #ff9900);
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.selection-button:hover {
  background-color: var(--primary-color, #ff9900);
  color: white;
}
</style>