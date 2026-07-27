package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.RecipeCategory;
import com.hnit.system.service.IRecipeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "菜谱分类关联管理")
@RestController
@RequestMapping("/recipeCategory")
public class RecipeCategoryController extends BaseController {

    @Resource
    private IRecipeCategoryService recipeCategoryService;

    @ApiOperation("根据菜谱ID查询关联的分类")
    @GetMapping("/recipe/{recipeId}")
    public AjaxResult getByRecipeId(@PathVariable String recipeId) {
        return success(recipeCategoryService.selectByRecipeId(recipeId));
    }

    @ApiOperation("根据分类ID查询关联的菜谱")
    @GetMapping("/category/{categoryId}")
    public AjaxResult getByCategoryId(@PathVariable String categoryId) {
        return success(recipeCategoryService.selectByCategoryId(categoryId));
    }

    @ApiOperation("新增菜谱分类关联")
    @PostMapping
    public AjaxResult add(@RequestBody RecipeCategory recipeCategory) {
        return toAjax(recipeCategoryService.insert(recipeCategory));
    }

    @ApiOperation("批量新增菜谱分类关联")
    @PostMapping("/batch")
    public AjaxResult batchAdd(@RequestBody List<RecipeCategory> list) {
        return toAjax(recipeCategoryService.batchInsert(list));
    }

    @ApiOperation("删除菜谱的所有分类关联")
    @DeleteMapping("/recipe/{recipeId}")
    public AjaxResult deleteByRecipeId(@PathVariable String recipeId) {
        return toAjax(recipeCategoryService.deleteByRecipeId(recipeId));
    }

    @ApiOperation("删除分类的所有菜谱关联")
    @DeleteMapping("/category/{categoryId}")
    public AjaxResult deleteByCategoryId(@PathVariable String categoryId) {
        return toAjax(recipeCategoryService.deleteByCategoryId(categoryId));
    }

    @ApiOperation("删除指定菜谱的指定分类关联")
    @DeleteMapping
    public AjaxResult deleteByRecipeAndCategory(@RequestParam String recipeId, @RequestParam String categoryId) {
        return toAjax(recipeCategoryService.deleteByRecipeAndCategory(recipeId, categoryId));
    }
}