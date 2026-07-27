package com.hnit.system.service.impl;

import com.hnit.system.domain.RecipeIngredients;
import com.hnit.system.mapper.RecipeIngredientsMapper;
import com.hnit.system.service.IRecipeIngredientsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class RecipeIngredientsServiceImpl implements IRecipeIngredientsService {
    @Resource
    private RecipeIngredientsMapper recipeIngredientsMapper;

    @Override
    public List<RecipeIngredients> selectRecipeIngredientsList(RecipeIngredients recipeIngredients) {
        return recipeIngredientsMapper.selectRecipeIngredientsList(recipeIngredients);
    }

    @Override
    public List<RecipeIngredients> selectRecipeIngredientsByRecipeId(String recipeId) {
        return recipeIngredientsMapper.selectRecipeIngredientsByRecipeId(recipeId);
    }

    @Override
    public int insertRecipeIngredients(RecipeIngredients recipeIngredients) {
        if (recipeIngredients.getId() == null) {
            recipeIngredients.setId(UUID.randomUUID().toString());
        }
        return recipeIngredientsMapper.insertRecipeIngredients(recipeIngredients);
    }

    @Override
    public int updateRecipeIngredients(RecipeIngredients recipeIngredients) {
        return recipeIngredientsMapper.updateRecipeIngredients(recipeIngredients);
    }

    @Override
    public int deleteRecipeIngredientsByRecipeIds(String[] recipeIds) {
        return recipeIngredientsMapper.deleteRecipeIngredientsByRecipeIds(recipeIds);
    }

    @Override
    public int deleteRecipeIngredientById(String id) {
        return recipeIngredientsMapper.deleteRecipeIngredientById(id);
    }
}