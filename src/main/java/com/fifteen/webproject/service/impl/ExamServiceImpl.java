package com.fifteen.webproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fifteen.webproject.bean.entity.*;
import com.fifteen.webproject.bean.vo.AddExamByQuestionVo;
import com.fifteen.webproject.bean.vo.ExamQueryVo;
import com.fifteen.webproject.mapper.AnswerMapper;
import com.fifteen.webproject.mapper.ExamMapper;
import com.fifteen.webproject.mapper.ExamQuestionMapper;
import com.fifteen.webproject.mapper.ExamRecordMapper;
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
    public Integer addExamRecord(ExamRecord examRecord, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        examRecord.setUserId(user.getId());
        // 设置id
        List<ExamRecord> examRecords = examRecordMapper.selectList(null);
        int id=1;
        if (examRecords.size() > 0) {
            id = examRecords.get(examRecords.size() - 1).getRecordId() + 1;
        }
        examRecord.setRecordId(id);
        // 设置逻辑题目的分数
        // 查询所有的题目答案信息
        List<Answer> answers = answerMapper.selectList(new QueryWrapper<Answer>().in("question_id", Arrays.asList(examRecord.getQuestionIds().split(","))));
        // 查询考试的题目的分数
        HashMap<String, String> map = new HashMap<>();// key是题目的id  value是题目分值
        ExamQuestion examQuestion = examQuestionMapper.selectOne(new QueryWrapper<ExamQuestion>().eq("exam_id", examRecord.getExamId()));
        // 题目的id
        String[] ids = examQuestion.getQuestionIds().split(",");
        // 题目在考试中对应的分数
        String[] scores = examQuestion.getScores().split(",");
        for (int i = 0; i < ids.length; i++) {
            map.put(ids[i], scores[i]);
        }
        // 逻辑分数
        int logicScore = 0;
        // 错题的id
        StringBuilder sf = new StringBuilder();
        // 用户的答案
        String[] userAnswers = examRecord.getUserAnswers().split("-");
        for (int i = 0; i < examRecord.getQuestionIds().split(",").length; i++) {
            int index = SaltEncryption.getIndex(answers, Integer.parseInt(examRecord.getQuestionIds().split(",")[i]));
            if (index != -1) {
                if (Objects.equals(userAnswers[i], answers.get(index).getTrueOption())) {
                    logicScore += Integer.parseInt(map.get(examRecord.getQuestionIds().split(",")[i]));
                } else {
                    sf.append(examRecord.getQuestionIds().split(",")[i]).append(",");
                }
            }
        }
        examRecord.setLogicScore(logicScore);
        if (sf.length() > 0) {// 存在错的逻辑题
            examRecord.setErrorQuestionIds(sf.substring(0, sf.toString().length() - 1));
        }

        System.out.println(examRecord);
        examRecord.setExamTime(new Date());
        examRecordMapper.insert(examRecord);
        return id;
    }
}
