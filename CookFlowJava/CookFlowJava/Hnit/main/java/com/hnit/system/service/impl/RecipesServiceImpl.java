package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.RecipesMapper;
import com.hnit.system.domain.Recipes;
import com.hnit.system.service.IRecipesService;

/**
 * 菜谱Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class RecipesServiceImpl implements IRecipesService 
{
    @Resource
    private RecipesMapper recipesMapper;

    /**
     * 查询菜谱
     * 
     * @param id 菜谱主键
     * @return 菜谱
     */
    @Override
    public Recipes selectRecipesById(Long id)
    {
        return recipesMapper.selectRecipesById(id);
    }

    /**
     * 查询菜谱列表
     * 
     * @param recipes 菜谱
     * @return 菜谱
     */
    @Override
    public List<Recipes> selectRecipesList(Recipes recipes)
    {
        return recipesMapper.selectRecipesList(recipes);
    }

    /**
     * 新增菜谱
     * 
     * @param recipes 菜谱
     * @return 结果
     */
    @Override
    public int insertRecipes(Recipes recipes)
    {
        return recipesMapper.insertRecipes(recipes);
    }

    /**
     * 修改菜谱
     * 
     * @param recipes 菜谱
     * @return 结果
     */
    @Override
    public int updateRecipes(Recipes recipes)
    {
        return recipesMapper.updateRecipes(recipes);
    }

    /**
     * 批量删除菜谱
     * 
     * @param ids 需要删除的菜谱主键
     * @return 结果
     */
    @Override
    public int deleteRecipesByIds(Long[] ids)
    {
        return recipesMapper.deleteRecipesByIds(ids);
    }

    /**
     * 删除菜谱信息
     * 
     * @param id 菜谱主键
     * @return 结果
     */
    @Override
    public int deleteRecipesById(Long id)
    {
        return recipesMapper.deleteRecipesById(id);
    }
}
