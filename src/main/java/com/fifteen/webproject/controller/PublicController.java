package com.fifteen.webproject.controller;

import com.fifteen.webproject.bean.dto.UpdatePasswordDTO;
import com.fifteen.webproject.bean.entity.Exam;
import com.fifteen.webproject.bean.entity.User;
import com.fifteen.webproject.bean.vo.AddExamByQuestionVo;
import com.fifteen.webproject.bean.vo.BankHaveQuestionSum;
import com.fifteen.webproject.bean.vo.ExamQueryVo;
import com.fifteen.webproject.bean.vo.QuestionVo;
import com.fifteen.webproject.service.ExamService;
import com.fifteen.webproject.service.QuestionBankService;
import com.fifteen.webproject.service.user.UserService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/4/29
 **/
@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private ExamService examService;
    @Autowired
    private QuestionBankService questionBankService;
    @Autowired
    private UserService userService;

    //根据信息查询考试的信息
    @PostMapping("/getExamInfo")
    public Result<List<Exam>> getExamInfo(@RequestBody ExamQueryVo examQueryVo) {
        return examService.getExamPage(examQueryVo);
    }
    //根据考试id查询考试信息
    @GetMapping("/getExamInfoId")
    public Result<Exam>getExamInfoById(@RequestParam Integer examId){
        Exam exam = examService.getById(examId);
        return new Result<>(exam,true,null);
    }
    //根据考试id查询考试的信息和题目列表
    @GetMapping("/getExamInfoById")
    public Result<AddExamByQuestionVo> getExamById(@RequestParam Integer examId) {
        AddExamByQuestionVo examInfoById = examService.getExamInfoById(examId);
        return new Result<>(examInfoById, true, null);
    }

    //查询考试所有信息
    @GetMapping("/allExamInfo")
    public Result<List<Exam>> allExamInfo() {
        List<Exam> result = examService.list(null);
        return new Result<>(result, true, null);
    }

    //获取题库中所有题目类型的数量
    @GetMapping("/getBankHaveQuestionSumByType")
    public Result<List<BankHaveQuestionSum>> getBankHaveQuestionSumByType(@RequestParam String bankName) {
        return questionBankService.getBankHaveQuestionSumByType(bankName);
    }


    //根据题库id和题目类型获取题目信息 type(1单选 2多选 3判断)
    @GetMapping("/getQuestionByBankIdAndType")
    public Result<List<QuestionVo>>getQuestionByBankIdAndType(Integer bankId,Integer type){
        List<QuestionVo> questionByBankIdAndType = questionBankService.getQuestionByBankIdAndType(bankId, type);
        return new Result<>(questionByBankIdAndType,null,null);
    }
    //根据题库获取所有的题目信息(单选,多选,判断题)
    @GetMapping("/getQuestionByBank")
    public Result<List<QuestionVo>>getQuestionByBank(Integer bankId){
        List<QuestionVo> questionByBank = questionBankService.getQuestionsByBankId(bankId);
        return new Result<>(questionByBank,null,null);
    }


    //修改密码
    @PostMapping("/updatePassword")
    public Result<String>updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        Integer userId = user.getId();
        System.out.println(userId);
        userService.updatePassword(userId, updatePasswordDTO.getPrePassword(), updatePasswordDTO.getPassword());
        return new Result<>(null,true,"修改成功");
    }
}
