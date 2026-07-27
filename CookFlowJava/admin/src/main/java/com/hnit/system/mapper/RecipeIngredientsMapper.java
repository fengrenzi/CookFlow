package com.hnit.system.mapper;

import com.hnit.system.domain.RecipeIngredients;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecipeIngredientsMapper {
    List<RecipeIngredients> selectRecipeIngredientsList(RecipeIngredients recipeIngredients);

    List<RecipeIngredients> selectRecipeIngredientsByRecipeId(String recipeId);

    int insertRecipeIngredients(RecipeIngredients recipeIngredients);

    int updateRecipeIngredients(RecipeIngredients recipeIngredients);

    int deleteRecipeIngredientsByRecipeIds(String[] recipeIds);

    int deleteRecipeIngredientById(String id);
}