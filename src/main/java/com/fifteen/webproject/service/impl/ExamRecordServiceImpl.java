package com.fifteen.webproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fifteen.webproject.bean.entity.ExamRecord;
import com.fifteen.webproject.mapper.ExamRecordMapper;
import com.fifteen.webproject.mapper.user.UserMapper;
import com.fifteen.webproject.service.ExamRecordService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
}
