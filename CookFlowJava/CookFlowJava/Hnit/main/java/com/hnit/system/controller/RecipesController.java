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
import com.hnit.system.domain.Recipes;
import com.hnit.system.service.IRecipesService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 菜谱Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/Recipes")
public class RecipesController extends BaseController
{
    @Resource
    private IRecipesService recipesService;

    /**
     * 查询菜谱列表
     */
    @PreAuthorize("@ss.hasPermi('system:Recipes:list')")
    @GetMapping("/list")
    public TableDataInfo list(Recipes recipes)
    {
        startPage();
        List<Recipes> list = recipesService.selectRecipesList(recipes);
        return getDataTable(list);
    }

    /**
     * 导出菜谱列表
     */
    @PreAuthorize("@ss.hasPermi('system:Recipes:export')")
    @Log(title = "菜谱", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Recipes recipes)
    {
        List<Recipes> list = recipesService.selectRecipesList(recipes);
        ExcelUtil<Recipes> util = new ExcelUtil<Recipes>(Recipes.class);
        util.exportExcel(response, list, "菜谱数据");
    }

    /**
     * 获取菜谱详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:Recipes:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(recipesService.selectRecipesById(id));
    }

    /**
     * 新增菜谱
     */
    @PreAuthorize("@ss.hasPermi('system:Recipes:add')")
    @Log(title = "菜谱", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Recipes recipes)
    {
        return toAjax(recipesService.insertRecipes(recipes));
    }

    /**
     * 修改菜谱
     */
    @PreAuthorize("@ss.hasPermi('system:Recipes:edit')")
    @Log(title = "菜谱", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Recipes recipes)
    {
        return toAjax(recipesService.updateRecipes(recipes));
    }

    /**
     * 删除菜谱
     */
    @PreAuthorize("@ss.hasPermi('system:Recipes:remove')")
    @Log(title = "菜谱", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(recipesService.deleteRecipesByIds(ids));
    }
}
