package com.fifteen.webproject.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author by wzz
 * @implNote 2020/11/1 17:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamQueryVo {

    private Integer examType;

    private String startTime;

    private String endTime;

    private String examName;

}
