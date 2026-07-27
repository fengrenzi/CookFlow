package com.hnit.system.mapper;

import com.hnit.system.domain.BookRecipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookRecipesMapper {

    // 根据主键 ID 查询（主键类型为 Long）
    BookRecipe selectById(@Param("id") Long id);

    // 根据书籍 ID 查询该书籍下的所有菜谱关联
    List<BookRecipe> selectByBookId(@Param("bookId") String bookId);

    // 根据菜谱 ID 查询哪些书籍包含了该菜谱
    List<BookRecipe> selectByRecipeId(@Param("recipeId") String recipeId);

    // 插入一条关联记录
    int insert(BookRecipe record);

    // 更新排序或页码
    int update(BookRecipe record);

    // 根据 ID 删除
    int deleteById(@Param("id") Long id);
}