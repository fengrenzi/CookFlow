package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.common.core.page.TableDataInfo;
import com.hnit.system.domain.Recipes;
import com.hnit.system.domain.dto.RecipeQueryDto;
import com.hnit.system.domain.dto.RecipesDto;
import com.hnit.system.service.IRecipesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Api(tags = "菜谱管理")
@RestController
@RequestMapping("/recipes")
public class RecipesController extends BaseController {
    @Resource
    private IRecipesService recipesService;

    @ApiOperation("获取菜谱详细信息")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable String id) {
        return success(recipesService.selectRecipesDetailById(id));
    }

    @ApiOperation("新增菜谱")
    @PostMapping
    public AjaxResult add(@RequestBody Recipes recipes) {
        recipes.setId(UUID.randomUUID().toString());
        return toAjax(recipesService.insertRecipes(recipes));
    }

    @ApiOperation("修改菜谱")
    @PutMapping
    public AjaxResult edit(@RequestBody Recipes recipes) {
        return toAjax(recipesService.updateRecipes(recipes));
    }

    @ApiOperation("删除菜谱")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(recipesService.deleteRecipesByIds(ids));
    }

    // 修改：使用 PageHelper 分页，返回 TableDataInfo
    @GetMapping("/list")
    public TableDataInfo list(@ModelAttribute RecipeQueryDto query) {
        startPage(); // 开启分页
        List<RecipesDto> list = recipesService.selectRecipesListWithCondition(query);
        return getDataTable(list); // 返回分页数据
    }

    @GetMapping("/hot")
    public AjaxResult getHotRecipes() {
        return success(recipesService.selectHotRecipes(30));
    }

    @GetMapping("/recommended")
    public AjaxResult getRecommendedRecipes() {
        return success(recipesService.selectRecommendedRecipes(8));
    }

    @GetMapping("/today")
    public AjaxResult getTodayRecommends() {
        return success(recipesService.selectTodayRecommends(8));
    }

    @GetMapping("/{id}/images")
    public AjaxResult getRecipeImages(@PathVariable String id) {
        return success(recipesService.selectImagesByRecipeId(id));
    }

    @GetMapping("/recommendByIngredients")
    public AjaxResult getRecommendedByIngredients(@RequestParam("ingredientIds") List<String> ingredientIds) {
        List<RecipesDto> list = recipesService.getRecommendedRecipesByIngredients(ingredientIds);
        return success(list);
    }
}