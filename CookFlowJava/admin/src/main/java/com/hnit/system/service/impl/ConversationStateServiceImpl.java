package com.hnit.system.service.impl;

import com.hnit.system.domain.ConversationMessage;
import com.hnit.system.domain.ConversationSession;
import com.hnit.system.mapper.ConversationMessageMapper;
import com.hnit.system.mapper.ConversationSessionMapper;
import com.hnit.system.service.IConversationStateService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConversationStateServiceImpl implements IConversationStateService {

    @Resource
    private ConversationSessionMapper sessionMapper;
    @Resource
    private ConversationMessageMapper messageMapper;

    @Override
    public ConversationSession getOrCreateSession(String sessionId, Long userId) {
        ConversationSession session = sessionMapper.selectActiveByUserId(userId);
        if (session == null) {
            session = new ConversationSession();
            session.setUserId(userId);
            session.setCurrentRecipeId(null);
            session.setCurrentStepIndex(0);
            session.setCookingMode(false);
            session.setStatus("active");
            sessionMapper.insert(session);
        }
        return session;
    }

    @Override
    @Transactional
    public void saveMessages(Long sessionId, String userText, String assistantReply) {
        ConversationMessage userMsg = new ConversationMessage();
        userMsg.setSessionId(sessionId);
        userMsg.setRole("user");
        userMsg.setContent(userText);
        messageMapper.insert(userMsg);

        ConversationMessage assistantMsg = new ConversationMessage();
        assistantMsg.setSessionId(sessionId);
        assistantMsg.setRole("assistant");
        assistantMsg.setContent(assistantReply);
        messageMapper.insert(assistantMsg);
    }

    @Override
    public List<ConversationMessage> getRecentMessages(Long sessionId, int limit) {
        return messageMapper.selectRecentBySessionId(sessionId, limit);
    }

    @Override
    public void updateSession(ConversationSession session) {
        sessionMapper.update(session);
    }
}