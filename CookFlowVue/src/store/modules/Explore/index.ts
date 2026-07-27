import { defineStore } from 'pinia';
import { mockHistoricalEvents } from '../Explore/historicalEventsData';
// 定义所有需要的类型
interface RankingItem {
  id: string;
  rank: number;
  icon: string;
  name: string;
  count: string;
  imageUrl: string;
  path: string;
}

interface Province {
  id: string;
  name: string;
  imageUrl?: string;
}

interface HistoricalEvent {
  id: string;
  year: string;
  title: string;
  description: string;
}

interface SeasonalFoodItem {
  id: string;
  month: number;
  foods: any[];
}

interface FoodFootprint {
  id: string;
  name: string;
  location: string;
}

interface DietPlan {
  id: string;
  name: string;
  category: string;
  difficulty: string;
  description: string;
  tags: string[];
}

interface SocialPlanet {
  id: string;
  name: string;
  description: string;
  tags: string[];
}

interface SocialPost {
  id: string;
  content: string;
  likes: number;
  liked: boolean;
}

interface RankingList {
  [key: string]: RankingItem[];
}

// 缓存键名常量
const CACHE_KEYS = {
  RANKING_LISTS: 'explore_ranking_lists_cache',
  PROVINCES: 'explore_provinces_cache',
  HISTORICAL_EVENTS: 'explore_historical_events_cache',
  SEASONAL_CALENDAR: 'explore_seasonal_calendar_cache',
  FOOD_FOOTPRINTS: 'explore_food_footprints_cache',
  DIET_PLANS: 'explore_diet_plans_cache',
  SOCIAL_PLANETS: 'explore_social_planets_cache',
  SOCIAL_POSTS: 'explore_social_posts_cache',
  TIMESTAMP: 'explore_data_timestamp'
};

// 缓存有效期（毫秒）
const CACHE_DURATION = 30 * 60 * 1000; // 30分钟

// 核心接口定义已移至 exploreLogic.ts

// 菜名列表
const recipeNames = [
  '红烧肉', '麻婆豆腐', '番茄炒蛋', '清蒸鲈鱼', '糖醋排骨',
  '宫保鸡丁', '水煮鱼', '鱼香肉丝', '干煸四季豆', '青椒土豆丝',
  '可乐鸡翅', '回锅肉', '梅菜扣肉', '蒜蓉粉丝蒸扇贝', '酸辣土豆丝',
  '香辣蟹', '黄焖鸡米饭', '蚂蚁上树', '夫妻肺片', '地三鲜',
  '红烧肉炖土豆', '啤酒鸭', '红烧狮子头', '剁椒鱼头', '水煮肉片',
  '锅包肉', '油焖大虾', '酸辣汤', '京酱肉丝', '木须肉',
  '糖醋里脊', '东坡肉', '辣子鸡', '口水鸡', '糖醋鱼',
  '蒜蓉蒸虾', '麻婆茄子', '火爆腰花', '干炒牛河', '烤鸭',
  '烤羊排', '蒸饺', '烧麦', '小笼包', '炒饭',
  '炒面', '炸酱面', '担担面', '刀削面', '油泼面'
];

// 生成静态榜单数据
const generateStaticRankingData = (listName: string, icon: string): RankingItem[] => {
  const list: RankingItem[] = [];
  for (let i = 0; i < 50; i++) {
    const isOfficial = i % 2 === 0; // 每隔一个是官方菜谱
    const id = `${listName}-${i + 1}`;
    const rank = i + 1;
    const name = recipeNames[i % recipeNames.length] || `菜品${i + 1}`; // 确保有默认值
    const count = (50000 - i * 1000).toString(); // 递减的数量
    const imageUrl = `/hots/${(i % 3) + 1}.jpg`; // 使用现有的图片资源
    const path = isOfficial ? `/recipe/${id}` : `/forum/share/${id}`;

    list.push({
      id,
      rank,
      icon,
      name,
      count,
      imageUrl,
      path
    });
  }
  return list;
};

// 静态榜单数据
const staticRankingLists: RankingList = {
  '收藏榜': generateStaticRankingData('收藏榜', '❤️'),
  '跟做榜': generateStaticRankingData('跟做榜', '👨‍🍳'),
  '评分榜': generateStaticRankingData('评分榜', '⭐'),
  '搜索榜': generateStaticRankingData('搜索榜', '🔍'),
  '分享榜': generateStaticRankingData('分享榜', '📤'),
  '评论榜': generateStaticRankingData('评论榜', '💬')
};

export const useExploreStore = defineStore('explore', {
  state: () => ({
    // 排行榜数据
    rankingLists: {} as RankingList,

    // 地图数据
    provinces: [] as Province[],
    selectedProvince: null as Province | null,

    // 历史事件数据
    historicalEvents: [] as HistoricalEvent[],

    // 季节食历数据
    seasonalCalendar: [] as SeasonalFoodItem[],

    // 美食足迹数据
    foodFootprints: [] as FoodFootprint[],

    // 饮食管理数据
    dietPlans: [] as DietPlan[],

    // 社交星球数据
    socialPlanets: [] as SocialPlanet[],
    socialPosts: [] as SocialPost[],

    // 加载状态
    loading: false,

    // 错误信息
    error: null as string | null
  }),

  getters: {
    // 获取排行榜数据
    getRankingLists: (state) => state.rankingLists,

    // 获取省份数据
    getAllProvinces: (state) => state.provinces,

    // 获取选中的省份
    getSelectedProvince: (state) => state.selectedProvince,

    // 获取历史事件
    getHistoricalEvents: (state) => state.historicalEvents,

    // 获取季节食历
    getSeasonalCalendar: (state) => state.seasonalCalendar,

    // 获取美食足迹
    getFoodFootprints: (state) => state.foodFootprints,

    // 获取饮食计划
    getDietPlans: (state) => state.dietPlans,

    // 获取社交星球
    getSocialPlanets: (state) => state.socialPlanets,

    // 获取社交帖子
    getSocialPosts: (state) => state.socialPosts,

    // 判断是否正在加载
    isLoading: (state) => state.loading
  },

  actions: {
    // 初始化store时直接设置默认数据
    setInitialData() {
      console.log('Explore store: setInitialData called');
      // 尝试从缓存读取数据
      const cachedRankingLists = this.getCachedData(CACHE_KEYS.RANKING_LISTS);
      const cachedProvinces = this.getCachedData(CACHE_KEYS.PROVINCES);
      const cachedHistoricalEvents = this.getCachedData(CACHE_KEYS.HISTORICAL_EVENTS);
      const cachedSeasonalCalendar = this.getCachedData(CACHE_KEYS.SEASONAL_CALENDAR);
      const cachedFoodFootprints = this.getCachedData(CACHE_KEYS.FOOD_FOOTPRINTS);
      const cachedDietPlans = this.getCachedData(CACHE_KEYS.DIET_PLANS);
      const cachedSocialPlanets = this.getCachedData(CACHE_KEYS.SOCIAL_PLANETS);
      const cachedSocialPosts = this.getCachedData(CACHE_KEYS.SOCIAL_POSTS);
      
      console.log('Explore store: 缓存检查结果 - historicalEvents:', !!cachedHistoricalEvents);

      // 如果有缓存数据，直接使用；否则使用模拟数据
      if (cachedRankingLists) this.rankingLists = cachedRankingLists;
      if (cachedProvinces) this.provinces = cachedProvinces;
      else this.provinces = this.generateMockProvinces();
      
      if (cachedHistoricalEvents) {
        this.historicalEvents = cachedHistoricalEvents;
        console.log('Explore store: 使用缓存的historicalEvents数据，数量:', this.historicalEvents.length);
      } else {
        this.historicalEvents = this.generateMockHistoricalEvents();
        console.log('Explore store: 使用模拟的historicalEvents数据，数量:', this.historicalEvents.length);
      }
      
      if (cachedSeasonalCalendar) this.seasonalCalendar = cachedSeasonalCalendar;
      else this.seasonalCalendar = this.generateMockSeasonalCalendar();
      
      if (cachedFoodFootprints) this.foodFootprints = cachedFoodFootprints;
      else this.foodFootprints = this.generateMockFoodFootprints();
      
      if (cachedDietPlans) this.dietPlans = cachedDietPlans;
      else this.dietPlans = this.generateMockDietPlans();
      
      if (cachedSocialPlanets) this.socialPlanets = cachedSocialPlanets;
      else this.socialPlanets = this.generateMockSocialPlanets();
      
      if (cachedSocialPosts) this.socialPosts = cachedSocialPosts;
      else this.socialPosts = this.generateMockSocialPosts();
    },

    // 从localStorage获取缓存数据
    getCachedData(key: string) {
      try {
        const data = localStorage.getItem(key);
        if (!data) return null;
        const parsedData = JSON.parse(data);
        const timestamp = localStorage.getItem(CACHE_KEYS.TIMESTAMP);
        if (!timestamp) return null;
        const now = Date.now();
        const cacheTime = parseInt(timestamp);
        if (now - cacheTime > CACHE_DURATION) {
          localStorage.removeItem(key);
          return null;
        }
        return parsedData;
      } catch (error) {
        return null;
      }
    },

    // 设置缓存数据
    setCachedData(key: string, data: any) {
      try {
        localStorage.setItem(key, JSON.stringify(data));
        localStorage.setItem(CACHE_KEYS.TIMESTAMP, Date.now().toString());
      } catch (error) { }
    },

    // 模拟从后端获取排行榜数据
    async fetchRankingLists() {
      try {
        this.loading = true;
        this.error = null;
        const cachedData = this.getCachedData(CACHE_KEYS.RANKING_LISTS);
        if (cachedData) {
          this.rankingLists = cachedData;
          return;
        }
        await new Promise(resolve => setTimeout(resolve, 300));
        this.rankingLists = staticRankingLists;
        this.setCachedData(CACHE_KEYS.RANKING_LISTS, staticRankingLists);
      } catch (error) {
        this.error = '获取榜单数据失败';
      } finally {
        this.loading = false;
      }
    },

    // 模拟从后端获取省份数据
    async fetchProvinces() {
      this.loading = true;
      try {
        // 实际项目中这里应该是API调用
        // const response = await api.get('/explore/provinces');
        // this.provinces = response.data;

        // 使用模拟数据
        this.provinces = this.generateMockProvinces();
        this.setCachedData(CACHE_KEYS.PROVINCES, this.provinces);
      } catch (error) {
        this.error = '获取省份数据失败';
        console.error(error);
      } finally {
        this.loading = false;
      }
    },

    // 设置选中的省份
    setSelectedProvince(province: Province) {
      this.selectedProvince = province;
    },

    // 模拟获取历史事件数据
    async fetchHistoricalEvents() {
      this.loading = true;
      try {
        // 实际项目中这里应该是API调用
        // const response = await api.get('/explore/historical-events');
        // this.historicalEvents = response.data;

        // 使用模拟数据
        this.historicalEvents = this.generateMockHistoricalEvents();
        this.setCachedData(CACHE_KEYS.HISTORICAL_EVENTS, this.historicalEvents);
      } catch (error) {
        this.error = '获取历史事件数据失败';
        console.error(error);
      } finally {
        this.loading = false;
      }
    },

    // 模拟获取季节食历数据
    async fetchSeasonalCalendar() {
      this.loading = true;
      try {
        // 实际项目中这里应该是API调用
        // const response = await api.get('/explore/seasonal-calendar');
        // this.seasonalCalendar = response.data;

        // 使用模拟数据
        this.seasonalCalendar = this.generateMockSeasonalCalendar();
        this.setCachedData(CACHE_KEYS.SEASONAL_CALENDAR, this.seasonalCalendar);
      } catch (error) {
        this.error = '获取季节食历数据失败';
        console.error(error);
      } finally {
        this.loading = false;
      }
    },

    // 简化的模拟数据生成器
    generateMockFoodFootprints() {
      return []; // 空数组作为占位符
    },

    generateMockDietPlans() {
      return []; // 空数组作为占位符
    },

    generateMockSocialPlanets() {
      return []; // 空数组作为占位符
    },

    generateMockSocialPosts() {
      return []; // 空数组作为占位符
    },

    // 模拟获取美食足迹数据
    async fetchFoodFootprints() {
      this.loading = true;
      try {
        // 实际项目中这里应该是API调用
        // const response = await api.get('/explore/food-footprints');
        // this.foodFootprints = response.data;

        // 使用模拟数据
        this.foodFootprints = this.generateMockFoodFootprints();
        this.setCachedData(CACHE_KEYS.FOOD_FOOTPRINTS, this.foodFootprints);
      } catch (error) {
        this.error = '获取美食足迹数据失败';
        console.error(error);
      } finally {
        this.loading = false;
      }
    },

    // 模拟获取饮食计划数据
    async fetchDietPlans() {
      this.loading = true;
      try {
        // 实际项目中这里应该是API调用
        // const response = await api.get('/explore/diet-plans');
        // this.dietPlans = response.data;

        // 使用模拟数据
        this.dietPlans = this.generateMockDietPlans();
        this.setCachedData(CACHE_KEYS.DIET_PLANS, this.dietPlans);
      } catch (error) {
        this.error = '获取饮食计划数据失败';
        console.error(error);
      } finally {
        this.loading = false;
      }
    },

    // 模拟获取社交星球数据
    async fetchSocialPlanets() {
      this.loading = true;
      try {
        // 实际项目中这里应该是API调用
        // const response = await api.get('/explore/social-planets');
        // this.socialPlanets = response.data;

        // 使用模拟数据
        this.socialPlanets = this.generateMockSocialPlanets();
        this.setCachedData(CACHE_KEYS.SOCIAL_PLANETS, this.socialPlanets);
      } catch (error) {
        this.error = '获取社交星球数据失败';
        console.error(error);
      } finally {
        this.loading = false;
      }
    },

    // 模拟获取社交帖子数据
    async fetchSocialPosts() {
      this.loading = true;
      try {
        // 实际项目中这里应该是API调用
        // const response = await api.get('/explore/social-posts');
        // this.socialPosts = response.data;

        // 使用模拟数据
        this.socialPosts = this.generateMockSocialPosts();
        this.setCachedData(CACHE_KEYS.SOCIAL_POSTS, this.socialPosts);
      } catch (error) {
        this.error = '获取社交帖子数据失败';
        console.error(error);
      } finally {
        this.loading = false;
      }
    },

    // 模拟获取所有数据
    async fetchAllData() {
      try {
        await Promise.all([
          this.fetchRankingLists(),
          this.fetchProvinces(),
          this.fetchHistoricalEvents(),
          this.fetchSeasonalCalendar(),
          this.fetchFoodFootprints(),
          this.fetchDietPlans(),
          this.fetchSocialPlanets(),
          this.fetchSocialPosts()
        ]);
      } catch (error) {
        this.error = '获取数据失败';
        console.error('获取数据失败:', error);
      }
    },

    // 简化的模拟数据生成器 - 继续添加
    generateMockProvinces() {
      return []; // 空数组作为占位符
    },

    generateMockHistoricalEvents() {
      console.log('Explore store: generateMockHistoricalEvents called');
      const result = mockHistoricalEvents;
      console.log('Explore store: 生成的模拟数据数量:', result.length);
      return result; // 返回模拟历史事件数据
    },

    generateMockSeasonalCalendar() {
      return []; // 空数组作为占位符
    },

    // 点赞功能
    toggleLike(postId: string) {
      const post = this.socialPosts.find(p => p.id === postId);
      if (post) {
        post.liked = !post.liked;
        post.likes += post.liked ? 1 : -1;
        this.setCachedData(CACHE_KEYS.SOCIAL_POSTS, this.socialPosts);

        // 实际项目中这里应该调用API更新后端数据
        // api.post(`/posts/${postId}/like`);
      }
    }
  }
});