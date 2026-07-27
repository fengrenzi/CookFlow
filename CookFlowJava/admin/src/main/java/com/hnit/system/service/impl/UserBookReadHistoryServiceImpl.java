package com.hnit.system.service.impl;

import com.hnit.common.utils.uuid.IdUtils;
import com.hnit.system.domain.Books;
import com.hnit.system.domain.UserBookReadHistory;
import com.hnit.system.domain.dto.AddReadHistoryDTO;
import com.hnit.system.domain.vo.ReadHistoryVO;
import com.hnit.system.mapper.BookMapper;
import com.hnit.system.mapper.UserBookReadHistoryMapper;
import com.hnit.system.service.IUserBookReadHistoryService;
import com.hnit.system.utils.ImageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserBookReadHistoryServiceImpl implements IUserBookReadHistoryService {

    @Resource
    private UserBookReadHistoryMapper historyMapper;
    @Resource
    private BookMapper booksMapper;

    @Override
    @Transactional
    public void saveOrUpdateProgress(Long userId, AddReadHistoryDTO dto) {
        UserBookReadHistory exist = historyMapper.selectByUserAndBook(userId, dto.getBookId());
        if (exist == null) {
            UserBookReadHistory history = new UserBookReadHistory();
            history.setId(IdUtils.fastSimpleUUID());
            history.setUserId(userId);
            history.setBookId(dto.getBookId());
            history.setProgress(dto.getProgress());
            historyMapper.insert(history);
        } else {
            historyMapper.updateProgress(userId, dto.getBookId(), dto.getProgress());
        }
    }

    @Override
    public List<ReadHistoryVO> getHistory(Long userId) {
        List<UserBookReadHistory> histories = historyMapper.selectByUserId(userId);
        if (histories.isEmpty()) return new ArrayList<>();
        List<String> bookIds = histories.stream().map(UserBookReadHistory::getBookId).collect(Collectors.toList());
        List<Books> books = booksMapper.selectByIds(bookIds);
        List<ReadHistoryVO> result = new ArrayList<>();
        for (UserBookReadHistory h : histories) {
            Books book = books.stream().filter(b -> b.getId().equals(h.getBookId())).findFirst().orElse(null);
            if (book != null) {
                ReadHistoryVO vo = new ReadHistoryVO();
                vo.setId(h.getId());
                vo.setBookId(book.getId());
                vo.setBookTitle(book.getTitle());
                vo.setAuthor(book.getAuthor());
                if (book.getCoverImageId() != null) {
                    vo.setCoverUrl(ImageUtils.getFullUrl(book.getCoverImageId()));
                }
                vo.setLastReadTime(h.getLastReadTime());
                vo.setProgress(h.getProgress());
                result.add(vo);
            }
        }
        return result;
    }
}