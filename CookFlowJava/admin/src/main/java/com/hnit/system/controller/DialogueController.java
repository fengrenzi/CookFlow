package com.hnit.system.controller;

import com.hnit.system.domain.dto.DialogueRequest;
import com.hnit.system.domain.dto.DialogueResponse;
import com.hnit.system.service.IDialogueService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dialogue")
@CrossOrigin(origins = "*")
public class DialogueController {

    @Resource
    private IDialogueService dialogueService;

    @PostMapping("/send")
    public DialogueResponse send(@RequestBody DialogueRequest request) {
        return dialogueService.chat(request);
    }

    @PostMapping("/select-recipe")
    public DialogueResponse selectRecipe(@RequestParam Long sessionId, @RequestParam Long recipeId) {
        return dialogueService.selectRecipe(sessionId, recipeId);
    }
}