package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.RecipeIngredientsMapper;
import com.hnit.system.domain.RecipeIngredients;
import com.hnit.system.service.IRecipeIngredientsService;

/**
 * 菜谱与食材关联（含数量）Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class RecipeIngredientsServiceImpl implements IRecipeIngredientsService 
{
    @Resource
    private RecipeIngredientsMapper recipeIngredientsMapper;

    /**
     * 查询菜谱与食材关联（含数量）
     * 
     * @param recipeId 菜谱与食材关联（含数量）主键
     * @return 菜谱与食材关联（含数量）
     */
    @Override
    public RecipeIngredients selectRecipeIngredientsByRecipeId(Long recipeId)
    {
        return recipeIngredientsMapper.selectRecipeIngredientsByRecipeId(recipeId);
    }

    /**
     * 查询菜谱与食材关联（含数量）列表
     * 
     * @param recipeIngredients 菜谱与食材关联（含数量）
     * @return 菜谱与食材关联（含数量）
     */
    @Override
    public List<RecipeIngredients> selectRecipeIngredientsList(RecipeIngredients recipeIngredients)
    {
        return recipeIngredientsMapper.selectRecipeIngredientsList(recipeIngredients);
    }

    /**
     * 新增菜谱与食材关联（含数量）
     * 
     * @param recipeIngredients 菜谱与食材关联（含数量）
     * @return 结果
     */
    @Override
    public int insertRecipeIngredients(RecipeIngredients recipeIngredients)
    {
        return recipeIngredientsMapper.insertRecipeIngredients(recipeIngredients);
    }

    /**
     * 修改菜谱与食材关联（含数量）
     * 
     * @param recipeIngredients 菜谱与食材关联（含数量）
     * @return 结果
     */
    @Override
    public int updateRecipeIngredients(RecipeIngredients recipeIngredients)
    {
        return recipeIngredientsMapper.updateRecipeIngredients(recipeIngredients);
    }

    /**
     * 批量删除菜谱与食材关联（含数量）
     * 
     * @param recipeIds 需要删除的菜谱与食材关联（含数量）主键
     * @return 结果
     */
    @Override
    public int deleteRecipeIngredientsByRecipeIds(Long[] recipeIds)
    {
        return recipeIngredientsMapper.deleteRecipeIngredientsByRecipeIds(recipeIds);
    }

    /**
     * 删除菜谱与食材关联（含数量）信息
     * 
     * @param recipeId 菜谱与食材关联（含数量）主键
     * @return 结果
     */
    @Override
    public int deleteRecipeIngredientsByRecipeId(Long recipeId)
    {
        return recipeIngredientsMapper.deleteRecipeIngredientsByRecipeId(recipeId);
    }
}
