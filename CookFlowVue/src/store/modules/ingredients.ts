import { defineStore } from 'pinia';
import type { Ingredient, RecommendedRecipe } from '@/scripts/ingredientsViewLogic';

// 食材分类数据
// 按新格式组织：分类下包含按字母分组的食材列表
export const categories = [
  {
    id: 1,
    name: '蔬菜',
    ingredients: [
      { id: 'b', names: [{ id: '1', name: '白菜', imgUrl: '/hots/1.jpg' }, { id: '2', name: '菠菜', imgUrl: '/hots/2.jpg' }] },
      { id: 'd', names: [{ id: '3', name: '大蒜', imgUrl: '/hots/3.jpg' }, { id: '4', name: '冬瓜', imgUrl: '/hots/1.jpg' }] },
      { id: 'h', names: [{ id: '5', name: '胡萝卜', imgUrl: '/hots/2.jpg' }, { id: '6', name: '黄瓜', imgUrl: '/hots/3.jpg' }] },
      { id: 'j', names: [{ id: '7', name: '姜', imgUrl: '/hots/1.jpg' }, { id: '8', name: '韭菜', imgUrl: '/hots/2.jpg' }] },
      { id: 'k', names: [{ id: '9', name: '苦瓜', imgUrl: '/hots/3.jpg' }] },
      { id: 'm', names: [{ id: '10', name: '蘑菇', imgUrl: '/hots/1.jpg' }] },
      { id: 'n', names: [{ id: '11', name: '南瓜', imgUrl: '/hots/2.jpg' }] },
      { id: 'q', names: [{ id: '12', name: '茄子', imgUrl: '/hots/3.jpg' }, { id: '13', name: '青椒', imgUrl: '/hots/1.jpg' }, { id: '14', name: '芹菜', imgUrl: '/hots/2.jpg' }] },
      { id: 's', names: [{ id: '15', name: '生菜', imgUrl: '/hots/3.jpg' }, { id: '16', name: '丝瓜', imgUrl: '/hots/1.jpg' }] },
      { id: 't', names: [{ id: '17', name: '土豆', imgUrl: '/hots/2.jpg' }] },
      { id: 'x', names: [{ id: '18', name: '西红柿', imgUrl: '/hots/3.jpg' }, { id: '19', name: '西兰花', imgUrl: '/hots/1.jpg' }] },
      { id: 'y', names: [{ id: '20', name: '洋葱', imgUrl: '/hots/2.jpg' }] }
    ]
  },
  {
    id: 2,
    name: '水果',
    ingredients: [
      { id: 'b', names: [{ id: '21', name: '菠萝', imgUrl: '/hots/3.jpg' }] },
      { id: 'c', names: [{ id: '22', name: '橙子', imgUrl: '/hots/1.jpg' }, { id: '23', name: '草莓', imgUrl: '/hots/2.jpg' }] },
      { id: 'h', names: [{ id: '24', name: '哈密瓜', imgUrl: '/hots/3.jpg' }, { id: '25', name: '火龙果', imgUrl: '/hots/1.jpg' }] },
      { id: 'l', names: [{ id: '26', name: '梨', imgUrl: '/hots/2.jpg' }, { id: '27', name: '荔枝', imgUrl: '/hots/3.jpg' }, { id: '28', name: '龙眼', imgUrl: '/hots/1.jpg' }, { id: '29', name: '蓝莓', imgUrl: '/hots/2.jpg' }] },
      { id: 'm', names: [{ id: '30', name: '芒果', imgUrl: '/hots/3.jpg' }, { id: '31', name: '猕猴桃', imgUrl: '/hots/1.jpg' }] },
      { id: 'n', names: [{ id: '32', name: '柠檬', imgUrl: '/hots/2.jpg' }] },
      { id: 'p', names: [{ id: '33', name: '苹果', imgUrl: '/hots/3.jpg' }, { id: '34', name: '葡萄', imgUrl: '/hots/1.jpg' }] },
      { id: 's', names: [{ id: '35', name: '石榴', imgUrl: '/hots/2.jpg' }, { id: '36', name: '山竹', imgUrl: '/hots/3.jpg' }] },
      { id: 't', names: [{ id: '37', name: '桃子', imgUrl: '/hots/1.jpg' }] },
      { id: 'x', names: [{ id: '38', name: '香蕉', imgUrl: '/hots/2.jpg' }, { id: '39', name: '西瓜', imgUrl: '/hots/3.jpg' }] },
      { id: 'y', names: [{ id: '40', name: '樱桃', imgUrl: '/hots/1.jpg' }] }
    ]
  },
  {
    id: 3,
    name: '肉类',
    ingredients: [
      { id: 'e', names: [{ id: '41', name: '鹅肉', imgUrl: '/hots/2.jpg' }] },
      { id: 'h', names: [{ id: '42', name: '火腿', imgUrl: '/hots/3.jpg' }] },
      { id: 'j', names: [{ id: '43', name: '鸡肉', imgUrl: '/hots/1.jpg' }, { id: '44', name: '鸡翅', imgUrl: '/hots/2.jpg' }, { id: '45', name: '鸡腿', imgUrl: '/hots/3.jpg' }, { id: '46', name: '鸡胸肉', imgUrl: '/hots/1.jpg' }] },
      { id: 'l', names: [{ id: '47', name: '里脊肉', imgUrl: '/hots/2.jpg' }, { id: '48', name: '腊肉', imgUrl: '/hots/3.jpg' }] },
      { id: 'n', names: [{ id: '49', name: '牛肉', imgUrl: '/hots/1.jpg' }, { id: '50', name: '牛排', imgUrl: '/hots/2.jpg' }, { id: '51', name: '牛肉干', imgUrl: '/hots/3.jpg' }] },
      { id: 'p', names: [{ id: '52', name: '培根', imgUrl: '/hots/1.jpg' }] },
      { id: 'r', names: [{ id: '53', name: '肉丸', imgUrl: '/hots/2.jpg' }] },
      { id: 't', names: [{ id: '54', name: '兔肉', imgUrl: '/hots/3.jpg' }] },
      { id: 'w', names: [{ id: '55', name: '五花肉', imgUrl: '/hots/1.jpg' }] },
      { id: 'x', names: [{ id: '56', name: '香肠', imgUrl: '/hots/2.jpg' }] },
      { id: 'y', names: [{ id: '57', name: '羊肉', imgUrl: '/hots/3.jpg' }, { id: '58', name: '鸭肉', imgUrl: '/hots/1.jpg' }] },
      { id: 'z', names: [{ id: '59', name: '猪肉', imgUrl: '/hots/2.jpg' }, { id: '60', name: '猪排', imgUrl: '/hots/3.jpg' }] }
    ]
  },
  {
    id: 4,
    name: '水产',
    ingredients: [
      { id: 'b', names: [{ id: '61', name: '贝类', imgUrl: '/hots/1.jpg' }, { id: '62', name: '鲍鱼', imgUrl: '/hots/2.jpg' }] },
      { id: 'c', names: [{ id: '63', name: '草鱼', imgUrl: '/hots/3.jpg' }] },
      { id: 'g', names: [{ id: '64', name: '蛤蜊', imgUrl: '/hots/1.jpg' }] },
      { id: 'h', names: [{ id: '65', name: '海带', imgUrl: '/hots/2.jpg' }, { id: '66', name: '海参', imgUrl: '/hots/3.jpg' }] },
      { id: 'j', names: [{ id: '67', name: '鲫鱼', imgUrl: '/hots/1.jpg' }, { id: '68', name: '金枪鱼', imgUrl: '/hots/2.jpg' }] },
      { id: 'l', names: [{ id: '69', name: '鲤鱼', imgUrl: '/hots/3.jpg' }, { id: '70', name: '鲈鱼', imgUrl: '/hots/1.jpg' }, { id: '71', name: '龙利鱼', imgUrl: '/hots/2.jpg' }] },
      { id: 's', names: [{ id: '72', name: '三文鱼', imgUrl: '/hots/3.jpg' }, { id: '73', name: '扇贝', imgUrl: '/hots/1.jpg' }, { id: '74', name: '生蚝', imgUrl: '/hots/2.jpg' }] },
      { id: 'x', names: [{ id: '75', name: '虾', imgUrl: '/hots/3.jpg' }, { id: '76', name: '蟹', imgUrl: '/hots/1.jpg' }, { id: '77', name: '鳕鱼', imgUrl: '/hots/2.jpg' }] },
      { id: 'y', names: [{ id: '78', name: '鱿鱼', imgUrl: '/hots/3.jpg' }] },
      { id: 'z', names: [{ id: '79', name: '章鱼', imgUrl: '/hots/1.jpg' }, { id: '80', name: '紫菜', imgUrl: '/hots/2.jpg' }] }
    ]
  },
  {
    id: 5,
    name: '面食',
    ingredients: [
      { id: 'b', names: [{ id: '81', name: '包子', imgUrl: '/hots/3.jpg' }, { id: '82', name: '饼', imgUrl: '/hots/1.jpg' }, { id: '83', name: '面包', imgUrl: '/hots/2.jpg' }] },
      { id: 'c', names: [{ id: '84', name: '春卷', imgUrl: '/hots/3.jpg' }] },
      { id: 'h', names: [{ id: '85', name: '馄饨', imgUrl: '/hots/1.jpg' }, { id: '86', name: '汉堡', imgUrl: '/hots/2.jpg' }] },
      { id: 'j', names: [{ id: '87', name: '饺子', imgUrl: '/hots/3.jpg' }] },
      { id: 'm', names: [{ id: '88', name: '面条', imgUrl: '/hots/1.jpg' }, { id: '89', name: '米饭', imgUrl: '/hots/2.jpg' }, { id: '90', name: '馒头', imgUrl: '/hots/3.jpg' }, { id: '91', name: '麻花', imgUrl: '/hots/1.jpg' }, { id: '92', name: '米粉', imgUrl: '/hots/2.jpg' }] },
      { id: 'n', names: [{ id: '93', name: '年糕', imgUrl: '/hots/3.jpg' }] },
      { id: 'p', names: [{ id: '94', name: '披萨', imgUrl: '/hots/1.jpg' }] },
      { id: 's', names: [{ id: '95', name: '三明治', imgUrl: '/hots/2.jpg' }, { id: '96', name: '寿司', imgUrl: '/hots/3.jpg' }] },
      { id: 't', names: [{ id: '97', name: '汤圆', imgUrl: '/hots/1.jpg' }] },
      { id: 'y', names: [{ id: '98', name: '油条', imgUrl: '/hots/2.jpg' }, { id: '99', name: '意面', imgUrl: '/hots/3.jpg' }] },
      { id: 'z', names: [{ id: '100', name: '粽子', imgUrl: '/hots/1.jpg' }] }
    ]
  },
  {
    id: 6,
    name: '干货',
    ingredients: [
      { id: 'g', names: [{ id: '101', name: '桂圆', imgUrl: '/hots/2.jpg' }, { id: '102', name: '枸杞', imgUrl: '/hots/3.jpg' }, { id: '103', name: '瓜子', imgUrl: '/hots/1.jpg' }] },
      { id: 'h', names: [{ id: '104', name: '红枣', imgUrl: '/hots/2.jpg' }, { id: '105', name: '核桃', imgUrl: '/hots/3.jpg' }, { id: '106', name: '花生', imgUrl: '/hots/1.jpg' }, { id: '107', name: '红豆', imgUrl: '/hots/2.jpg' }, { id: '108', name: '黄豆', imgUrl: '/hots/3.jpg' }, { id: '109', name: '黑豆', imgUrl: '/hots/1.jpg' }] },
      { id: 'k', names: [{ id: '110', name: '开心果', imgUrl: '/hots/2.jpg' }] },
      { id: 'l', names: [{ id: '111', name: '莲子', imgUrl: '/hots/3.jpg' }, { id: '112', name: '绿豆', imgUrl: '/hots/1.jpg' }] },
      { id: 'm', names: [{ id: '113', name: '木耳', imgUrl: '/hots/2.jpg' }, { id: '114', name: '蜜枣', imgUrl: '/hots/3.jpg' }] },
      { id: 'p', names: [{ id: '115', name: '葡萄干', imgUrl: '/hots/1.jpg' }] },
      { id: 's', names: [{ id: '116', name: '柿饼', imgUrl: '/hots/2.jpg' }] },
      { id: 'x', names: [{ id: '117', name: '香菇', imgUrl: '/hots/3.jpg' }, { id: '118', name: '杏仁', imgUrl: '/hots/1.jpg' }] },
      { id: 'y', names: [{ id: '119', name: '银耳', imgUrl: '/hots/2.jpg' }, { id: '120', name: '腰果', imgUrl: '/hots/3.jpg' }] }
    ]
  },
  {
    id: 7,
    name: '调味',
    ingredients: [
      { id: 'b', names: [{ id: '121', name: '八角', imgUrl: '/hots/1.jpg' }] },
      { id: 'c', names: [{ id: '122', name: '醋', imgUrl: '/hots/2.jpg' }] },
      { id: 'd', names: [{ id: '123', name: '豆瓣酱', imgUrl: '/hots/3.jpg' }, { id: '124', name: '豆腐乳', imgUrl: '/hots/1.jpg' }] },
      { id: 'f', names: [{ id: '125', name: '番茄酱', imgUrl: '/hots/2.jpg' }, { id: '126', name: '腐乳', imgUrl: '/hots/3.jpg' }] },
      { id: 'g', names: [{ id: '127', name: '橄榄油', imgUrl: '/hots/1.jpg' }, { id: '128', name: '桂皮', imgUrl: '/hots/2.jpg' }, { id: '129', name: '干辣椒', imgUrl: '/hots/3.jpg' }] },
      { id: 'h', names: [{ id: '130', name: '花椒', imgUrl: '/hots/1.jpg' }, { id: '131', name: '胡椒', imgUrl: '/hots/2.jpg' }] },
      { id: 'j', names: [{ id: '132', name: '酱油', imgUrl: '/hots/3.jpg' }, { id: '133', name: '芥末', imgUrl: '/hots/1.jpg' }, { id: '134', name: '鸡精', imgUrl: '/hots/2.jpg' }] },
      { id: 'l', names: [{ id: '135', name: '料酒', imgUrl: '/hots/3.jpg' }, { id: '136', name: '辣椒酱', imgUrl: '/hots/1.jpg' }] },
      { id: 't', names: [{ id: '137', name: '糖', imgUrl: '/hots/2.jpg' }] },
      { id: 'x', names: [{ id: '138', name: '香油', imgUrl: '/hots/3.jpg' }, { id: '139', name: '香叶', imgUrl: '/hots/1.jpg' }] },
      { id: 'y', names: [{ id: '140', name: '盐', imgUrl: '/hots/2.jpg' }] },
      { id: 'z', names: [{ id: '141', name: '孜然', imgUrl: '/hots/3.jpg' }] }
    ]
  }
];

// 生成增强的食材数组（包含所有必要属性）
const generateEnhancedIngredients = () => {
  return categories.flatMap(category =>
    category.ingredients.flatMap(ingredientGroup =>
      ingredientGroup.names.map((ingredient, index) => ({
        id: `${ingredientGroup.id}-${index}`, // 使用字母ID加索引确保唯一性
        name: ingredient.name,
        imgUrl: ingredient.imgUrl,
        image: ingredient.imgUrl, // 保持兼容性
        category: category.id.toString(),
        letter: ingredientGroup.id
      }))
    )
  );
};

export const useIngredientsStore = defineStore('ingredients', {
  state: () => ({
    selectedCategory: '1', // 更新为数字ID
    ingredients: generateEnhancedIngredients(),
    categories: categories,
    selectedIngredients: [] as Ingredient[],
    recommendedRecipes: [] as RecommendedRecipe[]
  }),

  getters: {
    currentCategory(): typeof categories[0] | undefined {
      return categories.find((cat) => cat.id.toString() === this.selectedCategory);
    },

    groupedIngredients(): Record<string, Array<{ image: string; id: string; name: string; imgUrl: string; category: string; letter: string }>> {
      const result: Record<string, Array<{ image: string; id: string; name: string; imgUrl: string; category: string; letter: string }>> = {};
      
      // 直接从当前选中的分类计算分组，提高性能
      const currentCat = this.currentCategory;
      if (!currentCat) return {};
      
      // 直接从分类数据生成，避免从整个扁平化数组中过滤
      currentCat.ingredients.forEach(ingredientGroup => {
        const letter = ingredientGroup.id;
        result[letter] = ingredientGroup.names.map((ingredient, index) => ({
          id: `${letter}-${index}`,
          name: ingredient.name,
          imgUrl: ingredient.imgUrl,
          image: ingredient.imgUrl,
          category: currentCat.id.toString(),
          letter
        }));
      });

      // 按字母顺序排序
      const sortedResult: Record<string, Array<{ image: string; id: string; name: string; imgUrl: string; category: string; letter: string }>> = {};
      Object.keys(result).sort().forEach(letter => {
        sortedResult[letter] = result[letter]!;
      });

      return sortedResult;
    }
  },

  actions: {
    setSelectedCategory(categoryId: string) {
      this.selectedCategory = categoryId;
    },
    
    toggleIngredientSelection(ingredient: Ingredient) {
      const index = this.selectedIngredients.findIndex(item => item.id === ingredient.id);
      
      if (index > -1) {
        // 如果已选中，则取消选中
        this.selectedIngredients.splice(index, 1);
      } else {
        // 如果未选中，则添加选中
        this.selectedIngredients.push(ingredient);
      }
    },
    
    clearAllSelectedIngredients() {
      this.selectedIngredients = [];
      this.recommendedRecipes = [];
    },
    
    setRecommendedRecipes(recipes: RecommendedRecipe[]) {
      this.recommendedRecipes = recipes;
    }
  }
});