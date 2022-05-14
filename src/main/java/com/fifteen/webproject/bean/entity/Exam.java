package com.fifteen.webproject.bean.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Fifteen
 * @Date 2022/4/28
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam implements Serializable {
    // 不需要主键自增id type 后面需要手动配置id
    @TableId
    private Integer examId;
    private String examName;
    private String examDesc;
    private Integer duration;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date endTime;
    private Integer totalScore;
    private Integer passScore;
    private Integer status;
}
