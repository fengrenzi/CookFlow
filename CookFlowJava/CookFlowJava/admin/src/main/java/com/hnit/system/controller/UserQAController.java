package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.system.domain.vo.QuestionSimpleVO;
import com.hnit.system.domain.vo.AnswerSimpleVO;
import com.hnit.system.service.IUserQAService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserQAController extends BaseController {

    @Resource
    private IUserQAService userQAService;

    private static final Long TEMP_USER_ID = 1L;

    @GetMapping("/questions")
    public AjaxResult getMyQuestions() {
        List<QuestionSimpleVO> list = userQAService.getMyQuestions(TEMP_USER_ID);
        return success(list);
    }

    @GetMapping("/answers")
    public AjaxResult getMyAnswers() {
        List<AnswerSimpleVO> list = userQAService.getMyAnswers(TEMP_USER_ID);
        return success(list);
    }

    @GetMapping("/questions/followed")
    public AjaxResult getFollowedQuestions() {
        List<QuestionSimpleVO> list = userQAService.getFollowedQuestions(TEMP_USER_ID);
        return success(list);
    }

    @GetMapping("/questions/collected")
    public AjaxResult getCollectedQuestions() {
        List<QuestionSimpleVO> list = userQAService.getCollectedQuestions(TEMP_USER_ID);
        return success(list);
    }
}