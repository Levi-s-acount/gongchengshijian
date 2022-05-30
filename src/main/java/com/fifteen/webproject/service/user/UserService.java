package com.fifteen.webproject.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fifteen.webproject.bean.entity.User;
import com.fifteen.webproject.bean.vo.UserVO;

/**
 * @Author Fifteen
 * @Date 2022/4/8
 **/
public interface UserService extends IService<User> {


    Integer studentLogin(String account,String password);

    Boolean userRegister(String name,String password,String account,Integer gender,Integer classId,String photo,Integer gradeId,Integer majorId,Integer collegeId,Integer universityId);

    User getStudentByAccount(String account);

    UserVO getUserInfoById(Integer userId);

    //修改密码
    void updatePassword(Integer userId,String prePassword,String password);

}
