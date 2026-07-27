package com.hnit.system.controller;

import com.hnit.common.core.domain.AjaxResult;
import com.hnit.common.core.page.TableDataInfo;
import com.hnit.system.domain.dto.BookQueryDto;
import com.hnit.system.service.IBookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/books")
public class BookController {

    @Resource
    private IBookService bookService;

    @GetMapping("/list")
    public TableDataInfo list(BookQueryDto query) {
        return bookService.listBooks(query);
    }

    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable String id) {
        return AjaxResult.success(bookService.getBookDetail(id));
    }

    // 热销书籍（按推荐排序）
    @GetMapping("/hot")
    public AjaxResult hotBooks(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "6") Integer pageSize) {
        BookQueryDto query = new BookQueryDto();
        query.setSortBy("recommended");
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        return AjaxResult.success(bookService.listBooks(query));
    }

    // 阅读热榜（按阅读人数排序）
    @GetMapping("/ranking")
    public AjaxResult readingRank(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        BookQueryDto query = new BookQueryDto();
        query.setSortBy("reading_count_desc");
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        return AjaxResult.success(bookService.listBooks(query));
    }

    // 推荐书籍（按评分排序）
    @GetMapping("/recommend")
    public AjaxResult recommend(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "4") Integer pageSize) {
        BookQueryDto query = new BookQueryDto();
        query.setSortBy("rating_desc");
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        return AjaxResult.success(bookService.listBooks(query));
    }
}