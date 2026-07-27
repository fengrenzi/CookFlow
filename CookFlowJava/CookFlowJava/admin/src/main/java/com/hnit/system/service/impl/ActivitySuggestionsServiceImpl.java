package com.hnit.system.service.impl;

import com.hnit.common.utils.uuid.IdUtils;
import com.hnit.system.domain.ActivitySuggestions;
import com.hnit.system.domain.dto.AddSuggestionDTO;
import com.hnit.system.mapper.ActivitySuggestionsMapper;
import com.hnit.system.service.IActivitySuggestionsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ActivitySuggestionsServiceImpl implements IActivitySuggestionsService {

    @Resource
    private ActivitySuggestionsMapper suggestionsMapper;

    @Override
    public void addSuggestion(Long userId, AddSuggestionDTO dto) {
        ActivitySuggestions suggestion = new ActivitySuggestions();
        suggestion.setId(IdUtils.fastSimpleUUID());
        suggestion.setUserId(userId);
        suggestion.setTitle(dto.getTitle());
        suggestion.setContent(dto.getContent());
        suggestion.setStatus(0); // 待审核
        suggestionsMapper.insert(suggestion);
    }
}