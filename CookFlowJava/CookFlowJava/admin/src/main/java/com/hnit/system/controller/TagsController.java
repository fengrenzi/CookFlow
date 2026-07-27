package com.hnit.system.controller;

import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.Tags;
import com.hnit.system.service.ITagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.hnit.common.core.domain.AjaxResult.success;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagsController {

    @Resource
    private ITagsService tagService;

    @GetMapping("/hot")
    public AjaxResult getHotTags(@RequestParam(defaultValue = "8") int limit,
                                 @RequestParam(defaultValue = "question") String type) {
        List<Tags> tags = tagService.getHotTags(limit, type);
        return AjaxResult.success(tags);
    }
}