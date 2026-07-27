<template>
  <div class="explore-container">
    <nav class="module-nav">
      <ul class="module-tabs">
        <li v-for="module in modules" :key="module.id" class="module-tab">
          <button
            class="module-tab-button"
            :class="{ active: activeModule === module.id }"
            @click="switchModule(module.id)"
          >
            {{ module.name }}
          </button>
        </li>
      </ul>
    </nav>

    <div class="module-content">
      <!-- 热度菜品模块 -->
      <section class="module-section" id="hot-recipes" v-show="activeModule === 'hot-recipes'">
        <div class="module-body">
          <div class="ranking-container">
            <div v-for="config in rankingConfigs" :key="config.name" class="ranking-card">
              <h3>{{ config.name }}</h3>
              <ul class="ranking-list">
                <li v-for="recipe in rankingListsLimited[config.name]" :key="recipe.id" class="ranking-item clickable-item" @click="onRankingItemClick(recipe)">
                  <span class="ranking-number">{{ recipe.rank }}</span>
                  <div class="ranking-name">
                    <img v-if="recipe.imageUrl" :src="recipe.imageUrl" :alt="recipe.name" class="recipe-image" />
                    <span>{{ recipe.name }}</span>
                  </div>
                  <span class="ranking-count">{{ recipe.count }}{{ config.icon }}</span>
                </li>
              </ul>
              <button class="view-more-btn" @click="openMoreModal(config.name)">查看更多</button>
            </div>
          </div>
        </div>
      </section>

      <!-- 菜系历史模块 -->
      <section class="module-section" id="cuisine-history" v-show="activeModule === 'cuisine-history'">
        <!-- <CuisineHistoryView /> -->
      </section>

      <!-- 季节食历模块 -->
      <section class="module-section" id="seasonal-calendar" v-show="activeModule === 'seasonal-calendar'">
        <p>季节食历功能开发中...</p>
      </section>

      <!-- 美食足迹模块 -->
      <section class="module-section" id="food-footprint" v-show="activeModule === 'food-footprint'">
        <p>美食足迹功能开发中...</p>
      </section>

      <!-- 社交星球模块 -->
      <section class="module-section" id="social-planet" v-show="activeModule === 'social-planet'">
        <p>社交星球功能开发中...</p>
      </section>

      <!-- 厨艺学习模块 -->
      <section class="module-section" id="cooking-learning" v-show="activeModule === 'cooking-learning'">
        <p>厨艺学习功能开发中...</p>
      </section>

      <!-- 美食地图模块 -->
      <section class="module-section" id="food-map" v-if="activeModule === 'food-map'">
        <FoodMapView ref="foodMapRef" />
      </section>
    </div>
    <!-- 查看更多弹出层 -->
  <div v-if="modalVisible" class="modal-overlay" @click="closeMoreModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h3>{{ currentRankingList }} - 前50</h3>
        <button class="close-btn" @click="closeMoreModal">×</button>
      </div>
      <div class="modal-body">
        <div class="full-ranking-container">
          <ul class="full-ranking-list">
            <li v-for="item in fullRankingData" :key="item.id" class="full-ranking-item clickable-item" @click="onRankingItemClick(item)">
              <span class="ranking-number" :class="{ 'top-one': item.rank === 1, 'top-two': item.rank === 2, 'top-three': item.rank === 3 }">
                {{ item.rank }}
              </span>
              <div class="ranking-name">
                <img v-if="item.imageUrl" :src="item.imageUrl" :alt="item.name" class="recipe-image-large" />
                <span>{{ item.name }}</span>
              </div>
              <span class="ranking-count">{{ item.count }}{{ item.icon }}</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { getRankingLists, getHistoricalEvents } from '@/api/explore'
import CuisineHistoryView from '@/views/CuisineHistoryView.vue'
import FoodMapView from '@/views/FoodMapView.vue'

interface RankingItem {
  id: string
  name: string
  rank: number
  count: number
  imageUrl?: string
  path?: string
  icon?: string
}

const modules = [
  { id: 'hot-recipes', name: '热度菜品' },
  { id: 'cuisine-history', name: '菜系历史' },
  { id: 'seasonal-calendar', name: '季节食历' },
  { id: 'food-footprint', name: '美食足迹' },
  { id: 'social-planet', name: '社交星球' },
  { id: 'cooking-learning', name: '厨艺学习' },
  { id: 'food-map', name: '美食地图' }
]

const rankingConfigs = [
  { name: '热门榜单', icon: '🔥' },
  { name: '收藏榜单', icon: '⭐' },
  { name: '评分榜单', icon: '👍' }
]

const activeModule = ref('hot-recipes')
const rankingLists = ref<Record<string, RankingItem[]>>({})
const modalVisible = ref(false)
const currentRankingList = ref('')
const fullRankingData = ref<RankingItem[]>([])
const foodMapRef = ref<InstanceType<typeof FoodMapView>>()

const rankingListsLimited = computed(() => {
  const result: Record<string, RankingItem[]> = {}
  for (const [key, list] of Object.entries(rankingLists.value)) {
    result[key] = list ? list.slice(0, 10) : []
  }
  return result
})

const switchModule = (id: string) => {
  activeModule.value = id
}

const loadRankings = async () => {
  try {
    const data = await getRankingLists()
    rankingLists.value = data
  } catch (err) {
    console.error('加载榜单失败', err)
  }
}

const loadHistoricalEvents = async () => {
  try {
    await getHistoricalEvents()
  } catch (err) {
    console.error('加载历史事件失败', err)
  }
}

const loadAllData = async () => {
  await Promise.all([loadRankings(), loadHistoricalEvents()])
}

const openMoreModal = (listName: string) => {
  currentRankingList.value = listName
  fullRankingData.value = rankingLists.value[listName] || []
  modalVisible.value = true
}

const closeMoreModal = () => {
  modalVisible.value = false
}

const router = useRouter()
const onRankingItemClick = (item: RankingItem) => {
  if (item.path) router.push(item.path)
}

// 监听模块切换，对地图组件调用 resize
watch(
  () => activeModule.value,
  async (newVal) => {
    if (newVal === 'food-map') {
      await nextTick()
      // 延迟确保组件完全渲染
      setTimeout(() => {
        if (foodMapRef.value && typeof foodMapRef.value.resize === 'function') {
          foodMapRef.value.resize()
        } else {
          console.warn('foodMapRef 尚未准备好或 resize 方法不存在')
        }
      }, 300)
    }
  }
)

onMounted(async () => {
  // await loadAllData()
})
</script>

<style scoped>
@import '@/styles/components/ExploreView.css';
</style>