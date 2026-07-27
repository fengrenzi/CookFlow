package com.hnit.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnit.common.core.domain.entity.SysUser;
import com.hnit.system.domain.*;
import com.hnit.system.domain.dto.ForumQuestionDto;
import com.hnit.system.domain.vo.ForumQuestionVo;
import com.hnit.system.mapper.*;
import com.hnit.system.service.IForumQuestionService;
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
public class ForumQuestionServiceImpl implements IForumQuestionService {

    @Resource
    private ForumQuestionMapper questionMapper;
    @Resource
    private UserInteractionMapper interactionMapper;
    @Resource
    private ImagesRecordMapper imagesRecordMapper;
    @Resource
    private ImageManagementMapper imageManagementMapper;
    @Resource
    private TagsMapper tagsMapper;
    @Resource
    private ResourceTagMapper resourceTagMapper;
    @Resource
    private SysUserMapper userMapper;   // 查询用户信息

    @Override
    @Transactional
    public void createQuestion(ForumQuestionDto dto, Long userId) {
        ForumQuestion question = new ForumQuestion();
        question.setId(UUID.randomUUID().toString());
        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());
        question.setUserId(userId);
        question.setAnswerCount(0);
        question.setFavoriteCount(0);
        question.setFollowCount(0);
        question.setViewCount(0L);
        question.setIsResolved(false);
        question.setStatus(0);
        question.setCreatedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.insert(question);

        // 处理图片
        if (dto.getImageIds() != null && !dto.getImageIds().isEmpty()) {
            for (int i = 0; i < dto.getImageIds().size(); i++) {
                ImagesRecord record = new ImagesRecord();
                record.setId(UUID.randomUUID().toString());
                record.setTableName("forum_question");
                record.setRecipeId(question.getId());
                record.setImageId(dto.getImageIds().get(i));
                record.setSort(i);
                record.setCreateTime(LocalDateTime.now());
                imagesRecordMapper.insert(record);
            }
        }

        // 处理标签
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            for (String tagName : dto.getTags()) {
                Tags tags = tagsMapper.selectOne(tagName, "question");
                if (tags == null) {
                    tags = new Tags();
                    tags.setId(UUID.randomUUID().toString());
                    tags.setName(tagName);
                    tags.setType("question");
                    tags.setUseCount(0);
                    tags.setCreatedAt(LocalDateTime.now());
                    tagsMapper.insert(tags);
                }
                tags.setUseCount(tags.getUseCount() + 1);
                tagsMapper.updateById(tags);

                ResourceTag rt = new ResourceTag();
                rt.setResourceType("question");
                rt.setResourceId(question.getId());
                rt.setTagId(tags.getId());
                rt.setCreatedAt(LocalDateTime.now());
                resourceTagMapper.insert(rt);
            }
        }
    }

    @Override
    public Page<ForumQuestionVo> listQuestions(int page, int size, String keyword, String sortBy, Long currentUserId) {
        int offset = (page - 1) * size;
        List<ForumQuestionVo> records = questionMapper.selectPageList(offset, size, keyword, sortBy);

        // 批量填充图片URL
        List<String> questionIds = records.stream()
                .map(ForumQuestionVo::getId)
                .collect(Collectors.toList());
        if (!questionIds.isEmpty()) {
            List<ImagesRecord> allRecords = imagesRecordMapper.selectListByRecipeIds("forum_question", questionIds);
            Map<String, List<ImagesRecord>> recordMap = allRecords.stream()
                    .collect(Collectors.groupingBy(ImagesRecord::getRecipeId));
            List<String> allImageIds = allRecords.stream().map(ImagesRecord::getImageId).collect(Collectors.toList());
            if (!allImageIds.isEmpty()) {
                List<ImageManagement> allImages = imageManagementMapper.selectBatchIds(allImageIds);
                Map<String, ImageManagement> imageMap = allImages.stream()
                        .collect(Collectors.toMap(ImageManagement::getId, img -> img));
                for (ForumQuestionVo vo : records) {
                    List<ImagesRecord> recs = recordMap.getOrDefault(vo.getId(), Collections.emptyList());
                    for (ImagesRecord rec : recs) {
                        ImageManagement img = imageMap.get(rec.getImageId());
                        if (img != null) {
                            // 关键修复：使用 img.getId()
                            rec.setImageUrl(ImageUtils.getFullUrl(img.getId()));
                        }
                    }
                    vo.setImages(recs);
                }
            }
        }

        // 填充标签
        for (ForumQuestionVo vo : records) {
            List<String> tags = getTagsForResource("question", vo.getId());
            vo.setTags(tags);
        }

        // 填充用户信息
        fillUserInfo(records);

        // 交互状态
        if (currentUserId != null) {
            for (ForumQuestionVo vo : records) {
                vo.setFavorited(interactionMapper.exists(currentUserId, "question", vo.getId(), "favorite"));
                vo.setFollowed(interactionMapper.exists(currentUserId, "question", vo.getId(), "follow"));
            }
        }

        int total = questionMapper.selectCountByStatus(0);
        Page<ForumQuestionVo> pageResult = new Page<>(page, size);
        pageResult.setRecords(records);
        pageResult.setTotal(total);
        return pageResult;
    }

    @Override
    public ForumQuestionVo getQuestionDetail(String id, Long currentUserId) {
        ForumQuestion question = questionMapper.selectById(id);
        if (question == null) return null;

        // 增加浏览数
        question.setViewCount(question.getViewCount() + 1);
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);

        // 查询图片
        List<ImagesRecord> images = imagesRecordMapper.selectList("forum_question", id);
        if (!images.isEmpty()) {
            List<String> imageIds = images.stream().map(ImagesRecord::getImageId).collect(Collectors.toList());
            List<ImageManagement> imgList = imageManagementMapper.selectBatchIds(imageIds);
            Map<String, ImageManagement> imgMap = imgList.stream()
                    .collect(Collectors.toMap(ImageManagement::getId, img -> img));
            for (ImagesRecord rec : images) {
                ImageManagement img = imgMap.get(rec.getImageId());
                if (img != null) {
                    // 关键修复：使用 img.getId()
                    rec.setImageUrl(ImageUtils.getFullUrl(img.getId()));
                }
            }
        }

        // 查询标签
        List<String> tags = getTagsForResource("question", id);

        // 查询用户信息
        SysUser user = userMapper.selectUserById(question.getUserId());
        String userName = (user != null && user.getNickName() != null) ? user.getNickName() : "用户" + question.getUserId();
        String userAvatar = (user != null && user.getAvatar() != null && !user.getAvatar().isEmpty())
                ? ImageUtils.getFullUrl(user.getAvatar())
                : "/default.png";

        ForumQuestionVo vo = new ForumQuestionVo();
        vo.setId(question.getId());
        vo.setTitle(question.getTitle());
        vo.setContent(question.getContent());
        vo.setUserId(question.getUserId());
        vo.setUserName(userName);
        vo.setUserAvatar(userAvatar);
        vo.setAnswerCount(question.getAnswerCount());
        vo.setFavoriteCount(question.getFavoriteCount());
        vo.setFollowCount(question.getFollowCount());
        vo.setViewCount(question.getViewCount());
        vo.setIsResolved(question.getIsResolved());
        vo.setCreatedAt(question.getCreatedAt());
        vo.setImages(images);
        vo.setTags(tags);

        if (currentUserId != null) {
            vo.setFavorited(interactionMapper.exists(currentUserId, "question", id, "favorite"));
            vo.setFollowed(interactionMapper.exists(currentUserId, "question", id, "follow"));
        }
        return vo;
    }

    @Override
    @Transactional
    public void toggleFavorite(String questionId, Long userId) {
        boolean exists = interactionMapper.exists(userId, "question", questionId, "favorite");
        ForumQuestion question = questionMapper.selectById(questionId);
        if (question == null) throw new RuntimeException("问题不存在");

        if (!exists) {
            UserInteraction interaction = new UserInteraction();
            interaction.setId(UUID.randomUUID().toString());
            interaction.setUserId(userId);
            interaction.setTargetType("question");
            interaction.setTargetId(questionId);
            interaction.setInteractionType("favorite");
            interaction.setCreatedAt(LocalDateTime.now());
            interactionMapper.insert(interaction);
            question.setFavoriteCount(question.getFavoriteCount() + 1);
        } else {
            interactionMapper.delete(userId, "question", questionId, "favorite");
            question.setFavoriteCount(question.getFavoriteCount() - 1);
        }
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);
    }

    @Override
    @Transactional
    public void toggleFollow(String questionId, Long userId) {
        boolean exists = interactionMapper.exists(userId, "question", questionId, "follow");
        ForumQuestion question = questionMapper.selectById(questionId);
        if (question == null) throw new RuntimeException("问题不存在");

        if (!exists) {
            UserInteraction interaction = new UserInteraction();
            interaction.setId(UUID.randomUUID().toString());
            interaction.setUserId(userId);
            interaction.setTargetType("question");
            interaction.setTargetId(questionId);
            interaction.setInteractionType("follow");
            interaction.setCreatedAt(LocalDateTime.now());
            interactionMapper.insert(interaction);
            question.setFollowCount(question.getFollowCount() + 1);
        } else {
            interactionMapper.delete(userId, "question", questionId, "follow");
            question.setFollowCount(question.getFollowCount() - 1);
        }
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);
    }

    @Override
    @Transactional
    public void deleteQuestion(String id, Long userId) {
        ForumQuestion question = questionMapper.selectById(id);
        if (question == null) throw new RuntimeException("问题不存在");
        if (!question.getUserId().equals(userId)) throw new RuntimeException("无权限删除");
        question.setStatus(1);
        question.setUpdatedAt(LocalDateTime.now());
        questionMapper.updateById(question);
    }

    // ---------- 辅助方法 ----------
    private List<String> getTagsForResource(String resourceType, String resourceId) {
        List<ResourceTag> resourceTags = resourceTagMapper.selectList(resourceType, resourceId);
        if (resourceTags.isEmpty()) return Collections.emptyList();
        List<String> tagIds = resourceTags.stream().map(ResourceTag::getTagId).collect(Collectors.toList());
        List<Tags> tags = tagsMapper.selectBatchIds(tagIds);
        return tags.stream().map(Tags::getName).collect(Collectors.toList());
    }

    private void fillUserInfo(List<ForumQuestionVo> records) {
        if (records.isEmpty()) return;
        Set<Long> userIds = records.stream().map(ForumQuestionVo::getUserId).collect(Collectors.toSet());
        List<SysUser> users = userIds.stream().map(userMapper::selectUserById).collect(Collectors.toList());
        Map<Long, SysUser> userMap = users.stream().collect(Collectors.toMap(SysUser::getUserId, u -> u));
        for (ForumQuestionVo vo : records) {
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