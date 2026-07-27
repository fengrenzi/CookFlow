package com.hnit.system.service;

import com.hnit.system.domain.ImagesRecord;

import java.util.List;

public interface IImagesRecordService {
    List<ImagesRecord> selectByRecipeId(String recipeId);

    int insert(ImagesRecord record);

    int batchInsert(List<ImagesRecord> list);

    int update(ImagesRecord record);

    int deleteById(String id);

    int deleteByRecipeId(String recipeId);
}