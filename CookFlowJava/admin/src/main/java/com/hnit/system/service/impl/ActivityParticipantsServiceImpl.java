package com.hnit.system.service.impl;

import com.hnit.common.utils.uuid.IdUtils;
import com.hnit.system.domain.ActivityParticipants;
import com.hnit.system.domain.dto.JoinActivityDTO;
import com.hnit.system.mapper.ActivityParticipantsMapper;
import com.hnit.system.service.IActivityParticipantsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityParticipantsServiceImpl implements IActivityParticipantsService {
    @Resource
    private ActivityParticipantsMapper participantsMapper;

    @Override
    @Transactional
    public void joinActivity(Long userId, JoinActivityDTO dto) {
        ActivityParticipants exist = participantsMapper.selectByActivityAndUser(dto.getActivityId(), userId);
        if (exist != null) {
            if (exist.getStatus() == 0) {
                participantsMapper.updateStatus(exist.getId(), 1);
            }
            return;
        }
        ActivityParticipants record = new ActivityParticipants();
        record.setId(IdUtils.fastSimpleUUID());
        record.setActivityId(dto.getActivityId());
        record.setUserId(userId);
        record.setStatus(1);
        participantsMapper.insert(record);
    }

    @Override
    @Transactional
    public void cancelJoinActivity(Long userId, String activityId) {
        ActivityParticipants exist = participantsMapper.selectByActivityAndUser(activityId, userId);
        if (exist != null && exist.getStatus() == 1) {
            participantsMapper.updateStatus(exist.getId(), 0);
        }
    }

    @Override
    public List<ActivityParticipants> getUserJoinedActivities(Long userId) {
        return participantsMapper.selectByUserId(userId);
    }
}