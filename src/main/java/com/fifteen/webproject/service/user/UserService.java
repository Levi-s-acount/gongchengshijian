package com.fifteen.webproject.service.user;

import com.fifteen.webproject.bean.entity.User;

/**
 * @Author Fifteen
 * @Date 2022/4/8
 **/
public interface UserService {


    Integer studentLogin(String account,String password);

    Boolean userRegister(String name,String password,String account,Integer gender,Integer classId,String photo,Integer gradeId,Integer majorId,Integer collegeId,Integer universityId);

    User getStudentByAccount(String account);

}
