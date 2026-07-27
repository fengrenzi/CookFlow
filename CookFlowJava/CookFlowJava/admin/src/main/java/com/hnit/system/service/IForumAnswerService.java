package com.hnit.system.service;

import com.hnit.system.domain.dto.ForumAnswerDto;
import com.hnit.system.domain.vo.ForumAnswerVo;
import java.util.List;

public interface IForumAnswerService {
    void createAnswer(String questionId, ForumAnswerDto dto, Long userId);
    List<ForumAnswerVo> getAnswersByQuestionId(String questionId, Long currentUserId);
    void toggleLike(String answerId, Long userId);
    void acceptAnswer(String answerId, Long userId);
    void deleteAnswer(String answerId, Long userId);
}