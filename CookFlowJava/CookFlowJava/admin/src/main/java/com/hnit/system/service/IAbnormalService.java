package com.hnit.system.service;


import com.hnit.system.domain.vo.AbnormalActionVO;
import com.hnit.system.domain.vo.AbnormalUserVO;

import java.util.List;

public interface IAbnormalService {
    List<AbnormalUserVO> getAbnormalUsers(String userName);
    List<AbnormalActionVO> getAbnormalActions(String userName);
}