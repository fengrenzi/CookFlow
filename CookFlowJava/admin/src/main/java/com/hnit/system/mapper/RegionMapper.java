package com.hnit.system.mapper;

import com.hnit.system.domain.Region;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RegionMapper {

    List<Region> selectByParentCode(@Param("parentCode") String parentCode);

    Region selectByCode(@Param("code") String code);

    List<Region> selectProvinces();

    List<Region> selectCitiesByProvince(@Param("provinceCode") String provinceCode);

    List<Region> selectCountiesByCity(@Param("cityCode") String cityCode);
}