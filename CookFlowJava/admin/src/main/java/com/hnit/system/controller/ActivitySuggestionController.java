package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.dto.AddSuggestionDTO;
import com.hnit.system.service.IActivitySuggestionsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/activity/suggestions")
public class ActivitySuggestionController extends BaseController {

    @Resource
    private IActivitySuggestionsService suggestionsService;

    private static final Long TEMP_USER_ID = 1L;

    @PostMapping
    public AjaxResult add(@Valid @RequestBody AddSuggestionDTO dto) {
        suggestionsService.addSuggestion(TEMP_USER_ID, dto);
        return success();
    }
}