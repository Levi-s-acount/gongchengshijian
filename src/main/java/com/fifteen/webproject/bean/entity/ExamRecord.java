package com.fifteen.webproject.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author by wzz
 * @implNote 2020/11/5 11:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "exam_record")
public class ExamRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer recordId;
    private Integer userId;
    private String userAnswers;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date examTime;
    private Integer logicScore;
    private Integer examId;
    private String questionIds;
    private Integer totalScore;
    private String errorQuestionIds;

}
