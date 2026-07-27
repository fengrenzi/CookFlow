package com.hnit.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.dto.ForumActivityDto;
import com.hnit.system.domain.vo.ForumActivityVo;
import com.hnit.system.service.IForumActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/forum/activity")
@RequiredArgsConstructor
public class ForumActivityController {

    @Resource
    private IForumActivityService activityService;

    @PostMapping
    public AjaxResult create(@Valid @RequestBody ForumActivityDto dto) {
        activityService.createActivity(dto, getCurrentUserId());
        return AjaxResult.success();
    }

    @GetMapping
    public AjaxResult list(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "12") int size,
                           @RequestParam(required = false) String category,
                           @RequestParam(defaultValue = "latest") String sortBy) {
        Page<ForumActivityVo> pageResult = activityService.listActivities(page, size, category, sortBy, getCurrentUserId());
        return AjaxResult.success(pageResult);
    }

    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable String id) {
        return AjaxResult.success(activityService.getActivityDetail(id, getCurrentUserId()));
    }

    @PostMapping("/{id}/join")
    public AjaxResult join(@PathVariable String id) {
        activityService.joinActivity(id, getCurrentUserId());
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}/join")
    public AjaxResult cancelJoin(@PathVariable String id) {
        activityService.cancelJoinActivity(id, getCurrentUserId());
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable String id) {
        activityService.deleteActivity(id, getCurrentUserId());
        return AjaxResult.success();
    }

    private Long getCurrentUserId() {
        return 1L;
    }
}