package com.fifteen.webproject.service.user.impl;

import com.fifteen.webproject.bean.vo.ClassVO;
import com.fifteen.webproject.mapper.user.ClassMapper;
import com.fifteen.webproject.service.user.ClassService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/4/10
 **/
@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    private ClassMapper classMapper;
    @Override
    public Result<List<ClassVO>> getClass(Integer gradeId,Integer majorId) {
        List<ClassVO> aClass = classMapper.getClass(gradeId,majorId);
        return new Result<>(aClass,true,null);
    }
}
