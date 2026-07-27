package com.hnit.system.controller;

import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.dto.CategoryTreeDto;
import com.hnit.system.service.ICategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.hnit.common.core.domain.AjaxResult.success;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    @Resource
    private ICategoryService categoryService;

    @GetMapping("/tree")
    public AjaxResult getIngredientTree() {
        List<CategoryTreeDto> tree = categoryService.getIngredientTree();
        return success(tree);
    }
}