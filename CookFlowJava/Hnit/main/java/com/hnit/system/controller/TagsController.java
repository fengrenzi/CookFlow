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
import com.hnit.system.domain.Tags;
import com.hnit.system.service.ITagsService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 标签Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/Tags")
public class TagsController extends BaseController
{
    @Resource
    private ITagsService tagsService;

    /**
     * 查询标签列表
     */
    @PreAuthorize("@ss.hasPermi('system:Tags:list')")
    @GetMapping("/list")
    public TableDataInfo list(Tags tags)
    {
        startPage();
        List<Tags> list = tagsService.selectTagsList(tags);
        return getDataTable(list);
    }

    /**
     * 导出标签列表
     */
    @PreAuthorize("@ss.hasPermi('system:Tags:export')")
    @Log(title = "标签", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Tags tags)
    {
        List<Tags> list = tagsService.selectTagsList(tags);
        ExcelUtil<Tags> util = new ExcelUtil<Tags>(Tags.class);
        util.exportExcel(response, list, "标签数据");
    }

    /**
     * 获取标签详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:Tags:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tagsService.selectTagsById(id));
    }

    /**
     * 新增标签
     */
    @PreAuthorize("@ss.hasPermi('system:Tags:add')")
    @Log(title = "标签", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Tags tags)
    {
        return toAjax(tagsService.insertTags(tags));
    }

    /**
     * 修改标签
     */
    @PreAuthorize("@ss.hasPermi('system:Tags:edit')")
    @Log(title = "标签", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Tags tags)
    {
        return toAjax(tagsService.updateTags(tags));
    }

    /**
     * 删除标签
     */
    @PreAuthorize("@ss.hasPermi('system:Tags:remove')")
    @Log(title = "标签", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tagsService.deleteTagsByIds(ids));
    }
}
