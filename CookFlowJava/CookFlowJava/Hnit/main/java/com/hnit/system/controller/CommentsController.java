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
import com.hnit.system.domain.Comments;
import com.hnit.system.service.ICommentsService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * 评论（支持@/回复/状态）Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/Comments")
public class CommentsController extends BaseController
{
    @Resource
    private ICommentsService commentsService;

    /**
     * 查询评论（支持@/回复/状态）列表
     */
    @PreAuthorize("@ss.hasPermi('system:Comments:list')")
    @GetMapping("/list")
    public TableDataInfo list(Comments comments)
    {
        startPage();
        List<Comments> list = commentsService.selectCommentsList(comments);
        return getDataTable(list);
    }

    /**
     * 导出评论（支持@/回复/状态）列表
     */
    @PreAuthorize("@ss.hasPermi('system:Comments:export')")
    @Log(title = "评论（支持@/回复/状态）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Comments comments)
    {
        List<Comments> list = commentsService.selectCommentsList(comments);
        ExcelUtil<Comments> util = new ExcelUtil<Comments>(Comments.class);
        util.exportExcel(response, list, "评论（支持@/回复/状态）数据");
    }

    /**
     * 获取评论（支持@/回复/状态）详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:Comments:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(commentsService.selectCommentsById(id));
    }

    /**
     * 新增评论（支持@/回复/状态）
     */
    @PreAuthorize("@ss.hasPermi('system:Comments:add')")
    @Log(title = "评论（支持@/回复/状态）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Comments comments)
    {
        return toAjax(commentsService.insertComments(comments));
    }

    /**
     * 修改评论（支持@/回复/状态）
     */
    @PreAuthorize("@ss.hasPermi('system:Comments:edit')")
    @Log(title = "评论（支持@/回复/状态）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Comments comments)
    {
        return toAjax(commentsService.updateComments(comments));
    }

    /**
     * 删除评论（支持@/回复/状态）
     */
    @PreAuthorize("@ss.hasPermi('system:Comments:remove')")
    @Log(title = "评论（支持@/回复/状态）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(commentsService.deleteCommentsByIds(ids));
    }
}
