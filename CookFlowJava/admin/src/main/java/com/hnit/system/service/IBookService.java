package com.hnit.system.service;

import com.hnit.common.core.page.TableDataInfo;
import com.hnit.system.domain.dto.BookQueryDto;
import com.hnit.system.domain.vo.BookVo;

public interface IBookService {
    BookVo getBookDetail(String id);
    TableDataInfo listBooks(BookQueryDto query);
}