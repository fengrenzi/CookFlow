package com.hnit.system.service;

import java.util.List;
import com.hnit.system.domain.Books;

/**
 * 书籍Service接口
 * 
 * @author Z
 * @date 2026-03-23
 */
public interface IBooksService 
{
    /**
     * 查询书籍
     * 
     * @param id 书籍主键
     * @return 书籍
     */
    public Books selectBooksById(String id);

    /**
     * 查询书籍列表
     * 
     * @param books 书籍
     * @return 书籍集合
     */
    public List<Books> selectBooksList(Books books);

    /**
     * 新增书籍
     * 
     * @param books 书籍
     * @return 结果
     */
    public int insertBooks(Books books);

    /**
     * 修改书籍
     * 
     * @param books 书籍
     * @return 结果
     */
    public int updateBooks(Books books);

    /**
     * 批量删除书籍
     * 
     * @param ids 需要删除的书籍主键集合
     * @return 结果
     */
    public int deleteBooksByIds(String[] ids);

    /**
     * 删除书籍信息
     * 
     * @param id 书籍主键
     * @return 结果
     */
    public int deleteBooksById(String id);
}
