package com.hnit.system.mapper;

import com.hnit.system.domain.SensitiveWord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SensitiveWordMapper {
    List<SensitiveWord> selectList(SensitiveWord word);
    int insert(SensitiveWord word);
    int updateById(SensitiveWord word);
    int deleteById(String id);
}