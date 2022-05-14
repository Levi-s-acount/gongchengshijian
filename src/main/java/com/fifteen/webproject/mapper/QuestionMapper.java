package com.fifteen.webproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fifteen.webproject.bean.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Fifteen
 * @Date 2022/5/2
 **/
@Repository
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
}
