package com.hnit.system.service;

import com.hnit.system.domain.dto.JoinActivityDTO;
import com.hnit.system.domain.ActivityParticipants;
import java.util.List;

public interface IActivityParticipantsService {
    void joinActivity(Long userId, JoinActivityDTO dto);
    void cancelJoinActivity(Long userId, String activityId);
    List<ActivityParticipants> getUserJoinedActivities(Long userId);
}