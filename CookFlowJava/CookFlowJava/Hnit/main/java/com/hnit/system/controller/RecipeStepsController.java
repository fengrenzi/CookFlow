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
import com.hnit.system.domain.RecipeSteps;
import com.hnit.system.service.IRecipeStepsService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 菜谱步骤（有序）Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/RecipeSteps")
public class RecipeStepsController extends BaseController
{
    @Resource
    private IRecipeStepsService recipeStepsService;

    /**
     * 查询菜谱步骤（有序）列表
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeSteps:list')")
    @GetMapping("/list")
    public TableDataInfo list(RecipeSteps recipeSteps)
    {
        startPage();
        List<RecipeSteps> list = recipeStepsService.selectRecipeStepsList(recipeSteps);
        return getDataTable(list);
    }

    /**
     * 导出菜谱步骤（有序）列表
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeSteps:export')")
    @Log(title = "菜谱步骤（有序）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RecipeSteps recipeSteps)
    {
        List<RecipeSteps> list = recipeStepsService.selectRecipeStepsList(recipeSteps);
        ExcelUtil<RecipeSteps> util = new ExcelUtil<RecipeSteps>(RecipeSteps.class);
        util.exportExcel(response, list, "菜谱步骤（有序）数据");
    }

    /**
     * 获取菜谱步骤（有序）详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeSteps:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(recipeStepsService.selectRecipeStepsById(id));
    }

    /**
     * 新增菜谱步骤（有序）
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeSteps:add')")
    @Log(title = "菜谱步骤（有序）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RecipeSteps recipeSteps)
    {
        return toAjax(recipeStepsService.insertRecipeSteps(recipeSteps));
    }

    /**
     * 修改菜谱步骤（有序）
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeSteps:edit')")
    @Log(title = "菜谱步骤（有序）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RecipeSteps recipeSteps)
    {
        return toAjax(recipeStepsService.updateRecipeSteps(recipeSteps));
    }

    /**
     * 删除菜谱步骤（有序）
     */
    @PreAuthorize("@ss.hasPermi('system:RecipeSteps:remove')")
    @Log(title = "菜谱步骤（有序）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(recipeStepsService.deleteRecipeStepsByIds(ids));
    }
}
