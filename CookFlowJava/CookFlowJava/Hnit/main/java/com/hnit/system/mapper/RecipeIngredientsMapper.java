package com.hnit.system.mapper;

import java.util.List;
import com.hnit.system.domain.RecipeIngredients;

/**
 * 菜谱与食材关联（含数量）Mapper接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface RecipeIngredientsMapper 
{
    /**
     * 查询菜谱与食材关联（含数量）
     * 
     * @param recipeId 菜谱与食材关联（含数量）主键
     * @return 菜谱与食材关联（含数量）
     */
    public RecipeIngredients selectRecipeIngredientsByRecipeId(Long recipeId);

    /**
     * 查询菜谱与食材关联（含数量）列表
     * 
     * @param recipeIngredients 菜谱与食材关联（含数量）
     * @return 菜谱与食材关联（含数量）集合
     */
    public List<RecipeIngredients> selectRecipeIngredientsList(RecipeIngredients recipeIngredients);

    /**
     * 新增菜谱与食材关联（含数量）
     * 
     * @param recipeIngredients 菜谱与食材关联（含数量）
     * @return 结果
     */
    public int insertRecipeIngredients(RecipeIngredients recipeIngredients);

    /**
     * 修改菜谱与食材关联（含数量）
     * 
     * @param recipeIngredients 菜谱与食材关联（含数量）
     * @return 结果
     */
    public int updateRecipeIngredients(RecipeIngredients recipeIngredients);

    /**
     * 删除菜谱与食材关联（含数量）
     * 
     * @param recipeId 菜谱与食材关联（含数量）主键
     * @return 结果
     */
    public int deleteRecipeIngredientsByRecipeId(Long recipeId);

    /**
     * 批量删除菜谱与食材关联（含数量）
     * 
     * @param recipeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRecipeIngredientsByRecipeIds(Long[] recipeIds);
}
