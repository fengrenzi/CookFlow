package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.NotificationsMapper;
import com.hnit.system.domain.Notifications;
import com.hnit.system.service.INotificationsService;

/**
 * 用户通知Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class NotificationsServiceImpl implements INotificationsService 
{
    @Resource
    private NotificationsMapper notificationsMapper;

    /**
     * 查询用户通知
     * 
     * @param id 用户通知主键
     * @return 用户通知
     */
    @Override
    public Notifications selectNotificationsById(Long id)
    {
        return notificationsMapper.selectNotificationsById(id);
    }

    /**
     * 查询用户通知列表
     * 
     * @param notifications 用户通知
     * @return 用户通知
     */
    @Override
    public List<Notifications> selectNotificationsList(Notifications notifications)
    {
        return notificationsMapper.selectNotificationsList(notifications);
    }

    /**
     * 新增用户通知
     * 
     * @param notifications 用户通知
     * @return 结果
     */
    @Override
    public int insertNotifications(Notifications notifications)
    {
        return notificationsMapper.insertNotifications(notifications);
    }

    /**
     * 修改用户通知
     * 
     * @param notifications 用户通知
     * @return 结果
     */
    @Override
    public int updateNotifications(Notifications notifications)
    {
        return notificationsMapper.updateNotifications(notifications);
    }

    /**
     * 批量删除用户通知
     * 
     * @param ids 需要删除的用户通知主键
     * @return 结果
     */
    @Override
    public int deleteNotificationsByIds(Long[] ids)
    {
        return notificationsMapper.deleteNotificationsByIds(ids);
    }

    /**
     * 删除用户通知信息
     * 
     * @param id 用户通知主键
     * @return 结果
     */
    @Override
    public int deleteNotificationsById(Long id)
    {
        return notificationsMapper.deleteNotificationsById(id);
    }
}
