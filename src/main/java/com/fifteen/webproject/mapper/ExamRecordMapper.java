package com.fifteen.webproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fifteen.webproject.bean.entity.ExamRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Fifteen
 * @Date 2022/5/5
 **/
@Repository
@Mapper
public interface ExamRecordMapper extends BaseMapper<ExamRecord> {
}
