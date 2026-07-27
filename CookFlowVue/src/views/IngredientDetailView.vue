<template>
  <div class="ingredient-detail-container">
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>
    <div v-else-if="error" class="error-container">
      <p class="error-message">{{ error }}</p>
      <el-button class="retry-btn" @click="fetchIngredientDetails">重试</el-button>
    </div>
    <template v-else>
      <!-- 轮播图区域 -->
      <div class="carousel-section">
        <div class="carousel-container">
          <div class="carousel-wrapper" :class="{
            'first-slide': currentIndex === 0,
            'middle-slide': carouselItems && carouselItems.length > 0 && currentIndex > 0 && currentIndex < carouselItems.length - 1,
            'last-slide': carouselItems && carouselItems.length > 0 && currentIndex === carouselItems.length - 1
          }">
            <div class="carousel-item active">
              <div class="carousel-content">
                <div class="carousel-image-container">
                  <!-- 使用 ImageDisplay 显示轮播图 -->
                  <ImageDisplay :image-id="carouselItems[currentIndex]?.imageId" class="carousel-image" />
                </div>
                <div class="carousel-description">
                  <h3 class="carousel-title">{{ carouselItems[currentIndex]?.name || '未知食材' }}</h3>
                  <p class="carousel-text">{{ carouselItems[currentIndex]?.description || '新鲜食材，营养丰富，是健康饮食的理想选择。' }}</p>
                </div>
              </div>
            </div>
            <div v-if="carouselItems && carouselItems.length > 0 && currentIndex < carouselItems.length - 1" class="carousel-item next-preview">
              <div class="carousel-content">
                <div class="carousel-image-container">
                  <ImageDisplay :image-id="carouselItems[currentIndex + 1]?.imageId" class="carousel-image" />
                </div>
              </div>
            </div>
            <div v-if="carouselItems && carouselItems.length > 0 && currentIndex > 0" class="carousel-item prev-preview">
              <div class="carousel-content">
                <div class="carousel-image-container">
                  <ImageDisplay :image-id="carouselItems[currentIndex - 1]?.imageId" class="carousel-image" />
                </div>
              </div>
            </div>
          </div>
          <div v-if="carouselItems && carouselItems.length > 0 && currentIndex > 0" class="carousel-nav carousel-prev" @click="prevSlide">
            <el-button circle style="background-color: rgba(240, 240, 240, 0.8); border-color: transparent;">
              <span style="font-size: 18px; font-weight: bold;">&lt;</span>
            </el-button>
          </div>
          <div v-if="carouselItems && carouselItems.length > 0 && currentIndex < carouselItems.length - 1" class="carousel-nav carousel-next" @click="nextSlide">
            <el-button circle style="background-color: rgba(240, 240, 240, 0.8); border-color: transparent;">
              <span style="font-size: 18px; font-weight: bold;">&gt;</span>
            </el-button>
          </div>
          <div v-if="carouselItems && carouselItems.length > 0" class="carousel-indicators">
            <span v-for="(_, index) in carouselItems" :key="index" class="indicator" :class="{ active: index === currentIndex }" @click="currentIndex = index"></span>
          </div>
        </div>
      </div>
    </template>

    <!-- 选项卡内容保持不变，因为内部无图片 -->
    <div class="tabs-container">
      <el-tabs v-model="activeTab" type="card">
        <el-tab-pane label="食材挑选" name="selection">
          <div v-if="selectionTips && selectionTips.categories && selectionTips.categories.length > 0" class="content-section">
            <div v-for="category in selectionTips.categories" :key="category.id" class="category-container">
              <h3 class="category-title">{{ category.name }}</h3>
              <div class="items-grid">
                <el-tag v-for="(item, index) in category.items" :key="index" class="tip-item" effect="dark" type="success">
                  {{ item }}
                </el-tag>
              </div>
            </div>
          </div>
          <div v-else class="empty-state"><p>暂无挑选技巧数据</p></div>
        </el-tab-pane>
        <el-tab-pane label="食材处理" name="processing">
          <div v-if="processingSteps && processingSteps.categories && processingSteps.categories.length > 0" class="content-section">
            <div v-for="category in processingSteps.categories" :key="category.id" class="category-container">
              <h3 class="category-title">{{ category.name }}</h3>
              <div v-if="category.items && category.items.length > 0" class="items-grid">
                <el-tag v-for="(item, index) in category.items" :key="index" class="step-item" effect="dark" type="primary">
                  {{ item }}
                </el-tag>
              </div>
              <div v-if="category.subcategories && category.subcategories.length > 0" class="subcategories-container">
                <div v-for="subcategory in category.subcategories" :key="subcategory.id" class="subcategory-container">
                  <h4 class="subcategory-title">{{ subcategory.name }}</h4>
                  <div class="items-grid">
                    <el-tag v-for="(item, index) in subcategory.items" :key="index" class="substep-item" effect="plain" type="info">
                      {{ item }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="empty-state"><p>暂无处理步骤数据</p></div>
        </el-tab-pane>
        <el-tab-pane label="食材科普" name="knowledge">
          <div v-if="knowledgePoints && knowledgePoints.categories && knowledgePoints.categories.length > 0" class="content-section">
            <div v-for="category in knowledgePoints.categories" :key="category.id" class="category-container">
              <h3 class="category-title">{{ category.name }}</h3>
              <div class="items-grid">
                <el-tag v-for="(item, index) in category.items" :key="index" class="knowledge-item" effect="dark" type="warning">
                  {{ item }}
                </el-tag>
              </div>
            </div>
          </div>
          <div v-else class="empty-state"><p>暂无科普知识数据</p></div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useIngredientDetailLogic } from '@/scripts/ingredientDetailViewLogic'
import { ElTag, ElButton } from 'element-plus'
import ImageDisplay from '@/components/ImageDisplay.vue'

const {
  activeTab,
  currentIndex,
  loading,
  error,
  carouselItems,
  selectionTips,
  processingSteps,
  knowledgePoints,
  prevSlide,
  nextSlide,
  fetchIngredientDetails
} = useIngredientDetailLogic()
</script>

<style scoped>
@import '@/styles/components/IngredientDetailView.css';
</style>