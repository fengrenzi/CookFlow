package com.hnit.system.mapper;

import java.util.List;
import com.hnit.system.domain.Tags;

/**
 * 标签Mapper接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface TagsMapper 
{
    /**
     * 查询标签
     * 
     * @param id 标签主键
     * @return 标签
     */
    public Tags selectTagsById(Long id);

    /**
     * 查询标签列表
     * 
     * @param tags 标签
     * @return 标签集合
     */
    public List<Tags> selectTagsList(Tags tags);

    /**
     * 新增标签
     * 
     * @param tags 标签
     * @return 结果
     */
    public int insertTags(Tags tags);

    /**
     * 修改标签
     * 
     * @param tags 标签
     * @return 结果
     */
    public int updateTags(Tags tags);

    /**
     * 删除标签
     * 
     * @param id 标签主键
     * @return 结果
     */
    public int deleteTagsById(Long id);

    /**
     * 批量删除标签
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTagsByIds(Long[] ids);
}
