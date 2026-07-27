package com.hnit.system.mapper;

import com.hnit.system.domain.ConversationMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ConversationMessageMapper {
    int insert(ConversationMessage message);
    List<ConversationMessage> selectRecentBySessionId(@Param("sessionId") Long sessionId,
                                                      @Param("limit") int limit);
}