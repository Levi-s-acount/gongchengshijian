package com.fifteen.webproject.bean.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fifteen.webproject.bean.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @Author Fifteen
 * @Date 2022/4/8
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {
    private Integer id;
    private Integer classId;
    private Integer roleId;
    private String name;
    private Integer gender;
    private String email;
    private String phone;
    private String account;
    private String photo;
    private Integer status;
    private Integer gradeId;
    private Integer majorId;
    private Integer collegeId;
    private Integer universityId;
    public static UserVO fromUser(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }


}
