package com.hnit.system.mapper;

import com.hnit.system.domain.ForumActivity;
import com.hnit.system.domain.vo.ForumActivityVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ForumActivityMapper {
    int insert(ForumActivity activity);
    ForumActivity selectById(@Param("id") String id);
    int updateById(ForumActivity activity);
    List<ForumActivityVo> selectPageList(@Param("offset") int offset,
                                         @Param("limit") int limit,
                                         @Param("category") String category,
                                         @Param("sortBy") String sortBy);
    int selectCountByStatus(@Param("status") int status);

    List<ForumActivity> selectByUserId(@Param("userId") Long userId);
    List<ForumActivity> selectByIds(@Param("ids") List<String> ids);
}