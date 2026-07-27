package com.hnit.system.service;


import com.hnit.system.domain.dto.DialogueRequest;
import com.hnit.system.domain.dto.DialogueResponse;

public interface IDialogueService {
    DialogueResponse chat(DialogueRequest request);
    DialogueResponse selectRecipe(Long sessionId, Long recipeId);
}