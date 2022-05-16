package com.fifteen.webproject.controller;

import com.fifteen.webproject.bean.entity.ExamRecord;
import com.fifteen.webproject.bean.entity.Question;
import com.fifteen.webproject.bean.entity.QuestionBank;
import com.fifteen.webproject.bean.vo.AddExamByBankVo;
import com.fifteen.webproject.bean.vo.AddExamByQuestionVo;
import com.fifteen.webproject.bean.vo.QuestionVo;
import com.fifteen.webproject.bean.vo.UserVO;
import com.fifteen.webproject.service.ExamRecordService;
import com.fifteen.webproject.service.ExamService;
import com.fifteen.webproject.service.QuestionBankService;
import com.fifteen.webproject.service.QuestionService;
import com.fifteen.webproject.service.user.UserService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/5/14
 **/
@RestController
@RequestMapping("/teacher")
@Validated
public class TeacherController {

    @Autowired
    private QuestionBankService questionBankService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private ExamService examService;
    @Autowired
    private ExamRecordService examRecordService;
    @Autowired
    private UserService userService;

    //获取所有题库信息
    @GetMapping("/getQuestionBank")
    public Result<List<QuestionBank>> getQuestionBank() {
        List<QuestionBank> allQuestionBanks = questionBankService.getAllQuestionBanks();
        return new Result<>(allQuestionBanks, true, null);
    }

    //获取题目信息,查询条件(可无)(questionType,questionBank,questionContent)
    @GetMapping("/getQuestion")
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

    //添加试题
    @PostMapping("/addQuestion")
    public Result<String>addQuestion(@RequestBody @Valid QuestionVo questionVo){
        questionService.addQuestion(questionVo);
        return new Result<>(null,true,"添加成功");
    }

    //更新试题
    @PostMapping("/updateQuestion")
    public Result<String>updateQuestion(@RequestBody @Valid QuestionVo questionVo){
        questionService.updateQuestion(questionVo);
        return new Result<>(null,true,"更新成功");
    }

    //删除题库并去除所有题目中的包含此题库的信息
    @GetMapping("/deleteQuestionBank")
    public Result<String>deleteQuestionBank(String ids){
        questionBankService.deleteQuestionBank(ids);
        return new Result<>(null,true,"删除成功");
    }

    //添加题库信息
    @PostMapping("/addQuestionBank")
    public Result<String>addQuestionBank(@RequestBody QuestionBank questionBank){
        questionBankService.addQuestionBank(questionBank);
        return new Result<>(null,true,null);
    }

    //操作考试的信息表(type 1启用 2禁用 3删除)
    @GetMapping("/operationExam/{type}")
    public Result<String>operationExam(@PathVariable("type") Integer type, String ids){
        examService.operationExam(type,ids);
        return new Result<>(null,true,"操作成功");
    }

    //根据题库添加考试
    @PostMapping("/addExamByBank")
    public Result<String>addExamByBank(@RequestBody @Valid AddExamByBankVo addExamByBankVo){
        examService.addExamByBank(addExamByBankVo);
        return new Result<>(null,true,"添加成功");
    }

    //根据题目列表添加考试
    @PostMapping("/addExamByQuestionList")
    public Result<String>addExamByQuestionList(@RequestBody @Valid AddExamByQuestionVo addExamByQuestionVo){
        examService.addExamByQuestionList(addExamByQuestionVo);
        return new Result<>(null,true,"添加成功");
    }

    //更新考试的信息
    @PostMapping("/updateExamInfo")
    public Result<String>updateExamInfo(@RequestBody AddExamByQuestionVo addExamByQuestionVo){
        examService.updateExamInfo(addExamByQuestionVo);
        return new Result<>(null,true,"修改成功");
    }

    //获取考试记录信息
    @GetMapping("/getExamRecord")
    public Result<List<ExamRecord>>getExamRecord(@RequestParam(required = false)Integer examId){
        return examRecordService.getExamRecord(examId);
    }

    // TODO 改成ids查询
    //根据用户id查询用户信息
    @GetMapping("/getUserById/{userId}")
    public  Result<UserVO>getUserById(@PathVariable Integer userId){
        UserVO user = userService.getUserInfoById(userId);
        return new Result<>(user,true,null);
    }
    @GetMapping("/setObjectQuestionScore")
    public Result<String> setObjectQuestionScore(Integer totalScore, Integer examRecordId){
        examRecordService.setObjectQuestionScore(totalScore,examRecordId);
        return new Result<>(null,true,"操作成功");
    }


}
