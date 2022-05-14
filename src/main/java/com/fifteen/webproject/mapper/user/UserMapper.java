package com.fifteen.webproject.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fifteen.webproject.bean.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Fifteen
 * @Date 2022/4/8
 **/
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
    /**
     * @param account
     * @return
     */
    User getStudentByAccount(String account);
}
