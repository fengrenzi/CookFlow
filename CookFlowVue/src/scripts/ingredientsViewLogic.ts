import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useIngredientsStore } from '@/store/modules/ingredients';

// 定义食材接口
export interface Ingredient {
  id: string;
  name: string;
  imgUrl?: string;
  category: string;
}

// 定义推荐菜谱接口
export interface RecommendedRecipe {
  id: number;
  name: string;
  image: string;
  description: string;
}

// 初始化食材视图逻辑
export const useIngredientsViewLogic = () => {
  const ingredientsStore = useIngredientsStore();
  const router = useRouter();
  
  // 从store获取选中的食材列表和推荐菜谱列表
  const selectedIngredients = computed(() => ingredientsStore.selectedIngredients);
  const recommendedRecipes = computed(() => ingredientsStore.recommendedRecipes);
  
  // 食材点击处理函数
  const handleIngredientClick = (ingredientId: string) => {
    // 只传递食材id，详情页通过id获取其他信息
    router.push(`/ingredients/${ingredientId}`);
  };
  
  // 切换食材选中状态
  const toggleIngredientSelection = (ingredient: Ingredient) => {
    ingredientsStore.toggleIngredientSelection(ingredient);
    
    // 根据选中的食材更新推荐菜谱
    updateRecommendedRecipes();
  };
  
  // 检查食材是否被选中
  const isIngredientSelected = (ingredientId: string): boolean => {
    return selectedIngredients.value.some(item => item.id === ingredientId);
  };
  
  // 更新推荐菜谱
  const updateRecommendedRecipes = () => {
    if (ingredientsStore.selectedIngredients.length === 0) {
      ingredientsStore.setRecommendedRecipes([]);
      return;
    }
    
    // 模拟推荐菜谱数据
    // 在实际应用中，这里应该调用API获取与选中食材相关的菜谱
    const mockRecipes: RecommendedRecipe[] = [];
    
    ingredientsStore.selectedIngredients.forEach(ingredient => {
      // 根据食材名称生成一些模拟的推荐菜谱
      const ingredientName = ingredient.name;
      
      // 为每个选中的食材添加几个推荐菜谱
      const ingredientRecipes: RecommendedRecipe[] = [
        {
          id: mockRecipes.length + 1,
          name: `${ingredientName}炒肉片`,
          image: ingredient.imgUrl || '/hots/1.jpg',
          description: `美味的${ingredientName}炒肉片，家常必备菜品。`
        },
        {
          id: mockRecipes.length + 2,
          name: `${ingredientName}汤`,
          image: ingredient.imgUrl || '/hots/2.jpg',
          description: `营养丰富的${ingredientName}汤，清淡爽口。`
        },
        {
          id: mockRecipes.length + 3,
          name: `${ingredientName}炖豆腐`,
          image: ingredient.imgUrl || '/hots/3.jpg',
          description: `${ingredientName}与豆腐的完美结合，鲜嫩可口。`
        }
      ];
      
      mockRecipes.push(...ingredientRecipes);
    });
    
    ingredientsStore.setRecommendedRecipes(mockRecipes.slice(0, 6)); // 限制显示6个推荐
  };
  
  // 使用computed包装getter，确保响应性
  const currentCategory = computed(() => ingredientsStore.currentCategory);
  const selectedCategory = computed(() => ingredientsStore.selectedCategory);
  
  // 计算每个分类下选中的食材数量
  const getSelectedCountByCategory = (categoryId: string) => {
    // 直接使用ingredientsStore中的ingredients数组，它已经包含了所有食材及其分类信息
    // 这样可以避免临时切换分类导致的界面闪烁问题
    return selectedIngredients.value.filter(ingredient => 
      ingredient.category === categoryId
    ).length;
  };
  
  // 计算每个字母分组下选中的食材数量
  const getSelectedCountByLetter = (categoryId: string, letterId: string) => {
    return selectedIngredients.value.filter(ingredient => 
      ingredient.category === categoryId && ingredient.id.startsWith(letterId)
    ).length;
  };
  
  // 清空所有选中的食材
  const clearAllSelectedIngredients = () => {
    ingredientsStore.clearAllSelectedIngredients();
  };

  return {
    // 返回store中的数据和方法
    categories: ingredientsStore.categories,
    selectedCategory,
    currentCategory,
    setSelectedCategory: ingredientsStore.setSelectedCategory,
    handleIngredientClick,
    toggleIngredientSelection,
    isIngredientSelected,
    selectedIngredients,
    recommendedRecipes,
    getSelectedCountByCategory,
    getSelectedCountByLetter,
    clearAllSelectedIngredients
  };
};