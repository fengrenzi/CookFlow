package com.hnit.system.service;

import com.hnit.system.domain.RecipeCategory;

import java.util.List;

public interface IRecipeCategoryService {

    /**
     * 根据菜谱ID查询关联的分类列表
     */
    List<RecipeCategory> selectByRecipeId(String recipeId);

    /**
     * 根据分类ID查询关联的菜谱列表
     */
    List<RecipeCategory> selectByCategoryId(String categoryId);

    /**
     * 新增一条关联
     */
    int insert(RecipeCategory recipeCategory);

    /**
     * 批量新增关联
     */
    int batchInsert(List<RecipeCategory> list);

    /**
     * 根据菜谱ID删除所有关联
     */
    int deleteByRecipeId(String recipeId);

    /**
     * 根据分类ID删除所有关联
     */
    int deleteByCategoryId(String categoryId);

    /**
     * 删除指定菜谱的指定分类关联
     */
    int deleteByRecipeAndCategory(String recipeId, String categoryId);
}