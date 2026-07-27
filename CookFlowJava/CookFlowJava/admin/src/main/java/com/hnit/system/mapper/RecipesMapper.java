package com.hnit.system.mapper;

import com.hnit.system.domain.Recipes;
import com.hnit.system.domain.dto.RecipeQueryDto;
import com.hnit.system.domain.dto.RecipesDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecipesMapper {
    List<Recipes> selectRecipesList(RecipeQueryDto query);

    Recipes selectRecipesById(String id);

    int insertRecipes(Recipes recipes);

    int updateRecipes(Recipes recipes);

    int deleteRecipesByIds(String[] ids);

    // 热门菜谱（只返回一张主图）
    List<RecipesDto> selectHotRecipes(@Param("limit") int limit, @Param("imgBaseUrl") String imgBaseUrl);

    // 推荐菜谱（只返回一张主图）
    List<RecipesDto> selectRecommendedRecipes(@Param("limit") int limit, @Param("imgBaseUrl") String imgBaseUrl);

    // 今日推荐（只返回一张主图）
    List<RecipesDto> selectTodayRecommends(@Param("limit") int limit, @Param("imgBaseUrl") String imgBaseUrl);

    // 查询某个菜谱的所有关联图片（用于轮播图）
    List<String> selectImagesByRecipeId(@Param("recipeId") String recipeId, @Param("imgBaseUrl") String imgBaseUrl);

    /**
     * 分页查询菜谱，返回 DTO（含作者信息、图片、分类）
     */
    List<RecipesDto> selectRecipesDtoList(@Param("query") RecipeQueryDto query,
                                          @Param("imgBaseUrl") String imgBaseUrl);

    RecipesDto selectRecipesDetailById(@Param("id") String id, @Param("imgBaseUrl") String imgBaseUrl);

    List<RecipesDto> selectRecommendedRecipesByIngredients(@Param("ingredientIds") List<String> ingredientIds,
                                                           @Param("imgBaseUrl") String imgBaseUrl);

    Recipes selectById(@Param("id") Long id);

    // 根据用户ID查询发布的菜谱
    List<Recipes> selectByUserId(@Param("userId") Long userId);

    // 根据ID列表批量查询菜谱
    List<Recipes> selectByIds(@Param("ids") List<String> ids);
}