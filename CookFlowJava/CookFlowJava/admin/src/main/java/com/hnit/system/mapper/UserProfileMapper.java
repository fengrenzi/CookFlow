package com.hnit.system.mapper;

import com.hnit.system.domain.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserProfileMapper {
    int insert(UserProfile record);
    int updateByUserId(UserProfile record);
    UserProfile selectByUserId(@Param("userId") Long userId);
    int deleteByUserId(@Param("userId") Long userId);
}