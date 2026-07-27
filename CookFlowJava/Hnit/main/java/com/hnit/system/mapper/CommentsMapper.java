package com.hnit.system.mapper;

import java.util.List;
import com.hnit.system.domain.Comments;

/**
 * 评论（支持@/回复/状态）Mapper接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface CommentsMapper 
{
    /**
     * 查询评论（支持@/回复/状态）
     * 
     * @param id 评论（支持@/回复/状态）主键
     * @return 评论（支持@/回复/状态）
     */
    public Comments selectCommentsById(Long id);

    /**
     * 查询评论（支持@/回复/状态）列表
     * 
     * @param comments 评论（支持@/回复/状态）
     * @return 评论（支持@/回复/状态）集合
     */
    public List<Comments> selectCommentsList(Comments comments);

    /**
     * 新增评论（支持@/回复/状态）
     * 
     * @param comments 评论（支持@/回复/状态）
     * @return 结果
     */
    public int insertComments(Comments comments);

    /**
     * 修改评论（支持@/回复/状态）
     * 
     * @param comments 评论（支持@/回复/状态）
     * @return 结果
     */
    public int updateComments(Comments comments);

    /**
     * 删除评论（支持@/回复/状态）
     * 
     * @param id 评论（支持@/回复/状态）主键
     * @return 结果
     */
    public int deleteCommentsById(Long id);

    /**
     * 批量删除评论（支持@/回复/状态）
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCommentsByIds(Long[] ids);
}
