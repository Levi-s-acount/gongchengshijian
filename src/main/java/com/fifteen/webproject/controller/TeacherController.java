package com.fifteen.webproject.controller;

import com.fifteen.webproject.bean.entity.Question;
import com.fifteen.webproject.bean.entity.QuestionBank;
import com.fifteen.webproject.service.QuestionBankService;
import com.fifteen.webproject.service.QuestionService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/5/14
 **/
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private QuestionBankService questionBankService;
    @Autowired
    private QuestionService questionService;

    //获取所有题库信息
    @GetMapping("/getQuestionBank")
    public Result<List<QuestionBank>> getQuestionBank() {
        List<QuestionBank> allQuestionBanks = questionBankService.getAllQuestionBanks();
        return new Result<>(allQuestionBanks, true, null);
    }

    //获取题目信息,查询条件(可无)(questionType,questionBank,questionContent)
    @GetMapping("/get/question")
    public Result<List<Question>> getQuestion(@RequestParam(required = false) String questionType,
                                              @RequestParam(required = false) String questionBank,
                                              @RequestParam(required = false) String questionContent) {
        return questionService.getQuestion(questionType, questionBank, questionContent);
    }

    //根据id批量删除
    @GetMapping("/deleteQuestion")
    public Result<String> deleteQuestion(String questionIds) {
        questionService.deleteQuestionByIds(questionIds);
        return new Result<>(null, true, null);
    }

    //将问题加入题库
    @GetMapping("/addBankQuestion")
    public Result<String> addBankQuestion(String questionIds, String banks) {
        questionBankService.addQuestionToBank(questionIds, banks);
        return new Result<>(null, true, "加入成功");
    }

    //将问题从题库移除
    @GetMapping("/removeBankQuestion")
    public Result<String>removeBankQuestion(String questionIds, String banks){
        questionBankService.removeBankQuestion(questionIds,banks);
        return new Result<>(null,true,"移除成功");
    }
}
