package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.TagMapMapper;
import com.hnit.system.domain.TagMap;
import com.hnit.system.service.ITagMapService;

/**
 * 标签映射Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class TagMapServiceImpl implements ITagMapService 
{
    @Resource
    private TagMapMapper tagMapMapper;

    /**
     * 查询标签映射
     * 
     * @param tagId 标签映射主键
     * @return 标签映射
     */
    @Override
    public TagMap selectTagMapByTagId(Long tagId)
    {
        return tagMapMapper.selectTagMapByTagId(tagId);
    }

    /**
     * 查询标签映射列表
     * 
     * @param tagMap 标签映射
     * @return 标签映射
     */
    @Override
    public List<TagMap> selectTagMapList(TagMap tagMap)
    {
        return tagMapMapper.selectTagMapList(tagMap);
    }

    /**
     * 新增标签映射
     * 
     * @param tagMap 标签映射
     * @return 结果
     */
    @Override
    public int insertTagMap(TagMap tagMap)
    {
        return tagMapMapper.insertTagMap(tagMap);
    }

    /**
     * 修改标签映射
     * 
     * @param tagMap 标签映射
     * @return 结果
     */
    @Override
    public int updateTagMap(TagMap tagMap)
    {
        return tagMapMapper.updateTagMap(tagMap);
    }

    /**
     * 批量删除标签映射
     * 
     * @param tagIds 需要删除的标签映射主键
     * @return 结果
     */
    @Override
    public int deleteTagMapByTagIds(Long[] tagIds)
    {
        return tagMapMapper.deleteTagMapByTagIds(tagIds);
    }

    /**
     * 删除标签映射信息
     * 
     * @param tagId 标签映射主键
     * @return 结果
     */
    @Override
    public int deleteTagMapByTagId(Long tagId)
    {
        return tagMapMapper.deleteTagMapByTagId(tagId);
    }
}
