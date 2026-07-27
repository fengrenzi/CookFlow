package com.hnit.system.service;

import com.hnit.system.domain.dto.RegionFoodDTO;

import java.util.List;

public interface IRegionFoodService {

    /**
     * 获取所有省份数据（带美食数量）
     */
    List<RegionFoodDTO> getAllProvinces();

    /**
     * 下钻到指定地区，返回地区详情和美食列表
     */
    RegionFoodDTO drillDown(String regionCode);

    /**
     * 获取省份下的城市列表（用于下钻）
     */
    List<RegionFoodDTO> getCitiesByProvince(String provinceCode);
}