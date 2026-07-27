package com.hnit.system.service;

import java.util.List;
import com.hnit.system.domain.Notifications;

/**
 * 用户通知Service接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface INotificationsService 
{
    /**
     * 查询用户通知
     * 
     * @param id 用户通知主键
     * @return 用户通知
     */
    public Notifications selectNotificationsById(Long id);

    /**
     * 查询用户通知列表
     * 
     * @param notifications 用户通知
     * @return 用户通知集合
     */
    public List<Notifications> selectNotificationsList(Notifications notifications);

    /**
     * 新增用户通知
     * 
     * @param notifications 用户通知
     * @return 结果
     */
    public int insertNotifications(Notifications notifications);

    /**
     * 修改用户通知
     * 
     * @param notifications 用户通知
     * @return 结果
     */
    public int updateNotifications(Notifications notifications);

    /**
     * 批量删除用户通知
     * 
     * @param ids 需要删除的用户通知主键集合
     * @return 结果
     */
    public int deleteNotificationsByIds(Long[] ids);

    /**
     * 删除用户通知信息
     * 
     * @param id 用户通知主键
     * @return 结果
     */
    public int deleteNotificationsById(Long id);
}
