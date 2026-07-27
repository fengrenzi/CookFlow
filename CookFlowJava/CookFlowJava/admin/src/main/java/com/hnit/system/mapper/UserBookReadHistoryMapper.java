package com.hnit.system.mapper;

import com.hnit.system.domain.UserBookReadHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserBookReadHistoryMapper {
    int insert(UserBookReadHistory record);
    int updateProgress(@Param("userId") Long userId, @Param("bookId") String bookId, @Param("progress") Integer progress);
    List<UserBookReadHistory> selectByUserId(@Param("userId") Long userId);
    UserBookReadHistory selectByUserAndBook(@Param("userId") Long userId, @Param("bookId") String bookId);
}