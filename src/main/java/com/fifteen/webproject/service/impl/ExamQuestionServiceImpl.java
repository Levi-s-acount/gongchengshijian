package com.fifteen.webproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fifteen.webproject.bean.entity.ExamQuestion;
import com.fifteen.webproject.mapper.ExamQuestionMapper;
import com.fifteen.webproject.service.ExamQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Fifteen
 * @Date 2022/5/9
 **/
@Service
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestionMapper, ExamQuestion> implements ExamQuestionService {
    @Autowired
    private ExamQuestionMapper examQuestionMapper;
    @Override
    public ExamQuestion getExamQuestionByExamId(Integer examId) {
        return examQuestionMapper.selectOne(new QueryWrapper<ExamQuestion>().eq("exam_id", examId));
    }
}
