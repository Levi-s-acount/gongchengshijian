package com.fifteen.webproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fifteen.webproject.bean.entity.*;
import com.fifteen.webproject.bean.vo.AddExamByBankVo;
import com.fifteen.webproject.bean.vo.AddExamByQuestionVo;
import com.fifteen.webproject.bean.vo.ExamQueryVo;
import com.fifteen.webproject.mapper.*;
import com.fifteen.webproject.service.ExamService;

import com.fifteen.webproject.utils.SaltEncryption;
import com.fifteen.webproject.utils.exception.AppException;
import com.fifteen.webproject.utils.result.PageResult;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.fifteen.webproject.utils.CommonUtils.setEqualsQueryWrapper;
import static com.fifteen.webproject.utils.CommonUtils.setLikeWrapper;

/**
 * @Author Fifteen
 * @Date 2022/4/29
 **/
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper,Exam> implements ExamService  {
    @Autowired
    private ExamMapper examMapper;
    @Autowired
    private ExamQuestionMapper examQuestionMapper;
    @Autowired
    private ExamRecordMapper examRecordMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public Result<List<Exam>> getExamPage(ExamQueryVo examQueryVo) {
        QueryWrapper<Exam>wrapper=new QueryWrapper<>();
        setEqualsQueryWrapper(wrapper, Collections.singletonMap("type",examQueryVo.getExamType()));
        setLikeWrapper(wrapper, Collections.singletonMap("exam_name", examQueryVo.getExamName()));
        if (examQueryVo.getStartTime() != null) {
            wrapper.gt("start_time", examQueryVo.getStartTime().substring(0, examQueryVo.getStartTime().indexOf("T")));
        }
        if (examQueryVo.getEndTime() != null) {
            wrapper.lt("end_time", examQueryVo.getEndTime().substring(0, examQueryVo.getEndTime().indexOf("T")));
        }
        List<Exam> exams = examMapper.selectList(wrapper);
        return new Result<>(exams, true,null);
    }

    @Override
    public AddExamByQuestionVo getExamInfoById(Integer examId) {
        Exam exam = Optional.ofNullable(examMapper.selectById(examId)).orElseThrow(() -> new AppException("发生错误"));
        AddExamByQuestionVo addExamByQuestionVo = new AddExamByQuestionVo();
        addExamByQuestionVo.setExamId(examId);
        addExamByQuestionVo.setExamDesc(exam.getExamDesc());
        addExamByQuestionVo.setExamName(exam.getExamName());
        addExamByQuestionVo.setExamDuration(exam.getDuration());
        addExamByQuestionVo.setPassScore(exam.getPassScore());
        addExamByQuestionVo.setTotalScore(exam.getTotalScore());
        addExamByQuestionVo.setStartTime(exam.getStartTime());
        addExamByQuestionVo.setEndTime(exam.getEndTime());
        addExamByQuestionVo.setStatus(exam.getStatus());

        // 考试中题目的对象
        ExamQuestion examQuestion = examQuestionMapper.selectOne(new QueryWrapper<ExamQuestion>().eq("exam_id", examId));
        addExamByQuestionVo.setQuestionIds(examQuestion.getQuestionIds());
        addExamByQuestionVo.setScores(examQuestion.getScores());
        return addExamByQuestionVo;
    }

    @Override
    public void operationExam(Integer type, String ids) {
        String[] id = ids.split(",");
        switch (type) {
            case 1:
                setExamStatus(id, 1);
                break;
            case 2:
                setExamStatus(id, 2);
                break;
            case 3:
                Map<String, Object> map = new HashMap<>();
                for (String s : id) {
                    map.clear();
                    map.put("exam_id", Integer.parseInt(s));
                    examMapper.deleteByMap(map);
                    examQuestionMapper.deleteByMap(map);
                }
                break;
            default:
                throw new AppException("错误的操作");
        }
    }

    @Override
    public void addExamByBank(AddExamByBankVo addExamByBankVo) {
        Exam exam = new Exam();
        exam.setStatus(addExamByBankVo.getStatus());
        exam.setDuration(addExamByBankVo.getExamDuration());
        if (addExamByBankVo.getEndTime() != null) exam.setEndTime(addExamByBankVo.getEndTime());
        if (addExamByBankVo.getStartTime() != null) exam.setStartTime(addExamByBankVo.getStartTime());
        exam.setExamDesc(addExamByBankVo.getExamDesc());
        exam.setExamName(addExamByBankVo.getExamName());
        exam.setPassScore(addExamByBankVo.getPassScore());
        // 设置id
        ExamQuestion examQuestion = buildExamQuestion(exam);
        // 设置题目id字符串
        HashSet<Integer> set = new HashSet<>();
        String[] bankNames = addExamByBankVo.getBankNames().split(",");

        for (String bankName : bankNames) {
            List<Question> questions = questionMapper.selectList(new QueryWrapper<Question>().like("qu_bank_name", bankName));
            for (Question question : questions) {
                set.add(question.getId());
            }
        }
        String quIds = set.toString().substring(1, set.toString().length() - 1).replaceAll(" ", "");
        System.out.println(quIds);
        examQuestion.setQuestionIds(quIds);
        // 设置每一题的分数
        String[] s = quIds.split(",");
        // 总分
        int totalScore = 0;
        StringBuilder sf = new StringBuilder();
        for (String s1 : s) {
            Question question = questionMapper.selectById(Integer.parseInt(s1));
            if (question.getQuType() == 1) {
                sf.append(addExamByBankVo.getSingleScore()).append(",");
                totalScore += addExamByBankVo.getSingleScore();
            } else if (question.getQuType() == 2) {
                sf.append(addExamByBankVo.getMultipleScore()).append(",");
                totalScore += addExamByBankVo.getMultipleScore();
            } else if (question.getQuType() == 3) {
                sf.append(addExamByBankVo.getJudgeScore()).append(",");
                totalScore += addExamByBankVo.getJudgeScore();
            } else if (question.getQuType() == 4) {
                sf.append(addExamByBankVo.getShortScore()).append(",");
                totalScore += addExamByBankVo.getShortScore();
            }
        }
        examQuestion.setScores(sf.substring(0, sf.toString().length() - 1));
        // 设置总成绩
        exam.setTotalScore(totalScore);

        examMapper.insert(exam);
        examQuestionMapper.insert(examQuestion);
    }

    @Override
    public void addExamByQuestionList(AddExamByQuestionVo addExamByQuestionVo) {
        Exam exam = new Exam();
        exam.setTotalScore(addExamByQuestionVo.getTotalScore());
        exam.setPassScore(addExamByQuestionVo.getPassScore());
        if (addExamByQuestionVo.getEndTime() != null) exam.setEndTime(addExamByQuestionVo.getEndTime());
        if (addExamByQuestionVo.getStartTime() != null) exam.setStartTime(addExamByQuestionVo.getStartTime());
        exam.setExamDesc(addExamByQuestionVo.getExamDesc());
        exam.setExamName(addExamByQuestionVo.getExamName());
        exam.setDuration(addExamByQuestionVo.getExamDuration());
        exam.setStatus(addExamByQuestionVo.getStatus());
        // 设置id
        ExamQuestion examQuestion = buildExamQuestion(exam);
        examQuestion.setScores(addExamByQuestionVo.getScores());
        examQuestion.setQuestionIds(addExamByQuestionVo.getQuestionIds());

        examMapper.insert(exam);
        examQuestionMapper.insert(examQuestion);
    }

    @Override
    public void updateExamInfo(AddExamByQuestionVo addExamByQuestionVo) {
        Exam exam = new Exam();
        exam.setTotalScore(addExamByQuestionVo.getTotalScore());
        exam.setPassScore(addExamByQuestionVo.getPassScore());
        exam.setEndTime(addExamByQuestionVo.getEndTime());
        exam.setStartTime(addExamByQuestionVo.getStartTime());
        exam.setExamDesc(addExamByQuestionVo.getExamDesc());
        exam.setExamName(addExamByQuestionVo.getExamName());
        exam.setDuration(addExamByQuestionVo.getExamDuration());
        exam.setStatus(addExamByQuestionVo.getStatus());
        exam.setExamId(addExamByQuestionVo.getExamId());
        // 设置考试的题目和分值信息
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setExamId(addExamByQuestionVo.getExamId());
        examQuestion.setScores(addExamByQuestionVo.getScores());
        examQuestion.setQuestionIds(addExamByQuestionVo.getQuestionIds());

        examMapper.update(exam, new UpdateWrapper<Exam>().eq("exam_id", exam.getExamId()));
        examQuestionMapper.update(examQuestion, new UpdateWrapper<ExamQuestion>().eq("exam_id", exam.getExamId()));
    }

    private ExamQuestion buildExamQuestion(Exam exam) {
        List<Exam> examList = examMapper.selectList(null);
        int id = 0;
        if (examList.size() != 0) {
            id = examList.get(examList.size() - 1).getExamId() + 1;
        }
        exam.setExamId(id);
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setExamId(id);
        return examQuestion;
    }

    private void setExamStatus(String[] id, int status) {
        for (String s : id) {
            Exam exam = examMapper.selectOne(new QueryWrapper<Exam>().eq("exam_id", Integer.parseInt(s)));
            exam.setStatus(status);
            examMapper.update(exam, new UpdateWrapper<Exam>().eq("exam_id", s));
        }
    }
}
