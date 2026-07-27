package com.hnit.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnit.system.domain.dto.ForumActivityDto;
import com.hnit.system.domain.vo.ForumActivityVo;

public interface IForumActivityService {
    void createActivity(ForumActivityDto dto, Long userId);
    Page<ForumActivityVo> listActivities(int page, int size, String category, String sortBy, Long currentUserId);
    ForumActivityVo getActivityDetail(String id, Long currentUserId);
    void joinActivity(String activityId, Long userId);
    void cancelJoinActivity(String activityId, Long userId);
    void deleteActivity(String id, Long userId);
}