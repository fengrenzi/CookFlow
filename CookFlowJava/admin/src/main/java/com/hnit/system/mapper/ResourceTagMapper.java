package com.hnit.system.mapper;

import com.hnit.system.domain.ResourceTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ResourceTagMapper {
    int insert(ResourceTag resourceTag);

    List<ResourceTag> selectList(@Param("resourceType") String resourceType,
                                 @Param("resourceId") String resourceId);
}