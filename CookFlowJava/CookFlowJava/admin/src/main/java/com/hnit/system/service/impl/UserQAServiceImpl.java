package com.hnit.system.service.impl;

import com.hnit.system.domain.ForumAnswer;
import com.hnit.system.domain.ForumQuestion;
import com.hnit.system.domain.UserInteraction;
import com.hnit.system.domain.vo.AnswerSimpleVO;
import com.hnit.system.domain.vo.QuestionSimpleVO;
import com.hnit.system.mapper.ForumAnswerMapper;
import com.hnit.system.mapper.ForumQuestionMapper;
import com.hnit.system.mapper.UserInteractionMapper;
import com.hnit.system.service.IUserQAService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserQAServiceImpl implements IUserQAService {

    @Resource
    private ForumQuestionMapper questionMapper;
    @Resource
    private ForumAnswerMapper answerMapper;
    @Resource
    private UserInteractionMapper interactionMapper;

    @Override
    public List<QuestionSimpleVO> getMyQuestions(Long userId) {
        List<ForumQuestion> questions = questionMapper.selectByUserId(userId);
        return questions.stream().map(this::convertToQuestionVO).collect(Collectors.toList());
    }

    @Override
    public List<AnswerSimpleVO> getMyAnswers(Long userId) {
        List<ForumAnswer> answers = answerMapper.selectByUserId(userId);
        return answers.stream().map(this::convertToAnswerVO).collect(Collectors.toList());
    }

    @Override
    public List<QuestionSimpleVO> getFollowedQuestions(Long userId) {
        List<UserInteraction> interactions = interactionMapper.selectByUserAndType(userId, "question", "follow");
        if (interactions.isEmpty()) return Collections.emptyList();
        List<String> questionIds = interactions.stream().map(UserInteraction::getTargetId).collect(Collectors.toList());
        List<ForumQuestion> questions = questionMapper.selectByIds(questionIds);
        return questions.stream().map(this::convertToQuestionVO).collect(Collectors.toList());
    }

    @Override
    public List<QuestionSimpleVO> getCollectedQuestions(Long userId) {
        List<UserInteraction> interactions = interactionMapper.selectByUserAndType(userId, "question", "favorite");
        if (interactions.isEmpty()) return Collections.emptyList();
        List<String> questionIds = interactions.stream().map(UserInteraction::getTargetId).collect(Collectors.toList());
        List<ForumQuestion> questions = questionMapper.selectByIds(questionIds);
        return questions.stream().map(this::convertToQuestionVO).collect(Collectors.toList());
    }

    private QuestionSimpleVO convertToQuestionVO(ForumQuestion q) {
        QuestionSimpleVO vo = new QuestionSimpleVO();
        vo.setId(q.getId());
        vo.setTitle(q.getTitle());
        vo.setContent(q.getContent());
        vo.setAnswers(q.getAnswerCount());
        vo.setViews(q.getViewCount().intValue());
        vo.setDate(q.getCreatedAt());
        return vo;
    }

    private AnswerSimpleVO convertToAnswerVO(ForumAnswer a) {
        AnswerSimpleVO vo = new AnswerSimpleVO();
        vo.setId(a.getId());
        // 需要查询问题标题
        ForumQuestion q = questionMapper.selectById(a.getQuestionId());
        vo.setQuestionTitle(q != null ? q.getTitle() : "");
        vo.setContent(a.getContent());
        vo.setLikes(a.getLikeCount());
        vo.setComments(a.getCommentCount());
        vo.setDate(a.getCreatedAt());
        return vo;
    }
}