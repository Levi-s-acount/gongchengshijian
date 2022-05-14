package com.fifteen.webproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fifteen.webproject.bean.entity.QuestionBank;
import com.fifteen.webproject.bean.vo.BankHaveQuestionSum;
import com.fifteen.webproject.bean.vo.QuestionVo;
import com.fifteen.webproject.utils.result.Result;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/5/2
 **/
public interface QuestionBankService extends IService<QuestionBank> {
    Result<List<BankHaveQuestionSum>> getBankHaveQuestionSumByType(String bankName);

    List<QuestionVo> getQuestionsByBankId(Integer bankId);

    List<QuestionVo> getQuestionByBankIdAndType(Integer bankId, Integer type);

    List<QuestionBank> getAllQuestionBanks();

    void addQuestionToBank(String questionIds, String banks);

    void removeBankQuestion(String questionIds, String banks);
}
