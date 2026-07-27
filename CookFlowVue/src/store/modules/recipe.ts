import { defineStore } from 'pinia';

export interface Recipe {
  id: number;
  name: string;
  description: string;
  image: string;
  ingredients: string[];
  steps: string[];
  category: string;
  prepTime: number;
  cookTime: number;
}

interface RecipeState {
  recipes: Recipe[];
  favoriteRecipes: number[];
  currentRecipe: Recipe | null;
}

export const useRecipeStore = defineStore('recipe', {
  state: (): RecipeState => ({
    recipes: [],
    favoriteRecipes: [],
    currentRecipe: null
  }),
  
  actions: {
    setRecipes(recipes: Recipe[]) {
      this.recipes = recipes;
    },
    
    setCurrentRecipe(recipe: Recipe) {
      this.currentRecipe = recipe;
    },
    
    toggleFavorite(recipeId: number) {
      const index = this.favoriteRecipes.indexOf(recipeId);
      if (index > -1) {
        this.favoriteRecipes.splice(index, 1);
      } else {
        this.favoriteRecipes.push(recipeId);
      }
      // 保存到localStorage
      localStorage.setItem('favoriteRecipes', JSON.stringify(this.favoriteRecipes));
    },
    
    loadFavorites() {
      const saved = localStorage.getItem('favoriteRecipes');
      if (saved) {
        this.favoriteRecipes = JSON.parse(saved);
      }
    }
  },
  
  getters: {
    getRecipeById: (state) => (id: number) => {
      return state.recipes.find(recipe => recipe.id === id);
    },
    
    getFavoriteRecipes: (state) => {
      return state.recipes.filter(recipe => state.favoriteRecipes.includes(recipe.id));
    }
  }
});