package com.fifteen.webproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fifteen.webproject.bean.entity.Answer;
import com.fifteen.webproject.bean.entity.ExamQuestion;
import com.fifteen.webproject.bean.entity.ExamRecord;
import com.fifteen.webproject.bean.entity.User;
import com.fifteen.webproject.mapper.AnswerMapper;
import com.fifteen.webproject.mapper.ExamQuestionMapper;
import com.fifteen.webproject.mapper.ExamRecordMapper;
import com.fifteen.webproject.mapper.user.UserMapper;
import com.fifteen.webproject.service.ExamRecordService;
import com.fifteen.webproject.utils.SaltEncryption;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.fifteen.webproject.utils.CommonUtils.setEqualsQueryWrapper;

/**
 * @Author Fifteen
 * @Date 2022/5/5
 **/
@Service
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordMapper, ExamRecord> implements ExamRecordService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ExamRecordMapper examRecordMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private ExamQuestionMapper examQuestionMapper;
    @Override
    public Result<List<ExamRecord>> getUserGrade(Integer userId, Integer examId) {
        QueryWrapper<ExamRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        setEqualsQueryWrapper(wrapper, Collections.singletonMap("exam_id", examId));
        List<ExamRecord> examRecords = examRecordMapper.selectList(wrapper);
        return new Result<>(examRecords,true,null);
    }

    @Override
    public ExamRecord getExamRecordById(Integer recordId) {
        return examRecordMapper.selectById(recordId);
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

    @Override
    public Result<List<ExamRecord>> getExamRecord(Integer examId) {
        QueryWrapper<ExamRecord> wrapper = new QueryWrapper<>();
        setEqualsQueryWrapper(wrapper, Collections.singletonMap("exam_id", examId));
        List<ExamRecord> examRecords = examRecordMapper.selectList(wrapper);
        return new Result<>(examRecords,true,null);
    }

    @Override
    public void setObjectQuestionScore(Integer totalScore, Integer examRecordId) {
        ExamRecord examRecord = examRecordMapper.selectOne(new QueryWrapper<ExamRecord>().eq("record_id", examRecordId));
        examRecord.setTotalScore(totalScore);
        examRecordMapper.update(examRecord, new UpdateWrapper<ExamRecord>().eq("record_id", examRecordId));
    }
}
