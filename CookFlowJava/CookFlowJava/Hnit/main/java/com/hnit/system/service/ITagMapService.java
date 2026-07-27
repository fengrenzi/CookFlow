package com.hnit.system.service;

import java.util.List;
import com.hnit.system.domain.TagMap;

/**
 * 标签映射Service接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface ITagMapService 
{
    /**
     * 查询标签映射
     * 
     * @param tagId 标签映射主键
     * @return 标签映射
     */
    public TagMap selectTagMapByTagId(Long tagId);

    /**
     * 查询标签映射列表
     * 
     * @param tagMap 标签映射
     * @return 标签映射集合
     */
    public List<TagMap> selectTagMapList(TagMap tagMap);

    /**
     * 新增标签映射
     * 
     * @param tagMap 标签映射
     * @return 结果
     */
    public int insertTagMap(TagMap tagMap);

    /**
     * 修改标签映射
     * 
     * @param tagMap 标签映射
     * @return 结果
     */
    public int updateTagMap(TagMap tagMap);

    /**
     * 批量删除标签映射
     * 
     * @param tagIds 需要删除的标签映射主键集合
     * @return 结果
     */
    public int deleteTagMapByTagIds(Long[] tagIds);

    /**
     * 删除标签映射信息
     * 
     * @param tagId 标签映射主键
     * @return 结果
     */
    public int deleteTagMapByTagId(Long tagId);
}
