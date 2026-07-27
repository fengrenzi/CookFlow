package com.hnit.system.mapper;

import com.hnit.system.domain.ImageManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageManagementMapper {
    List<ImageManagement> selectImageManagementList(ImageManagement imageManagement);
    ImageManagement selectImageManagementById(String id);
    int insertImageManagement(ImageManagement imageManagement);
    int updateImageManagement(ImageManagement imageManagement);
    int deleteImageManagementByIds(String[] ids);

    ImageManagement selectImageManagementByStoredName(@Param("storedName") String storedName);

    ImageManagement selectById(@Param("id") String id);

    List<ImageManagement> selectBatchIds(@Param("ids") List<String> ids);
}