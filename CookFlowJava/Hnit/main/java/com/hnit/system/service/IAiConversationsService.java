package com.hnit.system.service;

import java.util.List;
import com.hnit.system.domain.AiConversations;

/**
 * AI 会话元信息Service接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface IAiConversationsService 
{
    /**
     * 查询AI 会话元信息
     * 
     * @param id AI 会话元信息主键
     * @return AI 会话元信息
     */
    public AiConversations selectAiConversationsById(Long id);

    /**
     * 查询AI 会话元信息列表
     * 
     * @param aiConversations AI 会话元信息
     * @return AI 会话元信息集合
     */
    public List<AiConversations> selectAiConversationsList(AiConversations aiConversations);

    /**
     * 新增AI 会话元信息
     * 
     * @param aiConversations AI 会话元信息
     * @return 结果
     */
    public int insertAiConversations(AiConversations aiConversations);

    /**
     * 修改AI 会话元信息
     * 
     * @param aiConversations AI 会话元信息
     * @return 结果
     */
    public int updateAiConversations(AiConversations aiConversations);

    /**
     * 批量删除AI 会话元信息
     * 
     * @param ids 需要删除的AI 会话元信息主键集合
     * @return 结果
     */
    public int deleteAiConversationsByIds(Long[] ids);

    /**
     * 删除AI 会话元信息信息
     * 
     * @param id AI 会话元信息主键
     * @return 结果
     */
    public int deleteAiConversationsById(Long id);
}
