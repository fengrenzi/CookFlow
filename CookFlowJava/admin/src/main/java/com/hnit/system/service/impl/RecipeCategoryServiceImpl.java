package com.hnit.system.service.impl;

import com.hnit.system.domain.RecipeCategory;
import com.hnit.system.mapper.RecipeCategoryMapper;
import com.hnit.system.service.IRecipeCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RecipeCategoryServiceImpl implements IRecipeCategoryService {

    @Resource
    private RecipeCategoryMapper recipeCategoryMapper;

    @Override
    public List<RecipeCategory> selectByRecipeId(String recipeId) {
        return recipeCategoryMapper.selectByRecipeId(recipeId);
    }

    @Override
    public List<RecipeCategory> selectByCategoryId(String categoryId) {
        return recipeCategoryMapper.selectByCategoryId(categoryId);
    }

    @Override
    public int insert(RecipeCategory recipeCategory) {
        if (recipeCategory.getId() == null) {
            recipeCategory.setId(UUID.randomUUID().toString());
        }
        if (recipeCategory.getCreateTime() == null) {
            recipeCategory.setCreateTime(new Date());
        }
        return recipeCategoryMapper.insert(recipeCategory);
    }

    @Override
    @Transactional
    public int batchInsert(List<RecipeCategory> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        for (RecipeCategory rc : list) {
            if (rc.getId() == null) {
                rc.setId(UUID.randomUUID().toString());
            }
            if (rc.getCreateTime() == null) {
                rc.setCreateTime(new Date());
            }
        }
        return recipeCategoryMapper.batchInsert(list);
    }

    @Override
    public int deleteByRecipeId(String recipeId) {
        return recipeCategoryMapper.deleteByRecipeId(recipeId);
    }

    @Override
    public int deleteByCategoryId(String categoryId) {
        return recipeCategoryMapper.deleteByCategoryId(categoryId);
    }

    @Override
    public int deleteByRecipeAndCategory(String recipeId, String categoryId) {
        return recipeCategoryMapper.deleteByRecipeAndCategory(recipeId, categoryId);
    }
}