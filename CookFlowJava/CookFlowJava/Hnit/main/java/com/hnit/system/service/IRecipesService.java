package com.hnit.system.service;

import java.util.List;
import com.hnit.system.domain.Recipes;

/**
 * 菜谱Service接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface IRecipesService 
{
    /**
     * 查询菜谱
     * 
     * @param id 菜谱主键
     * @return 菜谱
     */
    public Recipes selectRecipesById(Long id);

    /**
     * 查询菜谱列表
     * 
     * @param recipes 菜谱
     * @return 菜谱集合
     */
    public List<Recipes> selectRecipesList(Recipes recipes);

    /**
     * 新增菜谱
     * 
     * @param recipes 菜谱
     * @return 结果
     */
    public int insertRecipes(Recipes recipes);

    /**
     * 修改菜谱
     * 
     * @param recipes 菜谱
     * @return 结果
     */
    public int updateRecipes(Recipes recipes);

    /**
     * 批量删除菜谱
     * 
     * @param ids 需要删除的菜谱主键集合
     * @return 结果
     */
    public int deleteRecipesByIds(Long[] ids);

    /**
     * 删除菜谱信息
     * 
     * @param id 菜谱主键
     * @return 结果
     */
    public int deleteRecipesById(Long id);
}
