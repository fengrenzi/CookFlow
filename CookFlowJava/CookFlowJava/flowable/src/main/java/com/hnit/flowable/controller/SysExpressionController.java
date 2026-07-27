package com.hnit.flowable.controller;

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
import com.hnit.system.domain.SysExpression;
import com.hnit.system.service.ISysExpressionService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 流程达式Controller
 *
 * @author hnit
 * @date 2022-12-12
 */
@RestController
@RequestMapping("/system/expression")
public class SysExpressionController extends BaseController
{
    @Resource
    private ISysExpressionService sysExpressionService;

    /**
     * 查询流程达式列表
     */
    @PreAuthorize("@ss.hasPermi('system:expression:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysExpression sysExpression)
    {
        startPage();
        List<SysExpression> list = sysExpressionService.selectSysExpressionList(sysExpression);
        return getDataTable(list);
    }

    /**
     * 导出流程达式列表
     */
    @PreAuthorize("@ss.hasPermi('system:expression:export')")
    @Log(title = "流程达式", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysExpression sysExpression)
    {
        List<SysExpression> list = sysExpressionService.selectSysExpressionList(sysExpression);
        ExcelUtil<SysExpression> util = new ExcelUtil<SysExpression>(SysExpression.class);
        util.exportExcel(response, list, "流程达式数据");
    }

    /**
     * 获取流程达式详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:expression:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysExpressionService.selectSysExpressionById(id));
    }

    /**
     * 新增流程达式
     */
    @PreAuthorize("@ss.hasPermi('system:expression:add')")
    @Log(title = "流程达式", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysExpression sysExpression)
    {
        return toAjax(sysExpressionService.insertSysExpression(sysExpression));
    }

    /**
     * 修改流程达式
     */
    @PreAuthorize("@ss.hasPermi('system:expression:edit')")
    @Log(title = "流程达式", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysExpression sysExpression)
    {
        return toAjax(sysExpressionService.updateSysExpression(sysExpression));
    }

    /**
     * 删除流程达式
     */
    @PreAuthorize("@ss.hasPermi('system:expression:remove')")
    @Log(title = "流程达式", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysExpressionService.deleteSysExpressionByIds(ids));
    }
}
