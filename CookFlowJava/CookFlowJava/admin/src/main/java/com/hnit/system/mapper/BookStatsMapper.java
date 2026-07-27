package com.hnit.system.mapper;

import com.hnit.system.domain.BookStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookStatsMapper {
    BookStats selectByBookId(@Param("bookId") String bookId);
    void insert(BookStats stats);
    void update(BookStats stats);
}