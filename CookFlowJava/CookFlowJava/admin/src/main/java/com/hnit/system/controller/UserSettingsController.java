package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/settings")
public class UserSettingsController extends BaseController {

    @GetMapping
    public AjaxResult getSettings() {
        return success("设置功能开发中");
    }
}