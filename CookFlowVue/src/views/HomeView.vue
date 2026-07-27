<template>
  <div class="home-view">
    <!-- 主横幅（保持不变） -->
    <section class="hero-section">
      <div class="gallery-box">
        <DomeGallery :images="images" />
      </div>
    </section>

    <!-- 主要内容区域 -->
    <section class="main-content">
      <div class="container">
        <!-- 热门分类区域（无图片，网格布局） -->
        <div class="section">
          <h2 class="section-title">热门分类</h2>
          <div class="hot-categories-grid">
            <div 
              v-for="category in hotCategories" 
              :key="category.id" 
              class="category-item"
              @click="handleCategoryClick(`/recipes?categoryId=${category.id}`)"
            >
              <span class="category-name">{{ category.name }}</span>
              <span class="category-count">{{ category.searchCount }}次检索</span>
            </div>
          </div>
        </div>

        <!-- 推荐菜谱区域（保持不变） -->
        <div class="section">
          <h2 class="section-title">推荐菜谱</h2>
          <el-row :gutter="20" class="section-row">
            <el-col
              :xs="12" :sm="6" :md="6" :lg="6"
              class="section-col"
              v-for="recipe in recommendedRecipes"
              :key="recipe.id"
            >
              <div class="section-card" @click="handleCategoryClick(`/recipe/${recipe.id}`)">
                <el-image :src="recipe.imgUrl" class="section-img" fit="cover"></el-image>
                <div class="section-name">{{ recipe.name }}</div>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 今日推荐区域（保持不变） -->
        <div class="section">
          <h2 class="section-title">今日推荐</h2>
          <el-row :gutter="20" class="section-row">
            <el-col
              :xs="12" :sm="6" :md="6" :lg="6"
              class="section-col"
              v-for="recommend in todayRecommends"
              :key="recommend.id"
            >
              <div class="section-card" @click="handleCategoryClick(`/recipe/${recommend.id}`)">
                <el-image :src="recommend.imgUrl" class="section-img" fit="cover"></el-image>
                <div class="section-name">{{ recommend.name }}</div>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 功能特点（保持不变） -->
        <div class="features-section">
          <h3>我们的特色</h3>
          <div class="features-grid">
            <div class="feature-card">
              <div class="feature-icon">📚</div>
              <h4>丰富食谱库</h4>
              <p>包含各种菜系的精选食谱，从简单家常菜到精致大餐</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">🍳</div>
              <h4>烹饪指导</h4>
              <p>详细的步骤说明和烹饪技巧，让您轻松掌握</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">🧺</div>
              <h4>食材管理</h4>
              <p>管理您的食材清单，轻松采购所需材料</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">👥</div>
              <h4>社区分享</h4>
              <p>与其他烹饪爱好者交流，分享您的创意和心得</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import DomeGallery from '@/components/DomeGallery/DomeGallery.vue';
import { getHotRecipes, getHotCategories, getRecommendedRecipes, getTodayRecommends } from '@/api/home';

const router = useRouter();

// 响应式数据
const images = ref<string[]>([]);
const segments = ref<number[]>([]);
const hotCategories = ref<any[]>([]);      // 热门分类（含 id, name, searchCount）
const recommendedRecipes = ref<any[]>([]);
const todayRecommends = ref<any[]>([]);

// 点击跳转
const handleCategoryClick = (path: string) => {
  router.push(path);
};

// 加载首页数据
const loadHomeData = async () => {
  try {
    const [imagesRes, categoriesRes, recipesRes, recommendsRes] = await Promise.all([
      getHotRecipes(),
      getHotCategories(),
      getRecommendedRecipes(),
      getTodayRecommends()
    ]);
    images.value = imagesRes.map((item: any) => ({
      src: item.imgUrl,
      id: item.id
    }));
    hotCategories.value = categoriesRes;   // 后端返回 id, name, searchCount
    recommendedRecipes.value = recipesRes;
    todayRecommends.value = recommendsRes;
  } catch (error) {
    console.error('加载首页数据失败', error);
  }
};

onMounted(() => {
  loadHomeData();
});
</script>

<style scoped>
@import '@/styles/components/HomeView.css';

/* 热门分类网格样式（一行8列） */
.hot-categories-grid {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 16px 12px;
  margin-top: 20px;
}

.category-item {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 12px 8px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid #e9ecef;
}

.category-item:hover {
  background-color: #e9ecef;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.category-name {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #2c3e50;
  margin-bottom: 4px;
}

.category-count {
  display: block;
  font-size: 12px;
  color: #6c757d;
}

/* 响应式：屏幕小于1200px时改为6列 */
@media (max-width: 1200px) {
  .hot-categories-grid {
    grid-template-columns: repeat(6, 1fr);
  }
}

/* 屏幕小于768px时改为4列 */
@media (max-width: 768px) {
  .hot-categories-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}
</style>