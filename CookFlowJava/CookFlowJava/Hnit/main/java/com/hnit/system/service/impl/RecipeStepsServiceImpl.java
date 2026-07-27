package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.RecipeStepsMapper;
import com.hnit.system.domain.RecipeSteps;
import com.hnit.system.service.IRecipeStepsService;

/**
 * 菜谱步骤（有序）Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class RecipeStepsServiceImpl implements IRecipeStepsService 
{
    @Resource
    private RecipeStepsMapper recipeStepsMapper;

    /**
     * 查询菜谱步骤（有序）
     * 
     * @param id 菜谱步骤（有序）主键
     * @return 菜谱步骤（有序）
     */
    @Override
    public RecipeSteps selectRecipeStepsById(Long id)
    {
        return recipeStepsMapper.selectRecipeStepsById(id);
    }

    /**
     * 查询菜谱步骤（有序）列表
     * 
     * @param recipeSteps 菜谱步骤（有序）
     * @return 菜谱步骤（有序）
     */
    @Override
    public List<RecipeSteps> selectRecipeStepsList(RecipeSteps recipeSteps)
    {
        return recipeStepsMapper.selectRecipeStepsList(recipeSteps);
    }

    /**
     * 新增菜谱步骤（有序）
     * 
     * @param recipeSteps 菜谱步骤（有序）
     * @return 结果
     */
    @Override
    public int insertRecipeSteps(RecipeSteps recipeSteps)
    {
        return recipeStepsMapper.insertRecipeSteps(recipeSteps);
    }

    /**
     * 修改菜谱步骤（有序）
     * 
     * @param recipeSteps 菜谱步骤（有序）
     * @return 结果
     */
    @Override
    public int updateRecipeSteps(RecipeSteps recipeSteps)
    {
        return recipeStepsMapper.updateRecipeSteps(recipeSteps);
    }

    /**
     * 批量删除菜谱步骤（有序）
     * 
     * @param ids 需要删除的菜谱步骤（有序）主键
     * @return 结果
     */
    @Override
    public int deleteRecipeStepsByIds(Long[] ids)
    {
        return recipeStepsMapper.deleteRecipeStepsByIds(ids);
    }

    /**
     * 删除菜谱步骤（有序）信息
     * 
     * @param id 菜谱步骤（有序）主键
     * @return 结果
     */
    @Override
    public int deleteRecipeStepsById(Long id)
    {
        return recipeStepsMapper.deleteRecipeStepsById(id);
    }
}
