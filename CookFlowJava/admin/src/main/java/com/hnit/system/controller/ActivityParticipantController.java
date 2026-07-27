package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.ActivityParticipants;
import com.hnit.system.domain.dto.JoinActivityDTO;
import com.hnit.system.service.IActivityParticipantsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/activity/participants")
@RequiredArgsConstructor
public class ActivityParticipantController extends BaseController {
    @Resource
    private IActivityParticipantsService participantsService;

    private static final Long TEMP_USER_ID = 1L;

    @PostMapping("/join")
    public AjaxResult join(@Valid @RequestBody JoinActivityDTO dto) {
        participantsService.joinActivity(TEMP_USER_ID, dto);
        return success();
    }

    @DeleteMapping("/cancel/{activityId}")
    public AjaxResult cancel(@PathVariable String activityId) {
        participantsService.cancelJoinActivity(TEMP_USER_ID, activityId);
        return success();
    }

    @GetMapping("/my")
    public AjaxResult myJoined() {
        List<ActivityParticipants> list = participantsService.getUserJoinedActivities(TEMP_USER_ID);
        return success(list);
    }
}