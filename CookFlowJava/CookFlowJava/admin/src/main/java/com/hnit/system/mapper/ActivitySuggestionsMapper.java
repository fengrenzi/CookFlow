package com.hnit.system.mapper;

import com.hnit.system.domain.ActivitySuggestions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ActivitySuggestionsMapper {
    int insert(ActivitySuggestions record);
    int updateStatus(@Param("id") String id, @Param("status") Integer status);
    int deleteById(@Param("id") String id);
    ActivitySuggestions selectById(@Param("id") String id);
    List<ActivitySuggestions> selectByStatus(@Param("status") Integer status);

    List<ActivitySuggestions> selectByUserId(@Param("userId") Long userId);
}