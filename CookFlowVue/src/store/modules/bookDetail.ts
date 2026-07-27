import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';

// 书籍数据接口定义
interface BookData {
  id: string;
  title: string;
  author: string;
  publisher: string;
  price: number;
  recipeCount: number;
  description: string;
  readCount: number;
  recommendCount: number;
  isbn: string;
  readingCount: number;
  coverImage: string;
  totalPages: number; // 总页数
}

// 书评接口定义
interface BookReview {
  id: string;
  author: string;
  date: string;
  content: string;
  rating: number;
  likes?: number;
  isLiked?: boolean;
}

// 推荐书籍接口定义
interface RecommendedBook {
  id: string;
  title: string;
  author: string;
  price: number;
  coverImage: string;
  rating: number;
  reviewCount: number;
  tags: string[];
  reason: string;
}

// 书籍中的菜谱数据接口
interface RecipeData {
  id: string;
  name: string;
  imageUrl: string;
  pageNumber: number;
  ingredients: Array<{
    id: string;
    name: string;
    unit: string;
    amount: number;
    imageUrl: string;
  }>;
}

// 统计数据接口
interface StatItem {
  label: string;
  value: number;
}

export const useBookDetailStore = defineStore('bookDetail', () => {
  // 状态定义
  const bookData = ref<BookData>({
    id: '',
    title: '中国家常菜谱大全',
    author: '张明亮',
    publisher: '中国烹饪出版社',
    price: 68.00,
    recipeCount: 500,
    description: '这是一本全面介绍中国家常菜的烹饪书籍，包含了鲁菜、川菜、粤菜、苏菜等各大菜系的经典家常菜做法。本书适合烹饪爱好者和家庭主妇使用，简单易学，图文并茂。这是一本全面介绍中国家常菜的烹饪书籍，包含了鲁菜、川菜、粤菜、苏菜等各大菜系的经典家常菜做法。本书适合烹饪爱好者和家庭主妇使用，简单易学，图文并茂。',
    readCount: 1234,
    recommendCount: 987,
    isbn: '9787506482341',
    readingCount: 345,
    coverImage: '/images/book-cover.jpg',
    totalPages: 15 // 示例总页数
  });

  const bookReviews = ref<BookReview[]>([]);
  const recommendedBooks = ref<RecommendedBook[]>([]);
  const isFavorite = ref(false);
  const pageLoaded = ref(false);
  // 添加用户阅读状态
  const isRead = ref(false);
  // 添加非核心数据加载状态
  const reviewsLoaded = ref(false);
  const recommendedLoaded = ref(false);
  
  // 统计数据列表
  const statsList = ref<StatItem[]>([
    { label: '菜谱数', value: 500 },
    { label: '阅读人数', value: 1234 },
    { label: '推荐人数', value: 987 },
    { label: '在读人数', value: 345 }
  ]);

  // 计算属性
  const averageRating = computed(() => {
    if (!bookReviews.value.length) return 0;
    const sum = bookReviews.value.reduce((acc, review) => acc + review.rating, 0);
    return sum / bookReviews.value.length;
  });

  const formattedRating = computed(() => {
    return averageRating.value.toFixed(1);
  });
  
  // 添加isLoaded属性以保持与模板的一致性
  const isLoaded = computed(() => pageLoaded.value);

  // 模拟书籍中的菜谱数据
  const mockRecipeData = ref<RecipeData>({
    id: 'recipe-' + Date.now(),
    name: '书中精选菜谱',
    imageUrl: '/images/recipe-placeholder.jpg',
    pageNumber: 1,
    ingredients: [
      {
        id: 'ing-1',
        name: '主料',
        unit: '份',
        amount: 1,
        imageUrl: '/images/default-ingredient.png'
      },
      {
        id: 'ing-2',
        name: '辅料',
        unit: '份',
        amount: 1,
        imageUrl: '/images/default-ingredient.png'
      }
    ]
  });

  // 方法定义
  // 初始化核心数据（书籍基本信息）
  const initializeCoreData = async (bookId: string) => {
    try {
      // 移除模拟延迟，直接加载数据
      
      // 设置书籍数据（实际应从API获取）
      bookData.value.id = bookId;
      
      // 设置统计数据
      statsList.value = [
        { label: '菜谱数', value: bookData.value.recipeCount },
        { label: '阅读人数', value: bookData.value.readCount },
        { label: '推荐人数', value: bookData.value.recommendCount },
        { label: '在读人数', value: bookData.value.readingCount }
      ];
      
      // 模拟判断用户是否已阅读（实际应从API获取）
      // 这里简单模拟，实际项目中应根据用户ID和书籍ID查询后端
      isRead.value = Math.random() > 0.5; // 随机模拟50%的概率已阅读

      // 标记核心数据加载完成，显示主要内容
      pageLoaded.value = true;
      
      return true;
    } catch (error) {
      ElMessage.error('加载书籍详情失败，请稍后重试');
      return false;
    }
  };

  // 加载评论数据
  const loadReviewsData = async () => {
    try {
      // 移除模拟延迟，直接加载数据
      
      bookReviews.value = [
        {
          id: '1',
          author: '美食爱好者',
          date: '2024-01-15',
          content: '这本书真的很棒，家常菜做法详细，步骤清晰，照着做了几道都很成功！图片质量也很好，一目了然。',
          rating: 5,
          likes: 24
        },
        {
          id: '2',
          author: '新手学做饭',
          date: '2024-01-10',
          content: '作为新手，这本书对我帮助很大，食材选择和调料用量都标注得很清楚。简单易学，非常适合初学者。',
          rating: 4,
          likes: 18
        },
        {
          id: '3',
          author: '家庭主厨',
          date: '2024-01-05',
          content: '菜品丰富多样，适合日常家庭制作，推荐给各位家庭主妇。有些菜品可以再创新一些会更好。',
          rating: 5,
          likes: 32
        },
        {
          id: '4',
          author: '烹饪达人',
          date: '2023-12-28',
          content: '作为有经验的烹饪爱好者，这本书虽然基础，但有些小技巧还是很实用的。整体质量不错。',
          rating: 4,
          likes: 15
        },
        {
          id: '5',
          author: '厨房小白',
          date: '2023-12-20',
          content: '对于完全没做过饭的人来说，这本书确实很友好，一步步教你怎么做，赞！',
          rating: 5,
          likes: 28
        }
      ];
      
      // 设置评论加载完成状态
      reviewsLoaded.value = true;
      
      return true;
    } catch (error) {
      console.error('加载评论数据失败:', error);
      return false;
    }
  };

  // 加载推荐书籍数据
  const loadRecommendedBooksData = async () => {
    try {
      // 移除模拟延迟，直接加载数据
      
      recommendedBooks.value = [
        {
          id: 'r1',
          title: '家常菜谱精选',
          author: '李小明',
          price: 58.00,
          coverImage: '/images/recommended-book1.jpg',
          rating: 4.7,
          reviewCount: 245,
          tags: ['畅销', '家常'],
          reason: '与您浏览的书籍类型相似'
        },
        {
          id: 'r2',
          title: '零基础学烹饪',
          author: '王大厨',
          price: 48.00,
          coverImage: '/images/recommended-book2.jpg',
          rating: 4.5,
          reviewCount: 189,
          tags: ['入门', '新手'],
          reason: '适合初学者的实用指南'
        },
        {
          id: 'r3',
          title: '四季养生食谱',
          author: '张医师',
          price: 78.00,
          coverImage: '/images/recommended-book3.jpg',
          rating: 4.8,
          reviewCount: 320,
          tags: ['养生', '健康'],
          reason: '热门养生食谱推荐'
        },
        {
          id: 'r4',
          title: '快手菜100道',
          author: '刘师傅',
          price: 42.50,
          coverImage: '/images/recommended-book4.jpg',
          rating: 4.6,
          reviewCount: 178,
          tags: ['快捷', '家常'],
          reason: '忙碌人士的厨房救星'
        },
        {
          id: 'r5',
          title: '中西餐烹饪技巧大全',
          author: '张厨师',
          price: 98.00,
          coverImage: '',
          rating: 4.9,
          reviewCount: 426,
          tags: ['专业', '技巧'],
          reason: '资深厨师必备参考'
        }
      ];
      
      // 设置推荐书籍加载完成状态
      recommendedLoaded.value = true;
      
      return true;
    } catch (error) {
      console.error('加载推荐书籍数据失败:', error);
      return false;
    }
  };

  // 初始化数据 - 分块加载
  const initializeData = async (bookId: string) => {
    try {
      // 首先加载核心数据
      const coreLoaded = await initializeCoreData(bookId);
      
      if (coreLoaded) {
        // 核心数据加载完成后，立即加载非核心数据
        await Promise.all([
          loadReviewsData(),
          loadRecommendedBooksData()
        ]);
      }
      
      return true;
    } catch (error) {
      console.error('初始化数据失败:', error);
      return false;
    }
  };

  // 切换收藏状态
  const toggleFavorite = async () => {
    try {
      // 模拟API请求
      await new Promise(resolve => setTimeout(resolve, 300));
      
      isFavorite.value = !isFavorite.value;
      ElMessage.success(isFavorite.value ? '已加入收藏' : '已取消收藏');
      return true;
    } catch (error) {
      console.error('切换收藏状态失败:', error);
      ElMessage.error('操作失败，请稍后重试');
      return false;
    }
  };

  // 点赞评论
  const likeReview = async (reviewId: string) => {
    try {
      // 模拟API请求
      await new Promise(resolve => setTimeout(resolve, 200));
      
      const review = bookReviews.value.find(r => r.id === reviewId);
      if (review) {
        // 切换点赞状态
        review.isLiked = !review.isLiked;
        // 根据点赞状态更新点赞数
        if (review.isLiked) {
          review.likes = (review.likes || 0) + 1;
        } else {
          review.likes = Math.max(0, (review.likes || 0) - 1);
        }
      }
      return true;
    } catch (error) {
      console.error('操作失败:', error);
      return false;
    }
  };

  // 获取指定评分的百分比
  const getRatingPercentage = (rating: number): number => {
    const count = bookReviews.value.filter(review => review.rating === rating).length;
    return bookReviews.value.length ? Math.round((count / bookReviews.value.length) * 100) : 0;
  };

  // 格式化日期
  const formatDate = (dateString: string): string => {
    const options: Intl.DateTimeFormatOptions = {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    };
    return new Date(dateString).toLocaleDateString('zh-CN', options);
  };

  // 获取用户名首字母
  const getInitials = (name: string): string => {
    return name.substring(0, 2);
  };

  // 获取模拟菜谱数据
  const getMockRecipeData = () => {
    return mockRecipeData.value;
  };

  return {
    // 状态
    bookData,
    bookReviews,
    recommendedBooks,
    isFavorite,
    isRead,
    pageLoaded,
    reviewsLoaded,
    recommendedLoaded,
    statsList,
    
    // 计算属性
    averageRating,
    formattedRating,
    isLoaded,
    
    // 方法
    initializeData,
    toggleFavorite,
    likeReview,
    getRatingPercentage,
    formatDate,
    getInitials,
    getMockRecipeData
  };
});