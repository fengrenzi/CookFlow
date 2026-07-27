package com.hnit.system.service.impl;

import com.hnit.system.domain.ImageManagement;
import com.hnit.system.mapper.ImageManagementMapper;
import com.hnit.system.service.IImageManagementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ImageManagementServiceImpl implements IImageManagementService {
    @Resource
    private ImageManagementMapper imageManagementMapper;

    @Override
    public List<ImageManagement> selectImageManagementList(ImageManagement imageManagement) {
        return imageManagementMapper.selectImageManagementList(imageManagement);
    }

    @Override
    public ImageManagement selectImageManagementById(String id) {
        return imageManagementMapper.selectImageManagementById(id);
    }

    @Override
    public int insertImageManagement(ImageManagement imageManagement) {
        return imageManagementMapper.insertImageManagement(imageManagement);
    }

    @Override
    public int updateImageManagement(ImageManagement imageManagement) {
        return imageManagementMapper.updateImageManagement(imageManagement);
    }

    @Override
    public int deleteImageManagementByIds(String[] ids) {
        return imageManagementMapper.deleteImageManagementByIds(ids);
    }

    @Override
    public ImageManagement selectImageManagementByStoredName(String storedName) {
        return imageManagementMapper.selectImageManagementByStoredName(storedName);
    }
}