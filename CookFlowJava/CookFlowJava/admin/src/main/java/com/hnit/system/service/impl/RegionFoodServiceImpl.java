package com.hnit.system.service.impl;

import com.hnit.system.domain.Region;
import com.hnit.system.domain.dto.FoodCardDTO;
import com.hnit.system.domain.dto.RegionFoodDTO;
import com.hnit.system.mapper.RegionFoodMapper;
import com.hnit.system.mapper.RegionMapper;
import com.hnit.system.service.IRegionFoodService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionFoodServiceImpl implements IRegionFoodService {

    @Resource
    private RegionMapper regionMapper;

    @Resource
    private RegionFoodMapper regionFoodMapper;

    @Value("${img.base-url:http://localhost:9999/image/}")
    private String baseUrl;

    @Override
    public List<RegionFoodDTO> getAllProvinces() {
        List<Region> provinces = regionMapper.selectProvinces();
        return provinces.stream().map(province -> {
            RegionFoodDTO dto = new RegionFoodDTO();
            dto.setCode(province.getCode());
            dto.setName(province.getName());
            dto.setLevel(province.getLevel());
            dto.setLng(province.getLng());
            dto.setLat(province.getLat());
            Integer count = regionFoodMapper.countFoodsByRegionCode(province.getCode());
            dto.setFoodCount(count != null ? count : 0);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public RegionFoodDTO drillDown(String regionCode) {
        Region region = regionMapper.selectByCode(regionCode);
        if (region == null) {
            return null;
        }

        RegionFoodDTO dto = new RegionFoodDTO();
        dto.setCode(region.getCode());
        dto.setName(region.getName());
        dto.setLevel(region.getLevel());
        dto.setLng(region.getLng());
        dto.setLat(region.getLat());

        List<FoodCardDTO> foods = regionFoodMapper.selectFoodsByRegionCode(regionCode);
        // 如果图片URL未拼接完整，进行拼接
        foods.forEach(food -> {
            if (food.getImageUrl() != null && !food.getImageUrl().startsWith("http")) {
                food.setImageUrl(baseUrl + food.getImageUrl());
            }
        });
        dto.setFoods(foods);
        dto.setFoodCount(foods.size());

        return dto;
    }

    @Override
    public List<RegionFoodDTO> getCitiesByProvince(String provinceCode) {
        List<Region> cities = regionMapper.selectCitiesByProvince(provinceCode);
        return cities.stream().map(city -> {
            RegionFoodDTO dto = new RegionFoodDTO();
            dto.setCode(city.getCode());
            dto.setName(city.getName());
            dto.setLevel(city.getLevel());
            dto.setLng(city.getLng());
            dto.setLat(city.getLat());
            Integer count = regionFoodMapper.countFoodsByRegionCode(city.getCode());
            dto.setFoodCount(count != null ? count : 0);
            return dto;
        }).collect(Collectors.toList());
    }
}