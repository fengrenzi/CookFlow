package com.hnit.system.service.impl;

import com.hnit.system.domain.Books;
import com.hnit.system.domain.UserBookReadHistory;
import com.hnit.system.domain.UserInteraction;
import com.hnit.system.domain.vo.BookSimpleVO;
import com.hnit.system.mapper.BookMapper;
import com.hnit.system.mapper.UserBookReadHistoryMapper;
import com.hnit.system.mapper.UserInteractionMapper;
import com.hnit.system.service.IUserBookService;
import com.hnit.system.utils.ImageUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserBookServiceImpl implements IUserBookService {

    @Resource
    private BookMapper booksMapper;
    @Resource
    private UserInteractionMapper interactionMapper;
    @Resource
    private UserBookReadHistoryMapper historyMapper;

    @Override
    public List<BookSimpleVO> getPublishedBooks(Long userId) {
        List<Books> books = booksMapper.selectByUserId(userId);
        return books.stream().map(this::convertToSimpleVO).collect(Collectors.toList());
    }

    @Override
    public List<BookSimpleVO> getCollectedBooks(Long userId) {
        List<UserInteraction> interactions = interactionMapper.selectByUserAndType(userId, "book", "favorite");
        if (interactions.isEmpty()) return Collections.emptyList();
        List<String> bookIds = interactions.stream().map(UserInteraction::getTargetId).collect(Collectors.toList());
        List<Books> books = booksMapper.selectByIds(bookIds);
        List<BookSimpleVO> result = books.stream().map(this::convertToSimpleVO).collect(Collectors.toList());
        // 填充收藏日期
        for (BookSimpleVO vo : result) {
            interactions.stream()
                    .filter(i -> i.getTargetId().equals(vo.getId()))
                    .findFirst()
                    .ifPresent(i -> vo.setCollectDate(i.getCreatedAt().toString()));
        }
        return result;
    }

    @Override
    public List<BookSimpleVO> getReadingHistory(Long userId) {
        List<UserBookReadHistory> histories = historyMapper.selectByUserId(userId);
        if (histories.isEmpty()) return Collections.emptyList();
        List<String> bookIds = histories.stream().map(UserBookReadHistory::getBookId).collect(Collectors.toList());
        List<Books> books = booksMapper.selectByIds(bookIds);
        List<BookSimpleVO> result = books.stream().map(this::convertToSimpleVO).collect(Collectors.toList());
        for (BookSimpleVO vo : result) {
            histories.stream()
                    .filter(h -> h.getBookId().equals(vo.getId()))
                    .findFirst()
                    .ifPresent(h -> {
                        vo.setLastRead(h.getLastReadTime().toString());
                        vo.setProgress(h.getProgress());
                    });
        }
        return result;
    }

    private BookSimpleVO convertToSimpleVO(Books book) {
        BookSimpleVO vo = new BookSimpleVO();
        vo.setId(book.getId());
        vo.setTitle(book.getTitle());
        vo.setAuthor(book.getAuthor());
        if (book.getCoverImageId() != null) {
            vo.setCoverUrl(ImageUtils.getFullUrl(book.getCoverImageId()));
        }
        vo.setPublishDate(book.getCreatedAt() != null ? book.getCreatedAt().toString() : null);
        return vo;
    }
}