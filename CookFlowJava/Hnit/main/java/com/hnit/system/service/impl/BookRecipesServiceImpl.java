package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.BookRecipesMapper;
import com.hnit.system.domain.BookRecipes;
import com.hnit.system.service.IBookRecipesService;

/**
 * 书籍中页码到菜谱的映射Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class BookRecipesServiceImpl implements IBookRecipesService 
{
    @Resource
    private BookRecipesMapper bookRecipesMapper;

    /**
     * 查询书籍中页码到菜谱的映射
     * 
     * @param bookId 书籍中页码到菜谱的映射主键
     * @return 书籍中页码到菜谱的映射
     */
    @Override
    public BookRecipes selectBookRecipesByBookId(String bookId)
    {
        return bookRecipesMapper.selectBookRecipesByBookId(bookId);
    }

    /**
     * 查询书籍中页码到菜谱的映射列表
     * 
     * @param bookRecipes 书籍中页码到菜谱的映射
     * @return 书籍中页码到菜谱的映射
     */
    @Override
    public List<BookRecipes> selectBookRecipesList(BookRecipes bookRecipes)
    {
        return bookRecipesMapper.selectBookRecipesList(bookRecipes);
    }

    /**
     * 新增书籍中页码到菜谱的映射
     * 
     * @param bookRecipes 书籍中页码到菜谱的映射
     * @return 结果
     */
    @Override
    public int insertBookRecipes(BookRecipes bookRecipes)
    {
        return bookRecipesMapper.insertBookRecipes(bookRecipes);
    }

    /**
     * 修改书籍中页码到菜谱的映射
     * 
     * @param bookRecipes 书籍中页码到菜谱的映射
     * @return 结果
     */
    @Override
    public int updateBookRecipes(BookRecipes bookRecipes)
    {
        return bookRecipesMapper.updateBookRecipes(bookRecipes);
    }

    /**
     * 批量删除书籍中页码到菜谱的映射
     * 
     * @param bookIds 需要删除的书籍中页码到菜谱的映射主键
     * @return 结果
     */
    @Override
    public int deleteBookRecipesByBookIds(String[] bookIds)
    {
        return bookRecipesMapper.deleteBookRecipesByBookIds(bookIds);
    }

    /**
     * 删除书籍中页码到菜谱的映射信息
     * 
     * @param bookId 书籍中页码到菜谱的映射主键
     * @return 结果
     */
    @Override
    public int deleteBookRecipesByBookId(String bookId)
    {
        return bookRecipesMapper.deleteBookRecipesByBookId(bookId);
    }
}
