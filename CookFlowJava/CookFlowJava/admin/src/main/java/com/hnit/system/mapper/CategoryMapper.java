package com.hnit.system.mapper;

import com.hnit.system.domain.Category;
import com.hnit.system.domain.dto.CategoryDto;
import com.hnit.system.domain.dto.CategoryQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectCategoryList(Category category);

    Category selectCategoryById(String id);

    int insertCategory(Category category);

    int updateCategory(Category category);

    int deleteCategoryByIds(String[] ids);

    List<Category> selectByTableName(String tableName);

    /**
     * 查询热门分类（二级分类，按检索次数排序）
     * @param tableName 表名
     * @param limit     返回条数
     * @return 分类列表（id, name, searchCount）
     */
    List<CategoryDto> selectHotCategoryDto(@Param("tableName") String tableName, @Param("limit") int limit);

    // 根据查询条件分页查询分类列表（带图片URL）
    List<CategoryDto> selectCategoryDtoList(@Param("query") CategoryQueryDto query,
                                            @Param("imgBaseUrl") String imgBaseUrl);

    // 查询指定表名下所有分类（含图片URL），用于构建树
    List<CategoryDto> selectAllByTableNameWithImages(@Param("tableName") String tableName,
                                                     @Param("imgBaseUrl") String imgBaseUrl);

    List<Category> selectIngredientsWithSort();

    /**
     * 查询指定 tableName 的一级分类（parent_id IS NULL）
     */
    List<Category> selectRootCategoriesByTableName(@Param("tableName") String tableName);

    /**
     * 查询指定 tableName 的所有二级分类，按 sort 排序
     */
    List<Category> selectSubCategoriesByTableName(@Param("tableName") String tableName);
}