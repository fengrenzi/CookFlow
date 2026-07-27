package com.hnit.system.service.impl;

import com.hnit.system.utils.ImageUtils;
import com.hnit.common.utils.bean.BeanUtils;
import com.hnit.system.domain.Recipes;
import com.hnit.system.domain.dto.RecipeQueryDto;
import com.hnit.system.domain.dto.RecipesDto;
import com.hnit.system.mapper.RecipesMapper;
import com.hnit.system.service.IRecipesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class RecipesServiceImpl implements IRecipesService {
    @Resource
    private RecipesMapper recipesMapper;

    @Override
    public List<Recipes> selectRecipesList(RecipeQueryDto query) {
        return recipesMapper.selectRecipesList(query);
    }

    @Override
    public Recipes selectRecipesById(String id) {
        return recipesMapper.selectRecipesById(id);
    }

    @Override
    public int insertRecipes(Recipes recipes) {
        return recipesMapper.insertRecipes(recipes);
    }

    @Override
    public int updateRecipes(Recipes recipes) {
        return recipesMapper.updateRecipes(recipes);
    }

    @Override
    public int deleteRecipesByIds(String[] ids) {
        return recipesMapper.deleteRecipesByIds(ids);
    }

    @Override
    public List<RecipesDto> selectHotRecipes(int limit) {
        return recipesMapper.selectHotRecipes(limit, ImageUtils.getBaseUrl());
    }

    @Override
    public List<RecipesDto> selectRecommendedRecipes(int limit) {
        return recipesMapper.selectRecommendedRecipes(limit, ImageUtils.getBaseUrl());
    }

    @Override
    public List<RecipesDto> selectTodayRecommends(int limit) {
        return recipesMapper.selectTodayRecommends(limit, ImageUtils.getBaseUrl());
    }

    @Override
    public List<String> selectImagesByRecipeId(String recipeId) {
        return recipesMapper.selectImagesByRecipeId(recipeId, ImageUtils.getBaseUrl());
    }

    @Override
    public List<RecipesDto> selectRecipesListWithCondition(RecipeQueryDto query) {
        // 注意：不要在这里调用 PageHelper，由 Controller 统一处理
        int offset = (query.getPageNum() - 1) * query.getPageSize();
        query.setOffset(offset);
        List<RecipesDto> list = recipesMapper.selectRecipesDtoList(query, ImageUtils.getBaseUrl());
        if (list != null) {
            for (RecipesDto dto : list) {
                if (dto.getCategoryNamesStr() != null && !dto.getCategoryNamesStr().isEmpty()) {
                    dto.setCategoryNames(Arrays.asList(dto.getCategoryNamesStr().split(",")));
                }
                if (dto.getIngredientsStr() != null && !dto.getIngredientsStr().isEmpty()) {
                    dto.setIngredients(Arrays.asList(dto.getIngredientsStr().split(",")));
                }
                dto.setCategoryNamesStr(null);
                dto.setIngredientsStr(null);
            }
        }
        return list != null ? list : Collections.emptyList();
    }

    @Override
    public RecipesDto selectRecipesDetailById(String id) {
        RecipesDto dto = recipesMapper.selectRecipesDetailById(id, ImageUtils.getBaseUrl());
        if (dto == null) return null;
        // 拆分分类名称列表
        if (dto.getCategoryNamesStr() != null && !dto.getCategoryNamesStr().isEmpty()) {
            dto.setCategoryNames(Arrays.asList(dto.getCategoryNamesStr().split(",")));
        }
        // 拆分图片 URL 列表
        if (dto.getImgUrlsStr() != null && !dto.getImgUrlsStr().isEmpty()) {
            dto.setImgUrls(Arrays.asList(dto.getImgUrlsStr().split(",")));
        }
        // 拆分食材名称列表
        if (dto.getIngredientsStr() != null && !dto.getIngredientsStr().isEmpty()) {
            dto.setIngredients(Arrays.asList(dto.getIngredientsStr().split(",")));
        }
        // 移除临时字段
        dto.setCategoryNamesStr(null);
        dto.setImgUrlsStr(null);
        dto.setIngredientsStr(null);
        return dto;
    }

    @Override
    public List<RecipesDto> getRecommendedRecipesByIngredients(List<String> ingredientIds) {
        if (ingredientIds == null || ingredientIds.isEmpty()) {
            return Collections.emptyList();
        }
        return recipesMapper.selectRecommendedRecipesByIngredients(ingredientIds, ImageUtils.getBaseUrl());
    }
}