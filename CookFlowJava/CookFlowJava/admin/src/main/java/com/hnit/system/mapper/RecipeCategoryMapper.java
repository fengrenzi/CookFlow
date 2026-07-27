package com.hnit.system.mapper;

import com.hnit.system.domain.RecipeCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecipeCategoryMapper {

    /**
     * 根据菜谱ID查询关联的分类
     */
    List<RecipeCategory> selectByRecipeId(@Param("recipeId") String recipeId);

    /**
     * 根据分类ID查询关联的菜谱
     */
    List<RecipeCategory> selectByCategoryId(@Param("categoryId") String categoryId);

    /**
     * 插入一条关联记录
     */
    int insert(RecipeCategory recipeCategory);

    /**
     * 批量插入关联记录
     */
    int batchInsert(List<RecipeCategory> list);

    /**
     * 根据菜谱ID删除所有关联
     */
    int deleteByRecipeId(@Param("recipeId") String recipeId);

    /**
     * 根据分类ID删除所有关联
     */
    int deleteByCategoryId(@Param("categoryId") String categoryId);

    /**
     * 根据菜谱ID和分类ID删除单条关联（若需要）
     */
    int deleteByRecipeAndCategory(@Param("recipeId") String recipeId, @Param("categoryId") String categoryId);
}