package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.AiMessagesMapper;
import com.hnit.system.domain.AiMessages;
import com.hnit.system.service.IAiMessagesService;

/**
 * AI 会话消息（按消息存储）Service业务层处理
 * 
 * @author hnit
 * @date 2026-03-23
 */
@Service
public class AiMessagesServiceImpl implements IAiMessagesService 
{
    @Resource
    private AiMessagesMapper aiMessagesMapper;

    /**
     * 查询AI 会话消息（按消息存储）
     * 
     * @param id AI 会话消息（按消息存储）主键
     * @return AI 会话消息（按消息存储）
     */
    @Override
    public AiMessages selectAiMessagesById(Long id)
    {
        return aiMessagesMapper.selectAiMessagesById(id);
    }

    /**
     * 查询AI 会话消息（按消息存储）列表
     * 
     * @param aiMessages AI 会话消息（按消息存储）
     * @return AI 会话消息（按消息存储）
     */
    @Override
    public List<AiMessages> selectAiMessagesList(AiMessages aiMessages)
    {
        return aiMessagesMapper.selectAiMessagesList(aiMessages);
    }

    /**
     * 新增AI 会话消息（按消息存储）
     * 
     * @param aiMessages AI 会话消息（按消息存储）
     * @return 结果
     */
    @Override
    public int insertAiMessages(AiMessages aiMessages)
    {
        return aiMessagesMapper.insertAiMessages(aiMessages);
    }

    /**
     * 修改AI 会话消息（按消息存储）
     * 
     * @param aiMessages AI 会话消息（按消息存储）
     * @return 结果
     */
    @Override
    public int updateAiMessages(AiMessages aiMessages)
    {
        return aiMessagesMapper.updateAiMessages(aiMessages);
    }

    /**
     * 批量删除AI 会话消息（按消息存储）
     * 
     * @param ids 需要删除的AI 会话消息（按消息存储）主键
     * @return 结果
     */
    @Override
    public int deleteAiMessagesByIds(Long[] ids)
    {
        return aiMessagesMapper.deleteAiMessagesByIds(ids);
    }

    /**
     * 删除AI 会话消息（按消息存储）信息
     * 
     * @param id AI 会话消息（按消息存储）主键
     * @return 结果
     */
    @Override
    public int deleteAiMessagesById(Long id)
    {
        return aiMessagesMapper.deleteAiMessagesById(id);
    }
}
