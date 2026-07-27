package com.hnit.system.mapper;

import com.hnit.system.domain.ActivityParticipants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ActivityParticipantsMapper {
    int insert(ActivityParticipants record);
    int updateStatus(@Param("id") String id, @Param("status") Integer status);
    int deleteByActivityAndUser(@Param("activityId") String activityId, @Param("userId") Long userId);
    ActivityParticipants selectByActivityAndUser(@Param("activityId") String activityId, @Param("userId") Long userId);

    List<ActivityParticipants> selectByUserId(@Param("userId") Long userId);
}