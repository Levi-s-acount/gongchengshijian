package com.fifteen.webproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fifteen.webproject.bean.entity.Answer;
import com.fifteen.webproject.bean.entity.ExamQuestion;
import com.fifteen.webproject.bean.entity.Question;
import com.fifteen.webproject.bean.vo.QuestionVo;
import com.fifteen.webproject.mapper.AnswerMapper;
import com.fifteen.webproject.mapper.ExamQuestionMapper;
import com.fifteen.webproject.mapper.QuestionMapper;
import com.fifteen.webproject.service.QuestionService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.fifteen.webproject.utils.CommonUtils.setEqualsQueryWrapper;
import static com.fifteen.webproject.utils.CommonUtils.setLikeWrapper;

/**
 * @Author Fifteen
 * @Date 2022/5/9
 **/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private ExamQuestionMapper examQuestionMapper;

    @Override
    public QuestionVo getQuestionVoById(Integer id) {
        Question question = questionMapper.selectById(id);
        Answer answer = answerMapper.selectOne(new QueryWrapper<Answer>().eq("question_id", id));
        QuestionVo questionVo = new QuestionVo();
        // 设置字段
        questionVo.setQuestionContent(question.getQuContent());
        questionVo.setAnalysis(question.getAnalysis());
        questionVo.setQuestionType(question.getQuType());
        questionVo.setQuestionLevel(question.getLevel());
        questionVo.setQuestionId(question.getId());
        if (question.getImage() != null && !Objects.equals(question.getImage(), "")) {
            questionVo.setImages(question.getImage().split(","));
        }
        questionVo.setCreatePerson(question.getCreatePerson());
        // 设置所属题库
        if (!Objects.equals(question.getQuBankId(), "")) {
            String[] bids = question.getQuBankId().split(",");
            Integer[] bankIds = new Integer[bids.length];
            for (int i = 0; i < bids.length; i++) {
                bankIds[i] = Integer.parseInt(bids[i]);
            }
            questionVo.setBankId(bankIds);
        }
        if (answer != null) {
            String[] allOption = answer.getAllOption().split(",");
            String[] imgs = answer.getImages().split(",");
            QuestionVo.Answer[] qa = new QuestionVo.Answer[allOption.length];
            if (question.getQuType() != 2) {
                for (int i = 0; i < allOption.length; i++) {
                    QuestionVo.Answer answer1 = new QuestionVo.Answer();
                    answer1.setId(i);
                    answer1.setAnswer(allOption[i]);
                    if (i <= imgs.length - 1 && !Objects.equals(imgs[i], ""))
                        answer1.setImages(new String[]{imgs[i]});
                    if (i == Integer.parseInt(answer.getTrueOption())) {
                        answer1.setIsTrue("true");
                        answer1.setAnalysis(answer.getAnalysis());
                    }
                    qa[i] = answer1;
                }
            } else {// 多选
                for (int i = 0; i < allOption.length; i++) {
                    QuestionVo.Answer answer1 = new QuestionVo.Answer();
                    answer1.setId(i);
                    answer1.setAnswer(allOption[i]);
                    answer1.setImages(imgs);
                    if (i < answer.getTrueOption().split(",").length && i == Integer.parseInt(answer.getTrueOption().split(",")[i])) {
                        answer1.setIsTrue("true");
                        answer1.setAnalysis(answer.getAnalysis());
                    }
                    qa[i] = answer1;
                }
            }
            questionVo.setAnswer(qa);
        }
        return questionVo;
    }

    @Override
    public List<QuestionVo> getAllQuestion(Integer examId) {
        ExamQuestion examQuestion = examQuestionMapper.selectOne(new QueryWrapper<ExamQuestion>().eq("exam_id", examId));
        String questionIds = examQuestion.getQuestionIds();
        String[] split = questionIds.split(",");
        ArrayList<QuestionVo> result = new ArrayList<>();
        for (String s : split) {
            int temp = Integer.parseInt(s);
            QuestionVo questionVoById = getQuestionVoById(temp);
            result.add(questionVoById);
        }
        return result;
    }

    @Override
    public Result<List<Question>> getQuestion(String questionType, String questionBank, String questionContent) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("qu_bank_name", questionBank);
        map.put("qu_content", questionContent);
        setLikeWrapper(wrapper, map);
        setEqualsQueryWrapper(wrapper, Collections.singletonMap("qu_type", questionType));
        List<Question> questions = questionMapper.selectList(wrapper);
        return new Result<>(questions, true, null);
    }

    @Override
    public void deleteQuestionByIds(String questionIds) {
        String[] ids = questionIds.split(",");
        HashMap<String, Object> map = new HashMap<>();
        for (String id : ids) {
            map.clear();
            map.put("question_id", id);
            //  1. 删除数据库的题目信息
            questionMapper.deleteById(Integer.parseInt(id));
            // 2. 删除答案表对应当前题目id的答案
            answerMapper.deleteByMap(map);
        }
    }

}
