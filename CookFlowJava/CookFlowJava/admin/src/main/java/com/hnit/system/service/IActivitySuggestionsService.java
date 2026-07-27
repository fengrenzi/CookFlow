package com.hnit.system.service;

import com.hnit.system.domain.dto.AddSuggestionDTO;

public interface IActivitySuggestionsService {
    void addSuggestion(Long userId, AddSuggestionDTO dto);
}