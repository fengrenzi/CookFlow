package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.dto.AddReadHistoryDTO;
import com.hnit.system.domain.vo.ReadHistoryVO;
import com.hnit.system.service.IUserBookReadHistoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/read-history")
public class UserBookReadHistoryController extends BaseController {

    @Resource
    private IUserBookReadHistoryService historyService;

    private static final Long TEMP_USER_ID = 1L;

    @PostMapping
    public AjaxResult saveOrUpdate(@Valid @RequestBody AddReadHistoryDTO dto) {
        historyService.saveOrUpdateProgress(TEMP_USER_ID, dto);
        return success();
    }

    @GetMapping
    public AjaxResult list() {
        List<ReadHistoryVO> list = historyService.getHistory(TEMP_USER_ID);
        return success(list);
    }
}