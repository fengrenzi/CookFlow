import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useCartStore } from '../store/modules/cart';
import { categories, mockRecipes, type Recipe, type Category } from '../data/recipesData';

export function useRecipesViewLogic() {
  // 直接在组合式函数中使用useRouter和useCartStore
  const router = useRouter();
  const cartStore = useCartStore();

  // 响应式状态
  const categoriesState = ref<Category[]>(categories);
  const selectedFilters = ref<string[]>(categoriesState.value.map(() => '全部'));
  const activeFilters = ref<string[]>([]);
  const sortBy = ref('newest');
  const viewMode = ref('grid');
  const currentPage = ref(1);
  const pageSize = ref(10);
  const totalRecipes = ref(120);
  const expandedCategories = ref<boolean[]>(categoriesState.value.map(() => false));
  const defaultVisibleCount = 15; // 默认显示的选项数量
  const searchQuery = ref(''); // 搜索关键词
  const filteredRecipes = ref<Recipe[]>([]); // 搜索过滤后的菜谱
  const recipes = ref<Recipe[]>(mockRecipes);
  const favorites = ref<Set<number>>(new Set());
  const isSearching = ref(false);

  // 设置监听器
  const setupWatchers = () => {
    // 监听搜索框变化，当清空搜索框时自动清除搜索
    watch(searchQuery, (newVal) => {
      if (!newVal.trim()) {
        clearSearch();
      }
    });
  };

  // 选择筛选条件
  const selectFilter = (categoryIndex: number, option: string) => {
    // 确保selectedFilters数组有足够的长度
    if (!selectedFilters.value) {
      selectedFilters.value = [];
    }
    // 确保categoriesState.value存在且categoryIndex在有效范围内
    if (categoryIndex >= 0 && categoriesState.value && categoryIndex < categoriesState.value.length) {
      selectedFilters.value[categoryIndex] = option;
      // 更新活跃筛选条件
    updateActiveFilters();
    // 重置搜索状态
    if (isSearching.value) {
      clearSearch();
    }
    // 重新计算过滤后的菜谱数和重置页码
    const filtered = filterRecipes();
    totalRecipes.value = filtered.length;
    currentPage.value = 1;
    // 更新过滤和排序结果
    updateFilteredAndSortedRecipes();
    }
  };

  // 更新活跃筛选条件
  const updateActiveFilters = () => {
    // 确保selectedFilters和categoriesState都已初始化
    if (!selectedFilters.value || !categoriesState.value) {
      activeFilters.value = [];
      return;
    }
    
    activeFilters.value = selectedFilters.value
      .filter((filter, index) => {
        // 确保filter存在且不等于'全部'，并且categoriesState在对应索引处有值
        return filter && filter !== '全部' && categoriesState.value[index];
      })
      .map(filter => {
        // 找到对应的原始索引，因为filter已经被过滤过
        const originalIndex = selectedFilters.value.findIndex(f => f === filter);
        const categoryName = categoriesState.value[originalIndex]?.分类名称 || '未知分类';
        return `${categoryName}: ${filter}`;
      });
  };

  // 移除筛选条件
  const removeFilter = (filter: string) => {
    // 确保activeFilters已初始化
    if (!activeFilters.value) {
      activeFilters.value = [];
      return;
    }

    // 从活跃筛选条件中移除
    const index = activeFilters.value.indexOf(filter);
    if (index > -1) {
      activeFilters.value.splice(index, 1);
    }

    // 重置对应的选择项为"全部"
    if (selectedFilters.value && categoriesState.value) {
      const categoryIndex = selectedFilters.value.findIndex((val, idx) => {
        // 确保idx在categoriesState的有效范围内
        if (idx >= 0 && idx < categoriesState.value.length) {
          return `${categoriesState.value[idx]?.分类名称 || '未知分类'}: ${val}` === filter;
        }
        return false;
      });
      if (categoryIndex > -1) {
        selectedFilters.value[categoryIndex] = '全部';
      }
    }
  };

  // 根据筛选条件过滤菜谱
  const filterRecipes = () => {
    // 确保所有必要的数据都存在
    if (!recipes.value || !categoriesState.value || !selectedFilters.value) {
      return recipes.value || [];
    }

    // 对原始菜谱进行筛选
    const filtered = recipes.value.filter(recipe => {
      if (!recipe) return false;
      
      // 检查每个分类的筛选条件
      return selectedFilters.value.every((filter, index) => {
        // 如果筛选条件是'全部'，则通过
        if (filter === '全部') return true;
        
        // 确保categoriesState在对应索引处有值
        if (index >= categoriesState.value.length) return true;
        
        const categoryName = categoriesState.value[index]?.分类名称;
        
        // 根据不同的分类名称应用不同的筛选逻辑
        switch (categoryName) {
          case '菜系':
            return (recipe.categories || []).includes(filter);
          case '食材':
            return (recipe.ingredients || []).some(ingredient => ingredient.includes(filter));
          case '工艺':
            return (recipe.categories || []).includes(filter);
          case '口味':
            return (recipe.categories || []).includes(filter);
          case '耗时':
            // 简单的时间范围匹配，实际可能需要更复杂的逻辑
            if (filter === '十分钟' && recipe.cookTime) return recipe.cookTime.includes('10分钟');
            if (filter === '二十分钟' && recipe.cookTime) return recipe.cookTime.includes('20分钟');
            if (filter === '半小时' && recipe.cookTime) return recipe.cookTime.includes('30分钟');
            if (filter === '一小时' && recipe.cookTime) return recipe.cookTime.includes('1小时');
            return (recipe.categories || []).includes(filter);
          case '难度':
            return (recipe.categories || []).includes(filter);
          case '场景':
            return (recipe.categories || []).includes(filter);
          case '人群':
            return (recipe.categories || []).includes(filter);
          case '养生':
            return (recipe.categories || []).includes(filter);
          case '时节':
            return (recipe.categories || []).includes(filter);
          default:
            // 默认检查分类是否包含该筛选条件
            return (recipe.categories || []).includes(filter);
        }
      });
    });
    
    return filtered;
  };

  // 对菜谱列表进行排序
  const sortRecipes = (recipesToSort: Recipe[]) => {
    if (!recipesToSort || recipesToSort.length <= 1) {
      return recipesToSort;
    }
    
    // 创建副本以避免修改原始数组
    const sorted = [...recipesToSort];
    
    switch (sortBy.value) {
      case 'newest':
        // 按发布时间降序排序（最新的在前）
        return sorted.sort((a, b) => {
          const dateA = new Date(a.publishTime || '').getTime();
          const dateB = new Date(b.publishTime || '').getTime();
          return dateB - dateA;
        });
      case 'popular':
        // 按收藏数降序排序（最受欢迎的在前）
        return sorted.sort((a, b) => {
          const favA = Number(a.favoriteCount || 0);
          const favB = Number(b.favoriteCount || 0);
          return favB - favA;
        });
      case 'difficulty':
        // 按难度排序（简单到困难）
        // 基于categories中包含的难度信息进行排序
        const difficultyOrder = {
          '简单': 0,
          '普通': 1,
          '中等': 2,
          '困难': 3,
          '挑战': 4,
          '大师': 5
        };
        
        return sorted.sort((a, b) => {
          // 获取菜谱的难度等级
          const getDifficultyLevel = (recipe: Recipe) => {
            if (!recipe.categories || !Array.isArray(recipe.categories)) return 6;
            
            for (const cat of recipe.categories) {
              if (difficultyOrder[cat as keyof typeof difficultyOrder] !== undefined) {
                return difficultyOrder[cat as keyof typeof difficultyOrder];
              }
            }
            return 6; // 默认最难
          };
          
          return getDifficultyLevel(a) - getDifficultyLevel(b);
        });
      default:
        return sorted;
    }
  };

  // 计算过滤后的菜谱列表（同时考虑筛选条件、搜索结果和排序）
  // 使用响应式引用存储过滤并排序后的菜谱，确保Vue能正确跟踪变化
  const filteredAndSortedRecipes = ref<Recipe[]>([]);
  
  // 更新过滤和排序结果的函数
  const updateFilteredAndSortedRecipes = () => {
    let result;
    
    // 如果正在搜索，使用搜索过滤的结果
    if (isSearching.value) {
      result = filteredRecipes.value;
    } else {
      // 否则应用筛选条件过滤
      result = filterRecipes();
    }
    
    // 应用排序
    filteredAndSortedRecipes.value = sortRecipes(result);
  };
  
  // 计算过滤后的菜谱列表（同时考虑筛选条件、搜索结果和排序）
  const getFilteredRecipes = () => {
    // 每次调用都重新计算，确保获取最新排序结果
    updateFilteredAndSortedRecipes();
    return filteredAndSortedRecipes.value;
  };

  // 清空所有筛选条件
  const clearAllFilters = () => {
    // 确保categoriesState.value存在
    if (categoriesState.value) {
      selectedFilters.value = categoriesState.value.map(() => '全部');
    } else {
      selectedFilters.value = [];
    }
    activeFilters.value = [];
    // 重置搜索状态
    if (isSearching.value) {
      clearSearch();
    }
    // 重新计算过滤后的菜谱数和重置页码
    const filtered = filterRecipes();
    totalRecipes.value = filtered.length;
    currentPage.value = 1;
    // 更新过滤和排序结果
    updateFilteredAndSortedRecipes();
  };

  // 切换展开/收起状态
  const toggleExpand = (categoryIndex: number) => {
    // 确保expandedCategories.value存在且categoryIndex在有效范围内
    if (expandedCategories.value && categoryIndex >= 0 && categoryIndex < expandedCategories.value.length) {
      expandedCategories.value[categoryIndex] = !expandedCategories.value[categoryIndex];
    }
  };

  // 处理页码大小变化
  const handleSizeChange = (val: number) => {
    pageSize.value = val;
    // 这里应该重新加载数据
  };

  // 处理页码变化
  const handleCurrentChange = (val: number) => {
    currentPage.value = val;
    // 这里应该重新加载数据
  };

  // 收藏/取消收藏菜谱
  const likeRecipe = (id?: number) => {
    if (!id) return;
    // 切换收藏状态
    if (favorites.value.has(id)) {
      favorites.value.delete(id);
      ElMessage.success('已取消收藏');
    } else {
      favorites.value.add(id);
      ElMessage.success('收藏成功');
    }
    // 更新响应式引用
    favorites.value = new Set(favorites.value);
  };

  // 添加到购物车
  const addToCart = (recipe?: Recipe) => {
    // 确保recipe对象存在并有必要的属性
    if (!recipe || !recipe.ingredients || !Array.isArray(recipe.ingredients)) {
      console.warn('Invalid recipe data, cannot add to cart');
      return;
    }

    // 将ingredients转换为购物车需要的格式
    const ingredients = recipe.ingredients.map((name: string) => ({
      id: `${recipe.id}-${name}`,
      name,
      unit: '份', // 由于模拟数据没有单位，暂时使用'份'
      amount: 1,
      imageUrl: ''
    }));

    // 确保cartStore对象存在并有addRecipeToCart方法
    if (cartStore && typeof cartStore.addRecipeToCart === 'function') {
      cartStore.addRecipeToCart(
        String(recipe.id),
        recipe.title,
        recipe.imageUrl,
        ingredients
      );

      ElMessage.success('已添加到购物车');
    } else {
      console.warn('Cart store not available, cannot add recipe to cart');
    }
  };

  // 导航到详情页
  const navigateToDetail = (id?: number) => {
    // 确保router对象存在且id有效
    if (id && router && typeof router.push === 'function') {
      router.push(`/recipe/${id}`);
    } else if (!id) {
      console.warn('Invalid recipe ID, cannot navigate to detail page');
    } else {
      console.warn('Router not available, cannot navigate to detail page');
    }
  };

  // 处理搜索
  const handleSearch = () => {
    if (!searchQuery.value.trim()) {
      filteredRecipes.value = [];
      isSearching.value = false;
      return;
    }

    isSearching.value = true;
    const query = searchQuery.value.toLowerCase().trim();

    // 多字段搜索实现，添加安全检查
    filteredRecipes.value = (recipes.value || []).filter(recipe => {
      if (!recipe) return false;
      
      const inTitle = recipe.title?.toLowerCase().includes(query) || false;
      const inIngredients = (recipe.ingredients || []).some(ingredient =>
        ingredient?.toLowerCase().includes(query) || false
      );
      const inAuthor = recipe.author?.toLowerCase().includes(query) || false;
      const inCategories = (recipe.categories || []).some(category =>
        category?.toLowerCase().includes(query) || false
      );

      return inTitle || inIngredients || inAuthor || inCategories;
    });

    currentPage.value = 1;
    totalRecipes.value = filteredRecipes.value.length;
    // 更新过滤和排序结果
    updateFilteredAndSortedRecipes();
  };

  // 清除搜索
  const clearSearch = () => {
    searchQuery.value = '';
    filteredRecipes.value = [];
    isSearching.value = false;
    currentPage.value = 1;
    totalRecipes.value = 120;
    // 更新过滤和排序结果
    updateFilteredAndSortedRecipes();
  };

  // 初始化
  setupWatchers();
  
  // 初始加载时计算过滤和排序结果
  updateFilteredAndSortedRecipes();

  // 切换排序方式
  const setSortBy = (value: string | number | boolean | undefined) => {
    if (typeof value === 'string') {
      // 使用Vue的响应式API确保更新能被正确追踪
      sortBy.value = value;
      // 重置到第一页
      currentPage.value = 1;
      // 立即更新过滤和排序结果
      updateFilteredAndSortedRecipes();
      console.log(`排序方式已切换为: ${value}`);
    }
  };

  // 返回所有状态和方法
  return {
    // 状态
    categories: categoriesState,
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
    recipes,
    favorites,
    isSearching,
    filteredAndSortedRecipes,
    // 方法
    selectFilter,
    updateActiveFilters,
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
    getFilteredRecipes,
    setSortBy
  };
}