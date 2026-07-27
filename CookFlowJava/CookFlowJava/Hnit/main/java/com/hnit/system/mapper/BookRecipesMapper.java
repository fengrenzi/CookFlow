package com.hnit.system.mapper;

import java.util.List;
import com.hnit.system.domain.BookRecipes;

/**
 * 书籍中页码到菜谱的映射Mapper接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface BookRecipesMapper 
{
    /**
     * 查询书籍中页码到菜谱的映射
     * 
     * @param bookId 书籍中页码到菜谱的映射主键
     * @return 书籍中页码到菜谱的映射
     */
    public BookRecipes selectBookRecipesByBookId(String bookId);

    /**
     * 查询书籍中页码到菜谱的映射列表
     * 
     * @param bookRecipes 书籍中页码到菜谱的映射
     * @return 书籍中页码到菜谱的映射集合
     */
    public List<BookRecipes> selectBookRecipesList(BookRecipes bookRecipes);

    /**
     * 新增书籍中页码到菜谱的映射
     * 
     * @param bookRecipes 书籍中页码到菜谱的映射
     * @return 结果
     */
    public int insertBookRecipes(BookRecipes bookRecipes);

    /**
     * 修改书籍中页码到菜谱的映射
     * 
     * @param bookRecipes 书籍中页码到菜谱的映射
     * @return 结果
     */
    public int updateBookRecipes(BookRecipes bookRecipes);

    /**
     * 删除书籍中页码到菜谱的映射
     * 
     * @param bookId 书籍中页码到菜谱的映射主键
     * @return 结果
     */
    public int deleteBookRecipesByBookId(String bookId);

    /**
     * 批量删除书籍中页码到菜谱的映射
     * 
     * @param bookIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBookRecipesByBookIds(String[] bookIds);
}
