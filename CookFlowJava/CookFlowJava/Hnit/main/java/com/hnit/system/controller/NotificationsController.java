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
import com.hnit.system.domain.Notifications;
import com.hnit.system.service.INotificationsService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 用户通知Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/Notifications")
public class NotificationsController extends BaseController
{
    @Resource
    private INotificationsService notificationsService;

    /**
     * 查询用户通知列表
     */
    @PreAuthorize("@ss.hasPermi('system:Notifications:list')")
    @GetMapping("/list")
    public TableDataInfo list(Notifications notifications)
    {
        startPage();
        List<Notifications> list = notificationsService.selectNotificationsList(notifications);
        return getDataTable(list);
    }

    /**
     * 导出用户通知列表
     */
    @PreAuthorize("@ss.hasPermi('system:Notifications:export')")
    @Log(title = "用户通知", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Notifications notifications)
    {
        List<Notifications> list = notificationsService.selectNotificationsList(notifications);
        ExcelUtil<Notifications> util = new ExcelUtil<Notifications>(Notifications.class);
        util.exportExcel(response, list, "用户通知数据");
    }

    /**
     * 获取用户通知详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:Notifications:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(notificationsService.selectNotificationsById(id));
    }

    /**
     * 新增用户通知
     */
    @PreAuthorize("@ss.hasPermi('system:Notifications:add')")
    @Log(title = "用户通知", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Notifications notifications)
    {
        return toAjax(notificationsService.insertNotifications(notifications));
    }

    /**
     * 修改用户通知
     */
    @PreAuthorize("@ss.hasPermi('system:Notifications:edit')")
    @Log(title = "用户通知", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Notifications notifications)
    {
        return toAjax(notificationsService.updateNotifications(notifications));
    }

    /**
     * 删除用户通知
     */
    @PreAuthorize("@ss.hasPermi('system:Notifications:remove')")
    @Log(title = "用户通知", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(notificationsService.deleteNotificationsByIds(ids));
    }
}
