package com.hnit.system.service;

import com.hnit.system.domain.Tags;

import java.util.List;

public interface ITagsService {
    List<Tags> getHotTags(int limit, String type);
}