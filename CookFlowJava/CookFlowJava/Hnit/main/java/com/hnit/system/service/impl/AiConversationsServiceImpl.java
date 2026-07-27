package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.AiConversationsMapper;
import com.hnit.system.domain.AiConversations;
import com.hnit.system.service.IAiConversationsService;

/**
 * AI 会话元信息Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class AiConversationsServiceImpl implements IAiConversationsService 
{
    @Resource
    private AiConversationsMapper aiConversationsMapper;

    /**
     * 查询AI 会话元信息
     * 
     * @param id AI 会话元信息主键
     * @return AI 会话元信息
     */
    @Override
    public AiConversations selectAiConversationsById(Long id)
    {
        return aiConversationsMapper.selectAiConversationsById(id);
    }

    /**
     * 查询AI 会话元信息列表
     * 
     * @param aiConversations AI 会话元信息
     * @return AI 会话元信息
     */
    @Override
    public List<AiConversations> selectAiConversationsList(AiConversations aiConversations)
    {
        return aiConversationsMapper.selectAiConversationsList(aiConversations);
    }

    /**
     * 新增AI 会话元信息
     * 
     * @param aiConversations AI 会话元信息
     * @return 结果
     */
    @Override
    public int insertAiConversations(AiConversations aiConversations)
    {
        return aiConversationsMapper.insertAiConversations(aiConversations);
    }

    /**
     * 修改AI 会话元信息
     * 
     * @param aiConversations AI 会话元信息
     * @return 结果
     */
    @Override
    public int updateAiConversations(AiConversations aiConversations)
    {
        return aiConversationsMapper.updateAiConversations(aiConversations);
    }

    /**
     * 批量删除AI 会话元信息
     * 
     * @param ids 需要删除的AI 会话元信息主键
     * @return 结果
     */
    @Override
    public int deleteAiConversationsByIds(Long[] ids)
    {
        return aiConversationsMapper.deleteAiConversationsByIds(ids);
    }

    /**
     * 删除AI 会话元信息信息
     * 
     * @param id AI 会话元信息主键
     * @return 结果
     */
    @Override
    public int deleteAiConversationsById(Long id)
    {
        return aiConversationsMapper.deleteAiConversationsById(id);
    }
}
