package com.fifteen.webproject.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fifteen.webproject.bean.vo.UniversityVO;
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
public interface UniversityMapper extends BaseMapper<UniversityVO> {
    @Select("select university_id,name from university")
    List<UniversityVO>getUniversity();
}
