package com.hnit.system.mapper;

import com.hnit.system.domain.CommentReplyStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CommentReplyStatusMapper {
    int insert(CommentReplyStatus record);
    int updateReadStatus(@Param("commentId") String commentId, @Param("targetUserId") Long targetUserId, @Param("isRead") Integer isRead);
    int countUnreadByUserId(@Param("targetUserId") Long targetUserId);
    List<CommentReplyStatus> selectByTargetUserId(@Param("targetUserId") Long targetUserId);
    CommentReplyStatus selectByCommentAndUser(@Param("commentId") String commentId, @Param("targetUserId") Long targetUserId);
}