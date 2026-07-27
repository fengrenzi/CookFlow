package com.hnit.system.service.impl;

import com.hnit.system.domain.vo.AbnormalActionVO;
import com.hnit.system.domain.vo.AbnormalUserVO;
import com.hnit.system.mapper.AbnormalMapper;
import com.hnit.system.service.IAbnormalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AbnormalServiceImpl implements IAbnormalService {

    @Resource
    private AbnormalMapper abnormalMapper;

    @Override
    public List<AbnormalUserVO> getAbnormalUsers(String userName) {
        return abnormalMapper.getAbnormalUsers(userName);
    }

    @Override
    public List<AbnormalActionVO> getAbnormalActions(String userName) {
        return abnormalMapper.getAbnormalActions(userName);
    }
}