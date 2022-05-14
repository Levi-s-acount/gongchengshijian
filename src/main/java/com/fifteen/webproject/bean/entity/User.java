package com.fifteen.webproject.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author Fifteen
 * @Date 2022/4/8
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer classId;
    private Integer roleId;
    private String name;
    private String password;
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
}
