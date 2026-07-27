package com.hnit.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hnit.common.core.page.TableDataInfo;
import com.hnit.system.utils.ImageUtils;
import com.hnit.system.domain.dto.BookQueryDto;
import com.hnit.system.domain.vo.BookVo;
import com.hnit.system.mapper.BookMapper;
import com.hnit.system.service.IBookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BookServiceImpl implements IBookService {

    @Resource
    private BookMapper bookMapper;

    @Override
    public BookVo getBookDetail(String id) {
        BookVo book = bookMapper.selectById(id);
        if (book != null && book.getCoverImageId() != null) {
            book.setCoverUrl(ImageUtils.getFullUrl(book.getCoverImageId()));
        }
        return book;
    }

    @Override
    public TableDataInfo listBooks(BookQueryDto query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<BookVo> list = bookMapper.selectPage(query);
        PageInfo<BookVo> pageInfo = new PageInfo<>(list);
        pageInfo.getList().forEach(book -> {
            if (book.getCoverImageId() != null) {
                book.setCoverUrl(ImageUtils.getFullUrl(book.getCoverImageId()));
            }
        });
        return new TableDataInfo(pageInfo.getList(), pageInfo.getTotal());
    }
}