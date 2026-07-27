package com.hnit.system.controller;

import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.dto.RegionFoodDTO;
import com.hnit.system.service.IRegionFoodService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionFoodController {

    @Resource
    private IRegionFoodService regionFoodService;  // 注入接口

    @GetMapping("/provinces")
    public AjaxResult getProvinces() {
        List<RegionFoodDTO> provinces = regionFoodService.getAllProvinces();
        return AjaxResult.success(provinces);
    }

    @GetMapping("/cities/{provinceCode}")
    public AjaxResult getCities(@PathVariable String provinceCode) {
        List<RegionFoodDTO> cities = regionFoodService.getCitiesByProvince(provinceCode);
        return AjaxResult.success(cities);
    }

    @GetMapping("/drill/{regionCode}")
    public AjaxResult drillDown(@PathVariable String regionCode) {
        RegionFoodDTO regionData = regionFoodService.drillDown(regionCode);
        if (regionData == null) {
            return AjaxResult.error("地区不存在");
        }
        return AjaxResult.success(regionData);
    }
}