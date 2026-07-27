package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.vo.RecipeSimpleVO;
import com.hnit.system.service.IUserRecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user/recipes")
public class UserRecipeController extends BaseController {

    @Resource
    private IUserRecipeService userRecipeService;

    private static final Long TEMP_USER_ID = 1L;

    @GetMapping("/published")
    public AjaxResult getPublished() {
        List<RecipeSimpleVO> list = userRecipeService.getPublishedRecipes(TEMP_USER_ID);
        return success(list);
    }

    @GetMapping("/collected")
    public AjaxResult getCollected() {
        List<RecipeSimpleVO> list = userRecipeService.getCollectedRecipes(TEMP_USER_ID);
        return success(list);
    }

    @GetMapping("/liked")
    public AjaxResult getLiked() {
        List<RecipeSimpleVO> list = userRecipeService.getLikedRecipes(TEMP_USER_ID);
        return success(list);
    }
}