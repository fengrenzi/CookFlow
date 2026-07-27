package com.hnit.system.controller;

import com.hnit.system.domain.vo.AbnormalActionVO;
import com.hnit.system.domain.vo.AbnormalUserVO;
import com.hnit.system.service.IAbnormalService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/food/abnormal")
public class AbnormalController {

    @Resource
    private IAbnormalService abnormalService;

    @GetMapping("/users")
    public List<AbnormalUserVO> getAbnormalUsers(@RequestParam(required = false) String userName) {
        return abnormalService.getAbnormalUsers(userName);
    }

    @GetMapping("/actions")
    public List<AbnormalActionVO> getAbnormalActions(@RequestParam(required = false) String userName) {
        return abnormalService.getAbnormalActions(userName);
    }
}