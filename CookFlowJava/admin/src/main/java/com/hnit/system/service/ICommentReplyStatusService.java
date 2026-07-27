package com.hnit.system.service;

import com.hnit.system.domain.vo.ReplyVO;
import java.util.List;

public interface ICommentReplyStatusService {
    int countUnread(Long userId);
    List<ReplyVO> getReplyList(Long userId);
    void markAsRead(Long userId, List<String> commentIds);
}