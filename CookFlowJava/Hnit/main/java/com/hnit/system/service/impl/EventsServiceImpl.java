package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.EventsMapper;
import com.hnit.system.domain.Events;
import com.hnit.system.service.IEventsService;

/**
 * 用户行为事件（事件流）Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class EventsServiceImpl implements IEventsService 
{
    @Resource
    private EventsMapper eventsMapper;

    /**
     * 查询用户行为事件（事件流）
     * 
     * @param id 用户行为事件（事件流）主键
     * @return 用户行为事件（事件流）
     */
    @Override
    public Events selectEventsById(Long id)
    {
        return eventsMapper.selectEventsById(id);
    }

    /**
     * 查询用户行为事件（事件流）列表
     * 
     * @param events 用户行为事件（事件流）
     * @return 用户行为事件（事件流）
     */
    @Override
    public List<Events> selectEventsList(Events events)
    {
        return eventsMapper.selectEventsList(events);
    }

    /**
     * 新增用户行为事件（事件流）
     * 
     * @param events 用户行为事件（事件流）
     * @return 结果
     */
    @Override
    public int insertEvents(Events events)
    {
        return eventsMapper.insertEvents(events);
    }

    /**
     * 修改用户行为事件（事件流）
     * 
     * @param events 用户行为事件（事件流）
     * @return 结果
     */
    @Override
    public int updateEvents(Events events)
    {
        return eventsMapper.updateEvents(events);
    }

    /**
     * 批量删除用户行为事件（事件流）
     * 
     * @param ids 需要删除的用户行为事件（事件流）主键
     * @return 结果
     */
    @Override
    public int deleteEventsByIds(Long[] ids)
    {
        return eventsMapper.deleteEventsByIds(ids);
    }

    /**
     * 删除用户行为事件（事件流）信息
     * 
     * @param id 用户行为事件（事件流）主键
     * @return 结果
     */
    @Override
    public int deleteEventsById(Long id)
    {
        return eventsMapper.deleteEventsById(id);
    }
}
