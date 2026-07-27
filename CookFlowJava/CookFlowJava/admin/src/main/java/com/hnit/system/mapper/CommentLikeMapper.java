package com.hnit.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentLikeMapper {
    int exists(@Param("commentId") String commentId, @Param("userId") Long userId);

    void insert(@Param("commentId") String commentId, @Param("userId") Long userId);

    void delete(@Param("commentId") String commentId, @Param("userId") Long userId);
}