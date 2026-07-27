package com.hnit.system.service;

import com.hnit.system.domain.vo.BookSimpleVO;
import java.util.List;

public interface IUserBookService {
    List<BookSimpleVO> getPublishedBooks(Long userId);
    List<BookSimpleVO> getCollectedBooks(Long userId);
    List<BookSimpleVO> getReadingHistory(Long userId);
}