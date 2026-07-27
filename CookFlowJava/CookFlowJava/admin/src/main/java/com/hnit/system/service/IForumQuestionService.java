package com.hnit.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnit.system.domain.dto.ForumQuestionDto;
import com.hnit.system.domain.vo.ForumQuestionVo;

public interface IForumQuestionService {
    void createQuestion(ForumQuestionDto dto, Long userId);
    Page<ForumQuestionVo> listQuestions(int page, int size, String keyword, String sortBy, Long currentUserId);
    ForumQuestionVo getQuestionDetail(String id, Long currentUserId);
    void toggleFavorite(String questionId, Long userId);
    void toggleFollow(String questionId, Long userId);
    void deleteQuestion(String id, Long userId);
}