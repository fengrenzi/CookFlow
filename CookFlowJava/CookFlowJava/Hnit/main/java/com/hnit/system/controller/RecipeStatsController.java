package com.hnit.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hnit.common.annotation.Log;
import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.common.enums.BusinessType;
import com.hnit.system.domain.RecipeStats;
import com.hnit.system.service.IRecipeStatsService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 菜谱聚合统计，用于排行榜Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/RecipeStats")
public class RecipeStatsController extends BaseController
{
    @Resource
    private IRecipeStatsService recipeStatsService;

    /**
     * 查询菜谱聚合统计，用于排行榜列表
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeStats:list')")
    @GetMapping("/list")
    public TableDataInfo list(RecipeStats recipeStats)
    {
        startPage();
        List<RecipeStats> list = recipeStatsService.selectRecipeStatsList(recipeStats);
        return getDataTable(list);
    }

    /**
     * 导出菜谱聚合统计，用于排行榜列表
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeStats:export')")
    @Log(title = "菜谱聚合统计，用于排行榜", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RecipeStats recipeStats)
    {
        List<RecipeStats> list = recipeStatsService.selectRecipeStatsList(recipeStats);
        ExcelUtil<RecipeStats> util = new ExcelUtil<RecipeStats>(RecipeStats.class);
        util.exportExcel(response, list, "菜谱聚合统计，用于排行榜数据");
    }

    /**
     * 获取菜谱聚合统计，用于排行榜详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeStats:query')")
    @GetMapping(value = "/{recipeId}")
    public AjaxResult getInfo(@PathVariable("recipeId") Long recipeId)
    {
        return success(recipeStatsService.selectRecipeStatsByRecipeId(recipeId));
    }

    /**
     * 新增菜谱聚合统计，用于排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeStats:add')")
    @Log(title = "菜谱聚合统计，用于排行榜", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RecipeStats recipeStats)
    {
        return toAjax(recipeStatsService.insertRecipeStats(recipeStats));
    }

    /**
     * 修改菜谱聚合统计，用于排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeStats:edit')")
    @Log(title = "菜谱聚合统计，用于排行榜", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RecipeStats recipeStats)
    {
        return toAjax(recipeStatsService.updateRecipeStats(recipeStats));
    }

    /**
     * 删除菜谱聚合统计，用于排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeStats:remove')")
    @Log(title = "菜谱聚合统计，用于排行榜", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recipeIds}")
    public AjaxResult remove(@PathVariable Long[] recipeIds)
    {
        return toAjax(recipeStatsService.deleteRecipeStatsByRecipeIds(recipeIds));
    }
}
