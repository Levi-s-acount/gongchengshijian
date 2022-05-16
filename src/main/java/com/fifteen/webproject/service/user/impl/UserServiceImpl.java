package com.fifteen.webproject.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fifteen.webproject.bean.entity.User;
import com.fifteen.webproject.bean.vo.UserVO;
import com.fifteen.webproject.mapper.user.UserMapper;
import com.fifteen.webproject.service.user.UserService;
import com.fifteen.webproject.utils.SecretUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.fifteen.webproject.bean.vo.UserVO.fromUser;

/**
 * @Author Fifteen
 * @Date 2022/4/8
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public Integer studentLogin(String account, String password) {
        User user = userMapper.getStudentByAccount(account);
        if (user ==null)return 0;
        password=SecretUtils.encrypt(password);
        if (!password.equals(user.getPassword())){
            return 0;
        }
        return user.getId();

    }

    @Override
    public Boolean userRegister(String name, String password, String account, Integer gender, Integer classId, String photo, Integer gradeId, Integer majorId, Integer collegeId, Integer universityId) {
        User user = new User();
        user.setName(name);
        user.setPassword(SecretUtils.encrypt(password));
        user.setAccount(account);
        user.setGender(gender);
        user.setClassId(classId);
        user.setPhone(photo);
        user.setGradeId(gradeId);
        user.setMajorId(majorId);
        user.setCollegeId(collegeId);
        user.setUniversityId(universityId);
        int insert = userMapper.insert(user);
        return insert==1;
    }

    @Override
    public User getStudentByAccount(String account) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account",account);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public UserVO getUserInfoById(Integer userId) {
        return fromUser(userMapper.selectById(userId));
    }
}
