package com.hnit.system.mapper;

import com.hnit.system.domain.ConversationSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface ConversationSessionMapper {

    /**
     * 插入会话
     */
    int insert(ConversationSession session);

    /**
     * 根据ID查询
     */
    ConversationSession selectById(@Param("id") Long id);

    /**
     * 根据用户ID查询活跃会话
     */
    ConversationSession selectActiveByUserId(@Param("userId") Long userId);

    /**
     * 更新会话
     */
    int update(ConversationSession session);
}