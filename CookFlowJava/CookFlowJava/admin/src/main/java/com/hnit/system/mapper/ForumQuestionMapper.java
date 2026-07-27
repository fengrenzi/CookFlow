package com.hnit.system.mapper;

import com.hnit.system.domain.ForumQuestion;
import com.hnit.system.domain.vo.ForumQuestionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ForumQuestionMapper {
    int insert(ForumQuestion question);
    ForumQuestion selectById(@Param("id") String id);
    int updateById(ForumQuestion question);
    List<ForumQuestionVo> selectPageList(@Param("offset") int offset,
                                         @Param("limit") int limit,
                                         @Param("keyword") String keyword,
                                         @Param("sortBy") String sortBy);
    int selectCountByStatus(@Param("status") int status);

    // 在 ForumQuestionMapper 中添加
    List<ForumQuestion> selectByUserId(@Param("userId") Long userId);
    List<ForumQuestion> selectByIds(@Param("ids") List<String> ids);
}