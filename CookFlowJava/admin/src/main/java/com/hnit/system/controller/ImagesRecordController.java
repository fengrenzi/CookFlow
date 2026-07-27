package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.ImagesRecord;
import com.hnit.system.service.IImagesRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "图片关联管理")
@RestController
@RequestMapping("/imagesRecord")
public class ImagesRecordController extends BaseController {
    @Resource
    private IImagesRecordService imagesRecordService;

    @ApiOperation("根据菜谱ID查询关联图片")
    @GetMapping("/recipe/{recipeId}")
    public AjaxResult getByRecipeId(@PathVariable String recipeId) {
        return success(imagesRecordService.selectByRecipeId(recipeId));
    }

    @ApiOperation("新增图片关联")
    @PostMapping
    public AjaxResult add(@RequestBody ImagesRecord record) {
        return toAjax(imagesRecordService.insert(record));
    }

    @ApiOperation("批量新增图片关联")
    @PostMapping("/batch")
    public AjaxResult batchAdd(@RequestBody List<ImagesRecord> list) {
        return toAjax(imagesRecordService.batchInsert(list));
    }

    @ApiOperation("修改图片关联")
    @PutMapping
    public AjaxResult edit(@RequestBody ImagesRecord record) {
        return toAjax(imagesRecordService.update(record));
    }

    @ApiOperation("删除图片关联")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable String id) {
        return toAjax(imagesRecordService.deleteById(id));
    }

    @ApiOperation("删除菜谱的所有图片关联")
    @DeleteMapping("/recipe/{recipeId}")
    public AjaxResult removeByRecipeId(@PathVariable String recipeId) {
        return toAjax(imagesRecordService.deleteByRecipeId(recipeId));
    }
}