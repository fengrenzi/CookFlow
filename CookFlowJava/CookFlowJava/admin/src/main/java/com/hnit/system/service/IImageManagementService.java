package com.hnit.system.service;

import com.hnit.system.domain.ImageManagement;

import java.util.List;

public interface IImageManagementService {
    List<ImageManagement> selectImageManagementList(ImageManagement imageManagement);

    ImageManagement selectImageManagementById(String id);

    int insertImageManagement(ImageManagement imageManagement);

    int updateImageManagement(ImageManagement imageManagement);

    int deleteImageManagementByIds(String[] ids);

    ImageManagement selectImageManagementByStoredName(String storedName);
}