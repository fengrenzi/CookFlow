package com.hnit.system.service;

import com.hnit.system.domain.Category;
import com.hnit.system.domain.dto.CategoryDto;
import com.hnit.system.domain.dto.CategoryQueryDto;
import com.hnit.system.domain.dto.CategoryTreeDto;

import java.util.List;

public interface ICategoryService {
    List<Category> selectCategoryList(Category category);

    Category selectCategoryById(String id);

    int insertCategory(Category category);

    int updateCategory(Category category);

    int deleteCategoryByIds(String[] ids);

    List<CategoryTreeDto> getCategoryTree(String tableName);

    public List<CategoryDto> selectHotCategoryDto(String tableName, int limit);

    // 新增方法：根据查询条件获取带图片URL的分类列表（用于分页）
    List<CategoryDto> selectCategoryDtoList(CategoryQueryDto query);

    // 新增方法：获取指定表名的分类树（每个节点包含图片URL）
    List<CategoryTreeDto> getCategoryTreeWithImages(String tableName);

    List<CategoryTreeDto> getIngredientTree();

}