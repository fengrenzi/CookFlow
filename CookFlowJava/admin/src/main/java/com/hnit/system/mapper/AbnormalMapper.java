package com.hnit.system.mapper;

import com.hnit.system.domain.vo.AbnormalActionVO;
import com.hnit.system.domain.vo.AbnormalUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AbnormalMapper {
    List<AbnormalUserVO> getAbnormalUsers(@Param("userName") String userName);
    List<AbnormalActionVO> getAbnormalActions(@Param("userName") String userName);
}