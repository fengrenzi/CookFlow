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
import com.hnit.system.domain.IngredientDetails;
import com.hnit.system.service.IIngredientDetailsService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 食材详情静态内容Controller
 * 
 * @author hnit
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/IngredientDetails")
public class IngredientDetailsController extends BaseController
{
    @Resource
    private IIngredientDetailsService ingredientDetailsService;

    /**
     * 查询食材详情静态内容列表
     */
    @PreAuthorize("@ss.hasPermi('system:IngredientDetails:list')")
    @GetMapping("/list")
    public TableDataInfo list(IngredientDetails ingredientDetails)
    {
        startPage();
        List<IngredientDetails> list = ingredientDetailsService.selectIngredientDetailsList(ingredientDetails);
        return getDataTable(list);
    }

    /**
     * 导出食材详情静态内容列表
     */
    @PreAuthorize("@ss.hasPermi('system:IngredientDetails:export')")
    @Log(title = "食材详情静态内容", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, IngredientDetails ingredientDetails)
    {
        List<IngredientDetails> list = ingredientDetailsService.selectIngredientDetailsList(ingredientDetails);
        ExcelUtil<IngredientDetails> util = new ExcelUtil<IngredientDetails>(IngredientDetails.class);
        util.exportExcel(response, list, "食材详情静态内容数据");
    }

    /**
     * 获取食材详情静态内容详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:IngredientDetails:query')")
    @GetMapping(value = "/{ingredientId}")
    public AjaxResult getInfo(@PathVariable("ingredientId") String ingredientId)
    {
        return success(ingredientDetailsService.selectIngredientDetailsByIngredientId(ingredientId));
    }

    /**
     * 新增食材详情静态内容
     */
    @PreAuthorize("@ss.hasPermi('system:IngredientDetails:add')")
    @Log(title = "食材详情静态内容", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IngredientDetails ingredientDetails)
    {
        return toAjax(ingredientDetailsService.insertIngredientDetails(ingredientDetails));
    }

    /**
     * 修改食材详情静态内容
     */
    @PreAuthorize("@ss.hasPermi('system:IngredientDetails:edit')")
    @Log(title = "食材详情静态内容", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IngredientDetails ingredientDetails)
    {
        return toAjax(ingredientDetailsService.updateIngredientDetails(ingredientDetails));
    }

    /**
     * 删除食材详情静态内容
     */
    @PreAuthorize("@ss.hasPermi('system:IngredientDetails:remove')")
    @Log(title = "食材详情静态内容", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ingredientIds}")
    public AjaxResult remove(@PathVariable String[] ingredientIds)
    {
        return toAjax(ingredientDetailsService.deleteIngredientDetailsByIngredientIds(ingredientIds));
    }
}
