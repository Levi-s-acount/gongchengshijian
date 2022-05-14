package com.fifteen.webproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fifteen.webproject.bean.entity.ExamQuestion;

/**
 * @Author Fifteen
 * @Date 2022/5/9
 **/
public interface ExamQuestionService extends IService<ExamQuestion> {
    ExamQuestion getExamQuestionByExamId(Integer examId);
}
