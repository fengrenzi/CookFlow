package com.hnit.system.service;

import java.util.List;
import com.hnit.system.domain.RecipeStats;

/**
 * 菜谱聚合统计，用于排行榜Service接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface IRecipeStatsService 
{
    /**
     * 查询菜谱聚合统计，用于排行榜
     * 
     * @param recipeId 菜谱聚合统计，用于排行榜主键
     * @return 菜谱聚合统计，用于排行榜
     */
    public RecipeStats selectRecipeStatsByRecipeId(Long recipeId);

    /**
     * 查询菜谱聚合统计，用于排行榜列表
     * 
     * @param recipeStats 菜谱聚合统计，用于排行榜
     * @return 菜谱聚合统计，用于排行榜集合
     */
    public List<RecipeStats> selectRecipeStatsList(RecipeStats recipeStats);

    /**
     * 新增菜谱聚合统计，用于排行榜
     * 
     * @param recipeStats 菜谱聚合统计，用于排行榜
     * @return 结果
     */
    public int insertRecipeStats(RecipeStats recipeStats);

    /**
     * 修改菜谱聚合统计，用于排行榜
     * 
     * @param recipeStats 菜谱聚合统计，用于排行榜
     * @return 结果
     */
    public int updateRecipeStats(RecipeStats recipeStats);

    /**
     * 批量删除菜谱聚合统计，用于排行榜
     * 
     * @param recipeIds 需要删除的菜谱聚合统计，用于排行榜主键集合
     * @return 结果
     */
    public int deleteRecipeStatsByRecipeIds(Long[] recipeIds);

    /**
     * 删除菜谱聚合统计，用于排行榜信息
     * 
     * @param recipeId 菜谱聚合统计，用于排行榜主键
     * @return 结果
     */
    public int deleteRecipeStatsByRecipeId(Long recipeId);
}
