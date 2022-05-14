package com.fifteen.webproject.controller;

import com.fifteen.webproject.bean.entity.ExamQuestion;
import com.fifteen.webproject.bean.entity.ExamRecord;
import com.fifteen.webproject.bean.entity.User;
import com.fifteen.webproject.bean.vo.QuestionVo;
import com.fifteen.webproject.service.ExamQuestionService;
import com.fifteen.webproject.service.ExamRecordService;
import com.fifteen.webproject.service.ExamService;
import com.fifteen.webproject.service.QuestionService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/5/5
 **/
@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private ExamRecordService examRecordService;
    @Autowired
    private ExamService examService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private ExamQuestionService examQuestionService;

    //获取个人成绩(分页 根据考试名查询)
    @GetMapping("/getMyGrade")
    public Result<List<ExamRecord>> getMyGrade(HttpServletRequest request, Integer examId) {
        User user = (User) request.getAttribute("user");
        Integer id = user.getId();
        return examRecordService.getUserGrade(id, examId);
    }

    //保存考试记录信息,返回保存记录的id
    @PostMapping("/addExamRecord")
    public Result<Integer> addExamRecord(@RequestBody ExamRecord examRecord, HttpServletRequest request) {
        Integer id = examService.addExamRecord(examRecord, request);
        return new Result<>(id, true, null);
    }
    //TODO 优化成多id一次查询
    //根据id获取题目信息
    @GetMapping("/getQuestionById/{id}")
    public Result<QuestionVo>getQuestionById(@PathVariable("id")Integer id){
        QuestionVo questionVoById = questionService.getQuestionVoById(id);
        return new Result<>(questionVoById,true,null);
    }
    //根据考试的记录id查询用户考试的信息
    @GetMapping("/getExamRecordById/{recordId}")
    public Result<ExamRecord>getExamRecordById(@PathVariable("recordId") Integer recordId){
        ExamRecord examRecordById = examRecordService.getExamRecordById(recordId);
        return new Result<>(examRecordById,true,null);
    }
    //根据考试id查询考试中的每一道题目id和分值
    @GetMapping("/getExamQuestionByExamId/{examId}")
    public Result<ExamQuestion>getExamQuestionByExamId(@PathVariable("examId") Integer examId){
        ExamQuestion examQuestionByExamId = examQuestionService.getExamQuestionByExamId(examId);
        return new Result<>(examQuestionByExamId,true,null);
    }

    //根据考试id查询所有题目
    @GetMapping("/getAllExamQuestionByExamId")
    public Result<List<QuestionVo>>getAllExamQuestionById(Integer examId){
        List<QuestionVo> allQuestion = questionService.getAllQuestion(examId);
        return new Result<>(allQuestion,true,null);
    }
}
