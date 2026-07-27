package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.vo.ActivitySimpleVO;
import com.hnit.system.service.IUserActivityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class UserActivityController extends BaseController {

    @Resource
    private IUserActivityService userActivityService;

    private static final Long TEMP_USER_ID = 1L;

    @GetMapping("/organized")
    public AjaxResult getOrganized() {
        List<ActivitySimpleVO> list = userActivityService.getOrganizedActivities(TEMP_USER_ID);
        return success(list);
    }

    @GetMapping("/joined")
    public AjaxResult getJoined() {
        List<ActivitySimpleVO> list = userActivityService.getJoinedActivities(TEMP_USER_ID);
        return success(list);
    }

    @GetMapping("/suggestions/my")
    public AjaxResult getSuggested() {
        List<ActivitySimpleVO> list = userActivityService.getSuggestedActivities(TEMP_USER_ID);
        return success(list);
    }
}