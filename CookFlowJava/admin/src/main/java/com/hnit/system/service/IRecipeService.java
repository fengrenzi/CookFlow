package com.hnit.system.service;

import com.hnit.system.domain.RecipeStep;
import com.hnit.system.domain.Recipes;
import com.hnit.system.domain.dto.RecipeOption;

import java.util.List;

public interface IRecipeService {
    Recipes getById(Long id);
    List<RecipeStep> getSteps(Long recipeId);
    List<RecipeOption> searchRecipesByKeywords(List<String> keywords);
    List<RecipeOption> getRecommendations();
}