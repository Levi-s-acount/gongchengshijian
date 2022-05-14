package com.fifteen.webproject.service.user.impl;

import com.fifteen.webproject.bean.vo.GradeVO;
import com.fifteen.webproject.mapper.user.GradeMapper;
import com.fifteen.webproject.service.user.GradeService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/4/10
 **/
@Service
public class GradeServiceImpl implements GradeService {
    @Autowired
    private GradeMapper gradeMapper;
    @Override
    public Result<List<GradeVO>> getGrade(Integer majorId) {
        List<GradeVO> grade = gradeMapper.getGrade(majorId);
        return new Result<>(grade,true,null);
    }
}
