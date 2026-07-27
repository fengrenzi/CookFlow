package com.hnit.system.service.impl;

import com.hnit.system.domain.vo.DishHotVO;
import com.hnit.system.domain.vo.UserActiveVO;
import com.hnit.system.mapper.AnalysisMapper;
import com.hnit.system.service.IAnalysisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AnalysisServiceImpl implements IAnalysisService {

    @Resource
    private AnalysisMapper analysisMapper;

    @Override
    public List<UserActiveVO> getUserActiveLast7Days() {
        return analysisMapper.getUserActiveLast7Days();
    }

    @Override
    public List<DishHotVO> getDishHotTopN(int limit) {
        return analysisMapper.getDishHotTopN(limit);
    }
}