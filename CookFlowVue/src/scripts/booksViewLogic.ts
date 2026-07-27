import { ref, computed } from 'vue';
import { useBooksStore } from '@/store/modules/books';
import type { Ref } from 'vue';

// 定义BooksViewLogic的返回类型
export interface BooksViewLogicReturn {
  // 筛选相关状态
  selectedThirdCategories: Ref<string[]>;
  priceRange: Ref<[number, number]>;
  searchQuery: Ref<string>;
  sortOption: Ref<string>;
  currentPage: Ref<number>;
  pageSize: number;
  priceSortDirection: Ref<'asc' | 'desc'>;
  sortOptions: Ref<Array<{ value: string; label: string }>>;
  
  // 单选筛选
  selectedDifficulty: Ref<string>;
  difficultyOptions: Ref<Array<{ value: string; label: string }>>;
  selectedRating: Ref<string>;
  ratingOptions: Ref<Array<{ value: string; label: string }>>;
  
  // 下拉菜单可见性
  isDifficultyVisible: Ref<boolean>;
  isRatingVisible: Ref<boolean>;
  
  // 分类相关状态
  selectedPrimaryId: Ref<string | null>;
  selectedSecondaryId: Ref<string | null>;
  hoveredPrimaryId: Ref<string | null>;
  
  // 引用
  booksViewRef: Ref<HTMLElement | null>;
  
  // 计算属性
  filteredBooks: Ref<any[]>;
  totalPages: Ref<number>;
  
  // 方法
  handleSortClick: (optionValue: string) => void;
  handleDifficultyMouseEnter: () => void;
  handleDifficultyMouseLeave: () => void;
  handleDifficultyDropdownMouseEnter: () => void;
  handleDifficultyDropdownMouseLeave: () => void;
  handleRatingMouseEnter: () => void;
  handleRatingMouseLeave: () => void;
  handleRatingDropdownMouseEnter: () => void;
  handleRatingDropdownMouseLeave: () => void;
  handlePrimaryMouseEnter: (primaryId: string) => void;
  handlePrimaryMouseLeave: () => void;
  handleDropdownMouseEnter: () => void;
  handleDropdownMouseLeave: () => void;
  handleScroll: () => void;
  resetCategories: () => void;
  getSelectedSecondaryName: (primaryId: string, secondaryId: string) => string;
  toggleThirdCategory: (thirdCategory: string) => void;
  getSelectedThirdCategories: (primaryId: string, secondaryId: string) => any[];
  handleSearch: () => void;
}

export function useBooksViewLogic(): BooksViewLogicReturn {
  const booksStore = useBooksStore();
  
  // 筛选相关数据
  const selectedThirdCategories = ref<string[]>([]);
  const priceRange = ref<[number, number]>([0, 500]);
  const searchQuery = ref('');
  const sortOption = ref('recommended');
  const currentPage = ref(1);
  const pageSize = 21;

  // 价格排序方向状态 (默认随机选择升序或降序)
  const priceSortDirection = ref<'asc' | 'desc'>(Math.random() > 0.5 ? 'asc' : 'desc');

  // 排序选项
  const sortOptions = ref([
    { value: 'recommended', label: '推荐排序' },
    { value: 'newest', label: '最新上架' },
    { value: 'price', label: '价格' }
  ]);

  // 单选筛选选项
  const selectedDifficulty = ref<string>('all');
  const difficultyOptions = ref([
    { value: 'all', label: '全部' },
    { value: 'easy', label: '简单' },
    { value: 'medium', label: '中等' },
    { value: 'hard', label: '困难' }
  ]);

  const selectedRating = ref<string>('all');
  const ratingOptions = ref([
    { value: 'all', label: '全部' },
    { value: '3+', label: '3分以上' },
    { value: '4+', label: '4分以上' },
    { value: '4.5+', label: '4.5分以上' }
  ]);

  // 控制筛选区域的显示与隐藏
  const isDifficultyVisible = ref(false);
  const isRatingVisible = ref(false);

  // 定时器变量
  let difficultyLeaveTimer: number | null = null;
  let ratingLeaveTimer: number | null = null;
  let mouseLeaveTimer: number | null = null;

  // 分类相关状态
  const selectedPrimaryId = ref<string | null>(null);
  const selectedSecondaryId = ref<string | null>(null);
  const hoveredPrimaryId = ref<string | null>(null);

  // 引用books-view元素
  const booksViewRef = ref<HTMLElement | null>(null);

  // 处理排序选项点击
  const handleSortClick = (optionValue: string) => {
    if (optionValue === 'price') {
      // 如果点击的是价格排序，切换排序方向
      priceSortDirection.value = priceSortDirection.value === 'asc' ? 'desc' : 'asc';
      // 设置当前排序选项为价格+方向
      sortOption.value = `price-${priceSortDirection.value}`;
    } else {
      // 其他排序选项直接设置
      sortOption.value = optionValue;
    }
  };

  // 难度筛选区域的鼠标事件处理
  const handleDifficultyMouseEnter = () => {
    if (difficultyLeaveTimer) {
      clearTimeout(difficultyLeaveTimer);
      difficultyLeaveTimer = null;
    }
    isDifficultyVisible.value = true;
  };

  const handleDifficultyMouseLeave = () => {
    difficultyLeaveTimer = setTimeout(() => {
      isDifficultyVisible.value = false;
      difficultyLeaveTimer = null;
    }, 300);
  };

  const handleDifficultyDropdownMouseEnter = () => {
    if (difficultyLeaveTimer) {
      clearTimeout(difficultyLeaveTimer);
      difficultyLeaveTimer = null;
    }
  };

  const handleDifficultyDropdownMouseLeave = () => {
    difficultyLeaveTimer = setTimeout(() => {
      isDifficultyVisible.value = false;
      difficultyLeaveTimer = null;
    }, 300);
  };

  // 评分筛选区域的鼠标事件处理
  const handleRatingMouseEnter = () => {
    if (ratingLeaveTimer) {
      clearTimeout(ratingLeaveTimer);
      ratingLeaveTimer = null;
    }
    isRatingVisible.value = true;
  };

  const handleRatingMouseLeave = () => {
    ratingLeaveTimer = setTimeout(() => {
      isRatingVisible.value = false;
      ratingLeaveTimer = null;
    }, 300);
  };

  const handleRatingDropdownMouseEnter = () => {
    if (ratingLeaveTimer) {
      clearTimeout(ratingLeaveTimer);
      ratingLeaveTimer = null;
    }
  };

  const handleRatingDropdownMouseLeave = () => {
    ratingLeaveTimer = setTimeout(() => {
      isRatingVisible.value = false;
      ratingLeaveTimer = null;
    }, 300);
  };

  // 处理一级分类鼠标进入
  const handlePrimaryMouseEnter = (primaryId: string) => {
    if (mouseLeaveTimer) {
      clearTimeout(mouseLeaveTimer);
      mouseLeaveTimer = null;
    }
    hoveredPrimaryId.value = primaryId;
  };

  // 处理一级分类鼠标离开
  const handlePrimaryMouseLeave = () => {
    // 设置定时器，延迟隐藏下拉菜单
    mouseLeaveTimer = window.setTimeout(() => {
      hoveredPrimaryId.value = null;
      mouseLeaveTimer = null;
    }, 200);
  };

  // 处理下拉菜单鼠标进入
  const handleDropdownMouseEnter = () => {
    if (mouseLeaveTimer) {
      clearTimeout(mouseLeaveTimer);
      mouseLeaveTimer = null;
    }
  };

  // 处理下拉菜单鼠标离开
  const handleDropdownMouseLeave = () => {
    handlePrimaryMouseLeave();
  };

  // 处理滚动事件，使顶部间隙随滚动逐渐缩小
  const handleScroll = () => {
    const scrollY = window.scrollY;
    // 直接设置padding-top属性，滚动时逐渐减小但保留少量间隙
    if (booksViewRef.value) {
      // 滚动时保留5px的顶部间隙，而不是完全消除
      const newPadding = scrollY > 5 ? 5 : 20;
      booksViewRef.value.style.paddingTop = `${newPadding}px`;
    }
  };

  // 重置所有筛选条件
  const resetCategories = () => {
    selectedPrimaryId.value = null;
    selectedSecondaryId.value = null;
    selectedThirdCategories.value = [];
    priceRange.value = [0, 500];
    selectedDifficulty.value = 'all';
    selectedRating.value = 'all';
    searchQuery.value = '';
    sortOption.value = 'recommended';
    currentPage.value = 1;
  };

  // 获取选中的二级分类名称
  const getSelectedSecondaryName = (primaryId: string, secondaryId: string) => {
    const primary = booksStore.primaryCategories.find(p => p.id === primaryId);
    if (!primary) return '';
    const secondary = primary.secondaryCategories.find(s => s.id === secondaryId);
    return secondary ? secondary.name : '';
  };

  // 切换三级分类（多选）
  const toggleThirdCategory = (thirdCategory: string) => {
    const index = selectedThirdCategories.value.indexOf(thirdCategory);
    if (index > -1) {
      selectedThirdCategories.value.splice(index, 1);
    } else {
      selectedThirdCategories.value.push(thirdCategory);
    }
    currentPage.value = 1; // 重置到第一页
  };

  // 获取选中的三级分类
  const getSelectedThirdCategories = (primaryId: string, secondaryId: string) => {
    const primary = booksStore.primaryCategories.find(p => p.id === primaryId);
    if (!primary) return [];

    const secondary = primary.secondaryCategories.find(s => s.id === secondaryId);
    if (!secondary) return [];

    return secondary.thirdCategories || [];
  };

  // 搜索处理函数
  const handleSearch = () => {
    currentPage.value = 1;
  };

  // 创建筛选函数，避免重复代码
  const filterBooks = (booksToFilter: any[]) => {
    let result = [...booksToFilter];

    // 应用三级分类筛选
    if (selectedThirdCategories.value.length > 0) {
      result = result.filter(book => {
        return selectedThirdCategories.value.some(third =>
          book.title?.toLowerCase().includes(third.toLowerCase()) || false
        );
      });
    }

    // 应用难度单选筛选
    if (selectedDifficulty.value !== 'all') {
      if (selectedDifficulty.value === 'easy') {
        result = result.filter(book => book.level === '入门');
      } else if (selectedDifficulty.value === 'medium') {
        result = result.filter(book => book.level === '进阶');
      } else if (selectedDifficulty.value === 'hard') {
        result = result.filter(book => book.level === '专业' || book.level === '大师级');
      }
    }

    // 应用价格筛选
    result = result.filter(book => book.price >= priceRange.value[0] && book.price <= priceRange.value[1]);

    // 应用评分单选筛选
    if (selectedRating.value !== 'all') {
      if (selectedRating.value === '3+') {
        result = result.filter(book => book.rating >= 3);
      } else if (selectedRating.value === '4+') {
        result = result.filter(book => book.rating >= 4);
      } else if (selectedRating.value === '4.5+') {
        result = result.filter(book => book.rating >= 4.5);
      }
    }

    // 应用搜索
    if (searchQuery.value) {
      const query = searchQuery.value.toLowerCase();
      result = result.filter(book =>
        (book.title?.toLowerCase().includes(query) || false) ||
        (book.author?.toLowerCase().includes(query) || false)
      );
    }

    return result;
  };

  // 总页数
  const totalPages = computed(() => {
    const filtered = filterBooks(booksStore.books);
    return Math.ceil(filtered.length / pageSize);
  });

  // 筛选后的书籍
  const filteredBooks = computed(() => {
    let result = filterBooks(booksStore.books);

    // 应用排序
    switch (sortOption.value) {
      case 'newest':
        result.sort((a, b) => a.id - b.id);
        break;
      case 'price-asc':
        result.sort((a, b) => a.price - b.price);
        break;
      case 'price-desc':
        result.sort((a, b) => b.price - a.price);
        break;
      case 'rating-desc':
        result.sort((a, b) => b.rating - a.rating);
        break;
      default: // recommended
        // 按热度和评分排序
        result.sort((a, b) => {
          const scoreA = (a.isHot ? 20 : 0) + (a.isNew ? 10 : 0) + a.rating * 10;
          const scoreB = (b.isHot ? 20 : 0) + (b.isNew ? 10 : 0) + b.rating * 10;
          return scoreB - scoreA;
        });
    }

    // 分页
    const start = (currentPage.value - 1) * pageSize;
    const end = start + pageSize;
    return result.slice(start, end);
  });

  return {
    // 筛选相关状态
    selectedThirdCategories,
    priceRange,
    searchQuery,
    sortOption,
    currentPage,
    pageSize,
    priceSortDirection,
    sortOptions,
    
    // 单选筛选
    selectedDifficulty,
    difficultyOptions,
    selectedRating,
    ratingOptions,
    
    // 下拉菜单可见性
    isDifficultyVisible,
    isRatingVisible,
    
    // 分类相关状态
    selectedPrimaryId,
    selectedSecondaryId,
    hoveredPrimaryId,
    
    // 引用
    booksViewRef,
    
    // 计算属性
    filteredBooks,
    totalPages,
    
    // 方法
    handleSortClick,
    handleDifficultyMouseEnter,
    handleDifficultyMouseLeave,
    handleDifficultyDropdownMouseEnter,
    handleDifficultyDropdownMouseLeave,
    handleRatingMouseEnter,
    handleRatingMouseLeave,
    handleRatingDropdownMouseEnter,
    handleRatingDropdownMouseLeave,
    handlePrimaryMouseEnter,
    handlePrimaryMouseLeave,
    handleDropdownMouseEnter,
    handleDropdownMouseLeave,
    handleScroll,
    resetCategories,
    getSelectedSecondaryName,
    toggleThirdCategory,
    getSelectedThirdCategories,
    handleSearch
  };
}