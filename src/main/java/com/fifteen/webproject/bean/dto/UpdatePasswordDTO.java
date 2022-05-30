package com.fifteen.webproject.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Fifteen
 * @Date 2022/5/31
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDTO {

    private String prePassword;
    private String password;
}
