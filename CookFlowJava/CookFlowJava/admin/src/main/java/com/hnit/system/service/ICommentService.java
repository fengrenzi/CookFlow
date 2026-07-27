package com.hnit.system.service;

import com.hnit.common.core.page.TableDataInfo;
import com.hnit.system.domain.dto.CommentDto;
import com.hnit.system.domain.dto.CommentQueryDto;
import com.hnit.system.domain.vo.CommentVo;

public interface ICommentService {
    TableDataInfo selectCommentList(CommentQueryDto query, Long currentUserId);
    CommentVo addComment(CommentDto commentDto, Long currentUserId);
    void likeComment(String commentId, Long currentUserId);
    void deleteComment(String commentId, Long currentUserId);
}