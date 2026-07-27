package com.hnit.system.mapper;

import java.util.List;
import com.hnit.system.domain.AiMessages;

/**
 * AI 会话消息（按消息存储）Mapper接口
 * 
 * @author hnit
 * @date 2026-03-23
 */
public interface AiMessagesMapper 
{
    /**
     * 查询AI 会话消息（按消息存储）
     * 
     * @param id AI 会话消息（按消息存储）主键
     * @return AI 会话消息（按消息存储）
     */
    public AiMessages selectAiMessagesById(Long id);

    /**
     * 查询AI 会话消息（按消息存储）列表
     * 
     * @param aiMessages AI 会话消息（按消息存储）
     * @return AI 会话消息（按消息存储）集合
     */
    public List<AiMessages> selectAiMessagesList(AiMessages aiMessages);

    /**
     * 新增AI 会话消息（按消息存储）
     * 
     * @param aiMessages AI 会话消息（按消息存储）
     * @return 结果
     */
    public int insertAiMessages(AiMessages aiMessages);

    /**
     * 修改AI 会话消息（按消息存储）
     * 
     * @param aiMessages AI 会话消息（按消息存储）
     * @return 结果
     */
    public int updateAiMessages(AiMessages aiMessages);

    /**
     * 删除AI 会话消息（按消息存储）
     * 
     * @param id AI 会话消息（按消息存储）主键
     * @return 结果
     */
    public int deleteAiMessagesById(Long id);

    /**
     * 批量删除AI 会话消息（按消息存储）
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAiMessagesByIds(Long[] ids);
}
