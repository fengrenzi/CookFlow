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
import com.hnit.system.domain.BookRecipes;
import com.hnit.system.service.IBookRecipesService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 书籍中页码到菜谱的映射Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/BookRecipes")
public class BookRecipesController extends BaseController
{
    @Resource
    private IBookRecipesService bookRecipesService;

    /**
     * 查询书籍中页码到菜谱的映射列表
     */
    @PreAuthorize("@ss.hasPermi('system:BookRecipes:list')")
    @GetMapping("/list")
    public TableDataInfo list(BookRecipes bookRecipes)
    {
        startPage();
        List<BookRecipes> list = bookRecipesService.selectBookRecipesList(bookRecipes);
        return getDataTable(list);
    }

    /**
     * 导出书籍中页码到菜谱的映射列表
     */
    @PreAuthorize("@ss.hasPermi('system:BookRecipes:export')")
    @Log(title = "书籍中页码到菜谱的映射", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BookRecipes bookRecipes)
    {
        List<BookRecipes> list = bookRecipesService.selectBookRecipesList(bookRecipes);
        ExcelUtil<BookRecipes> util = new ExcelUtil<BookRecipes>(BookRecipes.class);
        util.exportExcel(response, list, "书籍中页码到菜谱的映射数据");
    }

    /**
     * 获取书籍中页码到菜谱的映射详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:BookRecipes:query')")
    @GetMapping(value = "/{bookId}")
    public AjaxResult getInfo(@PathVariable("bookId") String bookId)
    {
        return success(bookRecipesService.selectBookRecipesByBookId(bookId));
    }

    /**
     * 新增书籍中页码到菜谱的映射
     */
    @PreAuthorize("@ss.hasPermi('system:BookRecipes:add')")
    @Log(title = "书籍中页码到菜谱的映射", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BookRecipes bookRecipes)
    {
        return toAjax(bookRecipesService.insertBookRecipes(bookRecipes));
    }

    /**
     * 修改书籍中页码到菜谱的映射
     */
    @PreAuthorize("@ss.hasPermi('system:BookRecipes:edit')")
    @Log(title = "书籍中页码到菜谱的映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BookRecipes bookRecipes)
    {
        return toAjax(bookRecipesService.updateBookRecipes(bookRecipes));
    }

    /**
     * 删除书籍中页码到菜谱的映射
     */
    @PreAuthorize("@ss.hasPermi('system:BookRecipes:remove')")
    @Log(title = "书籍中页码到菜谱的映射", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bookIds}")
    public AjaxResult remove(@PathVariable String[] bookIds)
    {
        return toAjax(bookRecipesService.deleteBookRecipesByBookIds(bookIds));
    }
}
