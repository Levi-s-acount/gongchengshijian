package com.fifteen.webproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fifteen.webproject.bean.entity.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Fifteen
 * @Date 2022/4/29
 **/
@Repository
@Mapper
public interface ExamMapper extends BaseMapper<Exam> {
}
