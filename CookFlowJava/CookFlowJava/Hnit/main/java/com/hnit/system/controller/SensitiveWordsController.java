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
import com.hnit.system.domain.SensitiveWords;
import com.hnit.system.service.ISensitiveWordsService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 敏感词库，用于内容审核/替换Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/SensitiveWords")
public class SensitiveWordsController extends BaseController
{
    @Resource
    private ISensitiveWordsService sensitiveWordsService;

    /**
     * 查询敏感词库，用于内容审核/替换列表
     */
    @PreAuthorize("@ss.hasPermi('system:SensitiveWords:list')")
    @GetMapping("/list")
    public TableDataInfo list(SensitiveWords sensitiveWords)
    {
        startPage();
        List<SensitiveWords> list = sensitiveWordsService.selectSensitiveWordsList(sensitiveWords);
        return getDataTable(list);
    }

    /**
     * 导出敏感词库，用于内容审核/替换列表
     */
    @PreAuthorize("@ss.hasPermi('system:SensitiveWords:export')")
    @Log(title = "敏感词库，用于内容审核/替换", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SensitiveWords sensitiveWords)
    {
        List<SensitiveWords> list = sensitiveWordsService.selectSensitiveWordsList(sensitiveWords);
        ExcelUtil<SensitiveWords> util = new ExcelUtil<SensitiveWords>(SensitiveWords.class);
        util.exportExcel(response, list, "敏感词库，用于内容审核/替换数据");
    }

    /**
     * 获取敏感词库，用于内容审核/替换详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:SensitiveWords:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sensitiveWordsService.selectSensitiveWordsById(id));
    }

    /**
     * 新增敏感词库，用于内容审核/替换
     */
    @PreAuthorize("@ss.hasPermi('system:SensitiveWords:add')")
    @Log(title = "敏感词库，用于内容审核/替换", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SensitiveWords sensitiveWords)
    {
        return toAjax(sensitiveWordsService.insertSensitiveWords(sensitiveWords));
    }

    /**
     * 修改敏感词库，用于内容审核/替换
     */
    @PreAuthorize("@ss.hasPermi('system:SensitiveWords:edit')")
    @Log(title = "敏感词库，用于内容审核/替换", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SensitiveWords sensitiveWords)
    {
        return toAjax(sensitiveWordsService.updateSensitiveWords(sensitiveWords));
    }

    /**
     * 删除敏感词库，用于内容审核/替换
     */
    @PreAuthorize("@ss.hasPermi('system:SensitiveWords:remove')")
    @Log(title = "敏感词库，用于内容审核/替换", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sensitiveWordsService.deleteSensitiveWordsByIds(ids));
    }
}
