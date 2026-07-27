package com.hnit.system.service;

import com.hnit.system.domain.ConversationMessage;
import com.hnit.system.domain.ConversationSession;
import java.util.List;

public interface IConversationStateService {
    ConversationSession getOrCreateSession(String sessionId, Long userId);
    void saveMessages(Long sessionId, String userText, String assistantReply);
    List<ConversationMessage> getRecentMessages(Long sessionId, int limit);
    void updateSession(ConversationSession session);
}