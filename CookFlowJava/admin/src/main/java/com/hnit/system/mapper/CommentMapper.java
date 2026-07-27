package com.hnit.system.mapper;

import com.hnit.system.domain.Comment;
import com.hnit.system.domain.dto.CommentQueryDto;
import com.hnit.system.domain.vo.CommentReplyVo;
import com.hnit.system.domain.vo.CommentVo;
import com.hnit.system.domain.vo.ReplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<CommentVo> selectCommentList(@Param("query") CommentQueryDto query,
                                      @Param("offset") int offset,
                                      @Param("currentUserId") Long currentUserId);

    int countCommentList(@Param("resourceType") String resourceType,
                         @Param("resourceId") String resourceId);

    Comment selectById(@Param("id") String id);

    void insert(Comment comment);

    void incrementReplyCount(@Param("id") String id);

    void softDelete(@Param("id") String id);

    void updateLikes(@Param("id") String id, @Param("increment") int increment);

    List<CommentReplyVo> selectRepliesByParentId(@Param("parentId") String parentId,
                                                 @Param("limit") int limit,
                                                 @Param("currentUserId") Long currentUserId);
    List<ReplyVO> selectRepliesByIds(@Param("ids") List<String> ids);
}