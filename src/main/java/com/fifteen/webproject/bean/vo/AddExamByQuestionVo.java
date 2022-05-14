package com.fifteen.webproject.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author by wzz
 * @implNote 2020/11/2 15:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddExamByQuestionVo {

    @NotBlank
    private String examName;

    private String examDesc;


    private Integer examDuration;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    private Integer totalScore;

    private Integer passScore;

    private Integer status;

    private String questionIds;

    private Integer examId;

    private String scores;
}
