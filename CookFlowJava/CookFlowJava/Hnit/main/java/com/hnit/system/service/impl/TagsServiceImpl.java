package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.TagsMapper;
import com.hnit.system.domain.Tags;
import com.hnit.system.service.ITagsService;

/**
 * 标签Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class TagsServiceImpl implements ITagsService 
{
    @Resource
    private TagsMapper tagsMapper;

    /**
     * 查询标签
     * 
     * @param id 标签主键
     * @return 标签
     */
    @Override
    public Tags selectTagsById(Long id)
    {
        return tagsMapper.selectTagsById(id);
    }

    /**
     * 查询标签列表
     * 
     * @param tags 标签
     * @return 标签
     */
    @Override
    public List<Tags> selectTagsList(Tags tags)
    {
        return tagsMapper.selectTagsList(tags);
    }

    /**
     * 新增标签
     * 
     * @param tags 标签
     * @return 结果
     */
    @Override
    public int insertTags(Tags tags)
    {
        return tagsMapper.insertTags(tags);
    }

    /**
     * 修改标签
     * 
     * @param tags 标签
     * @return 结果
     */
    @Override
    public int updateTags(Tags tags)
    {
        return tagsMapper.updateTags(tags);
    }

    /**
     * 批量删除标签
     * 
     * @param ids 需要删除的标签主键
     * @return 结果
     */
    @Override
    public int deleteTagsByIds(Long[] ids)
    {
        return tagsMapper.deleteTagsByIds(ids);
    }

    /**
     * 删除标签信息
     * 
     * @param id 标签主键
     * @return 结果
     */
    @Override
    public int deleteTagsById(Long id)
    {
        return tagsMapper.deleteTagsById(id);
    }
}
