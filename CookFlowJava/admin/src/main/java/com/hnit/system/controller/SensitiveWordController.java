package com.hnit.system.controller;

import com.hnit.system.domain.SensitiveWord;
import com.hnit.system.service.ISensitiveWordService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/food/sensitive")
public class SensitiveWordController {

    @Resource
    private ISensitiveWordService sensitiveWordService;

    @GetMapping("/list")
    public List<SensitiveWord> list(SensitiveWord word) {
        return sensitiveWordService.selectList(word);
    }

    @PostMapping
    public boolean add(@RequestBody SensitiveWord word) {
        return sensitiveWordService.save(word);
    }

    @PutMapping
    public boolean edit(@RequestBody SensitiveWord word) {
        return sensitiveWordService.updateById(word);
    }

    @DeleteMapping("/{ids}")
    public boolean remove(@PathVariable String[] ids) {
        return sensitiveWordService.removeByIds(Arrays.asList(ids));
    }

    @PostMapping("/filter")
    public String filter(@RequestBody String text) {
        return sensitiveWordService.filterText(text);
    }
}