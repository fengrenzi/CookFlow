package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.dto.UpdateUserProfileDTO;
import com.hnit.system.domain.vo.UserProfileVO;
import com.hnit.system.service.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user/profile")
@RequiredArgsConstructor
public class UserProfileController extends BaseController {
    @Resource
    private IUserProfileService userProfileService;

    // 临时固定用户ID
    private static final Long TEMP_USER_ID = 1L;

    @GetMapping
    public AjaxResult getProfile() {
        UserProfileVO vo = userProfileService.getUserProfile(TEMP_USER_ID);
        return success(vo);
    }

    @PutMapping
    public AjaxResult updateProfile(@RequestBody UpdateUserProfileDTO dto) {
        userProfileService.updateUserProfile(TEMP_USER_ID, dto);
        return success();
    }
}