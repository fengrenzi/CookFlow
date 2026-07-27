package com.hnit.system.controller;

import com.hnit.common.core.domain.AjaxResult;
import com.hnit.common.core.page.TableDataInfo;
import com.hnit.framework.web.service.TokenService;
import com.hnit.system.domain.dto.CommentDto;
import com.hnit.system.domain.dto.CommentQueryDto;
import com.hnit.system.service.ICommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Resource
    private ICommentService commentService;
    @Resource
    private TokenService tokenService;

    @GetMapping
    public TableDataInfo list(CommentQueryDto query, HttpServletRequest request) {
        Long currentUserId = 1L;
        return commentService.selectCommentList(query, currentUserId);
    }

    @PostMapping
    public AjaxResult add(@RequestBody CommentDto commentDto, HttpServletRequest request) {
        Long currentUserId = 1L;
        commentService.addComment(commentDto, currentUserId);
        return AjaxResult.success();
    }

    @PostMapping("/{id}/like")
    public AjaxResult like(@PathVariable String id, HttpServletRequest request) {
        Long currentUserId = 1L;
        commentService.likeComment(id, currentUserId);
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable String id, HttpServletRequest request) {
        Long currentUserId = 1L;
        commentService.deleteComment(id, currentUserId);
        return AjaxResult.success();
    }
}