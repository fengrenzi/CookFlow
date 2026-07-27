package com.hnit.system.service.impl;

import com.hnit.system.domain.Recipes;
import com.hnit.system.domain.UserInteraction;
import com.hnit.system.domain.vo.RecipeSimpleVO;
import com.hnit.system.mapper.RecipesMapper;
import com.hnit.system.mapper.UserInteractionMapper;
import com.hnit.system.service.IUserRecipeService;
import com.hnit.system.utils.ImageUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRecipeServiceImpl implements IUserRecipeService {

    @Resource
    private RecipesMapper recipesMapper;
    @Resource
    private UserInteractionMapper interactionMapper;

    @Override
    public List<RecipeSimpleVO> getPublishedRecipes(Long userId) {
        List<Recipes> recipes = recipesMapper.selectByUserId(userId);
        return recipes.stream().map(this::convertToSimpleVO).collect(Collectors.toList());
    }

    @Override
    public List<RecipeSimpleVO> getCollectedRecipes(Long userId) {
        List<UserInteraction> interactions = interactionMapper.selectByUserAndType(userId, "recipe", "favorite");
        if (interactions.isEmpty()) return Collections.emptyList();
        List<String> recipeIds = interactions.stream().map(UserInteraction::getTargetId).collect(Collectors.toList());
        List<Recipes> recipes = recipesMapper.selectByIds(recipeIds);
        return recipes.stream().map(this::convertToSimpleVO).collect(Collectors.toList());
    }

    @Override
    public List<RecipeSimpleVO> getLikedRecipes(Long userId) {
        List<UserInteraction> interactions = interactionMapper.selectByUserAndType(userId, "recipe", "like");
        if (interactions.isEmpty()) return Collections.emptyList();
        List<String> recipeIds = interactions.stream().map(UserInteraction::getTargetId).collect(Collectors.toList());
        List<Recipes> recipes = recipesMapper.selectByIds(recipeIds);
        return recipes.stream().map(this::convertToSimpleVO).collect(Collectors.toList());
    }

    private RecipeSimpleVO convertToSimpleVO(Recipes recipe) {
        RecipeSimpleVO vo = new RecipeSimpleVO();
        vo.setId(recipe.getId());
        vo.setTitle(recipe.getTitle());
        vo.setLikes(recipe.getLikes());
        vo.setComments(recipe.getCommentCount());
        // 获取封面图片
        vo.setImage(getMainImage(recipe.getId()));
        return vo;
    }

    private String getMainImage(String recipeId) {
        // 复用已有的获取菜谱主图方法，如果 ImageUtils 不可用可以暂时返回 null
        // 实际项目中可以从 images_record 表查询
        // 这里简化处理，可调用已有的 ImageUtils.getFullUrl 或查询数据库
        return null; // 请根据实际实现
    }
}