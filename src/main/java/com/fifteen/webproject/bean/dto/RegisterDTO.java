package com.fifteen.webproject.bean.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Fifteen
 * @Date 2022/4/8
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private Integer id;
    private Integer classId;
    private String name;
    private String password;
    private Integer gender;
    private String account;
    private String photo;
    private Integer gradeId;
    private Integer majorId;
    private Integer collegeId;
    private Integer universityId;
    private String verificationCode;
}
