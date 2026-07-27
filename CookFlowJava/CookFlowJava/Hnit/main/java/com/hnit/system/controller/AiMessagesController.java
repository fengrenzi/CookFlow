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
import com.hnit.system.domain.AiMessages;
import com.hnit.system.service.IAiMessagesService;
import com.hnit.common.utils.poi.ExcelUtil;
import com.hnit.common.core.page.TableDataInfo;

/**
 * AI 会话消息（按消息存储）Controller
 * 
 * @author hnit
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/AiMessages")
public class AiMessagesController extends BaseController
{
    @Resource
    private IAiMessagesService aiMessagesService;

    /**
     * 查询AI 会话消息（按消息存储）列表
     */
    @PreAuthorize("@ss.hasPermi('system:AiMessages:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiMessages aiMessages)
    {
        startPage();
        List<AiMessages> list = aiMessagesService.selectAiMessagesList(aiMessages);
        return getDataTable(list);
    }

    /**
     * 导出AI 会话消息（按消息存储）列表
     */
    @PreAuthorize("@ss.hasPermi('system:AiMessages:export')")
    @Log(title = "AI 会话消息（按消息存储）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiMessages aiMessages)
    {
        List<AiMessages> list = aiMessagesService.selectAiMessagesList(aiMessages);
        ExcelUtil<AiMessages> util = new ExcelUtil<AiMessages>(AiMessages.class);
        util.exportExcel(response, list, "AI 会话消息（按消息存储）数据");
    }

    /**
     * 获取AI 会话消息（按消息存储）详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:AiMessages:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aiMessagesService.selectAiMessagesById(id));
    }

    /**
     * 新增AI 会话消息（按消息存储）
     */
    @PreAuthorize("@ss.hasPermi('system:AiMessages:add')")
    @Log(title = "AI 会话消息（按消息存储）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiMessages aiMessages)
    {
        return toAjax(aiMessagesService.insertAiMessages(aiMessages));
    }

    /**
     * 修改AI 会话消息（按消息存储）
     */
    @PreAuthorize("@ss.hasPermi('system:AiMessages:edit')")
    @Log(title = "AI 会话消息（按消息存储）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiMessages aiMessages)
    {
        return toAjax(aiMessagesService.updateAiMessages(aiMessages));
    }

    /**
     * 删除AI 会话消息（按消息存储）
     */
    @PreAuthorize("@ss.hasPermi('system:AiMessages:remove')")
    @Log(title = "AI 会话消息（按消息存储）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aiMessagesService.deleteAiMessagesByIds(ids));
    }
}
