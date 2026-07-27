package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.common.core.page.TableDataInfo;
import com.hnit.system.domain.Category;
import com.hnit.system.domain.dto.CategoryDto;
import com.hnit.system.domain.dto.CategoryQueryDto;
import com.hnit.system.domain.dto.CategoryTreeDto;
import com.hnit.system.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "通用分类管理")
@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController {
    @Resource
    private ICategoryService categoryService;

    @ApiOperation("查询分类列表（支持分页和条件）")
    @GetMapping("/list")
    public TableDataInfo list(CategoryQueryDto query) {
        startPage();
        List<CategoryDto> list = categoryService.selectCategoryDtoList(query);
        return getDataTable(list);
    }

    @ApiOperation("获取分类详细信息")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable String id) {
        return success(categoryService.selectCategoryById(id));
    }

    @ApiOperation("新增分类")
    @PostMapping
    public AjaxResult add(@RequestBody Category category) {
        return toAjax(categoryService.insertCategory(category));
    }

    @ApiOperation("修改分类")
    @PutMapping
    public AjaxResult edit(@RequestBody Category category) {
        return toAjax(categoryService.updateCategory(category));
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(categoryService.deleteCategoryByIds(ids));
    }

    @ApiOperation("获取分类树（用于前端筛选器）")
    @GetMapping("/tree")
    public AjaxResult getCategoryTree(@RequestParam String tableName) {
        List<CategoryTreeDto> tree = categoryService.getCategoryTreeWithImages(tableName);
        return success(tree);
    }

    @ApiOperation("获取热门分类（按检索次数）")
    @GetMapping("/hot")
    public AjaxResult getHotCategories() {
        List<CategoryDto> list = categoryService.selectHotCategoryDto("recipes", 40);
        return success(list);
    }
}