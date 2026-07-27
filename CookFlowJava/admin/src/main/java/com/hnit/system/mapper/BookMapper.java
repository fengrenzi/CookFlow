package com.hnit.system.mapper;

import com.hnit.system.domain.Books;
import com.hnit.system.domain.dto.BookQueryDto;
import com.hnit.system.domain.vo.BookVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {
    BookVo selectById(@Param("id") String id);

    List<BookVo> selectPage(BookQueryDto query);

    int countPage(BookQueryDto query);

    List<Books> selectByUserId(@Param("userId") Long userId);
    List<Books> selectByIds(@Param("ids") List<String> ids);
}