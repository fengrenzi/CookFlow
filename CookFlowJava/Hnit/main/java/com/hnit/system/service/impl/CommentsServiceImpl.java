package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.CommentsMapper;
import com.hnit.system.domain.Comments;
import com.hnit.system.service.ICommentsService;

/**
 * 评论（支持@/回复/状态）Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class CommentsServiceImpl implements ICommentsService 
{
    @Resource
    private CommentsMapper commentsMapper;

    /**
     * 查询评论（支持@/回复/状态）
     * 
     * @param id 评论（支持@/回复/状态）主键
     * @return 评论（支持@/回复/状态）
     */
    @Override
    public Comments selectCommentsById(Long id)
    {
        return commentsMapper.selectCommentsById(id);
    }

    /**
     * 查询评论（支持@/回复/状态）列表
     * 
     * @param comments 评论（支持@/回复/状态）
     * @return 评论（支持@/回复/状态）
     */
    @Override
    public List<Comments> selectCommentsList(Comments comments)
    {
        return commentsMapper.selectCommentsList(comments);
    }

    /**
     * 新增评论（支持@/回复/状态）
     * 
     * @param comments 评论（支持@/回复/状态）
     * @return 结果
     */
    @Override
    public int insertComments(Comments comments)
    {
        return commentsMapper.insertComments(comments);
    }

    /**
     * 修改评论（支持@/回复/状态）
     * 
     * @param comments 评论（支持@/回复/状态）
     * @return 结果
     */
    @Override
    public int updateComments(Comments comments)
    {
        return commentsMapper.updateComments(comments);
    }

    /**
     * 批量删除评论（支持@/回复/状态）
     * 
     * @param ids 需要删除的评论（支持@/回复/状态）主键
     * @return 结果
     */
    @Override
    public int deleteCommentsByIds(Long[] ids)
    {
        return commentsMapper.deleteCommentsByIds(ids);
    }

    /**
     * 删除评论（支持@/回复/状态）信息
     * 
     * @param id 评论（支持@/回复/状态）主键
     * @return 结果
     */
    @Override
    public int deleteCommentsById(Long id)
    {
        return commentsMapper.deleteCommentsById(id);
    }
}
