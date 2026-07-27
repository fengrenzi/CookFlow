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
import com.hnit.system.domain.AiConversations;
import com.hnit.system.service.IAiConversationsService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * AI 会话元信息Controller
 * 
 * @author Z
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/AiConversations")
public class AiConversationsController extends BaseController
{
    @Resource
    private IAiConversationsService aiConversationsService;

    /**
     * 查询AI 会话元信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:AiConversations:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiConversations aiConversations)
    {
        startPage();
        List<AiConversations> list = aiConversationsService.selectAiConversationsList(aiConversations);
        return getDataTable(list);
    }

    /**
     * 导出AI 会话元信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:AiConversations:export')")
    @Log(title = "AI 会话元信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiConversations aiConversations)
    {
        List<AiConversations> list = aiConversationsService.selectAiConversationsList(aiConversations);
        ExcelUtil<AiConversations> util = new ExcelUtil<AiConversations>(AiConversations.class);
        util.exportExcel(response, list, "AI 会话元信息数据");
    }

    /**
     * 获取AI 会话元信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:AiConversations:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aiConversationsService.selectAiConversationsById(id));
    }

    /**
     * 新增AI 会话元信息
     */
    @PreAuthorize("@ss.hasPermi('system:AiConversations:add')")
    @Log(title = "AI 会话元信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiConversations aiConversations)
    {
        return toAjax(aiConversationsService.insertAiConversations(aiConversations));
    }

    /**
     * 修改AI 会话元信息
     */
    @PreAuthorize("@ss.hasPermi('system:AiConversations:edit')")
    @Log(title = "AI 会话元信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiConversations aiConversations)
    {
        return toAjax(aiConversationsService.updateAiConversations(aiConversations));
    }

    /**
     * 删除AI 会话元信息
     */
    @PreAuthorize("@ss.hasPermi('system:AiConversations:remove')")
    @Log(title = "AI 会话元信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aiConversationsService.deleteAiConversationsByIds(ids));
    }
}
