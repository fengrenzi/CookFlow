package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.RecipeStatsMapper;
import com.hnit.system.domain.RecipeStats;
import com.hnit.system.service.IRecipeStatsService;

/**
 * 菜谱聚合统计，用于排行榜Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class RecipeStatsServiceImpl implements IRecipeStatsService 
{
    @Resource
    private RecipeStatsMapper recipeStatsMapper;

    /**
     * 查询菜谱聚合统计，用于排行榜
     * 
     * @param recipeId 菜谱聚合统计，用于排行榜主键
     * @return 菜谱聚合统计，用于排行榜
     */
    @Override
    public RecipeStats selectRecipeStatsByRecipeId(Long recipeId)
    {
        return recipeStatsMapper.selectRecipeStatsByRecipeId(recipeId);
    }

    /**
     * 查询菜谱聚合统计，用于排行榜列表
     * 
     * @param recipeStats 菜谱聚合统计，用于排行榜
     * @return 菜谱聚合统计，用于排行榜
     */
    @Override
    public List<RecipeStats> selectRecipeStatsList(RecipeStats recipeStats)
    {
        return recipeStatsMapper.selectRecipeStatsList(recipeStats);
    }

    /**
     * 新增菜谱聚合统计，用于排行榜
     * 
     * @param recipeStats 菜谱聚合统计，用于排行榜
     * @return 结果
     */
    @Override
    public int insertRecipeStats(RecipeStats recipeStats)
    {
        return recipeStatsMapper.insertRecipeStats(recipeStats);
    }

    /**
     * 修改菜谱聚合统计，用于排行榜
     * 
     * @param recipeStats 菜谱聚合统计，用于排行榜
     * @return 结果
     */
    @Override
    public int updateRecipeStats(RecipeStats recipeStats)
    {
        return recipeStatsMapper.updateRecipeStats(recipeStats);
    }

    /**
     * 批量删除菜谱聚合统计，用于排行榜
     * 
     * @param recipeIds 需要删除的菜谱聚合统计，用于排行榜主键
     * @return 结果
     */
    @Override
    public int deleteRecipeStatsByRecipeIds(Long[] recipeIds)
    {
        return recipeStatsMapper.deleteRecipeStatsByRecipeIds(recipeIds);
    }

    /**
     * 删除菜谱聚合统计，用于排行榜信息
     * 
     * @param recipeId 菜谱聚合统计，用于排行榜主键
     * @return 结果
     */
    @Override
    public int deleteRecipeStatsByRecipeId(Long recipeId)
    {
        return recipeStatsMapper.deleteRecipeStatsByRecipeId(recipeId);
    }
}
