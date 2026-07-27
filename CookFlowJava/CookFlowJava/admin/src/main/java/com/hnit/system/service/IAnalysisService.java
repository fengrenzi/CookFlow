package com.hnit.system.service;


import com.hnit.system.domain.vo.DishHotVO;
import com.hnit.system.domain.vo.UserActiveVO;

import java.util.List;

public interface IAnalysisService {
    List<UserActiveVO> getUserActiveLast7Days();
    List<DishHotVO> getDishHotTopN(int limit);
}