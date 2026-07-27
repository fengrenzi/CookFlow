import { defineStore } from 'pinia';

// 购物车项基础接口
interface CartItemBase {
  id: string;
  type: 'recipe' | 'book';
  addedTime: Date;
  ingredients: Ingredient[];
  quantity: number;
}

// 食材接口
interface Ingredient {
  id: string;
  name: string;
  unit: string;
  amount: number;
  imageUrl: string;
}

// 菜谱类型购物车项
interface RecipeCartItem extends CartItemBase {
  type: 'recipe';
  recipeId: string;
  recipeName: string;
  recipeImageUrl: string;
}

// 书籍类型购物车项
interface BookCartItem extends CartItemBase {
  type: 'book';
  bookId: string;
  bookName: string;
  bookImageUrl: string;
  pageNumber: number;
  recipeName: string;
  recipeImageUrl: string;
}

// 购物车项类型联合
type CartItem = RecipeCartItem | BookCartItem;

interface CartState {
  items: CartItem[];
}

export const useCartStore = defineStore('cart', {
  state: (): CartState => ({
    items: [],
  }),
  
  getters: {
    // 获取所有购物车项
    allItems: (state) => state.items,
    
    // 获取购物车项数量
    itemCount: (state) => state.items.length,
    
    // 获取购物车中所有食材的总列表（去重并合并数量）
    allIngredients: (state) => {
      const ingredientMap = new Map<string, { item: Ingredient; totalAmount: number }>();
      
      state.items.forEach(item => {
        item.ingredients.forEach(ingredient => {
          const key = ingredient.id;
          if (ingredientMap.has(key)) {
            const existing = ingredientMap.get(key)!;
            existing.totalAmount += ingredient.amount * item.quantity;
          } else {
            ingredientMap.set(key, {
              item: { ...ingredient },
              totalAmount: ingredient.amount * item.quantity
            });
          }
        });
      });
      
      return Array.from(ingredientMap.values()).map(({ item, totalAmount }) => ({
        ...item,
        amount: totalAmount
      }));
    }
  },
  
  actions: {
    // 添加菜谱到购物车
    addRecipeToCart(recipeId: string, recipeName: string, recipeImageUrl: string, ingredients: Ingredient[], quantity: number = 1) {
      // 检查是否已存在相同的菜谱
      const existingIndex = this.items.findIndex(
        item => item.type === 'recipe' && item.recipeId === recipeId
      );
      
      if (existingIndex >= 0) {
        // 如果已存在，则增加数量
        this.items[existingIndex]!.quantity += quantity;
      } else {
        // 否则添加新项
        const newItem: RecipeCartItem = {
          id: `recipe-${Date.now()}`,
          type: 'recipe',
          recipeId,
          recipeName,
          recipeImageUrl,
          ingredients,
          quantity,
          addedTime: new Date()
        };
        this.items.push(newItem);
      }
      
      this.saveToLocalStorage();
    },
    
    // 添加书籍中的菜谱到购物车
    addBookRecipeToCart(bookId: string, bookName: string, bookImageUrl: string, pageNumber: number, recipeName: string, recipeImageUrl: string, ingredients: Ingredient[], quantity: number = 1) {
      // 检查是否已存在相同的书籍菜谱
      const existingIndex = this.items.findIndex(
        item => item.type === 'book' && item.bookId === bookId && item.recipeName === recipeName
      );
      
      if (existingIndex >= 0) {
        // 如果已存在，则增加数量
        this.items[existingIndex]!.quantity += quantity;
      } else {
        // 否则添加新项
        const newItem: BookCartItem = {
          id: `book-${Date.now()}`,
          type: 'book',
          bookId,
          bookName,
          bookImageUrl,
          pageNumber,
          recipeName,
          recipeImageUrl,
          ingredients,
          quantity,
          addedTime: new Date()
        };
        this.items.push(newItem);
      }
      
      this.saveToLocalStorage();
    },
    
    // 从购物车移除项
    removeFromCart(itemId: string) {
      this.items = this.items.filter(item => item.id !== itemId);
      this.saveToLocalStorage();
    },
    
    // 更新购物车项数量
    updateQuantity(itemId: string, quantity: number) {
      if (quantity <= 0) {
        this.removeFromCart(itemId);
        return;
      }
      
      const item = this.items.find(item => item.id === itemId);
      if (item) {
        item.quantity = quantity;
        this.saveToLocalStorage();
      }
    },
    
    // 清空购物车
    clearCart() {
      this.items = [];
      this.saveToLocalStorage();
    },
    
    // 保存到localStorage
    saveToLocalStorage() {
      localStorage.setItem('cartItems', JSON.stringify(this.items));
    },
    
    // 从localStorage加载
    loadFromLocalStorage() {
      const savedItems = localStorage.getItem('cartItems');
      if (savedItems) {
        try {
          const parsedItems = JSON.parse(savedItems);
          // 将日期字符串转换回Date对象
          this.items = parsedItems.map((item: any) => ({
            ...item,
            addedTime: new Date(item.addedTime)
          }));
        } catch (error) {
          console.error('Failed to load cart items from localStorage', error);
        }
      }
    },
    
    // 生成购物清单文本
    generateShoppingListText() {
      const ingredients = this.allIngredients;
      let text = '购物清单\n';
      text += `生成时间: ${new Date().toLocaleString()}\n\n`;

      ingredients.forEach(ingredient => {
        text += `- ${ingredient.name}: ${ingredient.amount} ${ingredient.unit}\n`;
      });

      text += `\n共 ${ingredients.length} 种食材`;
      return text;
    }
  }
});