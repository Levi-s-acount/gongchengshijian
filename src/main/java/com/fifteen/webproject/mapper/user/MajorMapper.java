package com.fifteen.webproject.mapper.user;

import com.fifteen.webproject.bean.vo.MajorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/4/10
 **/
@Repository
@Mapper
public interface MajorMapper {
    @Select("select major_id,name from major where college_id=#{collegeId}")
    List<MajorVO>getMajor(Integer collegeId);
}
