package com.hnit.system.controller;

import com.hnit.system.domain.vo.DishHotVO;
import com.hnit.system.domain.vo.UserActiveVO;
import com.hnit.system.service.IAnalysisService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/food/analysis")
public class AnalysisController {

    @Resource
    private IAnalysisService analysisService;

    @GetMapping("/userActive")
    public Map<String, Object> getUserActive() {
        List<UserActiveVO> list = analysisService.getUserActiveLast7Days();
        Map<String, Object> result = new HashMap<>();
        // 使用 collect(Collectors.toList()) 替代 toList()
        List<String> dates = list.stream().map(UserActiveVO::getDate).collect(Collectors.toList());
        List<Long> counts = list.stream().map(UserActiveVO::getActiveCounts).collect(Collectors.toList());
        result.put("dates", dates);
        result.put("activeCounts", counts);
        return result;
    }

    @GetMapping("/dishHot")
    public List<DishHotVO> getDishHot() {
        return analysisService.getDishHotTopN(5);
    }
}