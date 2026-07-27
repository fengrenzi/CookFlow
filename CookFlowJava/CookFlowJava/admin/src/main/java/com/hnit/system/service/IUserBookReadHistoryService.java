package com.hnit.system.service;

import com.hnit.system.domain.dto.AddReadHistoryDTO;
import com.hnit.system.domain.vo.ReadHistoryVO;
import java.util.List;

public interface IUserBookReadHistoryService {
    void saveOrUpdateProgress(Long userId, AddReadHistoryDTO dto);
    List<ReadHistoryVO> getHistory(Long userId);
}