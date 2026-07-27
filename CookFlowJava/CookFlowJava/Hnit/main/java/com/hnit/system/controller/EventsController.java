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
import com.hnit.system.domain.Events;
import com.hnit.system.service.IEventsService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 用户行为事件（事件流）Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/Events")
public class EventsController extends BaseController
{
    @Resource
    private IEventsService eventsService;

    /**
     * 查询用户行为事件（事件流）列表
     */
    @PreAuthorize("@ss.hasPermi('system:Events:list')")
    @GetMapping("/list")
    public TableDataInfo list(Events events)
    {
        startPage();
        List<Events> list = eventsService.selectEventsList(events);
        return getDataTable(list);
    }

    /**
     * 导出用户行为事件（事件流）列表
     */
    @PreAuthorize("@ss.hasPermi('system:Events:export')")
    @Log(title = "用户行为事件（事件流）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Events events)
    {
        List<Events> list = eventsService.selectEventsList(events);
        ExcelUtil<Events> util = new ExcelUtil<Events>(Events.class);
        util.exportExcel(response, list, "用户行为事件（事件流）数据");
    }

    /**
     * 获取用户行为事件（事件流）详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:Events:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(eventsService.selectEventsById(id));
    }

    /**
     * 新增用户行为事件（事件流）
     */
    @PreAuthorize("@ss.hasPermi('system:Events:add')")
    @Log(title = "用户行为事件（事件流）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Events events)
    {
        return toAjax(eventsService.insertEvents(events));
    }

    /**
     * 修改用户行为事件（事件流）
     */
    @PreAuthorize("@ss.hasPermi('system:Events:edit')")
    @Log(title = "用户行为事件（事件流）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Events events)
    {
        return toAjax(eventsService.updateEvents(events));
    }

    /**
     * 删除用户行为事件（事件流）
     */
    @PreAuthorize("@ss.hasPermi('system:Events:remove')")
    @Log(title = "用户行为事件（事件流）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eventsService.deleteEventsByIds(ids));
    }
}
