package com.hnit.system.mapper;

import com.hnit.system.domain.Tags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagsMapper {
    int insert(Tags tags);

    int updateById(Tags tags);

    Tags selectOne(@Param("name") String name, @Param("type") String type);

    List<Tags> selectBatchIds(@Param("ids") List<String> ids);

    List<Tags> selectByTypeAndLimit(@Param("type") String type, @Param("limit") int limit);

    List<Tags> selectHotTags(@Param("limit") int limit, @Param("type") String type);
}