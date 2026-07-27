package com.hnit.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.hnit.common.core.page.TableDataInfo;
import com.hnit.common.exception.ServiceException;
import com.hnit.system.domain.Comment;
import com.hnit.system.domain.ImagesRecord;
import com.hnit.system.domain.dto.CommentDto;
import com.hnit.system.domain.dto.CommentQueryDto;
import com.hnit.system.domain.vo.CommentReplyVo;
import com.hnit.system.domain.vo.CommentVo;
import com.hnit.system.mapper.CommentLikeMapper;
import com.hnit.system.mapper.CommentMapper;
import com.hnit.system.mapper.ImagesRecordMapper;
import com.hnit.system.service.ICommentService;
import com.hnit.system.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CommentServiceImpl implements ICommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private CommentLikeMapper commentLikeMapper;
    @Resource
    private ImagesRecordMapper imagesRecordMapper;

    @Override
    public TableDataInfo selectCommentList(CommentQueryDto query, Long currentUserId) {
        int offset = (query.getPageNum() - 1) * query.getPageSize();
        List<CommentVo> list = commentMapper.selectCommentList(query, offset, currentUserId);

        // 填充头像和图片 URL
        for (CommentVo vo : list) {
            // 用户头像
            if (vo.getUser() != null && vo.getUser().getAvatar() != null) {
                vo.setAvatarUrl(ImageUtils.getFullUrl(vo.getUser().getAvatar()));
            }
            // 评论图片
            if (vo.getImages() != null && !vo.getImages().isEmpty()) {
                List<String> urls = new ArrayList<>();
                for (ImagesRecord img : vo.getImages()) {
                    if (img.getImageId() != null) {
                        urls.add(ImageUtils.getFullUrl(img.getImageId()));
                    }
                }
                vo.setImageUrls(urls);
            }
            // 回复中的头像
            if (vo.getReplies() != null) {
                for (CommentReplyVo reply : vo.getReplies()) {
                    if (reply.getUser() != null && reply.getUser().getAvatar() != null) {
                        reply.setAvatarUrl(ImageUtils.getFullUrl(reply.getUser().getAvatar()));
                    }
                }
            }
        }

        int total = commentMapper.countCommentList(query.getResourceType(), query.getResourceId());
        PageInfo<CommentVo> pageInfo = new PageInfo<>(list);
        return new TableDataInfo(pageInfo.getList(), total);
    }

    @Override
    @Transactional
    public CommentVo addComment(CommentDto commentDto, Long currentUserId) {
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        comment.setResourceType(commentDto.getResourceType());
        comment.setResourceId(commentDto.getResourceId());
        comment.setParentId(commentDto.getParentId());
        comment.setUserId(currentUserId);
        comment.setContent(commentDto.getContent());
        comment.setRating(commentDto.getRating());
        commentMapper.insert(comment);

        // 关联图片
        if (commentDto.getImageIds() != null && !commentDto.getImageIds().isEmpty()) {
            for (int i = 0; i < commentDto.getImageIds().size(); i++) {
                ImagesRecord record = new ImagesRecord();
                record.setId(UUID.randomUUID().toString());
                record.setTableName("comment");
                record.setRecipeId(comment.getId()); // recipe_id 字段存储 comment_id
                record.setImageId(commentDto.getImageIds().get(i));
                record.setSort(i);
                imagesRecordMapper.insert(record);
            }
        }

        // 如果是回复，增加父评论的回复计数
        if (commentDto.getParentId() != null && !commentDto.getParentId().isEmpty()) {
            commentMapper.incrementReplyCount(commentDto.getParentId());
        }

        // 返回简略信息
        CommentVo vo = new CommentVo();
        vo.setId(comment.getId());
        return vo;
    }

    @Override
    @Transactional
    public void likeComment(String commentId, Long currentUserId) {
        int exists = commentLikeMapper.exists(commentId, currentUserId);
        if (exists > 0) {
            commentLikeMapper.delete(commentId, currentUserId);
            commentMapper.updateLikes(commentId, -1);
        } else {
            commentLikeMapper.insert(commentId, currentUserId);
            commentMapper.updateLikes(commentId, 1);
        }
    }

    @Override
    public void deleteComment(String commentId, Long currentUserId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new ServiceException("评论不存在");
        }
        if (!comment.getUserId().equals(currentUserId)) {
            throw new ServiceException("无权删除他人评论");
        }
        commentMapper.softDelete(commentId);
    }
}