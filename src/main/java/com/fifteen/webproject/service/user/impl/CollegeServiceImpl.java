package com.fifteen.webproject.service.user.impl;

import com.fifteen.webproject.bean.vo.CollegeVO;
import com.fifteen.webproject.mapper.user.CollegeMapper;
import com.fifteen.webproject.service.user.CollegeService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/4/10
 **/
@Service
public class CollegeServiceImpl implements CollegeService {
    @Autowired
    private CollegeMapper collegeMapper;
    @Override
    public Result<List<CollegeVO>> getCollege(Integer universityId) {
        List<CollegeVO> college = collegeMapper.getCollege(universityId);
        return new Result<>(college,true,null);
    }
}
