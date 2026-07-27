package com.hnit.system.service;

import java.util.List;
import com.hnit.system.domain.RecipeSteps;

/**
 * 菜谱步骤（有序）Service接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface IRecipeStepsService 
{
    /**
     * 查询菜谱步骤（有序）
     * 
     * @param id 菜谱步骤（有序）主键
     * @return 菜谱步骤（有序）
     */
    public RecipeSteps selectRecipeStepsById(Long id);

    /**
     * 查询菜谱步骤（有序）列表
     * 
     * @param recipeSteps 菜谱步骤（有序）
     * @return 菜谱步骤（有序）集合
     */
    public List<RecipeSteps> selectRecipeStepsList(RecipeSteps recipeSteps);

    /**
     * 新增菜谱步骤（有序）
     * 
     * @param recipeSteps 菜谱步骤（有序）
     * @return 结果
     */
    public int insertRecipeSteps(RecipeSteps recipeSteps);

    /**
     * 修改菜谱步骤（有序）
     * 
     * @param recipeSteps 菜谱步骤（有序）
     * @return 结果
     */
    public int updateRecipeSteps(RecipeSteps recipeSteps);

    /**
     * 批量删除菜谱步骤（有序）
     * 
     * @param ids 需要删除的菜谱步骤（有序）主键集合
     * @return 结果
     */
    public int deleteRecipeStepsByIds(Long[] ids);

    /**
     * 删除菜谱步骤（有序）信息
     * 
     * @param id 菜谱步骤（有序）主键
     * @return 结果
     */
    public int deleteRecipeStepsById(Long id);
}
