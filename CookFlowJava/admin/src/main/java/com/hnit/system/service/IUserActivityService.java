package com.hnit.system.service;

import com.hnit.system.domain.vo.ActivitySimpleVO;
import java.util.List;

public interface IUserActivityService {
    List<ActivitySimpleVO> getOrganizedActivities(Long userId);
    List<ActivitySimpleVO> getJoinedActivities(Long userId);
    List<ActivitySimpleVO> getSuggestedActivities(Long userId);
}