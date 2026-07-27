package com.hnit.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnit.common.core.domain.entity.SysUser;
import com.hnit.system.domain.*;
import com.hnit.system.domain.dto.ForumShareDto;
import com.hnit.system.domain.vo.ForumShareVo;
import com.hnit.system.mapper.*;
import com.hnit.system.service.IForumShareService;
import com.hnit.system.utils.ImageUtils;
import com.hnit.system.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumShareServiceImpl implements IForumShareService {

    @Resource
    private ForumShareMapper forumShareMapper;
    @Resource
    private UserInteractionMapper userInteractionMapper;
    @Resource
    private ImageManagementMapper imageManagementMapper;
    @Resource
    private SysUserMapper userMapper;   // 查询用户信息

    @Override
    @Transactional
    public void createShare(ForumShareDto dto, Long userId) {
        ForumShare share = new ForumShare();
        share.setId(UUID.randomUUID().toString());
        share.setTitle(dto.getTitle());
        share.setContent(dto.getContent());
        share.setType(dto.getType());
        share.setCategory(dto.getCategory());
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            share.setTags(JsonUtils.toJson(dto.getTags()));
        }
        share.setDifficulty(dto.getDifficulty());
        share.setIsPublic(dto.getIsPublic());
        share.setUserId(userId);
        share.setLikeCount(0);
        share.setFavoriteCount(0);
        share.setCommentCount(0);
        share.setViewCount(0L);
        share.setStatus(0);
        share.setCreatedAt(LocalDateTime.now());
        share.setUpdatedAt(LocalDateTime.now());

        if ("image".equals(dto.getType())) {
            // 取第一张图片作为主图
            if (dto.getImageIds() != null && !dto.getImageIds().isEmpty()) {
                share.setResourceId(dto.getImageIds().get(0));
            }
        } else if ("video".equals(dto.getType())) {
            share.setResourceId(dto.getVideoUrl());
        }

        forumShareMapper.insert(share);
    }

    @Override
    public Page<ForumShareVo> listShares(int page, int size, String category, String sortBy, Long currentUserId) {
        int offset = (page - 1) * size;
        List<ForumShareVo> records = forumShareMapper.selectPageList(offset, size, category, sortBy);

        // 批量查询主图URL（仅 image 类型）
        for (ForumShareVo vo : records) {
            if ("image".equals(vo.getType()) && vo.getResourceId() != null) {
                ImageManagement img = imageManagementMapper.selectById(vo.getResourceId());
                if (img != null) {
                    String url = ImageUtils.getFullUrl(img.getId());
                    vo.setImageUrl(url);
                    vo.setImageUrls(Collections.singletonList(url));
                } else {
                    vo.setImageUrls(Collections.emptyList());
                }
            } else if ("video".equals(vo.getType())) {
                vo.setImageUrl(vo.getResourceId());
                vo.setImageUrls(Collections.singletonList(vo.getResourceId()));
            } else {
                vo.setImageUrls(Collections.emptyList());
            }
        }

        // 批量填充用户信息
        fillUserInfo(records);

        // 用户交互状态
        if (currentUserId != null) {
            for (ForumShareVo vo : records) {
                vo.setLiked(userInteractionMapper.exists(currentUserId, "share", vo.getId(), "like"));
                vo.setFavorited(userInteractionMapper.exists(currentUserId, "share", vo.getId(), "favorite"));
            }
        }

        int total = forumShareMapper.selectCountByStatus(0);
        Page<ForumShareVo> pageResult = new Page<>(page, size);
        pageResult.setRecords(records);
        pageResult.setTotal(total);
        return pageResult;
    }

    @Override
    public ForumShareVo getShareDetail(String id, Long currentUserId) {
        ForumShare share = forumShareMapper.selectById(id);
        if (share == null) return null;

        // 增加浏览数
        share.setViewCount(share.getViewCount() + 1);
        share.setUpdatedAt(LocalDateTime.now());
        forumShareMapper.updateById(share);

        // 组装 VO
        ForumShareVo vo = new ForumShareVo();
        vo.setId(share.getId());
        vo.setTitle(share.getTitle());
        vo.setContent(share.getContent());
        vo.setType(share.getType());
        vo.setResourceId(share.getResourceId());
        vo.setCategory(share.getCategory());
        vo.setTags(share.getTags());
        vo.setDifficulty(share.getDifficulty());
        vo.setIsPublic(share.getIsPublic());
        vo.setUserId(share.getUserId());

        // 查询真实用户信息
        SysUser user = userMapper.selectUserById(share.getUserId());
        String userName = (user != null && user.getNickName() != null) ? user.getNickName() : "用户" + share.getUserId();
        String userAvatar = (user != null && user.getAvatar() != null && !user.getAvatar().isEmpty())
                ? ImageUtils.getFullUrl(user.getAvatar())
                : "/default.png";
        vo.setUserName(userName);
        vo.setUserAvatar(userAvatar);

        vo.setLikeCount(share.getLikeCount());
        vo.setFavoriteCount(share.getFavoriteCount());
        vo.setCommentCount(share.getCommentCount());
        vo.setViewCount(share.getViewCount());
        vo.setCreatedAt(share.getCreatedAt());

        // 填充图片URL
        if ("image".equals(share.getType()) && share.getResourceId() != null) {
            ImageManagement img = imageManagementMapper.selectById(share.getResourceId());
            if (img != null) {
                vo.setImageUrl(ImageUtils.getFullUrl(img.getId()));
            }
        } else if ("video".equals(share.getType())) {
            vo.setImageUrl(share.getResourceId());
        }

        if (currentUserId != null) {
            vo.setLiked(userInteractionMapper.exists(currentUserId, "share", id, "like"));
            vo.setFavorited(userInteractionMapper.exists(currentUserId, "share", id, "favorite"));
        }
        return vo;
    }

    @Override
    @Transactional
    public void toggleLike(String shareId, Long userId) {
        boolean exists = userInteractionMapper.exists(userId, "share", shareId, "like");
        ForumShare share = forumShareMapper.selectById(shareId);
        if (share == null) throw new RuntimeException("分享不存在");

        if (!exists) {
            UserInteraction interaction = new UserInteraction();
            interaction.setId(UUID.randomUUID().toString());
            interaction.setUserId(userId);
            interaction.setTargetType("share");
            interaction.setTargetId(shareId);
            interaction.setInteractionType("like");
            interaction.setCreatedAt(LocalDateTime.now());
            userInteractionMapper.insert(interaction);
            share.setLikeCount(share.getLikeCount() + 1);
        } else {
            userInteractionMapper.delete(userId, "share", shareId, "like");
            share.setLikeCount(share.getLikeCount() - 1);
        }
        share.setUpdatedAt(LocalDateTime.now());
        forumShareMapper.updateById(share);
    }

    @Override
    @Transactional
    public void toggleFavorite(String shareId, Long userId) {
        boolean exists = userInteractionMapper.exists(userId, "share", shareId, "favorite");
        ForumShare share = forumShareMapper.selectById(shareId);
        if (share == null) throw new RuntimeException("分享不存在");

        if (!exists) {
            UserInteraction interaction = new UserInteraction();
            interaction.setId(UUID.randomUUID().toString());
            interaction.setUserId(userId);
            interaction.setTargetType("share");
            interaction.setTargetId(shareId);
            interaction.setInteractionType("favorite");
            interaction.setCreatedAt(LocalDateTime.now());
            userInteractionMapper.insert(interaction);
            share.setFavoriteCount(share.getFavoriteCount() + 1);
        } else {
            userInteractionMapper.delete(userId, "share", shareId, "favorite");
            share.setFavoriteCount(share.getFavoriteCount() - 1);
        }
        share.setUpdatedAt(LocalDateTime.now());
        forumShareMapper.updateById(share);
    }

    @Override
    @Transactional
    public void deleteShare(String id, Long userId) {
        ForumShare share = forumShareMapper.selectById(id);
        if (share == null) throw new RuntimeException("分享不存在");
        if (!share.getUserId().equals(userId)) throw new RuntimeException("无权限删除");
        share.setStatus(1);
        share.setUpdatedAt(LocalDateTime.now());
        forumShareMapper.updateById(share);
    }

    // ---------- 辅助方法 ----------
    private void fillUserInfo(List<ForumShareVo> records) {
        if (records.isEmpty()) return;
        Set<Long> userIds = records.stream().map(ForumShareVo::getUserId).collect(Collectors.toSet());
        List<SysUser> users = userIds.stream().map(userMapper::selectUserById).collect(Collectors.toList());
        Map<Long, SysUser> userMap = users.stream().collect(Collectors.toMap(SysUser::getUserId, u -> u));
        for (ForumShareVo vo : records) {
            SysUser user = userMap.get(vo.getUserId());
            if (user != null) {
                vo.setUserName(user.getNickName() != null ? user.getNickName() : user.getUserName());
                if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                    vo.setUserAvatar(ImageUtils.getFullUrl(user.getAvatar()));
                } else {
                    vo.setUserAvatar("/default.png");
                }
            } else {
                vo.setUserName("用户" + vo.getUserId());
                vo.setUserAvatar("/default.png");
            }
        }
    }
}