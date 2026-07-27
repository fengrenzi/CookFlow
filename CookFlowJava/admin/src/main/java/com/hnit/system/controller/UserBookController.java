package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.vo.BookSimpleVO;
import com.hnit.system.service.IUserBookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user/books")
public class UserBookController extends BaseController {

    @Resource
    private IUserBookService userBookService;

    private static final Long TEMP_USER_ID = 1L;

    @GetMapping("/published")
    public AjaxResult getPublished() {
        List<BookSimpleVO> list = userBookService.getPublishedBooks(TEMP_USER_ID);
        return success(list);
    }

    @GetMapping("/collected")
    public AjaxResult getCollected() {
        List<BookSimpleVO> list = userBookService.getCollectedBooks(TEMP_USER_ID);
        return success(list);
    }

    @GetMapping("/history")
    public AjaxResult getReadingHistory() {
        List<BookSimpleVO> list = userBookService.getReadingHistory(TEMP_USER_ID);
        return success(list);
    }
}