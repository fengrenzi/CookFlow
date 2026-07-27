package com.hnit.system.service;

import com.hnit.system.domain.dto.UpdateUserProfileDTO;
import com.hnit.system.domain.vo.UserProfileVO;

public interface IUserProfileService {
    UserProfileVO getUserProfile(Long userId);
    void updateUserProfile(Long userId, UpdateUserProfileDTO dto);
}