package com.fifteen.webproject.utils;


import com.fifteen.webproject.bean.entity.Answer;

import java.util.List;
import java.util.Objects;

public class SaltEncryption {
    //根据题目id获取答案列表中的答案索引
    public static int getIndex(List<Answer> list, Integer questionId) {
        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(list.get(i).getQuestionId(), questionId)) {
                return i;
            }
        }
        return -1;
    }
}
