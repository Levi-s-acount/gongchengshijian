package com.fifteen.webproject.mapper.user;

import com.fifteen.webproject.bean.vo.CollegeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/4/10
 **/
@Mapper
@Repository
public interface CollegeMapper {
    @Select("select college_id,name from college where university_id=#{universityId}")
    List<CollegeVO>getCollege(Integer universityId);
}
