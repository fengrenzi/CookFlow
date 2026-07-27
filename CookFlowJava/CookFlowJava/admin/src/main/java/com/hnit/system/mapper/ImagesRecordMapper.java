package com.hnit.system.mapper;

import com.hnit.system.domain.ImagesRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImagesRecordMapper {
    List<ImagesRecord> selectByRecipeId(@Param("recipeId") String recipeId);

    int insert(ImagesRecord record);

    int batchInsert(List<ImagesRecord> list);

    int update(ImagesRecord record);

    int deleteById(@Param("id") String id);

    int deleteByRecipeId(@Param("recipeId") String recipeId);

    List<ImagesRecord> selectByTableAndRecordId(@Param("tableName") String tableName,
                                                @Param("recordId") String recordId);


    List<ImagesRecord> selectList(@Param("tableName") String tableName,
                                  @Param("recipeId") String recipeId);

    List<ImagesRecord> selectListByRecipeIds(@Param("tableName") String tableName, @Param("recipeIds") List<String> recipeIds);
}