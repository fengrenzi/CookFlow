package com.hnit.system.service.impl;

import com.hnit.system.domain.ForumAnswer;
import com.hnit.system.domain.ForumQuestion;
import com.hnit.system.domain.UserInteraction;
import com.hnit.system.domain.dto.ForumAnswerDto;
import com.hnit.system.domain.vo.ForumAnswerVo;
import com.hnit.system.mapper.ForumAnswerMapper;
import com.hnit.system.mapper.ForumQuestionMapper;
import com.hnit.system.mapper.UserInteractionMapper;
import com.hnit.system.service.IForumAnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumAnswerServiceImpl implements IForumAnswerService {

    @Resource
    private ForumAnswerMapper answerMapper;
    @Resource
    private ForumQuestionMapper questionMapper;
    @Resource
    private UserInteractionMapper interactionMapper;

    @Override
    @Transactional
    public void createAnswer(String questionId, ForumAnswerDto dto, Long userId) {
        ForumQuestion question = questionMapper.selectById(questionId);
        if (question == null) throw new RuntimeException("问题不存在");
        if (question.getStatus() != 0) throw new RuntimeException("问题已关闭或删除");

        ForumAnswer answer = new ForumAnswer();
        answer.setId(UUID.randomUUID().toString());
        answer.setQuestionId(questionId);
        answer.setContent(dto.getContent());
        answer.setUserId(userId);
        answer.setLikeCount(0);
        answer.setCommentCount(0);
        answer.setIsAccepted(false);
        answer.setStatus(0);
        answer.setCreatedAt(LocalDateTime.now());
        answer.setUpdatedAt(LocalDateTime.now());
        answerMapper.insert(answer);

        // 更新问题回答数
        question.setAnswerCount(question.getAnswerCount() + 1);
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);
    }

    @Override
    public List<ForumAnswerVo> getAnswersByQuestionId(String questionId, Long currentUserId) {
        List<ForumAnswerVo> answers = answerMapper.selectByQuestionId(questionId);
        if (currentUserId != null) {
            for (ForumAnswerVo vo : answers) {
                vo.setLiked(interactionMapper.exists(currentUserId, "answer", vo.getId(), "like"));
            }
        }
        return answers;
    }

    @Override
    @Transactional
    public void toggleLike(String answerId, Long userId) {
        boolean exists = interactionMapper.exists(userId, "answer", answerId, "like");
        ForumAnswer answer = answerMapper.selectById(answerId);
        if (answer == null) throw new RuntimeException("回答不存在");

        if (!exists) {
            UserInteraction interaction = new UserInteraction();
            interaction.setId(UUID.randomUUID().toString());
            interaction.setUserId(userId);
            interaction.setTargetType("answer");
            interaction.setTargetId(answerId);
            interaction.setInteractionType("like");
            interaction.setCreatedAt(LocalDateTime.now());
            interactionMapper.insert(interaction);
            answer.setLikeCount(answer.getLikeCount() + 1);
        } else {
            interactionMapper.delete(userId, "answer", answerId, "like");
            answer.setLikeCount(answer.getLikeCount() - 1);
        }
        answer.setUpdatedAt(LocalDateTime.now());
        answerMapper.updateById(answer);
    }

    @Override
    @Transactional
    public void acceptAnswer(String answerId, Long userId) {
        ForumAnswer answer = answerMapper.selectById(answerId);
        if (answer == null) throw new RuntimeException("回答不存在");
        ForumQuestion question = questionMapper.selectById(answer.getQuestionId());
        if (question == null) throw new RuntimeException("问题不存在");
        // 只有问题作者可以采纳
        if (!question.getUserId().equals(userId)) throw new RuntimeException("无权限采纳");

        // 如果已有采纳的回答，先取消其采纳状态
        if (question.getIsResolved()) {
            // 使用 ForumAnswerVo 列表接收
            List<ForumAnswerVo> answers = answerMapper.selectByQuestionId(question.getId());
            for (ForumAnswerVo a : answers) {
                if (a.getIsAccepted()) {
                    // 查询完整的 ForumAnswer 实体并更新
                    ForumAnswer existing = answerMapper.selectById(a.getId());
                    existing.setIsAccepted(false);
                    existing.setUpdatedAt(LocalDateTime.now());
                    answerMapper.updateById(existing);
                    break;
                }
            }
        }
        answer.setIsAccepted(true);
        answer.setUpdatedAt(LocalDateTime.now());
        answerMapper.updateById(answer);

        question.setIsResolved(true);
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);
    }

    @Override
    @Transactional
    public void deleteAnswer(String answerId, Long userId) {
        ForumAnswer answer = answerMapper.selectById(answerId);
        if (answer == null) throw new RuntimeException("回答不存在");
        if (!answer.getUserId().equals(userId)) throw new RuntimeException("无权限删除");
        answer.setStatus(1);
        answer.setUpdatedAt(LocalDateTime.now());
        answerMapper.updateById(answer);

        // 更新问题的回答数
        ForumQuestion question = questionMapper.selectById(answer.getQuestionId());
        if (question != null) {
            question.setAnswerCount(question.getAnswerCount() - 1);
            question.setUpdatedAt(LocalDateTime.now());
            questionMapper.updateById(question);
        }
    }
}