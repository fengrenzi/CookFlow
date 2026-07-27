package com.hnit.system.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hnit.system.mapper.BooksMapper;
import com.hnit.system.domain.Books;
import com.hnit.system.service.IBooksService;

/**
 * 书籍Service业务层处理
 * 
 * @author Z
 * @date 2026-03-23
 */
@Service
public class BooksServiceImpl implements IBooksService 
{
    @Resource
    private BooksMapper booksMapper;

    /**
     * 查询书籍
     * 
     * @param id 书籍主键
     * @return 书籍
     */
    @Override
    public Books selectBooksById(String id)
    {
        return booksMapper.selectBooksById(id);
    }

    /**
     * 查询书籍列表
     * 
     * @param books 书籍
     * @return 书籍
     */
    @Override
    public List<Books> selectBooksList(Books books)
    {
        return booksMapper.selectBooksList(books);
    }

    /**
     * 新增书籍
     * 
     * @param books 书籍
     * @return 结果
     */
    @Override
    public int insertBooks(Books books)
    {
        return booksMapper.insertBooks(books);
    }

    /**
     * 修改书籍
     * 
     * @param books 书籍
     * @return 结果
     */
    @Override
    public int updateBooks(Books books)
    {
        return booksMapper.updateBooks(books);
    }

    /**
     * 批量删除书籍
     * 
     * @param ids 需要删除的书籍主键
     * @return 结果
     */
    @Override
    public int deleteBooksByIds(String[] ids)
    {
        return booksMapper.deleteBooksByIds(ids);
    }

    /**
     * 删除书籍信息
     * 
     * @param id 书籍主键
     * @return 结果
     */
    @Override
    public int deleteBooksById(String id)
    {
        return booksMapper.deleteBooksById(id);
    }
}
