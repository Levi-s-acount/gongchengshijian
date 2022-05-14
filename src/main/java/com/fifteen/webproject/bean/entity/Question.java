package com.fifteen.webproject.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author by wzz
 * @implNote 2020/10/24 14:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "question")
public class Question {

    // 在新增试题的时候自己设置id,使用自增会影响
//    @TableId(type = IdType.AUTO)
    private Integer id;
    private String quContent;
    private Date createTime;
    private String createPerson;
    private Integer quType;
    private Integer level;
    private String image;
    private String quBankId;
    private String quBankName;
    private String analysis;

}
