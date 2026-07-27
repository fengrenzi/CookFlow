package com.hnit.system.service.impl;

import com.hnit.system.domain.ImagesRecord;
import com.hnit.system.mapper.ImagesRecordMapper;
import com.hnit.system.service.IImagesRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ImagesRecordServiceImpl implements IImagesRecordService {
    @Resource
    private ImagesRecordMapper imagesRecordMapper;

    @Override
    public List<ImagesRecord> selectByRecipeId(String recipeId) {
        return imagesRecordMapper.selectByRecipeId(recipeId);
    }

    @Override
    public int insert(ImagesRecord record) {
        if (record.getId() == null) {
            record.setId(UUID.randomUUID().toString());
        }
        if (record.getCreateTime() == null) {
            record.setCreateTime(LocalDateTime.now());
        }
        return imagesRecordMapper.insert(record);
    }

    @Override
    @Transactional
    public int batchInsert(List<ImagesRecord> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        for (ImagesRecord record : list) {
            if (record.getId() == null) {
                record.setId(UUID.randomUUID().toString());
            }
            if (record.getCreateTime() == null) {
                record.setCreateTime(LocalDateTime.now());
            }
        }
        return imagesRecordMapper.batchInsert(list);
    }

    @Override
    public int update(ImagesRecord record) {
        return imagesRecordMapper.update(record);
    }

    @Override
    public int deleteById(String id) {
        return imagesRecordMapper.deleteById(id);
    }

    @Override
    public int deleteByRecipeId(String recipeId) {
        return imagesRecordMapper.deleteByRecipeId(recipeId);
    }
}