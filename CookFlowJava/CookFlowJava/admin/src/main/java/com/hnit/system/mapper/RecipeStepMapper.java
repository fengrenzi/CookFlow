package com.hnit.system.mapper;

import com.hnit.system.domain.RecipeStep;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RecipeStepMapper {
    List<RecipeStep> selectByRecipeId(@Param("recipeId") Long recipeId);
}