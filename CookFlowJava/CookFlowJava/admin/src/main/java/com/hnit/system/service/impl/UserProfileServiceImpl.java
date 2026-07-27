package com.hnit.system.service.impl;

import com.hnit.common.core.domain.entity.SysUser;
import com.hnit.common.utils.uuid.IdUtils;
import com.hnit.system.domain.UserProfile;
import com.hnit.system.domain.dto.UpdateUserProfileDTO;
import com.hnit.system.domain.vo.UserProfileVO;
import com.hnit.system.mapper.UserProfileMapper;
import com.hnit.system.mapper.SysUserMapper; 
import com.hnit.system.service.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements IUserProfileService {
    @Resource
    private UserProfileMapper userProfileMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserProfileVO getUserProfile(Long userId) {
        SysUser user = sysUserMapper.selectUserById(userId);
        UserProfile profile = userProfileMapper.selectByUserId(userId);
        UserProfileVO vo = new UserProfileVO();
        vo.setUserId(userId);
        vo.setUserName(user.getUserName());
        vo.setEmail(user.getEmail());
        vo.setPhonenumber(user.getPhonenumber());
        vo.setAvatar(user.getAvatar());
        if (profile != null) {
            vo.setBio(profile.getBio());
            vo.setRealName(profile.getRealName());
            vo.setGender(profile.getGender());
            vo.setBirthday(profile.getBirthday());
            vo.setLocation(profile.getLocation());
            vo.setCreateTime(profile.getCreateTime());
        }
        return vo;
    }

    @Override
    @Transactional
    public void updateUserProfile(Long userId, UpdateUserProfileDTO dto) {
        UserProfile profile = userProfileMapper.selectByUserId(userId);
        if (profile == null) {
            profile = new UserProfile();
            profile.setId(IdUtils.fastSimpleUUID());
            profile.setUserId(userId);
            profile.setBio(dto.getBio());
            profile.setRealName(dto.getRealName());
            profile.setGender(dto.getGender());
            profile.setBirthday(dto.getBirthday());
            profile.setLocation(dto.getLocation());
            userProfileMapper.insert(profile);
        } else {
            profile.setBio(dto.getBio());
            profile.setRealName(dto.getRealName());
            profile.setGender(dto.getGender());
            profile.setBirthday(dto.getBirthday());
            profile.setLocation(dto.getLocation());
            userProfileMapper.updateByUserId(profile);
        }
    }
}