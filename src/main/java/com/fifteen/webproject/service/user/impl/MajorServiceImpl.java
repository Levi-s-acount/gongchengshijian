package com.fifteen.webproject.service.user.impl;

import com.fifteen.webproject.bean.vo.MajorVO;
import com.fifteen.webproject.mapper.user.MajorMapper;
import com.fifteen.webproject.service.user.MajorService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/4/10
 **/
@Service
public class MajorServiceImpl implements MajorService {
    @Autowired
    private MajorMapper majorMapper;
    @Override
    public Result<List<MajorVO>> getMajor(Integer collegeId) {
        List<MajorVO> major = majorMapper.getMajor(collegeId);
        return new Result<>(major,true,null);
    }
}
