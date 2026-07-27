<template>
  <div class="food-map-wrapper">
    <!-- 地图容器 -->
    <div ref="mapContainer" class="map-container"></div>

    <!-- 面包屑导航 -->
    <div class="breadcrumb" v-if="currentRegion">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item @click="backToProvince">美食地图</el-breadcrumb-item>
        <el-breadcrumb-item v-if="currentRegion.level >= 2" @click="goUp">
          {{ provinceName }}
        </el-breadcrumb-item>
        <el-breadcrumb-item v-if="currentRegion.level >= 3">
          {{ currentRegion.name }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 美食卡片区域 -->
    <div
      class="food-cards-container"
      :class="{ 'horizontal-scroll': isHorizontalScroll }"
      v-if="currentRegion && currentRegion.foods && currentRegion.foods.length > 0"
    >
      <div class="cards-title">
        <h3>{{ currentRegion.name }}特色美食</h3>
        <span class="food-count">共{{ currentRegion.foods.length }}道美食</span>
      </div>
      <div class="cards-wrapper" :class="{ horizontal: isHorizontalScroll }">
        <div
          v-for="food in currentRegion.foods"
          :key="food.recipeId"
          class="food-card"
          @click="goToRecipe(food.recipeId)"
        >
          <div class="card-image">
            <el-image :src="food.imageUrl" fit="cover">
              <template #error>
                <div class="image-placeholder">暂无图片</div>
              </template>
            </el-image>
            <span v-if="food.isSpecialty" class="specialty-tag">特色</span>
          </div>
          <div class="card-info">
            <h4>{{ food.title }}</h4>
            <div class="card-meta">
              <div class="difficulty">
                <span>难度：</span>
                <span :class="['difficulty-text', getDifficultyClass(food.difficulty)]">
                  {{ getDifficultyText(food.difficulty) }}
                </span>
              </div>
              <div class="time">
                <el-icon><Timer /></el-icon>
                <span>{{ food.prepTime }}分钟</span>
              </div>
            </div>
            <p class="description">{{ food.description || '暂无描述' }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 无美食提示 -->
    <div
      class="no-food"
      v-else-if="currentRegion && currentRegion.foods && currentRegion.foods.length === 0"
    >
      <el-empty description="暂无美食数据" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { getProvinces, getCities, drillDown, type RegionData } from '@/api/region'
import { Timer } from '@element-plus/icons-vue'

const router = useRouter()
const mapContainer = ref<HTMLElement>()
let chart: echarts.ECharts | null = null

const provinces = ref<RegionData[]>([])
const currentRegion = ref<RegionData | null>(null)
const provinceName = ref('')
const cityLevelData = ref<RegionData[]>([])

const isHorizontalScroll = computed(() => {
  if (!currentRegion.value?.foods) return false
  return currentRegion.value.foods.length <= 4
})

const getDifficultyText = (difficulty: number) => {
  const map: Record<number, string> = { 1: '简单', 2: '中等', 3: '困难' }
  return map[difficulty] || '未知'
}

const getDifficultyClass = (difficulty: number) => {
  const map: Record<number, string> = { 1: 'easy', 2: 'medium', 3: 'hard' }
  return map[difficulty] || ''
}

// 初始化地图
const initMap = () => {
  if (!mapContainer.value) return
  const width = mapContainer.value.clientWidth
  const height = mapContainer.value.clientHeight
  if (width === 0 || height === 0) {
    console.warn('容器尺寸为0，延迟重试')
    requestAnimationFrame(() => initMap())
    return
  }

  if (chart) chart.dispose()
  chart = echarts.init(mapContainer.value)

  const option = {
    title: { show: false },
    tooltip: {
      trigger: 'item',
      formatter: (params: any) => `${params.name}<br/>美食数量：${params.value || 0}`
    },
    visualMap: {
      min: 0,
      max: 100,
      left: 'left',
      top: 'bottom',
      calculable: true,
      inRange: { color: ['#e0f3f8', '#abd9e9', '#74add1', '#4575b4', '#313695'] },
      text: ['高', '低'],
      textStyle: { color: '#333' }
    },
    series: [
      {
        name: '美食地图',
        type: 'map',
        map: 'china',
        roam: true,
        zoom: 1.2,
        scaleLimit: { min: 0.8, max: 3 },
        label: {
          show: true,
          color: '#333',
          fontSize: 10,
          formatter: (params: any) => params.name
        },
        emphasis: {
          label: { show: true, fontWeight: 'bold' },
          itemStyle: { areaColor: '#ffd700', borderWidth: 2 }
        },
        data: provinces.value.map(province => ({
          name: province.name,
          value: province.foodCount
        })),
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 1,
          areaColor: '#f0f0f0'
        }
      }
    ]
  }

  chart.setOption(option)

  chart.on('click', async (params: any) => {
    if (params.componentType === 'series') {
      const clickedProvince = provinces.value.find(p => p.name === params.name)
      if (clickedProvince) {
        await handleProvinceClick(clickedProvince)
      }
    }
  })

  console.log('地图初始化成功')
}

// 业务逻辑
const handleProvinceClick = async (province: RegionData) => {
  const cities = await getCities(province.code)
  cityLevelData.value = cities
  updateMapToCityLevel(cities, province.name)

  const provinceDetail = await drillDown(province.code)
  if (provinceDetail && provinceDetail.foods && provinceDetail.foods.length > 0) {
    currentRegion.value = provinceDetail
    provinceName.value = province.name
  } else if (cities.length > 0) {
    const firstCity = await drillDown(cities[0].code)
    if (firstCity) {
      currentRegion.value = firstCity
      provinceName.value = province.name
    }
  }
}

const updateMapToCityLevel = (cities: RegionData[], provinceName: string) => {
  if (!chart) return
  chart.setOption({
    series: [{
      data: cities.map(city => ({ name: city.name, value: city.foodCount }))
    }]
  })
  chart.dispatchAction({ type: 'geoRoam', dx: 0, dy: 0, zoom: 1.5 })
}

const backToProvince = () => {
  currentRegion.value = null
  provinceName.value = ''
  cityLevelData.value = []
  if (chart) {
    chart.setOption({
      series: [{
        data: provinces.value.map(province => ({ name: province.name, value: province.foodCount }))
      }]
    })
    chart.dispatchAction({ type: 'geoRoam', dx: 0, dy: 0, zoom: 1.2 })
  }
}

const goUp = async () => {
  if (currentRegion.value?.level === 3) {
    const cityCode = currentRegion.value.code.substring(0, 4) + '00'
    const cityData = await drillDown(cityCode)
    if (cityData) currentRegion.value = cityData
  } else if (currentRegion.value?.level === 2) {
    backToProvince()
  }
}

const goToRecipe = (recipeId: string) => {
  router.push(`/recipe/${recipeId}`)
}

const loadData = async () => {
  try {
    const data = await getProvinces()
    provinces.value = data
  } catch (error) {
    console.error('加载省份数据失败，使用模拟数据', error)
    provinces.value = [
      { code: '110000', name: '北京市', level: 1, lng: 116.4074, lat: 39.9042, foodCount: 10 },
      { code: '310000', name: '上海市', level: 1, lng: 121.4802, lat: 31.2363, foodCount: 8 },
      { code: '440000', name: '广东省', level: 1, lng: 113.2644, lat: 23.1291, foodCount: 15 },
      { code: '350000', name: '福建省', level: 1, lng: 119.2951, lat: 26.1008, foodCount: 6 },
      { code: '510000', name: '四川省', level: 1, lng: 104.0657, lat: 30.6595, foodCount: 12 },
    ]
  }
}

const resize = () => {
  chart?.resize()
}

onMounted(async () => {
  await nextTick()
  await loadData()
  requestAnimationFrame(() => {
    initMap()
  })
  window.addEventListener('resize', resize)
})

onUnmounted(() => {
  window.removeEventListener('resize', resize)
  chart?.dispose()
})

defineExpose({ resize })
</script>

<style scoped>
.food-map-wrapper {
  width: 100%;
  height: 600px;
  position: relative;
  background: #f5f5f5;
}

.map-container {
  width: 100%;
  height: 100%;
}

.breadcrumb {
  position: absolute;
  top: 20px;
  left: 20px;
  z-index: 10;
  background: rgba(255, 255, 255, 0.9);
  padding: 10px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.breadcrumb :deep(.el-breadcrumb__item) {
  cursor: pointer;
}

.breadcrumb :deep(.el-breadcrumb__item:last-child) {
  cursor: default;
}

.food-cards-container {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px 20px 0 0;
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.1);
  z-index: 10;
  backdrop-filter: blur(10px);
}

.cards-title {
  padding: 16px 24px 8px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.cards-title h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.cards-title .food-count {
  font-size: 14px;
  color: #999;
}

.cards-wrapper {
  padding: 16px 24px;
  max-height: 400px;
  overflow-y: auto;
}

.cards-wrapper.horizontal {
  display: flex;
  gap: 16px;
  overflow-x: auto;
  overflow-y: hidden;
}

.cards-wrapper.horizontal .food-card {
  flex: 0 0 280px;
}

.cards-wrapper:not(.horizontal) {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.food-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.food-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.card-image {
  position: relative;
  width: 100%;
  height: 160px;
  overflow: hidden;
}

.card-image :deep(.el-image) {
  width: 100%;
  height: 100%;
}

.card-image :deep(.el-image img) {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
}

.specialty-tag {
  position: absolute;
  top: 8px;
  right: 8px;
  background: linear-gradient(135deg, #ff6b6b, #ff4757);
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.card-info {
  padding: 12px;
}

.card-info h4 {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-meta {
  margin-bottom: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.difficulty {
  font-size: 12px;
  color: #666;
}

.difficulty-text.easy {
  color: #67c23a;
}

.difficulty-text.medium {
  color: #e6a23c;
}

.difficulty-text.hard {
  color: #f56c6c;
}

.time {
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 4px;
}

.description {
  font-size: 12px;
  color: #999;
  line-height: 1.4;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.no-food {
  position: absolute;
  bottom: 20%;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(255, 255, 255, 0.95);
  padding: 20px 40px;
  border-radius: 12px;
  z-index: 10;
}

.cards-wrapper::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.cards-wrapper::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.cards-wrapper::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.cards-wrapper::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>