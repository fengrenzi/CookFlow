package com.hnit.system.service;

import com.hnit.system.domain.vo.QuestionSimpleVO;
import com.hnit.system.domain.vo.AnswerSimpleVO;
import java.util.List;

public interface IUserQAService {
    List<QuestionSimpleVO> getMyQuestions(Long userId);
    List<AnswerSimpleVO> getMyAnswers(Long userId);
    List<QuestionSimpleVO> getFollowedQuestions(Long userId);
    List<QuestionSimpleVO> getCollectedQuestions(Long userId);
}