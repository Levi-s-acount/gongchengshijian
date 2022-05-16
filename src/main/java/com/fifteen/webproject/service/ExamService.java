package com.fifteen.webproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fifteen.webproject.bean.entity.Exam;
import com.fifteen.webproject.bean.entity.ExamRecord;
import com.fifteen.webproject.bean.vo.AddExamByBankVo;
import com.fifteen.webproject.bean.vo.AddExamByQuestionVo;
import com.fifteen.webproject.bean.vo.ExamQueryVo;
import com.fifteen.webproject.utils.result.Result;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/4/29
 **/
public interface ExamService extends IService<Exam> {
    Result<List<Exam>> getExamPage(ExamQueryVo examQueryVo);

    AddExamByQuestionVo getExamInfoById(Integer examId);

    void operationExam(Integer type, String ids);

    void addExamByBank(AddExamByBankVo addExamByBankVo);

    void addExamByQuestionList(AddExamByQuestionVo addExamByQuestionVo);

    void updateExamInfo(AddExamByQuestionVo addExamByQuestionVo);
}
