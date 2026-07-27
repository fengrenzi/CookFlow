package com.hnit.system.service;

import com.hnit.system.domain.Recipes;
import com.hnit.system.domain.dto.RecipeQueryDto;
import com.hnit.system.domain.dto.RecipesDto;

import java.util.List;

public interface IRecipesService {
    List<Recipes> selectRecipesList(RecipeQueryDto query);

    Recipes selectRecipesById(String id);

    int insertRecipes(Recipes recipes);

    int updateRecipes(Recipes recipes);

    int deleteRecipesByIds(String[] ids);

    // 新增方法：返回 DTO 类型，用于 Controller 直接返回
    List<RecipesDto> selectRecipesListWithCondition(RecipeQueryDto query);

    // 新增：热门菜谱、推荐菜谱、今日推荐（只返回主图）
    List<RecipesDto> selectHotRecipes(int limit);
    List<RecipesDto> selectRecommendedRecipes(int limit);
    List<RecipesDto> selectTodayRecommends(int limit);

    // 新增：获取菜谱所有图片（用于轮播图）
    List<String> selectImagesByRecipeId(String recipeId);

    RecipesDto selectRecipesDetailById(String id);

    List<RecipesDto> getRecommendedRecipesByIngredients(List<String> ingredientIds);
}