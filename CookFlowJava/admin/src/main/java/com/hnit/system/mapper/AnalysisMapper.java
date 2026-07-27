package com.hnit.system.mapper;

import com.hnit.system.domain.vo.DishHotVO;
import com.hnit.system.domain.vo.UserActiveVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnalysisMapper {
    List<UserActiveVO> getUserActiveLast7Days();
    List<DishHotVO> getDishHotTopN(@Param("limit") int limit);
}