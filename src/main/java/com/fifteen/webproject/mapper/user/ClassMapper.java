package com.fifteen.webproject.mapper.user;

import com.fifteen.webproject.bean.vo.ClassVO;
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
public interface ClassMapper {
    @Select("select class_id,name from class where grade_id=#{gradeId} and major_id=#{majorId}")
    List<ClassVO>getClass(Integer gradeId,Integer majorId);
}
