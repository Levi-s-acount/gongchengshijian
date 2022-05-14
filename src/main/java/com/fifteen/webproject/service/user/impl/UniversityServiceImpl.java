package com.fifteen.webproject.service.user.impl;

import com.fifteen.webproject.bean.vo.UniversityVO;
import com.fifteen.webproject.mapper.user.UniversityMapper;
import com.fifteen.webproject.service.user.UniversityService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/4/10
 **/
@Service
public class UniversityServiceImpl implements UniversityService {
    @Autowired
    private UniversityMapper universityMapper;
    @Override
    public Result<List<UniversityVO>> getUniversity() {
        List<UniversityVO> university = universityMapper.getUniversity();
        return new Result<>(university,true,null);
    }
}
