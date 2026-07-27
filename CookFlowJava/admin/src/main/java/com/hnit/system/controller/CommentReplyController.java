package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.common.core.page.TableDataInfo;
import com.hnit.system.domain.vo.ReplyVO;
import com.hnit.system.service.ICommentReplyStatusService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/replies")
public class CommentReplyController extends BaseController {

    @Resource
    private ICommentReplyStatusService replyStatusService;

    private static final Long TEMP_USER_ID = 1L;

    @GetMapping("/unread/count")
    public AjaxResult unreadCount() {
        int count = replyStatusService.countUnread(TEMP_USER_ID);
        return success(count);
    }

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        startPage();
        List<ReplyVO> list = replyStatusService.getReplyList(TEMP_USER_ID);
        return getDataTable(list);
    }

    @PutMapping("/mark-read")
    public AjaxResult markAsRead(@RequestBody List<String> commentIds) {
        replyStatusService.markAsRead(TEMP_USER_ID, commentIds);
        return success();
    }
}