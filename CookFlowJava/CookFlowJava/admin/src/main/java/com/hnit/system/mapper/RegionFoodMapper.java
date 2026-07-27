package com.hnit.system.mapper;

import com.hnit.system.domain.dto.FoodCardDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RegionFoodMapper {

    Integer countFoodsByRegionCode(@Param("regionCode") String regionCode);

    List<FoodCardDTO> selectFoodsByRegionCode(@Param("regionCode") String regionCode);
}