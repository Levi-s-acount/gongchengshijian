package com.fifteen.webproject.mapper.user;

import com.fifteen.webproject.bean.vo.GradeVO;
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
public interface GradeMapper {
    @Select("select grade_id,name from grade where major_id=#{majorId}")
    List<GradeVO>getGrade(Integer majorId);
}
