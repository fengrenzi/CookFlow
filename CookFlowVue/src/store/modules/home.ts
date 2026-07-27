import { defineStore } from 'pinia';

// 缓存键名常量
const CACHE_KEYS = {
  IMAGES: 'home_images_cache',
  CATEGORIES: 'home_categories_cache',
  RECIPES: 'home_recipes_cache',
  RECOMMENDS: 'home_recommends_cache',
  TIMESTAMP: 'home_data_timestamp'
};

// 缓存有效期（毫秒）
const CACHE_DURATION = 30 * 60 * 1000; // 30分钟

// 定义图片接口
export interface GalleryImage {
  id: number;
  src: string;
  alt: string;
}

// 定义分类接口
export interface Category {
  id: number;
  name: string;
  imageUrl: string;
  path: string;
}

// 定义食谱接口
export interface Recipe {
  id: number;
  name: string;
  imageUrl: string;
  path: string;
}

export const useHomeStore = defineStore('home', {
  state: () => ({
    // 首页轮播图数据
    images: [] as GalleryImage[],
    
    // 热门分类数据
    categories: [] as Category[],
    
    // 最新食谱数据
    recipes: [] as Recipe[],
    
    // 今日推荐数据
    recommends: [] as Recipe[],
    
    // 加载状态
    loading: false,
    
    // 错误信息
    error: null as string | null
  }),
  
  getters: {
    // 获取所有轮播图
    getAllImages: (state) => state.images,
    
    // 获取所有分类
    getAllCategories: (state) => state.categories,
    
    // 获取所有食谱
    getAllRecipes: (state) => state.recipes,
    
    // 获取所有推荐
    getAllRecommends: (state) => state.recommends,
    
    // 判断是否正在加载
    isLoading: (state) => state.loading
  },
  
  actions: {
    // 初始化store时直接设置默认数据（无需等待API调用）
    setInitialData() {
      // 尝试从缓存读取数据
      const cachedImages = this.getCachedData(CACHE_KEYS.IMAGES);
      const cachedCategories = this.getCachedData(CACHE_KEYS.CATEGORIES);
      const cachedRecipes = this.getCachedData(CACHE_KEYS.RECIPES);
      const cachedRecommends = this.getCachedData(CACHE_KEYS.RECOMMENDS);
      
      // 如果有缓存数据，直接使用
      if (cachedImages) this.images = cachedImages;
      if (cachedCategories) this.categories = cachedCategories;
      if (cachedRecipes) this.recipes = cachedRecipes;
      if (cachedRecommends) this.recommends = cachedRecommends;
      
      // 如果没有缓存数据，使用模拟数据作为初始值
      if (!this.images.length) this.images = this.generateMockImages();
      if (!this.categories.length) this.categories = this.generateMockCategories();
      if (!this.recipes.length) this.recipes = this.generateMockRecipes();
      if (!this.recommends.length) this.recommends = this.generateMockRecommends();
    },
    
    // 从localStorage获取缓存数据
    getCachedData(key: string) {
      try {
        const cached = localStorage.getItem(key);
        if (!cached) return null;
        
        const cacheObj = JSON.parse(cached);
        const now = Date.now();
        
        // 检查缓存是否过期
        if (now - cacheObj.timestamp > CACHE_DURATION) {
          localStorage.removeItem(key);
          return null;
        }
        
        return cacheObj.data;
      } catch (error) {
        return null;
      }
    },
    
    // 将数据缓存到localStorage
    setCachedData(key: string, data: any) {
      try {
        localStorage.setItem(key, JSON.stringify({
          data,
          timestamp: Date.now()
        }));
      } catch (error) {
      }
    },
    
    // 从后端获取轮播图数据
    async fetchImages() {
      try {
        // 这里将来会替换为实际的API调用
        // const response = await api.get('/api/home/images');
        // this.images = response.data;
        
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 300));
        const newImages = this.generateMockImages();
        this.images = newImages;
        // 缓存数据
        this.setCachedData(CACHE_KEYS.IMAGES, newImages);
      } catch (error) {
        this.error = '获取轮播图数据失败';
      }
    },
    
    // 从后端获取分类数据
    async fetchCategories() {
      try {
        // 这里将来会替换为实际的API调用
        // const response = await api.get('/api/home/categories');
        // this.categories = response.data;
        
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 300));
        const newCategories = this.generateMockCategories();
        this.categories = newCategories;
        // 缓存数据
        this.setCachedData(CACHE_KEYS.CATEGORIES, newCategories);
      } catch (error) {
        this.error = '获取分类数据失败';
      }
    },
    
    // 从后端获取最新食谱数据
    async fetchRecipes() {
      try {
        // 这里将来会替换为实际的API调用
        // const response = await api.get('/api/home/recipes');
        // this.recipes = response.data;
        
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 300));
        const newRecipes = this.generateMockRecipes();
        this.recipes = newRecipes;
        // 缓存数据
        this.setCachedData(CACHE_KEYS.RECIPES, newRecipes);
      } catch (error) {
        this.error = '获取最新食谱数据失败';
        console.error(error);
      }
    },
    
    // 从后端获取今日推荐数据
    async fetchRecommends() {
      try {
        // 这里将来会替换为实际的API调用
        // const response = await api.get('/api/home/recommends');
        // this.recommends = response.data;
        
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 300));
        const newRecommends = this.generateMockRecommends();
        this.recommends = newRecommends;
        // 缓存数据
        this.setCachedData(CACHE_KEYS.RECOMMENDS, newRecommends);
      } catch (error) {
        this.error = '获取推荐数据失败';
      }
    },
    
    // 获取所有首页数据
    async fetchAllHomeData() {
      // 设置loading状态
      this.loading = true;
      
      try {
        // 并行获取所有数据
        await Promise.all([
          this.fetchImages(),
          this.fetchCategories(),
          this.fetchRecipes(),
          this.fetchRecommends()
        ]);
        
        // 更新缓存时间戳
        localStorage.setItem(CACHE_KEYS.TIMESTAMP, Date.now().toString());
      } catch (error) {
      } finally {
        this.loading = false;
      }
    },
    
    // 生成模拟轮播图数据
    generateMockImages(): GalleryImage[] {
      return [
        { id: 1, src: '/public/hots/1.jpg', alt: '美食轮播图1' },
        { id: 2, src: '/public/hots/2.jpg', alt: '美食轮播图2' },
        { id: 3, src: '/public/hots/3.jpg', alt: '美食轮播图3' }
      ];
    },
    
    // 生成模拟分类数据
    generateMockCategories(): Category[] {
      return [
        { id: 1, name: '开胃菜', imageUrl: '/public/hots/1.jpg', path: '/categories/appetizers' },
        { id: 2, name: '小食', imageUrl: '/images/categories/baking-sweet.jpg', path: '/categories/snacks' },
        { id: 3, name: '饮品', imageUrl: '/images/categories/baking-sweet.jpg', path: '/categories/drinks' },
        { id: 4, name: '烘焙-甜味', imageUrl: '/images/categories/baking-sweet.jpg', path: '/categories/baking-sweet' },
        { id: 5, name: '汤品', imageUrl: '/images/categories/soup.jpg', path: '/categories/soups' },
        { id: 6, name: '早餐', imageUrl: '/images/categories/breakfast.jpg', path: '/categories/breakfast' }
      ];
    },
    
    // 生成模拟食谱数据
    generateMockRecipes(): Recipe[] {
      return [
        { id: 1, name: '紫薯牛奶冰皮月饼', imageUrl: '/public/hots/1.jpg', path: '/recipes/1' },
        { id: 2, name: '意式青豆泥烩饭配鹅肝', imageUrl: '/images/recipes/risotto.jpg', path: '/recipes/2' },
        { id: 3, name: '蓝色玛格丽特', imageUrl: '/images/recipes/margarita.jpg', path: '/recipes/3' },
        { id: 4, name: '黑松露面包粒', imageUrl: '/images/recipes/truffle-bread.jpg', path: '/recipes/4' }
      ];
    },
    
    // 生成模拟推荐数据
    generateMockRecommends(): Recipe[] {
      return [
        { id: 1, name: '紫薯牛奶冰皮月饼', imageUrl: '/public/hots/1.jpg', path: '/recipes/1' },
        { id: 2, name: '意式青豆泥烩饭配鹅肝', imageUrl: '/images/recipes/risotto.jpg', path: '/recipes/2' },
        { id: 3, name: '蓝色玛格丽特', imageUrl: '/images/recipes/margarita.jpg', path: '/recipes/3' },
        { id: 4, name: '黑松露面包粒', imageUrl: '/images/recipes/truffle-bread.jpg', path: '/recipes/4' }
      ];
    }
  }
});