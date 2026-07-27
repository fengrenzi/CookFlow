package com.hnit.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.dto.ForumShareDto;
import com.hnit.system.domain.vo.ForumShareVo;
import com.hnit.system.service.IForumShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
public class ForumShareController {

    @Resource
    private IForumShareService forumShareService;

    @PostMapping
    public AjaxResult createShare(@Valid @RequestBody ForumShareDto dto) {
        Long userId = getCurrentUserId();
        forumShareService.createShare(dto, userId);
        return AjaxResult.success();
    }

    @GetMapping
    public AjaxResult listShares(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "latest") String sortBy) {
        Long currentUserId = getCurrentUserId();
        Page<ForumShareVo> pageResult = forumShareService.listShares(page, size, category, sortBy, currentUserId);
        return AjaxResult.success(pageResult);
    }

    @GetMapping("/{id}")
    public AjaxResult getShareDetail(@PathVariable String id) {
        Long currentUserId = getCurrentUserId();
        ForumShareVo vo = forumShareService.getShareDetail(id, currentUserId);
        return AjaxResult.success(vo);
    }

    @PostMapping("/{id}/like")
    public AjaxResult toggleLike(@PathVariable String id) {
        Long userId = getCurrentUserId();
        forumShareService.toggleLike(id, userId);
        return AjaxResult.success();
    }

    @PostMapping("/{id}/favorite")
    public AjaxResult toggleFavorite(@PathVariable String id) {
        Long userId = getCurrentUserId();
        forumShareService.toggleFavorite(id, userId);
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    public AjaxResult deleteShare(@PathVariable String id) {
        Long userId = getCurrentUserId();
        forumShareService.deleteShare(id, userId);
        return AjaxResult.success();
    }

    private Long getCurrentUserId() {
        // 实际从 SecurityContext 获取
        return 1L;
    }
}