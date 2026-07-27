package com.hnit.system.service.impl;

import com.hnit.system.domain.ActivityParticipants;
import com.hnit.system.domain.ActivitySuggestions;
import com.hnit.system.domain.ForumActivity;
import com.hnit.system.domain.vo.ActivitySimpleVO;
import com.hnit.system.mapper.ActivityParticipantsMapper;
import com.hnit.system.mapper.ActivitySuggestionsMapper;
import com.hnit.system.mapper.ForumActivityMapper;
import com.hnit.system.service.IUserActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserActivityServiceImpl implements IUserActivityService {

    @Resource
    private ForumActivityMapper activityMapper;
    @Resource
    private ActivityParticipantsMapper participantsMapper;
    @Resource
    private ActivitySuggestionsMapper suggestionsMapper;

    @Override
    public List<ActivitySimpleVO> getOrganizedActivities(Long userId) {
        List<ForumActivity> activities = activityMapper.selectByUserId(userId);
        return activities.stream().map(this::convertToActivityVO).collect(Collectors.toList());
    }

    @Override
    public List<ActivitySimpleVO> getJoinedActivities(Long userId) {
        List<ActivityParticipants> participants = participantsMapper.selectByUserId(userId);
        if (participants.isEmpty()) return Collections.emptyList();
        List<String> activityIds = participants.stream().map(ActivityParticipants::getActivityId).collect(Collectors.toList());
        List<ForumActivity> activities = activityMapper.selectByIds(activityIds);
        return activities.stream().map(this::convertToActivityVO).collect(Collectors.toList());
    }

    @Override
    public List<ActivitySimpleVO> getSuggestedActivities(Long userId) {
        List<ActivitySuggestions> suggestions = suggestionsMapper.selectByUserId(userId);
        return suggestions.stream().map(this::convertToSuggestionVO).collect(Collectors.toList());
    }

    private ActivitySimpleVO convertToActivityVO(ForumActivity a) {
        ActivitySimpleVO vo = new ActivitySimpleVO();
        vo.setId(a.getId());
        vo.setTitle(a.getTitle());
        vo.setDate(a.getStartTime());
        vo.setLocation("");
        vo.setParticipants(a.getParticipantCount());
        vo.setStatus(a.getStatus() == 0 ? "进行中" : "已结束");
        vo.setOrganizer("用户" + a.getUserId());
        return vo;
    }

    private ActivitySimpleVO convertToSuggestionVO(ActivitySuggestions s) {
        ActivitySimpleVO vo = new ActivitySimpleVO();
        vo.setId(s.getId());
        vo.setTitle(s.getTitle());
        vo.setSuggestion(s.getContent());
        vo.setDate(s.getCreateTime());
        String statusText = s.getStatus() == 0 ? "待审核" : (s.getStatus() == 1 ? "已采纳" : "已拒绝");
        vo.setStatus(statusText);
        return vo;
    }
}