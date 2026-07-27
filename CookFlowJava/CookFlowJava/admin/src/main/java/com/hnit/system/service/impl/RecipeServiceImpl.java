package com.hnit.system.service.impl;

import com.hnit.system.domain.RecipeStep;
import com.hnit.system.domain.Recipes;
import com.hnit.system.domain.dto.RecipeOption;
import com.hnit.system.mapper.RecipesMapper;
import com.hnit.system.mapper.RecipeStepMapper;
import com.hnit.system.service.IRecipeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RecipeServiceImpl implements IRecipeService {

    @Resource
    private RecipesMapper recipesMapper;
    @Resource
    private RecipeStepMapper stepMapper;

    @Override
    public Recipes getById(Long id) {
        return recipesMapper.selectById(id);
    }

    @Override
    public List<RecipeStep> getSteps(Long recipeId) {
        return stepMapper.selectByRecipeId(recipeId);
    }

    @Override
    public List<RecipeOption> searchRecipesByKeywords(List<String> keywords) {
        // 示例：返回两条模拟数据，实际应实现数据库检索
        List<RecipeOption> options = new ArrayList<>();
        RecipeOption opt1 = new RecipeOption();
        opt1.setId(1L);
        opt1.setTitle("番茄炒蛋");
        opt1.setImgUrl("http://localhost:9999/image/tomato_egg.jpg");
        options.add(opt1);

        RecipeOption opt2 = new RecipeOption();
        opt2.setId(2L);
        opt2.setTitle("宫保鸡丁");
        opt2.setImgUrl("http://localhost:9999/image/kung_pao.jpg");
        options.add(opt2);
        return options;
    }

    @Override
    public List<RecipeOption> getRecommendations() {
        return searchRecipesByKeywords(Collections.emptyList());
    }
}