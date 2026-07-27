package com.hnit.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnit.common.core.domain.entity.SysUser;
import com.hnit.system.domain.*;
import com.hnit.system.domain.dto.ForumActivityDto;
import com.hnit.system.domain.vo.ForumActivityVo;
import com.hnit.system.mapper.*;
import com.hnit.system.service.IForumActivityService;
import com.hnit.system.utils.ImageUtils;
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
public class ForumActivityServiceImpl implements IForumActivityService {

    @Resource
    private ForumActivityMapper activityMapper;
    @Resource
    private UserInteractionMapper interactionMapper;
    @Resource
    private ImagesRecordMapper imagesRecordMapper;
    @Resource
    private ImageManagementMapper imageManagementMapper;
    @Resource
    private SysUserMapper userMapper;   // 用于查询用户信息

    @Override
    @Transactional
    public void createActivity(ForumActivityDto dto, Long userId) {
        ForumActivity activity = new ForumActivity();
        activity.setId(UUID.randomUUID().toString());
        activity.setTitle(dto.getTitle());
        activity.setSummary(dto.getSummary());
        activity.setStartTime(dto.getStartTime());
        activity.setEndTime(dto.getEndTime());
        activity.setCategory(dto.getCategory());
        activity.setTag(dto.getTag());
        activity.setUserId(userId);
        activity.setParticipantCount(0);
        activity.setStatus(0);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());
        activityMapper.insert(activity);

        if (dto.getImageIds() != null && !dto.getImageIds().isEmpty()) {
            for (int i = 0; i < dto.getImageIds().size(); i++) {
                ImagesRecord record = new ImagesRecord();
                record.setId(UUID.randomUUID().toString());
                record.setTableName("forum_activity");
                record.setRecipeId(activity.getId());
                record.setImageId(dto.getImageIds().get(i));
                record.setSort(i);
                record.setCreateTime(LocalDateTime.now());
                imagesRecordMapper.insert(record);
            }
        }
    }

    @Override
    public Page<ForumActivityVo> listActivities(int page, int size, String category, String sortBy, Long currentUserId) {
        int offset = (page - 1) * size;
        List<ForumActivityVo> records = activityMapper.selectPageList(offset, size, category, sortBy);

        // 批量填充图片URL
        List<String> activityIds = records.stream()
                .map(ForumActivityVo::getId)
                .collect(Collectors.toList());
        if (!activityIds.isEmpty()) {
            List<ImagesRecord> allRecords = imagesRecordMapper.selectListByRecipeIds("forum_activity", activityIds);
            Map<String, List<ImagesRecord>> recordMap = allRecords.stream()
                    .collect(Collectors.groupingBy(ImagesRecord::getRecipeId));
            List<String> allImageIds = allRecords.stream().map(ImagesRecord::getImageId).collect(Collectors.toList());
            Map<String, ImageManagement> imageMap = new HashMap<>();
            if (!allImageIds.isEmpty()) {
                List<ImageManagement> allImages = imageManagementMapper.selectBatchIds(allImageIds);
                imageMap = allImages.stream()
                        .collect(Collectors.toMap(ImageManagement::getId, img -> img));
            }
            for (ForumActivityVo vo : records) {
                List<ImagesRecord> recs = recordMap.getOrDefault(vo.getId(), Collections.emptyList());
                List<String> urlList = new ArrayList<>();
                for (ImagesRecord rec : recs) {
                    ImageManagement img = imageMap.get(rec.getImageId());
                    if (img != null) {
                        String url = ImageUtils.getFullUrl(img.getId());
                        rec.setImageUrl(url);
                        urlList.add(url);
                    }
                }
                vo.setImages(recs);
                vo.setImageUrls(urlList);   // 设置字符串列表
            }
        } else {
            for (ForumActivityVo vo : records) {
                vo.setImages(Collections.emptyList());
                vo.setImageUrls(Collections.emptyList());
            }
        }

        // 填充用户信息（昵称、头像）
        fillUserInfo(records);

        if (currentUserId != null) {
            for (ForumActivityVo vo : records) {
                vo.setParticipated(interactionMapper.exists(currentUserId, "activity", vo.getId(), "participate"));
            }
        }

        int total = activityMapper.selectCountByStatus(0);
        Page<ForumActivityVo> pageResult = new Page<>(page, size);
        pageResult.setRecords(records);
        pageResult.setTotal(total);
        return pageResult;
    }

    @Override
    public ForumActivityVo getActivityDetail(String id, Long currentUserId) {
        ForumActivity activity = activityMapper.selectById(id);
        if (activity == null) return null;

        List<ImagesRecord> images = imagesRecordMapper.selectList("forum_activity", id);
        List<String> urlList = new ArrayList<>();
        if (!images.isEmpty()) {
            List<String> imageIds = images.stream().map(ImagesRecord::getImageId).collect(Collectors.toList());
            List<ImageManagement> imgList = imageManagementMapper.selectBatchIds(imageIds);
            Map<String, ImageManagement> imgMap = imgList.stream()
                    .collect(Collectors.toMap(ImageManagement::getId, img -> img));
            for (ImagesRecord rec : images) {
                ImageManagement img = imgMap.get(rec.getImageId());
                if (img != null) {
                    String url = ImageUtils.getFullUrl(img.getId());
                    rec.setImageUrl(url);
                    urlList.add(url);
                }
            }
        }

        // 查询真实用户信息
        SysUser user = userMapper.selectUserById(activity.getUserId());
        String userName = (user != null && user.getNickName() != null) ? user.getNickName() : "用户" + activity.getUserId();
        String userAvatar = (user != null && user.getAvatar() != null && !user.getAvatar().isEmpty())
                ? ImageUtils.getFullUrl(user.getAvatar())
                : "/default.png";

        ForumActivityVo vo = new ForumActivityVo();
        vo.setId(activity.getId());
        vo.setTitle(activity.getTitle());
        vo.setSummary(activity.getSummary());
        vo.setStartTime(activity.getStartTime());
        vo.setEndTime(activity.getEndTime());
        vo.setCategory(activity.getCategory());
        vo.setTag(activity.getTag());
        vo.setUserId(activity.getUserId());
        vo.setUserName(userName);
        vo.setUserAvatar(userAvatar);
        vo.setParticipantCount(activity.getParticipantCount());
        vo.setStatus(activity.getStatus());
        vo.setCreatedAt(activity.getCreatedAt());
        vo.setImages(images);
        vo.setImageUrls(urlList);

        if (currentUserId != null) {
            vo.setParticipated(interactionMapper.exists(currentUserId, "activity", id, "participate"));
        }
        return vo;
    }

    @Override
    @Transactional
    public void joinActivity(String activityId, Long userId) {
        boolean exists = interactionMapper.exists(userId, "activity", activityId, "participate");
        ForumActivity activity = activityMapper.selectById(activityId);
        if (activity == null) throw new RuntimeException("活动不存在");
        if (activity.getStatus() != 0) throw new RuntimeException("活动已结束或未开始");

        if (!exists) {
            UserInteraction interaction = new UserInteraction();
            interaction.setId(UUID.randomUUID().toString());
            interaction.setUserId(userId);
            interaction.setTargetType("activity");
            interaction.setTargetId(activityId);
            interaction.setInteractionType("participate");
            interaction.setCreatedAt(LocalDateTime.now());
            interactionMapper.insert(interaction);
            activity.setParticipantCount(activity.getParticipantCount() + 1);
            activity.setUpdatedAt(LocalDateTime.now());
            activityMapper.updateById(activity);
        }
    }

    @Override
    @Transactional
    public void cancelJoinActivity(String activityId, Long userId) {
        boolean exists = interactionMapper.exists(userId, "activity", activityId, "participate");
        ForumActivity activity = activityMapper.selectById(activityId);
        if (activity == null) throw new RuntimeException("活动不存在");

        if (exists) {
            interactionMapper.delete(userId, "activity", activityId, "participate");
            activity.setParticipantCount(Math.max(0, activity.getParticipantCount() - 1));
            activity.setUpdatedAt(LocalDateTime.now());
            activityMapper.updateById(activity);
        }
    }

    @Override
    @Transactional
    public void deleteActivity(String id, Long userId) {
        ForumActivity activity = activityMapper.selectById(id);
        if (activity == null) throw new RuntimeException("活动不存在");
        if (!activity.getUserId().equals(userId)) throw new RuntimeException("无权限删除");
        activity.setStatus(1);
        activity.setUpdatedAt(LocalDateTime.now());
        activityMapper.updateById(activity);
    }

    // ---------- 辅助方法 ----------
    private void fillUserInfo(List<ForumActivityVo> records) {
        if (records.isEmpty()) return;
        Set<Long> userIds = records.stream().map(ForumActivityVo::getUserId).collect(Collectors.toSet());
        List<SysUser> users = userIds.stream().map(userMapper::selectUserById).collect(Collectors.toList());
        Map<Long, SysUser> userMap = users.stream().collect(Collectors.toMap(SysUser::getUserId, u -> u));
        for (ForumActivityVo vo : records) {
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