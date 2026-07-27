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
import com.hnit.system.domain.RecipeIngredients;
import com.hnit.system.service.IRecipeIngredientsService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 菜谱与食材关联（含数量）Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/RecipeIngredients")
public class RecipeIngredientsController extends BaseController
{
    @Resource
    private IRecipeIngredientsService recipeIngredientsService;

    /**
     * 查询菜谱与食材关联（含数量）列表
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeIngredients:list')")
    @GetMapping("/list")
    public TableDataInfo list(RecipeIngredients recipeIngredients)
    {
        startPage();
        List<RecipeIngredients> list = recipeIngredientsService.selectRecipeIngredientsList(recipeIngredients);
        return getDataTable(list);
    }

    /**
     * 导出菜谱与食材关联（含数量）列表
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeIngredients:export')")
    @Log(title = "菜谱与食材关联（含数量）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RecipeIngredients recipeIngredients)
    {
        List<RecipeIngredients> list = recipeIngredientsService.selectRecipeIngredientsList(recipeIngredients);
        ExcelUtil<RecipeIngredients> util = new ExcelUtil<RecipeIngredients>(RecipeIngredients.class);
        util.exportExcel(response, list, "菜谱与食材关联（含数量）数据");
    }

    /**
     * 获取菜谱与食材关联（含数量）详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeIngredients:query')")
    @GetMapping(value = "/{recipeId}")
    public AjaxResult getInfo(@PathVariable("recipeId") Long recipeId)
    {
        return success(recipeIngredientsService.selectRecipeIngredientsByRecipeId(recipeId));
    }

    /**
     * 新增菜谱与食材关联（含数量）
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeIngredients:add')")
    @Log(title = "菜谱与食材关联（含数量）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RecipeIngredients recipeIngredients)
    {
        return toAjax(recipeIngredientsService.insertRecipeIngredients(recipeIngredients));
    }

    /**
     * 修改菜谱与食材关联（含数量）
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeIngredients:edit')")
    @Log(title = "菜谱与食材关联（含数量）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RecipeIngredients recipeIngredients)
    {
        return toAjax(recipeIngredientsService.updateRecipeIngredients(recipeIngredients));
    }

    /**
     * 删除菜谱与食材关联（含数量）
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeIngredients:remove')")
    @Log(title = "菜谱与食材关联（含数量）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recipeIds}")
    public AjaxResult remove(@PathVariable Long[] recipeIds)
    {
        return toAjax(recipeIngredientsService.deleteRecipeIngredientsByRecipeIds(recipeIds));
    }
}
