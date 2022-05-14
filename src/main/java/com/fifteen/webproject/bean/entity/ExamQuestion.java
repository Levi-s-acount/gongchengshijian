package com.fifteen.webproject.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author by wzz
 * @implNote 2020/11/2 15:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "exam_question")
public class ExamQuestion implements Serializable {

    //  对应数据库的主键(uuid,自增id,雪花算法, redis,zookeeper)
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String questionIds;
    private Integer examId;
    private String scores;

}
