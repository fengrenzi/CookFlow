package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.common.core.page.TableDataInfo;
import com.hnit.system.domain.RecipeIngredients;
import com.hnit.system.service.IRecipeIngredientsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "菜谱食材关联管理")
@RestController
@RequestMapping("/recipeIngredients")
public class RecipeIngredientsController extends BaseController {
    @Resource
    private IRecipeIngredientsService recipeIngredientsService;

    @ApiOperation("查询菜谱食材关联列表")
    @GetMapping("/list")
    public TableDataInfo list(RecipeIngredients recipeIngredients) {
        startPage();
        List<RecipeIngredients> list = recipeIngredientsService.selectRecipeIngredientsList(recipeIngredients);
        return getDataTable(list);
    }

    @ApiOperation("根据菜谱ID查询关联食材")
    @GetMapping("/recipe/{recipeId}")
    public AjaxResult getByRecipeId(@PathVariable String recipeId) {
        return success(recipeIngredientsService.selectRecipeIngredientsByRecipeId(recipeId));
    }

    @ApiOperation("新增菜谱食材关联")
    @PostMapping
    public AjaxResult add(@RequestBody RecipeIngredients recipeIngredients) {
        return toAjax(recipeIngredientsService.insertRecipeIngredients(recipeIngredients));
    }

    @ApiOperation("修改菜谱食材关联")
    @PutMapping
    public AjaxResult edit(@RequestBody RecipeIngredients recipeIngredients) {
        return toAjax(recipeIngredientsService.updateRecipeIngredients(recipeIngredients));
    }

    @ApiOperation("删除菜谱的所有食材关联")
    @DeleteMapping("/recipes/{recipeIds}")
    public AjaxResult removeByRecipeIds(@PathVariable String[] recipeIds) {
        return toAjax(recipeIngredientsService.deleteRecipeIngredientsByRecipeIds(recipeIds));
    }

    @ApiOperation("删除指定关联（根据关联ID）")
    @DeleteMapping("/{id}")
    public AjaxResult removeById(@PathVariable String id) {
        return toAjax(recipeIngredientsService.deleteRecipeIngredientById(id));
    }
}