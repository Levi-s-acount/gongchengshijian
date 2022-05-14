package com.fifteen.webproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fifteen.webproject.bean.entity.Answer;
import com.fifteen.webproject.bean.entity.Question;
import com.fifteen.webproject.bean.entity.QuestionBank;
import com.fifteen.webproject.bean.vo.BankHaveQuestionSum;
import com.fifteen.webproject.bean.vo.QuestionVo;
import com.fifteen.webproject.mapper.AnswerMapper;
import com.fifteen.webproject.mapper.QuestionBankMapper;
import com.fifteen.webproject.mapper.QuestionMapper;
import com.fifteen.webproject.service.QuestionBankService;
import com.fifteen.webproject.utils.result.PageResult;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.fifteen.webproject.utils.CommonUtils.setLikeWrapper;

/**
 * @Author Fifteen
 * @Date 2022/5/2
 **/
@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank> implements QuestionBankService {

    @Autowired
    private QuestionBankMapper questionBankMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public Result<List<BankHaveQuestionSum>> getBankHaveQuestionSumByType(String bankName) {
        QueryWrapper<QuestionBank> wrapper = new QueryWrapper<>();
        setLikeWrapper(wrapper, Collections.singletonMap("bank_name", bankName));

        List<QuestionBank> records = questionBankMapper.selectList(wrapper);
        ArrayList<BankHaveQuestionSum> list = new ArrayList<>();
        for (QuestionBank questionBank : records) {
            // 创建vo对象
            BankHaveQuestionSum bankHaveQuestionSum = new BankHaveQuestionSum();
            bankHaveQuestionSum.setQuestionBank(questionBank);
            // 设置单选题的数量
            Integer singleQuestionCount = questionMapper.selectCount(new QueryWrapper<Question>().eq("qu_type", 1).like("qu_bank_name", questionBank.getBankName()));
            bankHaveQuestionSum.setSingleChoice(singleQuestionCount);
            // 设置多选题的数量
            Integer multipleQuestionCount = questionMapper.selectCount(new QueryWrapper<Question>().eq("qu_type", 2).like("qu_bank_name", questionBank.getBankName()));
            bankHaveQuestionSum.setMultipleChoice(multipleQuestionCount);
            // 设置判断题的数量
            Integer judgeQuestionCount = questionMapper.selectCount(new QueryWrapper<Question>().eq("qu_type", 3).like("qu_bank_name", questionBank.getBankName()));
            bankHaveQuestionSum.setJudge(judgeQuestionCount);
            // 设置简答题的数量
            Integer shortAnswerQuestionCount = questionMapper.selectCount(new QueryWrapper<Question>().eq("qu_type", 4).like("qu_bank_name", questionBank.getBankName()));
            bankHaveQuestionSum.setShortAnswer(shortAnswerQuestionCount);
            // 加入list中
            list.add(bankHaveQuestionSum);
        }
        return new Result<>(list,true,null);
    }

    @Override
    public List<QuestionVo> getQuestionsByBankId(Integer bankId) {
        QuestionBank bank = questionBankMapper.selectById(bankId);
        // 在题库中的(单选,多选,判断题)题目
        List<Question> questions = questionMapper.selectList(new QueryWrapper<Question>().like("qu_bank_name", bank.getBankName()).in("qu_type", 1, 2, 3));
        // 构造前端需要的vo对象
        List<QuestionVo> questionVos = new ArrayList<>();
        for (Question question : questions) {
            QuestionVo questionVo = new QuestionVo();
            questionVo.setQuestionId(question.getId());
            questionVo.setQuestionType(question.getQuType());
            questionVo.setQuestionLevel(question.getLevel());
            if (question.getImage() != null && !question.getImage().equals("")) // 防止没有图片对象
                questionVo.setImages(question.getImage().split(","));
            questionVo.setCreatePerson(question.getCreatePerson());
            questionVo.setAnalysis(question.getAnalysis());
            questionVo.setQuestionContent(question.getQuContent());
            questionVo.setQuestionType(question.getQuType());

            Answer answer = answerMapper.selectOne(new QueryWrapper<Answer>().eq("question_id", question.getId()));
            String[] options = answer.getAllOption().split(",");
            String[] images = answer.getImages().split(",");

            QuestionVo.Answer[] handleAnswer = new QuestionVo.Answer[options.length];
            // 字段处理
            for (int i = 0; i < options.length; i++) {
                QuestionVo.Answer answer1 = new QuestionVo.Answer();
                if (images.length - 1 >= i && images[i] != null && !images[i].equals(""))
                    answer1.setImages(new String[]{images[i]});
                answer1.setAnswer(options[i]);
                answer1.setId(i);
                answer1.setIsTrue("false");
                handleAnswer[i] = answer1;
            }
            if (question.getQuType() != 2) {// 单选和判断
                int trueOption = Integer.parseInt(answer.getTrueOption());
                handleAnswer[trueOption].setIsTrue("true");
                handleAnswer[trueOption].setAnalysis(answer.getAnalysis());
            } else {// 多选
                String[] trueOptions = answer.getTrueOption().split(",");
                for (String trueOption : trueOptions) {
                    handleAnswer[Integer.parseInt(trueOption)].setIsTrue("true");
                    handleAnswer[Integer.parseInt(trueOption)].setAnalysis(answer.getAnalysis());
                }
            }
            questionVo.setAnswer(handleAnswer);
            questionVos.add(questionVo);
        }
        return questionVos;
    }

    @Override
    public List<QuestionVo> getQuestionByBankIdAndType(Integer bankId, Integer type) {
        List<QuestionVo> questionVoList = getQuestionsByBankId(bankId);
        questionVoList.removeIf(questionVo -> !Objects.equals(questionVo.getQuestionType(), type));
        return questionVoList;
    }

    @Override
    public List<QuestionBank> getAllQuestionBanks() {
        return questionBankMapper.selectList(null);
    }

    @Override
    public void addQuestionToBank(String questionIds, String banks) {
        String[] quIds = questionIds.split(",");
        String[] bankIds = banks.split(",");
        // 将每一个题目放入每一个题库中
        for (String quId : quIds) {
            // 当前的问题对象
            Question question = questionMapper.selectById(Integer.parseInt(quId));
            String quBankId = question.getQuBankId();
            // 当前已经有的题库id
            String[] qid = quBankId.split(",");
            System.out.println(quBankId);
            // 存在去重后的题库id
            Set<Integer> allId = new HashSet<>();
            if (!quBankId.equals("")) {//防止题目没有题库
                for (String s : qid) {
                    allId.add(Integer.parseInt(s));
                }
            }
            // 将新增的仓库id放入
            for (String bankId : bankIds) {
                allId.add(Integer.parseInt(bankId));
            }
            // 处理后的id字符串 例如(1,2,3)
            String handleHaveBankIds = allId.toString().replaceAll(" ", "");
            handleHaveBankIds = handleHaveBankIds.substring(1, handleHaveBankIds.length() - 1);
            // 更新当前用户的题库id值
            question.setQuBankId(handleHaveBankIds);
            // 将存放处理后的set集合遍历,然后替换数据库的题库名
            StringBuilder bankNames = new StringBuilder();
            for (Integer id : allId) {
                bankNames.append(questionBankMapper.selectById(id).getBankName()).append(",");
            }
            // 替换原来的仓库名称
            question.setQuBankName(bankNames.substring(0, bankNames.toString().length() - 1));
            questionMapper.update(question, new UpdateWrapper<Question>().eq("id", question.getId()));
        }
    }

    @Override
    public void removeBankQuestion(String questionIds, String banks) {
        // 需要操作的问题
        String[] quIds = questionIds.split(",");
        // 需要移除的题库id
        String[] bankIds = banks.split(",");
        // 操作需要移除仓库的问题
        for (String quId : quIds) {
            Question question = questionMapper.selectById(Integer.parseInt(quId));
            String quBankId = question.getQuBankId();
            // 当前问题拥有的仓库id
            String[] curHaveId = quBankId.split(",");
            // 存储处理后的id
            Set<Integer> handleId = new HashSet<>();
            if (!quBankId.equals("")) {
                for (String s : curHaveId) {
                    handleId.add(Integer.parseInt(s));
                }
            }
            // 遍历查询set中是否含有需要删除的仓库id
            for (String bankId : bankIds) {
                handleId.remove(Integer.parseInt(bankId));
            }
            // 处理后的id字符串 例如(1,2,3)
            String handleHaveBankIds = handleId.toString().replaceAll(" ", "");
            handleHaveBankIds = handleHaveBankIds.substring(1, handleHaveBankIds.length() - 1);
            // 更新当前用户的题库id值
            question.setQuBankId(handleHaveBankIds);

            if (!handleHaveBankIds.equals("")) {// 删除后还存在剩余的题库
                // 将存放处理后的set集合遍历,然后替换数据库的题库名
                StringBuilder bankNames = new StringBuilder();
                for (Integer id : handleId) {
                    bankNames.append(questionBankMapper.selectById(id).getBankName()).append(",");
                }
                // 替换原来的仓库名称
                question.setQuBankName(bankNames.substring(0, bankNames.toString().length() - 1));
            } else {// 不剩题库了
                question.setQuBankName("");
            }
            // 更新问题对象
            questionMapper.update(question, new UpdateWrapper<Question>().eq("id", question.getId()));
        }
    }
}
