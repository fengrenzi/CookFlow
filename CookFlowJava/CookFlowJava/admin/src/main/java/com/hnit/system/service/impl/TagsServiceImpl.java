package com.hnit.system.service.impl;

import com.hnit.system.domain.Tags;
import com.hnit.system.mapper.TagsMapper;
import com.hnit.system.service.ITagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagsServiceImpl implements ITagsService {

    @Resource
    private TagsMapper tagsMapper;

    @Override
    public List<Tags> getHotTags(int limit, String type) {
        return tagsMapper.selectHotTags(limit, type);
    }
}