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
import com.hnit.system.domain.Media;
import com.hnit.system.service.IMediaService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 媒体资源（图片/视频/音频）Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/Media")
public class MediaController extends BaseController
{
    @Resource
    private IMediaService mediaService;

    /**
     * 查询媒体资源（图片/视频/音频）列表
     */
    @PreAuthorize("@ss.hasPermi('system:Media:list')")
    @GetMapping("/list")
    public TableDataInfo list(Media media)
    {
        startPage();
        List<Media> list = mediaService.selectMediaList(media);
        return getDataTable(list);
    }

    /**
     * 导出媒体资源（图片/视频/音频）列表
     */
    @PreAuthorize("@ss.hasPermi('system:Media:export')")
    @Log(title = "媒体资源（图片/视频/音频）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Media media)
    {
        List<Media> list = mediaService.selectMediaList(media);
        ExcelUtil<Media> util = new ExcelUtil<Media>(Media.class);
        util.exportExcel(response, list, "媒体资源（图片/视频/音频）数据");
    }

    /**
     * 获取媒体资源（图片/视频/音频）详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:Media:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mediaService.selectMediaById(id));
    }

    /**
     * 新增媒体资源（图片/视频/音频）
     */
    @PreAuthorize("@ss.hasPermi('system:Media:add')")
    @Log(title = "媒体资源（图片/视频/音频）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Media media)
    {
        return toAjax(mediaService.insertMedia(media));
    }

    /**
     * 修改媒体资源（图片/视频/音频）
     */
    @PreAuthorize("@ss.hasPermi('system:Media:edit')")
    @Log(title = "媒体资源（图片/视频/音频）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Media media)
    {
        return toAjax(mediaService.updateMedia(media));
    }

    /**
     * 删除媒体资源（图片/视频/音频）
     */
    @PreAuthorize("@ss.hasPermi('system:Media:remove')")
    @Log(title = "媒体资源（图片/视频/音频）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mediaService.deleteMediaByIds(ids));
    }
}
