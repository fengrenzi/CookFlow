package com.hnit.system.mapper;

import com.hnit.system.domain.ForumShare;
import com.hnit.system.domain.vo.ForumShareVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ForumShareMapper {
    int insert(ForumShare share);
    ForumShare selectById(@Param("id") String id);
    int updateById(ForumShare share);
    List<ForumShareVo> selectPageList(@Param("offset") int offset,
                                      @Param("limit") int limit,
                                      @Param("category") String category,
                                      @Param("sortBy") String sortBy);
    int selectCountByStatus(@Param("status") int status);
}