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
import com.hnit.system.domain.Ingredients;
import com.hnit.system.service.IIngredientsService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 食材Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/Ingredients")
public class IngredientsController extends BaseController
{
    @Resource
    private IIngredientsService ingredientsService;

    /**
     * 查询食材列表
     */
    @PreAuthorize("@ss.hasPermi('system:Ingredients:list')")
    @GetMapping("/list")
    public TableDataInfo list(Ingredients ingredients)
    {
        startPage();
        List<Ingredients> list = ingredientsService.selectIngredientsList(ingredients);
        return getDataTable(list);
    }

    /**
     * 导出食材列表
     */
    @PreAuthorize("@ss.hasPermi('system:Ingredients:export')")
    @Log(title = "食材", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Ingredients ingredients)
    {
        List<Ingredients> list = ingredientsService.selectIngredientsList(ingredients);
        ExcelUtil<Ingredients> util = new ExcelUtil<Ingredients>(Ingredients.class);
        util.exportExcel(response, list, "食材数据");
    }

    /**
     * 获取食材详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:Ingredients:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(ingredientsService.selectIngredientsById(id));
    }

    /**
     * 新增食材
     */
    @PreAuthorize("@ss.hasPermi('system:Ingredients:add')")
    @Log(title = "食材", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Ingredients ingredients)
    {
        return toAjax(ingredientsService.insertIngredients(ingredients));
    }

    /**
     * 修改食材
     */
    @PreAuthorize("@ss.hasPermi('system:Ingredients:edit')")
    @Log(title = "食材", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Ingredients ingredients)
    {
        return toAjax(ingredientsService.updateIngredients(ingredients));
    }

    /**
     * 删除食材
     */
    @PreAuthorize("@ss.hasPermi('system:Ingredients:remove')")
    @Log(title = "食材", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(ingredientsService.deleteIngredientsByIds(ids));
    }
}
