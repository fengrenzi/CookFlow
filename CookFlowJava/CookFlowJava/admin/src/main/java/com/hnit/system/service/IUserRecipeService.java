package com.hnit.system.service;

import com.hnit.system.domain.vo.RecipeSimpleVO;
import java.util.List;

public interface IUserRecipeService {
    List<RecipeSimpleVO> getPublishedRecipes(Long userId);
    List<RecipeSimpleVO> getCollectedRecipes(Long userId);
    List<RecipeSimpleVO> getLikedRecipes(Long userId);
}