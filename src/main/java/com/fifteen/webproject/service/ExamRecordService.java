package com.fifteen.webproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fifteen.webproject.bean.entity.ExamRecord;
import com.fifteen.webproject.utils.result.Result;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/5/5
 **/
public interface ExamRecordService extends IService<ExamRecord> {

    Result<List<ExamRecord>> getUserGrade(Integer userId, Integer examId);

    ExamRecord getExamRecordById(Integer recordId);
}
