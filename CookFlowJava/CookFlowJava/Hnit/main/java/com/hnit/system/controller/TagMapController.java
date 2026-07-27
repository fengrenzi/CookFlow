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
import com.hnit.system.domain.TagMap;
import com.hnit.system.service.ITagMapService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 标签映射Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/TagMap")
public class TagMapController extends BaseController
{
    @Resource
    private ITagMapService tagMapService;

    /**
     * 查询标签映射列表
     */
    @PreAuthorize("@ss.hasPermi('system:TagMap:list')")
    @GetMapping("/list")
    public TableDataInfo list(TagMap tagMap)
    {
        startPage();
        List<TagMap> list = tagMapService.selectTagMapList(tagMap);
        return getDataTable(list);
    }

    /**
     * 导出标签映射列表
     */
    @PreAuthorize("@ss.hasPermi('system:TagMap:export')")
    @Log(title = "标签映射", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TagMap tagMap)
    {
        List<TagMap> list = tagMapService.selectTagMapList(tagMap);
        ExcelUtil<TagMap> util = new ExcelUtil<TagMap>(TagMap.class);
        util.exportExcel(response, list, "标签映射数据");
    }

    /**
     * 获取标签映射详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:TagMap:query')")
    @GetMapping(value = "/{tagId}")
    public AjaxResult getInfo(@PathVariable("tagId") Long tagId)
    {
        return success(tagMapService.selectTagMapByTagId(tagId));
    }

    /**
     * 新增标签映射
     */
    @PreAuthorize("@ss.hasPermi('system:TagMap:add')")
    @Log(title = "标签映射", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TagMap tagMap)
    {
        return toAjax(tagMapService.insertTagMap(tagMap));
    }

    /**
     * 修改标签映射
     */
    @PreAuthorize("@ss.hasPermi('system:TagMap:edit')")
    @Log(title = "标签映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TagMap tagMap)
    {
        return toAjax(tagMapService.updateTagMap(tagMap));
    }

    /**
     * 删除标签映射
     */
    @PreAuthorize("@ss.hasPermi('system:TagMap:remove')")
    @Log(title = "标签映射", businessType = BusinessType.DELETE)
	@DeleteMapping("/{tagIds}")
    public AjaxResult remove(@PathVariable Long[] tagIds)
    {
        return toAjax(tagMapService.deleteTagMapByTagIds(tagIds));
    }
}
