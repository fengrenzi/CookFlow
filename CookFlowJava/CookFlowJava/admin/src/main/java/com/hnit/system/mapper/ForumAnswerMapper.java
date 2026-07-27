package com.hnit.system.mapper;

import com.hnit.system.domain.ForumAnswer;
import com.hnit.system.domain.vo.ForumAnswerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ForumAnswerMapper {
    int insert(ForumAnswer answer);
    ForumAnswer selectById(@Param("id") String id);
    int updateById(ForumAnswer answer);
    List<ForumAnswerVo> selectByQuestionId(@Param("questionId") String questionId);

    List<ForumAnswer> selectByUserId(@Param("userId") Long userId);
    List<ForumAnswer> selectByIds(@Param("ids") List<String> ids);
}