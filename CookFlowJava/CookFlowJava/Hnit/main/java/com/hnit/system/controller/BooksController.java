package com.hnit.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hnit.common.annotation.Log;
import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.common.enums.BusinessType;
import com.hnit.system.domain.Books;
import com.hnit.system.service.IBooksService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 书籍Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/Books")
public class BooksController extends BaseController
{
    @Resource
    private IBooksService booksService;

    /**
     * 查询书籍列表
     */
    @PreAuthorize("@ss.hasPermi('system:Books:list')")
    @GetMapping("/list")
    public TableDataInfo list(Books books)
    {
        startPage();
        List<Books> list = booksService.selectBooksList(books);
        return getDataTable(list);
    }

    /**
     * 导出书籍列表
     */
    @PreAuthorize("@ss.hasPermi('system:Books:export')")
    @Log(title = "书籍", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Books books)
    {
        List<Books> list = booksService.selectBooksList(books);
        ExcelUtil<Books> util = new ExcelUtil<Books>(Books.class);
        util.exportExcel(response, list, "书籍数据");
    }

    /**
     * 获取书籍详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:Books:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(booksService.selectBooksById(id));
    }

    /**
     * 新增书籍
     */
    @PreAuthorize("@ss.hasPermi('system:Books:add')")
    @Log(title = "书籍", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Books books)
    {
        return toAjax(booksService.insertBooks(books));
    }

    /**
     * 修改书籍
     */
    @PreAuthorize("@ss.hasPermi('system:Books:edit')")
    @Log(title = "书籍", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Books books)
    {
        return toAjax(booksService.updateBooks(books));
    }

    /**
     * 删除书籍
     */
    @PreAuthorize("@ss.hasPermi('system:Books:remove')")
    @Log(title = "书籍", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(booksService.deleteBooksByIds(ids));
    }
}
