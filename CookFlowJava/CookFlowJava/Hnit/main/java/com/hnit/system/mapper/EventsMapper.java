package com.hnit.system.mapper;

import java.util.List;
import com.hnit.system.domain.Events;

/**
 * 用户行为事件（事件流）Mapper接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface EventsMapper 
{
    /**
     * 查询用户行为事件（事件流）
     * 
     * @param id 用户行为事件（事件流）主键
     * @return 用户行为事件（事件流）
     */
    public Events selectEventsById(Long id);

    /**
     * 查询用户行为事件（事件流）列表
     * 
     * @param events 用户行为事件（事件流）
     * @return 用户行为事件（事件流）集合
     */
    public List<Events> selectEventsList(Events events);

    /**
     * 新增用户行为事件（事件流）
     * 
     * @param events 用户行为事件（事件流）
     * @return 结果
     */
    public int insertEvents(Events events);

    /**
     * 修改用户行为事件（事件流）
     * 
     * @param events 用户行为事件（事件流）
     * @return 结果
     */
    public int updateEvents(Events events);

    /**
     * 删除用户行为事件（事件流）
     * 
     * @param id 用户行为事件（事件流）主键
     * @return 结果
     */
    public int deleteEventsById(Long id);

    /**
     * 批量删除用户行为事件（事件流）
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEventsByIds(Long[] ids);
}
