package com.fifteen.webproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fifteen.webproject.bean.entity.Question;
import com.fifteen.webproject.bean.vo.QuestionVo;
import com.fifteen.webproject.utils.result.Result;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/5/9
 **/
public interface QuestionService extends IService<Question> {
    QuestionVo getQuestionVoById(Integer id);

    List<QuestionVo>getAllQuestion(Integer examId);

    Result<List<Question>>getQuestion(String questionType, String questionBank, String questionContent);

    void deleteQuestionByIds(String questionIds);

    void addQuestion(QuestionVo questionVo);

    void updateQuestion(QuestionVo questionVo);

    List<Question>getAllBQuestion();
}
