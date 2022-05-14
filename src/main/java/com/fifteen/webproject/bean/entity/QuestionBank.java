package com.fifteen.webproject.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by wzz
 * @implNote 2020/10/24 15:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "question_bank")
public class QuestionBank {

    @TableId(type = IdType.AUTO)
    private Integer bankId;
    private String bankName;

}
