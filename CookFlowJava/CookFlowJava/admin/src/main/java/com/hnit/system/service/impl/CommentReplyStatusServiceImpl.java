package com.hnit.system.service.impl;

import com.hnit.system.domain.CommentReplyStatus;
import com.hnit.system.domain.vo.ReplyVO;
import com.hnit.system.mapper.CommentMapper;
import com.hnit.system.mapper.CommentReplyStatusMapper;
import com.hnit.system.service.ICommentReplyStatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentReplyStatusServiceImpl implements ICommentReplyStatusService {

    @Resource
    private CommentReplyStatusMapper replyStatusMapper;
    @Resource
    private CommentMapper commentMapper;

    @Override
    public int countUnread(Long userId) {
        return replyStatusMapper.countUnreadByUserId(userId);
    }

    @Override
    public List<ReplyVO> getReplyList(Long userId) {
        List<CommentReplyStatus> statusList = replyStatusMapper.selectByTargetUserId(userId);
        if (statusList.isEmpty()) return new ArrayList<>();
        List<String> commentIds = statusList.stream()
                .map(CommentReplyStatus::getCommentId)
                .collect(Collectors.toList());
        List<ReplyVO> replies = commentMapper.selectRepliesByIds(commentIds);
        for (ReplyVO reply : replies) {
            statusList.stream()
                    .filter(s -> s.getCommentId().equals(reply.getId()))
                    .findFirst()
                    .ifPresent(s -> reply.setIsRead(s.getIsRead() == 1));
        }
        return replies;
    }

    @Override
    @Transactional
    public void markAsRead(Long userId, List<String> commentIds) {
        for (String commentId : commentIds) {
            CommentReplyStatus record = replyStatusMapper.selectByCommentAndUser(commentId, userId);
            if (record != null && record.getIsRead() == 0) {
                replyStatusMapper.updateReadStatus(commentId, userId, 1);
            }
        }
    }
}