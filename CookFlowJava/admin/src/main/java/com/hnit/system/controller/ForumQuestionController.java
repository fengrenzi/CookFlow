package com.hnit.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.dto.ForumQuestionDto;
import com.hnit.system.domain.vo.ForumQuestionVo;
import com.hnit.system.service.IForumQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/forum/questions")
@RequiredArgsConstructor
public class ForumQuestionController {

    @Resource
    private IForumQuestionService questionService;

    @PostMapping
    public AjaxResult create(@Valid @RequestBody ForumQuestionDto dto) {
        questionService.createQuestion(dto, getCurrentUserId());
        return AjaxResult.success();
    }

    @GetMapping
    public AjaxResult list(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "12") int size,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "latest") String sortBy) {
        Page<ForumQuestionVo> pageResult = questionService.listQuestions(page, size, keyword, sortBy, getCurrentUserId());
        return AjaxResult.success(pageResult);
    }

    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable String id) {
        return AjaxResult.success(questionService.getQuestionDetail(id, getCurrentUserId()));
    }

    @PostMapping("/{id}/favorite")
    public AjaxResult favorite(@PathVariable String id) {
        questionService.toggleFavorite(id, getCurrentUserId());
        return AjaxResult.success();
    }

    @PostMapping("/{id}/follow")
    public AjaxResult follow(@PathVariable String id) {
        questionService.toggleFollow(id, getCurrentUserId());
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable String id) {
        questionService.deleteQuestion(id, getCurrentUserId());
        return AjaxResult.success();
    }

    private Long getCurrentUserId() {
        // 实际从 SecurityContext 获取
        return 1L;
    }
}