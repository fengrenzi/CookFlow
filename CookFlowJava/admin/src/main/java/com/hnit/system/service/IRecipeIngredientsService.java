package com.hnit.system.service;

import com.hnit.system.domain.RecipeIngredients;

import java.util.List;

public interface IRecipeIngredientsService {
    List<RecipeIngredients> selectRecipeIngredientsList(RecipeIngredients recipeIngredients);

    List<RecipeIngredients> selectRecipeIngredientsByRecipeId(String recipeId);

    int insertRecipeIngredients(RecipeIngredients recipeIngredients);

    int updateRecipeIngredients(RecipeIngredients recipeIngredients);

    int deleteRecipeIngredientsByRecipeIds(String[] recipeIds);

    int deleteRecipeIngredientById(String id);
}