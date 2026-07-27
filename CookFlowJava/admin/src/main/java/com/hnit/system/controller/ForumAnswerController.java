package com.hnit.system.controller;

import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.dto.ForumAnswerDto;
import com.hnit.system.domain.vo.ForumAnswerVo;
import com.hnit.system.service.IForumAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/forum/answers")
@RequiredArgsConstructor
public class ForumAnswerController {

    @Resource
    private IForumAnswerService answerService;

    @PostMapping("/question/{questionId}")
    public AjaxResult create(@PathVariable String questionId, @Valid @RequestBody ForumAnswerDto dto) {
        answerService.createAnswer(questionId, dto, getCurrentUserId());
        return AjaxResult.success();
    }

    @GetMapping("/question/{questionId}")
    public AjaxResult listByQuestion(@PathVariable String questionId) {
        List<ForumAnswerVo> answers = answerService.getAnswersByQuestionId(questionId, getCurrentUserId());
        return AjaxResult.success(answers);
    }

    @PostMapping("/{id}/like")
    public AjaxResult like(@PathVariable String id) {
        answerService.toggleLike(id, getCurrentUserId());
        return AjaxResult.success();
    }

    @PostMapping("/{id}/accept")
    public AjaxResult accept(@PathVariable String id) {
        answerService.acceptAnswer(id, getCurrentUserId());
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable String id) {
        answerService.deleteAnswer(id, getCurrentUserId());
        return AjaxResult.success();
    }

    private Long getCurrentUserId() {
        return 1L;
    }
}